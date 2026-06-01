package com.minlish.app.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val displayName: String = "",
    val email: String = "",
    val selectedGoal: String = "General",
    val dailyWordTarget: Int = 10,
    val dailyReviewTarget: Int = 20,
    val pushNotifications: Boolean = true,
    val emailNotifications: Boolean = true,
    val reminderTime: String = "20:00",
    val currentStreak: Int = 0,
    val darkModeEnabled: Boolean = false,   // TODO: De day kip lam sau
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ProfileViewModel: ViewModel() {
    private val userRepo = UserRepository()
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    init{
        loadProfileData()
    }
    fun loadProfileData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // Tải song song 3 API
                val profileDeferred = async { userRepo.getProfile() }
                val learningDeferred = async { userRepo.getLearningProfile() }
                val streakDeferred = async { userRepo.getCurrentStreak() }
                val profileRes = profileDeferred.await()
                val learningRes = learningDeferred.await()
                val streakRes = streakDeferred.await()
                if (profileRes.isSuccess && learningRes.isSuccess) {
                    val user = profileRes.getOrThrow()
                    val learning = learningRes.getOrThrow()
                    _uiState.update {
                        it.copy(
                            displayName = user.name,
                            email = user.email,
                            selectedGoal = learning.learningGoal
                                .replaceFirstChar { c -> c.uppercase() },
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
                    val errorMsg = listOf(
                        profileRes.exceptionOrNull()?.message,
                        learningRes.exceptionOrNull()?.message
                    ).filterNotNull().joinToString(", ")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMsg.ifEmpty { "Không tải được dữ liệu" }
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
                } else {
                    _uiState.update {
                        it.copy(isSaving = false, errorMessage = "Cập nhật thất bại")
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