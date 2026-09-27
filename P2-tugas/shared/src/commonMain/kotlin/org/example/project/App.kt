package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    val viewModel = remember { NewsFeedViewModel() }
    DisposableEffect(viewModel) { onDispose { viewModel.clear() } }
    val news by viewModel.displayedNews.collectAsState()
    val selectedCategory by viewModel.category.collectAsState()
    val readCount by viewModel.readCount.collectAsState()
    val detail by viewModel.selectedDetail.collectAsState()
    val loadingDetail by viewModel.isLoadingDetail.collectAsState()

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                .safeContentPadding().padding(16.dp),
        ) {
            Text("News Feed", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Simulator berita baru setiap 2 detik", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))
            Text("✓ $readCount berita sudah dibaca", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))
            CategoryFilter(selectedCategory, viewModel::selectCategory)
            Spacer(Modifier.height(12.dp))

            if (loadingDetail) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(Modifier.width(20.dp).height(20.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Mengambil detail berita...")
                }
            } else if (detail != null) {
                DetailCard(detail!!, viewModel::closeDetail)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(news, key = { it.id }) { item -> NewsCardItem(item) { viewModel.openNews(item.id) } }
            }
        }
    }
}

@Composable
private fun CategoryFilter(selected: Category, onSelected: (Category) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        Category.entries.forEach { category ->
            AssistChip(
                onClick = { onSelected(category) },
                label = { Text(if (selected == category) "✓ ${category.label}" else category.label) },
            )
        }
    }
}

@Composable
private fun NewsCardItem(news: NewsCard, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(news.categoryEmoji)
                Spacer(Modifier.width(6.dp))
                Text(news.categoryLabel, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.weight(1f))
                Text(news.timeLabel, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(Modifier.height(6.dp))
            Text(news.headline, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(news.preview, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun DetailCard(detail: NewsDetail, close: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
    ) {
        Column(Modifier.padding(14.dp)) {
            Row {
                Text("Detail berita", fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text("Tutup", modifier = Modifier.clickable(onClick = close), color = MaterialTheme.colorScheme.primary)
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Text(detail.title, fontWeight = FontWeight.Bold)
            Text(detail.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

