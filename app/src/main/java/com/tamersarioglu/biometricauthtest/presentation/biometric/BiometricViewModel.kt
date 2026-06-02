package com.tamersarioglu.biometricauthtest.presentation.biometric

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.biometricauthtest.domain.model.BiometricAuthError
import com.tamersarioglu.biometricauthtest.domain.model.BiometricAvailability
import com.tamersarioglu.biometricauthtest.domain.usecase.CheckBiometricAvailabilityUseCase
import com.tamersarioglu.biometricauthtest.domain.usecase.OpenBiometricEnrollmentUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BiometricViewModel @Inject constructor(
    private val checkBiometricAvailability: CheckBiometricAvailabilityUseCase,
    private val openBiometricEnrollment: OpenBiometricEnrollmentUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<BiometricUiState>(BiometricUiState.Locked())
    val state: StateFlow<BiometricUiState> = _state.asStateFlow()

    private val _effect = Channel<BiometricEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: BiometricIntent) {
        when (intent) {
            is BiometricIntent.AuthenticateClicked -> authenticate()
            is BiometricIntent.LogoutClicked -> {
                _state.value = BiometricUiState.Locked("Screen locked.")
            }
            is BiometricIntent.PromptSucceeded -> {
                _state.value = BiometricUiState.Authenticated()
            }
            is BiometricIntent.PromptFailed -> {
                _state.value = BiometricUiState.Locked(intent.message)
            }
        }
    }

    private fun authenticate() {
        when (val availability = checkBiometricAvailability()) {
            is BiometricAvailability.Available -> sendEffect(BiometricEffect.ShowBiometricPrompt)
            is BiometricAvailability.Unavailable -> handleUnavailable(availability.error)
        }
    }

    private fun handleUnavailable(error: BiometricAuthError) {
        _state.value = BiometricUiState.Locked(error.toMessage())
        if (error is BiometricAuthError.NoneEnrolled) {
            openBiometricEnrollment()
            sendEffect(BiometricEffect.OpenBiometricEnrollment)
        }
    }

    private fun sendEffect(effect: BiometricEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
