package com.tamersarioglu.biometricauthtest.presentation.biometric

sealed interface BiometricUiState {
    val message: String

    data class Locked(
        override val message: String = "Waiting for fingerprint authentication..."
    ) : BiometricUiState

    data class Authenticated(
        override val message: String = "Sign in successful."
    ) : BiometricUiState
}

sealed interface BiometricIntent {
    data object AuthenticateClicked : BiometricIntent
    data object LogoutClicked : BiometricIntent
    data object PromptSucceeded : BiometricIntent
    data class PromptFailed(val message: String) : BiometricIntent
}

sealed interface BiometricEffect {
    data object ShowBiometricPrompt : BiometricEffect
    data object OpenBiometricEnrollment : BiometricEffect
}
