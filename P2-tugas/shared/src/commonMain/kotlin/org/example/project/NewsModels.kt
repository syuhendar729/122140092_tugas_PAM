package org.example.project

/** Data mentah yang diterima dari sumber berita. */
data class News(
    val id: Int,
    val title: String,
    val category: Category,
    val summary: String,
    val minutesAgo: Int,
)

enum class Category(val label: String, val emoji: String) {
    ALL("Semua", "📰"),
    TECHNOLOGY("Teknologi", "💻"),
    SPORTS("Olahraga", "⚽"),
    BUSINESS("Bisnis", "📈"),
    ENTERTAINMENT("Hiburan", "🎬"),
}

/** Format yang memang dipakai oleh tampilan. */
data class NewsCard(
    val id: Int,
    val headline: String,
    val categoryLabel: String,
    val categoryEmoji: String,
    val timeLabel: String,
    val preview: String,
)

data class NewsDetail(val id: Int, val title: String, val content: String)

fun News.toCard(): NewsCard = NewsCard(
    id = id,
    headline = title,
    categoryLabel = category.label,
    categoryEmoji = category.emoji,
    timeLabel = "$minutesAgo menit lalu",
    preview = summary,
)
