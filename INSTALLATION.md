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

### Testing Speaker dengan 32 Frequencies

1. **Siapkan Speaker**
   - Nyalakan speaker yang ingin ditest
   - Posisikan microphone 30-50cm dari speaker
   - Pastikan ruangan relatif sepi (untuk hasil optimal)

2. **Jalankan Analisis**
   - Tap tombol "Start Analysis"
   - Status akan berubah menjadi "🔴 Recording..."
   - Mainkan **white noise** atau **pink noise** melalui speaker (min 10-15 detik untuk hasil akurat)
   - Aplikasi akan menganalisis 32 frekuensi berbeda secara real-time

3. **Interpretasi Hasil (32 Band Equalizer)**
   
   **Level Indikator:**
   - 🔴 Red (≥0 dB): Frekuensi sangat kuat - output maksimal
   - 🟠 Orange (-20 hingga 0 dB): Frekuensi kuat - output baik
   - 🟡 Yellow (-40 hingga -20 dB): Frekuensi medium - output normal
   - 🟢 Green (-60 hingga -40 dB): Frekuensi lemah - responsif terbatas
   - 🔵 Blue (-90 hingga -60 dB): Frekuensi sangat lemah
   - ⚪ White (<-90 dB): Frekuensi tidak terdeteksi

4. **Hentikan Analisis**
   - Tap tombol "Stop" untuk menghentikan recording
   - Hasil akan tetap ditampilkan dengan statistik lengkap (Avg/Max/Min dB)

## Fitur-Fitur Utama

### 📊 32-Band Frequency Analysis
- Distribusi **logaritmik** dari 20 Hz hingga 20 kHz
- Sesuai dengan persepsi pendengaran manusia (human hearing response)
- Sampling rate 44100 Hz dengan FFT 4096 points

### 🎯 Target Frequencies (32 band)
```
20Hz, 25Hz, 31Hz, 39Hz, 49Hz, 62Hz, 78Hz, 98Hz,
123Hz, 155Hz, 195Hz, 246Hz, 310Hz, 391Hz, 492Hz, 620Hz,
781Hz, 984Hz, 1.2kHz, 1.6kHz, 1.9kHz, 2.5kHz, 3.1kHz, 3.9kHz,
4.9kHz, 6.2kHz, 7.8kHz, 9.8kHz, 12.4kHz, 15.6kHz, 19.7kHz, 20kHz
```

### 📈 Interactive Bar Chart
- **Scroll & Zoom** untuk melihat detail setiap frequency band
- Drag untuk navigasi chart
- Real-time update saat recording berlangsung
- Support landscape mode untuk tampilan lebih lebar

### 💾 Dual-Column Display
- Tampilan efisien untuk semua 32 frekuensi
- Statistik ringkas (Average, Max, Min dB)
- Format mudah dibaca dengan indikator visual

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
- Smooth 32-band visualization

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

### ❌ Chart tidak menampilkan data dengan baik
```
Solusi:
- Pastikan sampling rate 44100 Hz mendukung perangkat
- Mainkan noise dengan volume cukup keras (min -40 dB)
- Tunggu 5-10 detik agar FFT stabil
- Coba restart aplikasi
```

### ❌ 32 frekuensi tidak semuanya terdeteksi
```
Solusi:
- Ini NORMAL jika speaker tidak support semua range
- Lihat statistik Average dB untuk performa keseluruhan
- Mainkan pink noise (lebih konsisten dari white noise)
- Perpanjang durasi analisis (min 15 detik)
```

### ❌ Lag atau freeze
```
Solusi:
- Tutup aplikasi lain yang berjalan
- Gunakan release build (optimized)
- Kurangi brightness untuk efisiensi battery
```

### ❌ File "databinding" error saat build
```
Solusi:
- Clean build: ./gradlew clean
- Rebuild: ./gradlew build
- Invalidate cache & restart di Android Studio
```

## Hardware Requirements

| Spesifikasi | Minimum | Recommended |
|------------|---------|------------|
| Android Version | 8.0 (API 26) | 10+ (API 29+) |
| RAM | 2 GB | 4 GB+ |
| Storage | 50 MB | 100 MB |
| Processor | Quad-core | Octa-core |
| Microphone | Built-in | High sensitivity |
| Speaker | Any | Quality speaker (20-20kHz range) |

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
│   │   │       │   └── AudioAnalyzer.kt   # Core FFT engine (32 freq)
│   │   │       └── ui/
│   │   │           └── MainActivity.kt    # UI & 32-band display
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
├── README.md
└── INSTALLATION.md
```

## Testing Notes

### Cara Menggunakan dengan White Noise Generator
1. Buka YouTube atau aplikasi white noise generator
2. Mainkan white noise dengan volume sedang-tinggi
3. Posisikan speaker 30-50cm dari microphone
4. Tap "Start Analysis" di app
5. Biarkan analisis berjalan 10-15 detik untuk hasil optimal
6. Tap "Stop" untuk melihat hasil lengkap dengan 32 frekuensi

### Interpretasi Speaker Performance

**Speaker Bagus (20-20kHz Response):**
- Sebagian besar band menunjukkan 🟠 atau 🟡 (>-40 dB)
- Respons relatif flat di mid-range
- Min/Avg dB mendekati -20 dB

**Speaker Standar (50-15kHz Response):**
- Band bawah 20-50Hz lemah (🟢-🔵)
- Band atas 15-20kHz lemah (🟢-🔵)
- Mid-range kuat (🟠-🟡)

**Speaker Budget (100-10kHz Response):**
- Banyak band rendah/tinggi tidak terdeteksi
- Hanya mid-range yang responsif
- Avg dB di bawah -40 dB

## Support

Untuk masalah atau saran:
1. Cek README.md untuk dokumentasi lengkap
2. Lihat troubleshooting section di atas
3. Create GitHub Issue jika bug ditemukan
4. Test dengan berbagai jenis noise (white, pink, brown)

---

**Happy analyzing dengan 32 frequencies! 🎵📊**
