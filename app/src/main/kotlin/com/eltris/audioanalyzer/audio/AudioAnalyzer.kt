package com.eltris.audioanalyzer.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcher
import be.tarsos.dsp.ooura.FFT
import kotlinx.coroutines.*
import kotlin.math.log10
import kotlin.math.sqrt

/**
 * Audio Analyzer untuk deteksi frekuensi dan level dB
 * Dioptimalkan untuk perangkat RAM 4GB dengan efficient memory usage
 */
class AudioAnalyzer(private val onFrequencyDataReady: (Map<String, Float>) -> Unit) {

    companion object {
        private const val SAMPLE_RATE = 44100 // Hz
        private const val BUFFER_SIZE = 4096 // FFT size
        private const val AUDIO_SOURCE = MediaRecorder.AudioSource.MIC
        private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        
        // Target frequencies untuk analisis
        private val TARGET_FREQUENCIES = listOf(
            20f, 40f, 60f, 100f, 200f, 500f, 1000f, 2000f, 5000f, 10000f, 20000f
        )
        
        // Reference pressure for dB calculation (20 µPa)
        private const val REFERENCE_PRESSURE = 0.00002f
    }

    private var audioDispatcher: AudioDispatcher? = null
    private var isRecording = false
    private var analysisJob: Job? = null
    
    // FFT processor
    private val fftProcessor = FFTProcessor()

    /**
     * Mulai analisis audio
     */
    fun startAnalysis() {
        if (isRecording) return
        
        isRecording = true
        analysisJob = CoroutineScope(Dispatchers.Default).launch {
            try {
                val minBufferSize = AudioRecord.getMinBufferSize(
                    SAMPLE_RATE,
                    CHANNEL_CONFIG,
                    AUDIO_FORMAT
                )
                val bufferSize = maxOf(minBufferSize, BUFFER_SIZE * 2)

                audioDispatcher = AudioDispatcher(
                    AUDIO_SOURCE,
                    SAMPLE_RATE,
                    bufferSize,
                    BUFFER_SIZE
                ).apply {
                    addAudioProcessor(fftProcessor)
                    run()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                isRecording = false
            }
        }
    }

    /**
     * Hentikan analisis audio
     */
    fun stopAnalysis() {
        isRecording = false
        audioDispatcher?.stop()
        audioDispatcher = null
        analysisJob?.cancel()
        analysisJob = null
    }

    /**
     * FFT Processor untuk real-time frequency analysis
     */
    private inner class FFTProcessor : AudioProcessor {
        private val fft = FFT(BUFFER_SIZE)
        private val complexFFT = Array(BUFFER_SIZE) { FloatArray(2) }
        private var windowBuffer = FloatArray(BUFFER_SIZE)
        
        override fun process(audioEvent: AudioEvent?): Boolean {
            audioEvent?.let { event ->
                val floatBuffer = event.floatBuffer
                
                if (floatBuffer.size >= BUFFER_SIZE) {
                    // Apply Hann window untuk mengurangi spectral leakage
                    applyHannWindow(floatBuffer, windowBuffer)
                    
                    // Copy ke complex FFT array
                    for (i in 0 until BUFFER_SIZE) {
                        complexFFT[i][0] = windowBuffer[i]
                        complexFFT[i][1] = 0f
                    }
                    
                    // Lakukan FFT
                    fft.complexForward(complexFFT)
                    
                    // Hitung frequency bins dengan dB levels
                    val frequencyData = calculateFrequencyData(complexFFT)
                    
                    // Panggil callback dengan data
                    onFrequencyDataReady(frequencyData)
                }
            }
            return true
        }
        
        override fun processingFinished() {}
        
        private fun applyHannWindow(input: FloatArray, output: FloatArray) {
            val size = BUFFER_SIZE
            for (i in 0 until size) {
                val window = 0.5f * (1f - kotlin.math.cos(2f * Math.PI * i / (size - 1))).toFloat()
                output[i] = input[i] * window
            }
        }
        
        private fun calculateFrequencyData(complexFFT: Array<FloatArray>): Map<String, Float> {
            val result = mutableMapOf<String, Float>()
            val freqBinWidth = SAMPLE_RATE.toFloat() / BUFFER_SIZE
            
            for (targetFreq in TARGET_FREQUENCIES) {
                // Cari bin yang paling dekat dengan target frequency
                val binIndex = (targetFreq / freqBinWidth).toInt()
                
                if (binIndex in 0 until BUFFER_SIZE / 2) {
                    // Hitung magnitude dari real dan imaginary parts
                    val real = complexFFT[binIndex][0]
                    val imaginary = complexFFT[binIndex][1]
                    val magnitude = sqrt(real * real + imaginary * imaginary)
                    
                    // Normalisasi
                    val normalizedMagnitude = magnitude / (BUFFER_SIZE / 2)
                    
                    // Konversi ke dB dengan reference pressure
                    val dB = if (normalizedMagnitude > 0) {
                        20f * log10(normalizedMagnitude / REFERENCE_PRESSURE)
                    } else {
                        -120f // Minimum dB value
                    }
                    
                    // Clamp nilai dB antara -120 hingga 120
                    val clampedDB = dB.coerceIn(-120f, 120f)
                    
                    result["${targetFreq.toInt()}Hz"] = clampedDB
                }
            }
            
            return result
        }
    }
}