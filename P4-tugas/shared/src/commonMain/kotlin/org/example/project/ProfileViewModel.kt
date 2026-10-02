package org.example.project

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProfileUiState(
    val name: String = "Syuhendar",
    val bio: String = "Mahasiswa Teknik Informatika",
    val nameInput: String = "Syuhendar",
    val bioInput: String = "Mahasiswa Teknik Informatika",
    val isDarkMode: Boolean = false
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.update { it.copy(nameInput = name) }
    }

    fun updateBio(bio: String) {
        _uiState.update { it.copy(bioInput = bio) }
    }

    fun saveProfile() {
        _uiState.update {
            it.copy(name = it.nameInput, bio = it.bioInput)
        }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }
}
