package com.hbaez.settings_presentation.settings_overview

data class AppSettingsState(
    val isAnonymous: Boolean = true,
    val userId: String = ""
)
