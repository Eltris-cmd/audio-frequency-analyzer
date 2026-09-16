# Panduan Instalasi & Setup

## Prerequisites

- Android Studio versi terbaru (Arctic Fox atau lebih baru)
- JDK 11+
- Android SDK 34
- Perangkat Android atau emulator dengan Android 8.0+

## Langkah Instalasi

### 1. Clone Repository
```bash
git clone https://github.com/Eltris-cmd/audio-frequency-analyzer.git
cd audio-frequency-analyzer
```

### 2. Buka di Android Studio
- Buka Android Studio
- File → Open
- Pilih folder `audio-frequency-analyzer`
- Tunggu gradle sync selesai

### 3. Build APK
```bash
# Debug APK
./gradlew assembleDebug

# Release APK (optimized untuk 4GB RAM)
./gradlew assembleRelease
```

### 4. Install ke Device
```bash
# Via USB (Device terhubung)
./gradlew installDebug

# Via Emulator
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Penggunaan Aplikasi

### Startup
1. Buka aplikasi Audio Frequency Analyzer
2. Izinkan akses microphone saat diminta
3. Bersiaplah untuk melakukan testing

### Testing Speaker

1. **Siapkan Speaker**
   - Nyalakan speaker yang ingin ditest
   - Posisikan microphone 30-50cm dari speaker
   - Pastikan ruangan relatif sepi

2. **Jalankan Analisis**
   - Tap tombol "Start Analysis"
   - Status akan berubah menjadi "🔴 Recording..."
   - Mainkan white noise atau pink noise melalui speaker

3. **Interpretasi Hasil**
   - 🔴 Red Level (>0 dB): Frekuensi sangat kuat
   - 🟠 Orange Level (-20 to 0 dB): Frekuensi kuat
   - 🟡 Yellow Level (-40 to -20 dB): Frekuensi medium
   - 🟢 Green Level (-60 to -40 dB): Frekuensi lemah
   - ⚪ Gray Level (<-60 dB): Frekuensi sangat lemah

4. **Hentikan Analisis**
   - Tap tombol "Stop" untuk menghentikan recording
   - Hasil akan tetap ditampilkan untuk referensi

## Optimisasi untuk 4GB RAM

Aplikasi ini sudah dioptimalkan dengan:

✅ **Memory Optimization**
- Menggunakan buffer ukuran minimal yang diperlukan
- FFT window hanya 4096 samples
- ProGuard rules untuk mengurangi ukuran APK
- Efficient garbage collection

✅ **Performance Features**
- Real-time processing tanpa blocking UI
- Coroutine-based async operations
- Lightweight chart library (MPAndroidChart)
- Minimal resource consumption

✅ **Android Optimization**
- Minify & shrink resources di release build
- Targeting API 26-34 untuk compatibility
- Battery & memory friendly audio recording

## Troubleshooting

### ❌ Microphone tidak terdeteksi
```
Solusi:
- Check manifest permission di AndroidManifest.xml
- Pastikan Android 6.0+ runtime permission diberikan
- Cek perangkat atau emulator memiliki mic virtual
```

### ❌ Chart tidak menampilkan data
```
Solusi:
- Pastikan sampling rate 44100 Hz mendukung perangkat
- Cek audio input level (min -120 dB, max 0 dB)
- Restart aplikasi
```

### ❌ Lag atau freeze
```
Solusi:
- Tutup aplikasi lain yang berjalan
- Gunakan release build (optimized)
- Kurangi sampling duration
```

### ❌ File "databinding" error
```
Solusi:
- Clean build: ./gradlew clean
- Rebuild: ./gradlew build
- Invalidate cache & restart di Android Studio
```

## Hardware Requirements

| Spesifikasi | Minimum | Recommended |
|------------|---------|-------------|
| Android Version | 8.0 (API 26) | 10+ (API 29+) |
| RAM | 2 GB | 4 GB+ |
| Storage | 50 MB | 100 MB |
| Processor | Quad-core | Octa-core |
| Microphone | Built-in | High sensitivity |

## File Penting

```
audio-frequency-analyzer/
├── app/
│   ├── build.gradle              # Dependencies & config
│   ├── proguard-rules.pro        # Optimization rules
│   ├── src/main/
│   │   ├── kotlin/
│   │   │   └── com/eltris/audioanalyzer/
│   │   │       ├── audio/
│   │   │       │   └── AudioAnalyzer.kt   # Core FFT engine
│   │   │       └── ui/
│   │   │           └── MainActivity.kt    # UI & display
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml      # UI layout
│   │   │   └── values/
│   │   │       ├── colors.xml
│   │   │       ├── strings.xml
│   │   │       └── themes.xml
│   │   └── AndroidManifest.xml
│   └── src/
├── build.gradle                  # Root config
├── settings.gradle               # Project settings
└── README.md
```

## Support

Untuk masalah atau saran:
1. Cek README.md untuk dokumentasi lengkap
2. Lihat troubleshooting section di atas
3. Create GitHub Issue jika bug ditemukan

---

**Happy analyzing! 🎵📊**