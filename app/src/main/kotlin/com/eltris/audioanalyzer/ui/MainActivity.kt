package com.eltris.audioanalyzer.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.eltris.audioanalyzer.R
import com.eltris.audioanalyzer.audio.AudioAnalyzer
import com.eltris.audioanalyzer.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var audioAnalyzer: AudioAnalyzer
    private lateinit var barChart: BarChart
    private var isAnalyzing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        checkAndRequestPermissions()
    }

    private fun setupUI() {
        barChart = binding.frequencyChart
        setupChart()

        binding.startButton.setOnClickListener {
            if (!isAnalyzing) {
                startAnalysis()
            }
        }

        binding.stopButton.setOnClickListener {
            if (isAnalyzing) {
                stopAnalysis()
            }
        }

        binding.stopButton.isEnabled = false

        // Inisialisasi AudioAnalyzer
        audioAnalyzer = AudioAnalyzer { frequencyData ->
            updateChart(frequencyData)
            updateDataDisplay(frequencyData)
        }
    }

    private fun setupChart() {
        barChart.apply {
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)
            description.isEnabled = false
            setMaxVisibleValueCount(32)
            animateY(1000)
            isScaleXEnabled = true
            isScaleYEnabled = true
            isDragXEnabled = true
            isDragYEnabled = true
            
            // X axis
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                labelCount = 8
                labelRotationAngle = 45f
                textSize = 9f
            }
            
            // Y axis
            axisLeft.apply {
                axisMinimum = -120f
                axisMaximum = 20f
                setDrawGridLines(true)
                labelCount = 8
                textSize = 10f
            }
            
            axisRight.isEnabled = false
            
            legend.apply {
                isEnabled = true
                textSize = 11f
            }
        }
    }

    private fun updateChart(frequencyData: Map<String, Float>) {
        lifecycleScope.launch {
            val entries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()
            
            // Maintain order dari LinkedHashMap yang sudah terurut logaritmik
            frequencyData.entries.forEachIndexed { index, (freq, db) ->
                entries.add(BarEntry(index.toFloat(), db))
                labels.add(freq)
            }
            
            val dataSet = BarDataSet(entries, "dB Level").apply {
                color = ContextCompat.getColor(this@MainActivity, R.color.purple_500)
                valueTextSize = 7f
                isHighlightEnabled = true
            }
            
            val barData = BarData(dataSet).apply {
                barWidth = 0.85f
            }
            
            barChart.data = barData
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            barChart.notifyDataSetChanged()
            barChart.invalidate()
        }
    }

    private fun updateDataDisplay(frequencyData: Map<String, Float>) {
        lifecycleScope.launch {
            val displayText = StringBuilder().apply {
                append("📊 32-Band Frequency Analysis\n")
                append("════════════════════════════════════════════════\n\n")
                
                // Tampilkan dalam 2 kolom untuk efisiensi ruang
                val freqList = frequencyData.entries.toList()
                val midPoint = (freqList.size + 1) / 2
                
                for (i in 0 until midPoint) {
                    // Kolom kiri
                    if (i < freqList.size) {
                        val (freq, db) = freqList[i]
                        val level = getLevelIndicator(db)
                        append(String.format("%-10s: %6.1f dB %s", freq, db, level))
                    }
                    
                    // Kolom kanan (jika ada)
                    if (i + midPoint < freqList.size) {
                        val (freq, db) = freqList[i + midPoint]
                        val level = getLevelIndicator(db)
                        append(String.format("   |   %-10s: %6.1f dB %s", freq, db, level))
                    }
                    
                    append("\n")
                }
                
                // Statistik
                val avgDb = frequencyData.values.average()
                val maxDb = frequencyData.values.maxOrNull() ?: 0f
                val minDb = frequencyData.values.minOrNull() ?: -120f
                
                append("\n════════════════════════════════════════════════\n")
                append(String.format("📈 Statistics:\n"))
                append(String.format("   Avg: %.2f dB | Max: %.2f dB | Min: %.2f dB\n", avgDb, maxDb, minDb))
            }.toString()
            
            binding.dataTextView.text = displayText
        }
    }

    private fun getLevelIndicator(db: Float): String {
        return when {
            db >= 0 -> "🔴"
            db >= -20 -> "🟠"
            db >= -40 -> "🟡"
            db >= -60 -> "🟢"
            db >= -90 -> "🔵"
            else -> "⚪"
        }
    }

    private fun startAnalysis() {
        isAnalyzing = true
        binding.startButton.isEnabled = false
        binding.stopButton.isEnabled = true
        binding.statusTextView.text = "🔴 Recording... Play white/pink noise through speaker"
        audioAnalyzer.startAnalysis()
    }

    private fun stopAnalysis() {
        isAnalyzing = false
        binding.startButton.isEnabled = true
        binding.stopButton.isEnabled = false
        binding.statusTextView.text = "✅ Analysis completed - 32 frequencies detected"
        audioAnalyzer.stopAnalysis()
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                binding.statusTextView.text = "❌ Microphone permission required"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isAnalyzing) {
            audioAnalyzer.stopAnalysis()
        }
    }
}