package com.hbaez.onboarding_presentation.weight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hbaez.core.domain.preferences.Preferences
import com.hbaez.core.util.UiEvent
import com.hbaez.core.util.UiText
import com.hbaez.core.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private val preferences: Preferences,
): ViewModel() {

    var weight by mutableStateOf("180")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onWeightChange(weight: String) {
        this.weight = weight
    }
    fun onNextClick() {
        viewModelScope.launch {
            val weightNumber = weight.toFloatOrNull() ?: kotlin.run {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(R.string.error_weight_cant_be_empty)
                    )
                )
                return@launch
            }
            preferences.saveWeight(weightNumber)
            _uiEvent.send(UiEvent.Success)
        }
    }
}