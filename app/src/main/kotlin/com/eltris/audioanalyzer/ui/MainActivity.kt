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
            setDrawValueAboveBar(true)
            description.isEnabled = false
            setMaxVisibleValueCount(11)
            animateY(1000)
            
            // X axis
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                labelCount = 11
            }
            
            // Y axis
            axisLeft.apply {
                axisMinimum = -120f
                axisMaximum = 20f
                setDrawGridLines(true)
                labelCount = 8
            }
            
            axisRight.isEnabled = false
            
            legend.isEnabled = true
        }
    }

    private fun updateChart(frequencyData: Map<String, Float>) {
        lifecycleScope.launch {
            val entries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()
            
            // Urutkan data berdasarkan frequency
            val sortedData = frequencyData.entries.sortedBy { 
                it.key.removeSuffix("Hz").toFloatOrNull() ?: 0f 
            }
            
            sortedData.forEachIndexed { index, (freq, db) ->
                entries.add(BarEntry(index.toFloat(), db))
                labels.add(freq)
            }
            
            val dataSet = BarDataSet(entries, "dB Level").apply {
                color = ContextCompat.getColor(this@MainActivity, R.color.purple_500)
                valueTextSize = 8f
            }
            
            val barData = BarData(dataSet)
            barChart.data = barData
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            barChart.invalidate()
        }
    }

    private fun updateDataDisplay(frequencyData: Map<String, Float>) {
        lifecycleScope.launch {
            val displayText = StringBuilder().apply {
                append("📊 Frequency Analysis Results\n")
                append("════════════════════════════\n\n")
                
                frequencyData.entries
                    .sortedBy { it.key.removeSuffix("Hz").toFloatOrNull() ?: 0f }
                    .forEach { (freq, db) ->
                        val level = when {
                            db >= 0 -> "🔴 Very Strong"
                            db >= -20 -> "🟠 Strong"
                            db >= -40 -> "🟡 Medium"
                            db >= -60 -> "🟢 Weak"
                            else -> "⚪ Very Weak"
                        }
                        append(String.format("%-8s: %6.2f dB  %s\n", freq, db, level))
                    }
            }.toString()
            
            binding.dataTextView.text = displayText
        }
    }

    private fun startAnalysis() {
        isAnalyzing = true
        binding.startButton.isEnabled = false
        binding.stopButton.isEnabled = true
        binding.statusTextView.text = "🔴 Recording... Play noise through speaker"
        audioAnalyzer.startAnalysis()
    }

    private fun stopAnalysis() {
        isAnalyzing = false
        binding.startButton.isEnabled = true
        binding.stopButton.isEnabled = false
        binding.statusTextView.text = "✅ Analysis completed"
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
        super.onDestroy()        if (isAnalyzing) {
            audioAnalyzer.stopAnalysis()
        }
    }
}