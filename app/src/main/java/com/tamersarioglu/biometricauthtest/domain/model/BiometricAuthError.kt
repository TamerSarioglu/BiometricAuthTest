package com.tamersarioglu.biometricauthtest.domain.model

sealed interface BiometricAuthError {
    data object NoHardware : BiometricAuthError
    data object HardwareUnavailable : BiometricAuthError
    data object NoneEnrolled : BiometricAuthError
    data object AuthenticationFailed : BiometricAuthError
    data class PromptError(val message: String) : BiometricAuthError
    data class Unknown(val message: String) : BiometricAuthError
}
