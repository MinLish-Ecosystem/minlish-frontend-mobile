package com.minlish.app.presentation.screens.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.local.TokenManager
import com.minlish.app.data.local.entity.UserEntity
import com.minlish.app.data.repository.AuthRepository
import com.minlish.app.data.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val displayName: String = "",
    val email: String = "",
    val avatar: String? = null,
    val joinYear: String = "2026",
    val userLevel: String = "Beginner",
    val selectedGoal: String = "General",
    val dailyWordTarget: Int = 10,
    val dailyReviewTarget: Int = 20,
    val pushNotifications: Boolean = true,
    val emailNotifications: Boolean = true,
    val reminderTime: String = "20:00",
    val currentStreak: Int = 0,
    val darkModeEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ProfileViewModel(private val userRepo: UserRepository = UserRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    val userProfile = userRepo.getLocalUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            userRepo.refreshUserProfile()
        }

        viewModelScope.launch {
            userProfile.collect { user ->
                user?.let { u ->
                    val year = u.createdAt?.substring(0, 4) ?: "2026"
                    _uiState.update {
                        it.copy(
                            displayName = u.name,
                            email = u.email,
                            avatar = u.avatar,
                            joinYear = year
                        )
                    }
                }
            }
        }
        loadProfileData()
    }

    fun loadProfileData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val learningDeferred = async { userRepo.getLearningProfile() }
                val streakDeferred = async { userRepo.getCurrentStreak() }

                val learningRes = learningDeferred.await()
                val streakRes = streakDeferred.await()

                if (learningRes.isSuccess) {
                    val learning = learningRes.getOrThrow()
                    val rawLevel = learning.currentLevel ?: "beginner"
                    val formattedLevel = rawLevel.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } + " Learner"

                    _uiState.update {
                        it.copy(
                            userLevel = formattedLevel,
                            selectedGoal = learning.learningGoal.replaceFirstChar { c -> c.uppercase() },
                            dailyWordTarget = learning.dailyGoal,
                            dailyReviewTarget = learning.reviewPerDay,
                            pushNotifications = learning.preferences.pushNotification,
                            emailNotifications = learning.preferences.emailNotification,
                            reminderTime = learning.reminderTime,
                            currentStreak = streakRes.getOrDefault(0),
                            isLoading = false
                        )
                    }
                } else {
                    val errorMsg = learningRes.exceptionOrNull()?.message
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMsg ?: "Không tải được cấu hình học tập"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

    fun updateDisplayName(value: String) { _uiState.update { it.copy(displayName = value) } }
    fun updateEmail(value: String) { _uiState.update { it.copy(email = value) } }
    fun updateSelectedGoal(value: String) { _uiState.update { it.copy(selectedGoal = value) } }
    fun updateWordTarget(value: Int) { _uiState.update { it.copy(dailyWordTarget = value) } }
    fun updateReviewTarget(value: Int) { _uiState.update { it.copy(dailyReviewTarget = value) } }
    fun updatePushNotifications(value: Boolean) { _uiState.update { it.copy(pushNotifications = value) } }
    fun updateEmailNotifications(value: Boolean) { _uiState.update { it.copy(emailNotifications = value) } }
    fun updateReminderTime(value: String) { _uiState.update { it.copy(reminderTime = value) } }
    fun updateDarkMode(value: Boolean) { _uiState.update { it.copy(darkModeEnabled = value) } }

    fun uploadAvatar(base64Image: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, saveSuccess = false, errorMessage = null) }
            try {
                val result = userRepo.updateProfile(_uiState.value.displayName, base64Image)
                if (result.isSuccess) {
                    val user = result.getOrThrow()
                    _uiState.update {
                        it.copy(
                            avatar = user.avatar,
                            isSaving = false,
                            saveSuccess = true
                        )
                    }
                    // Đồng bộ lưu ảnh mới vào DB cục bộ
                    userRepo.refreshUserProfile()
                } else {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = result.exceptionOrNull()?.message ?: "Upload avatar failed"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSaving = false, errorMessage = e.message)
                }
            }
        }
    }

    fun saveChanges() {
        val s = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, saveSuccess = false, errorMessage = null) }
            try {
                val saveProfileD = async { userRepo.updateProfile(s.displayName) }
                val saveLearningD = async {
                    userRepo.updateLearningProfile(
                        goal = s.selectedGoal,
                        dailyGoal = s.dailyWordTarget,
                        reviewPerDay = s.dailyReviewTarget,
                        reminderTime = s.reminderTime,
                        pushNotification = s.pushNotifications,
                        emailNotification = s.emailNotifications
                    )
                }
                val profileRes = saveProfileD.await()
                val learningRes = saveLearningD.await()

                if (profileRes.isSuccess && learningRes.isSuccess) {
                    _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                    // Đồng bộ lưu thông tin mới vào DB cục bộ
                    userRepo.refreshUserProfile()
                } else {
                    val errorMsg = listOf(
                        profileRes.exceptionOrNull()?.message,
                        learningRes.exceptionOrNull()?.message
                    ).filterNotNull().joinToString(", ")
                    _uiState.update {
                        it.copy(isSaving = false, errorMessage = errorMsg.ifEmpty { "Cập nhật thất bại" })
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSaving = false, errorMessage = e.message)
                }
            }
        }
    }

    fun dismissSuccess() { _uiState.update { it.copy(saveSuccess = false) } }
    fun dismissError() { _uiState.update { it.copy(errorMessage = null) } }
}