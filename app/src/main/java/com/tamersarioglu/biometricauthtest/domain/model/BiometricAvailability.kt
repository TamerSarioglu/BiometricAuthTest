package com.tamersarioglu.biometricauthtest.domain.model

sealed interface BiometricAvailability {
    data object Available : BiometricAvailability
    data class Unavailable(val error: BiometricAuthError) : BiometricAvailability
}
