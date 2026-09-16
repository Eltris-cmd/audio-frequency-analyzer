# Audio Frequency Analyzer

Aplikasi analisis frekuensi audio untuk Android yang mendeteksi kemampuan speaker dengan menampilkan level dB per frekuensi dengan presisi tinggi.

## ✨ Fitur Utama

- 🎵 **32-Band Real-time Frequency Analysis** - Analisis 32 frekuensi berbeda secara real-time
- 📊 **Detailed dB Levels** - Menampilkan level dB untuk setiap frequency band (20 Hz - 20 kHz)
- 🔊 **Comprehensive Speaker Testing** - Uji kemampuan speaker dengan noise detection lengkap
- 📈 **Interactive Spectrum Graph** - Grafik visual spektrum frekuensi dengan zoom & scroll
- 💾 **Dual-Column Display** - Tampilan efisien untuk 32 frekuensi dengan statistik
- ⚡ **Optimized untuk 4GB RAM** - Efficient memory usage untuk Android low-end
- 🎯 **Logarithmic Frequency Distribution** - Sesuai dengan persepsi pendengaran manusia

## 📱 Frekuensi Target (32 Band)

Distribusi logaritmik dari 20 Hz hingga 20 kHz:

```
20Hz   → 25Hz   → 31Hz   → 39Hz   → 49Hz   → 62Hz   → 78Hz   → 98Hz
123Hz  → 155Hz  → 195Hz  → 246Hz  → 310Hz  → 391Hz  → 492Hz  → 620Hz
781Hz  → 984Hz  → 1.2kHz → 1.6kHz → 1.9kHz → 2.5kHz → 3.1kHz → 3.9kHz
4.9kHz → 6.2kHz → 7.8kHz → 9.8kHz → 12.4kHz → 15.6kHz → 19.7kHz → 20kHz
```

## 📋 Requirements

- Android 8.0+ (API Level 26+)
- RAM: Minimum 2GB (Optimal: 4GB+)
- Microphone permission
- Storage: ~50-100MB untuk APK

## 🚀 Instalasi Cepat

```bash
# Clone repository
git clone https://github.com/Eltris-cmd/audio-frequency-analyzer.git
cd audio-frequency-analyzer

# Build release APK (optimized)
./gradlew assembleRelease

# Install ke device
adb install app/build/outputs/apk/release/app-release.apk
```

## 🔧 Teknologi

- **Android Native** - Kotlin + Java
- **Audio Processing** - TarsosDSP (FFT 4096 points)
- **Visualization** - MPAndroidChart (Lightweight)
- **Async Processing** - Kotlin Coroutines
- **Sampling Rate** - 44100 Hz (CD Quality)

## 📖 Dokumentasi

- **INSTALLATION.md** - Panduan setup lengkap & troubleshooting
- **README.md** - File ini (overview & quick start)

## 💡 Cara Menggunakan

1. **Buka Aplikasi**
   - Izinkan akses microphone saat diminta
   - Kalibrasi ruang untuk hasil optimal (sepi/noise minimal)

2. **Siapkan Speaker**
   - Nyalakan speaker yang ingin ditest
   - Posisikan microphone 30-50cm dari speaker
   - Mainkan white noise atau pink noise

3. **Jalankan Analisis**
   - Tap "Start Analysis"
   - Biarkan recording berjalan 10-15 detik
   - Tap "Stop" untuk melihat hasil

4. **Interpretasi Hasil**
   - 🔴 Red (≥0 dB): Frekuensi sangat kuat
   - 🟠 Orange (-20 to 0 dB): Frekuensi kuat
   - 🟡 Yellow (-40 to -20 dB): Frekuensi medium
   - 🟢 Green (-60 to -40 dB): Frekuensi lemah
   - 🔵 Blue (-90 to -60 dB): Frekuensi sangat lemah
   - ⚪ White (<-90 dB): Tidak terdeteksi

## 🎯 Use Cases

✅ **Speaker Quality Testing** - Evaluasi respons frekuensi speaker
✅ **Audio Equipment Diagnosis** - Identifikasi frekuensi bermasalah
✅ **Room Acoustics Analysis** - Analisis akustik ruangan
✅ **Audio System Troubleshooting** - Debug masalah audio
✅ **Educational Purpose** - Pembelajaran tentang frekuensi audio

## ⚙️ Optimisasi untuk 4GB RAM

- ✨ Buffer minimal (4096 FFT)
- ✨ ProGuard obfuscation & minify
- ✨ Efficient coroutine-based processing
- ✨ Lightweight dependencies
- ✨ Garbage collection friendly

## 📊 Spesifikasi

| Aspek | Detail |
|-------|--------|
| **Frequency Range** | 20 Hz - 20 kHz |
| **Resolution** | 32 Bands |
| **Sampling Rate** | 44100 Hz |
| **FFT Size** | 4096 points |
| **Window Function** | Hann Window |
| **dB Range** | -120 dB to +20 dB |
| **Update Rate** | Real-time |

## 🛠️ Build & Deploy

### Debug Build
```bash
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Release Build (Optimized)
```bash
./gradlew assembleRelease
# APK terletak di: app/build/outputs/apk/release/app-release.apk
```

## 📝 Project Structure

```
audio-frequency-analyzer/
├── app/
│   ├── src/main/kotlin/com/eltris/audioanalyzer/
│   │   ├── audio/
│   │   │   └── AudioAnalyzer.kt          ← 32-band FFT engine
│   │   └── ui/
│   │       └── MainActivity.kt           ← UI dengan 32-band display
│   ├── src/main/res/
│   │   ├── layout/activity_main.xml
│   │   └── values/{colors,strings,themes}.xml
│   ├── build.gradle                      ← Dependencies
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
├── README.md
└── INSTALLATION.md
```

## 🐛 Known Issues & Workarounds

| Issue | Solusi |
|-------|--------|
| Chart lag pada device low-end | Disable live update, gunakan release build |
| Microphone tidak terdeteksi | Cek runtime permission, restart app |
| Hanya beberapa band terdeteksi | NORMAL jika speaker tidak support range |
| FFT noise tinggi | Mainkan noise lebih keras, tunggu stabilisasi |

## 🎓 Technical Notes

- **FFT Processing**: Ooura FFT via TarsosDSP
- **Window Function**: Hann window untuk mengurangi spectral leakage
- **dB Calculation**: 20 * log10(magnitude / reference_pressure)
- **Reference Pressure**: 20 µPa (SPL standard)
- **Frequency Distribution**: Logarithmic spacing (32 bands dari 20Hz-20kHz)

## 📄 License

MIT License - Bebas digunakan untuk tujuan komersial & non-komersial

## 🤝 Contributing

Kontribusi welcome! Silakan buat:
- Issue untuk bug reports
- Pull request untuk improvements
- Suggestions untuk fitur baru

## 📞 Support

Untuk bantuan:
1. Baca INSTALLATION.md untuk troubleshooting
2. Cek GitHub Issues untuk pertanyaan serupa
3. Create new issue jika menemukan bug

---

**Version:** 1.1.0 (32-Band Support)  
**Last Updated:** 2024  
**Status:** ✅ Production Ready

**Happy analyzing! 🎵📊**