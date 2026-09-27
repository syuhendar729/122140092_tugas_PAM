package org.example.project

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewsFeedViewModel {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val selectedCategory = MutableStateFlow(Category.ALL)
    private val allNews = MutableStateFlow<List<News>>(emptyList())
    private val readIds = mutableSetOf<Int>()

    // Requirement: StateFlow menyimpan jumlah berita yang telah dibaca.
    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount
    val category: StateFlow<Category> = selectedCategory

    // Filter + transform dari News ke NewsCard dilakukan secara reaktif.
    val displayedNews: StateFlow<List<NewsCard>> = combine(allNews, selectedCategory) { news, filter ->
        news.filter { filter == Category.ALL || it.category == filter }.map { it.toCard() }
    }.stateIn(scope, SharingStarted.Eagerly, emptyList())

    private val _selectedDetail = MutableStateFlow<NewsDetail?>(null)
    val selectedDetail: StateFlow<NewsDetail?> = _selectedDetail
    private val _isLoadingDetail = MutableStateFlow(false)
    val isLoadingDetail: StateFlow<Boolean> = _isLoadingDetail

    init {
        scope.launch { newsStream().collect { freshNews -> allNews.value = listOf(freshNews) + allNews.value } }
    }

    fun selectCategory(category: Category) { selectedCategory.value = category }

    fun openNews(id: Int) {
        markAsRead(id)
        scope.launch {
            _isLoadingDetail.value = true
            // Requirement: detail diambil asynchronous menggunakan coroutine async.
            val detailDeferred = async { fetchNewsDetail(id) }
            _selectedDetail.value = detailDeferred.await()
            _isLoadingDetail.value = false
        }
    }

    fun closeDetail() { _selectedDetail.value = null }
    private fun markAsRead(id: Int) { if (readIds.add(id)) _readCount.value = readIds.size }
    fun clear() = scope.cancel()
}

/** Flow yang mengirim sebuah berita baru setiap dua detik. */
fun newsStream(): Flow<News> = flow {
    val samples = listOf(
        Triple("Startup lokal meluncurkan fitur AI untuk UMKM", Category.TECHNOLOGY, "Teknologi baru membantu pelaku usaha mengelola pelanggan."),
        Triple("Tim nasional menang dramatis di laga persahabatan", Category.SPORTS, "Gol pada menit akhir memastikan kemenangan penting."),
        Triple("Indeks pasar menguat setelah pembukaan perdagangan", Category.BUSINESS, "Saham sektor energi dan perbankan menjadi pendorong utama."),
        Triple("Festival film daerah menarik ribuan pengunjung", Category.ENTERTAINMENT, "Karya sineas muda mendapat sambutan hangat dari penonton."),
    )
    var id = 1
    while (true) {
        delay(2_000)
        val sample = samples[(id - 1) % samples.size]
        emit(News(id, sample.first, sample.second, sample.third, id * 2))
        id++
    }
}

private suspend fun fetchNewsDetail(id: Int): NewsDetail {
    delay(700) // simulasi request ke API
    return NewsDetail(id, "Detail berita #$id", "Ini adalah detail berita nomor $id yang diambil secara asynchronous. Pada aplikasi nyata, bagian ini dapat diganti dengan pemanggilan API menggunakan coroutine.")
}
