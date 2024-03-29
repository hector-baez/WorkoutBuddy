package com.hbaez.settings_presentation.settings_overview

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbaez.core.domain.model.ActivityLevel
import com.hbaez.core.domain.model.Gender
import com.hbaez.core.domain.model.GoalType
import com.hbaez.core.domain.model.UserInfo
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.user_auth_presentation.AuthViewModel
import com.hbaez.user_auth_presentation.model.service.AccountService
import com.hbaez.user_auth_presentation.model.service.LogService
import com.hbaez.user_auth_presentation.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService,
    private val preferences: Preferences,
    logService: LogService
): AuthViewModel(logService){

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    var uiState by mutableStateOf(AppSettingsUiState(
        preferences = preferences.loadUserInfo()
    ))
        private set
    val state = accountService.currentUser.map {
        AppSettingsState(isAnonymous = it.isAnonymous, userId = it.id)
    }

    fun onEvent(event: AppSettingsEvent){
        when(event) {
            is AppSettingsEvent.OnLogoutButtonClick -> {
                uiState = uiState.copy(
                    shouldShowLogoutCard = !uiState.shouldShowLogoutCard
                )
            }

            is AppSettingsEvent.OnSignOut -> {
                launchCatching {
                    accountService.signOut()
                    storageService.saveUserInfo(preferences.loadUserInfo())
                }
            }
            is AppSettingsEvent.OnDeleteButtonClick -> {
                uiState = uiState.copy(
                    shouldShowDeleteCard = !uiState.shouldShowDeleteCard
                )
            }
            is AppSettingsEvent.OnDeleteAccount -> {
                launchCatching {
                    storageService.deleteAllForUser(accountService.currentUserId)
                    accountService.deleteAccount()
                }
            }

            AppSettingsEvent.RefreshPreferences -> {
                uiState = uiState.copy(
                    preferences = preferences.loadUserInfo()
                )
            }

            is AppSettingsEvent.UpdateTimer -> {
                if(event.isJump){
                    preferences.saveTimerJump(event.time)
                } else {
                    preferences.saveTimerSeconds(event.time)
                }
            }
        }
    }
}