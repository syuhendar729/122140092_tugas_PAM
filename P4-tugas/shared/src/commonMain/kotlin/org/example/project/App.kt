package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun App(profileViewModel: ProfileViewModel = viewModel()) {
    val uiState by profileViewModel.uiState.collectAsState()

    MaterialTheme(
        colorScheme = if (uiState.isDarkMode) {
            androidx.compose.material3.darkColorScheme()
        } else {
            androidx.compose.material3.lightColorScheme()
        }
    ) {
        ProfileScreen(
            uiState = uiState,
            onNameChanged = profileViewModel::updateName,
            onBioChanged = profileViewModel::updateBio,
            onSave = profileViewModel::saveProfile,
            onDarkModeChanged = { profileViewModel.toggleDarkMode() }
        )
    }
}

@Composable
private fun ProfileScreen(
    uiState: ProfileUiState,
    onNameChanged: (String) -> Unit,
    onBioChanged: (String) -> Unit,
    onSave: () -> Unit,
    onDarkModeChanged: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.name.take(1).uppercase(),
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(top = 19.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(uiState.name, style = MaterialTheme.typography.titleLarge)
                    Text(
                        uiState.bio,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Edit profile", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = uiState.nameInput,
                        onValueChange = onNameChanged,
                        label = { Text("Nama") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = uiState.bioInput,
                        onValueChange = onBioChanged,
                        label = { Text("Bio") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onSave,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Simpan perubahan")
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Dark mode", style = MaterialTheme.typography.titleMedium)
                        Text(
                            if (uiState.isDarkMode) "Mode gelap aktif" else "Mode terang aktif",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = uiState.isDarkMode,
                        onCheckedChange = { onDarkModeChanged() }
                    )
                }
            }
        }
    }
}
