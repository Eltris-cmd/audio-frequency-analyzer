# Audio Frequency Analyzer

Aplikasi analisis frekuensi audio untuk Android yang mendeteksi kemampuan speaker dengan menampilkan level dB per frekuensi.

## Fitur

- 🎵 **Real-time Frequency Analysis** - Analisis frekuensi audio secara real-time
- 📊 **Detailed dB Levels** - Menampilkan level dB untuk frekuensi 20 Hz hingga 20 kHz
- 🔊 **Speaker Testing** - Uji kemampuan speaker dengan noise detection
- 📈 **Visual Spectrum Graph** - Grafik visual spektrum frekuensi
- ⚡ **Optimized untuk 4GB RAM** - Efficient memory usage untuk Android low-end
- 🎯 **Target Frekuensi Utama** - 20Hz, 40Hz, 60Hz, 100Hz, 200Hz, 500Hz, 1kHz, 2kHz, 5kHz, 10kHz, 20kHz

## Requirements

- Android 8.0+ (API Level 26+)
- Microphone permission
- Minimum RAM: 2GB (Optimal: 4GB+)

## Instalasi

```bash
git clone https://github.com/Eltris-cmd/audio-frequency-analyzer.git
cd audio-frequency-analyzer
```

## Teknologi

- Android Native (Kotlin/Java)
- Web-based FFT Library (TarsosDSP / Web Audio API for Flutter alternative)
- Real-time Audio Processing

## Struktur Project

```
audio-frequency-analyzer/
├── app/
│   ├── src/main/
│   │   ├── kotlin/
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
└── README.md
```

## License

MIT License