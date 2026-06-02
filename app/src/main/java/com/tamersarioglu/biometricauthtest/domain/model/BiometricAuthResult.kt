package com.tamersarioglu.biometricauthtest.domain.model

sealed interface BiometricAuthResult {
    data object Success : BiometricAuthResult
    data class Error(val error: BiometricAuthError) : BiometricAuthResult
}
