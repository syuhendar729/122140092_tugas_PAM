# News Feed Simulator

Aplikasi Kotlin Multiplatform sederhana untuk mensimulasikan feed berita secara real-time. Target utama proyek ini adalah Android (dan UI bersama juga tersedia untuk iOS).

## Fitur tugas

| Requirement | Implementasi |
|---|---|
| Flow berita tiap 2 detik | `newsStream()` memakai `flow`, `delay(2_000)`, dan `emit()` |
| Filter kategori | `combine(allNews, selectedCategory)` kemudian `filter` |
| Transformasi data | `News.toCard()` mengubah data mentah menjadi model UI `NewsCard` |
| StateFlow berita dibaca | `readCount: StateFlow<Int>` bertambah saat kartu berita ditekan |
| Coroutine async detail | `async { fetchNewsDetail(id) }` dan `await()` saat membuka detail |

## Cara menjalankan

### Android Studio

1. Buka folder proyek ini melalui Android Studio.
2. Tunggu proses Gradle sync selesai.
3. Pilih konfigurasi **androidApp**, kemudian tekan Run pada emulator atau perangkat Android.
4. Tunggu dua detik untuk berita pertama, pilih kategori untuk memfilter, lalu tekan kartu berita untuk melihat detail dan menandainya sebagai sudah dibaca.

### Terminal

Build APK debug dengan:

```bash
./gradlew :androidApp:assembleDebug
```

APK hasil build berada di `androidApp/build/outputs/apk/debug/`.

## Struktur penting

```
shared/src/commonMain/kotlin/org/example/project/
├── App.kt                 # UI Compose
├── NewsModels.kt          # Data class dan fungsi transformasi
└── NewsFeedViewModel.kt   # Flow, StateFlow, filter, dan async detail
```

## Teknologi

- Kotlin Multiplatform
- Compose Multiplatform / Material 3
- Kotlin Coroutines dan Flow
