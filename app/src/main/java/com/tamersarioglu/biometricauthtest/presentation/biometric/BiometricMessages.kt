package com.tamersarioglu.biometricauthtest.presentation.biometric

import com.tamersarioglu.biometricauthtest.domain.model.BiometricAuthError
import com.tamersarioglu.biometricauthtest.domain.model.BiometricAuthResult

fun BiometricAuthResult.toIntent(): BiometricIntent {
    return when (this) {
        BiometricAuthResult.Success -> BiometricIntent.PromptSucceeded
        is BiometricAuthResult.Error -> BiometricIntent.PromptFailed(error.toMessage())
    }
}

fun BiometricAuthError.toMessage(): String {
    return when (this) {
        BiometricAuthError.AuthenticationFailed -> "Authentication failed. Try again."
        BiometricAuthError.HardwareUnavailable -> "Biometric hardware is currently unavailable."
        BiometricAuthError.NoHardware -> "This device does not have biometric hardware."
        BiometricAuthError.NoneEnrolled -> "No biometric credentials found. Opening settings..."
        is BiometricAuthError.PromptError -> "Error: $message"
        is BiometricAuthError.Unknown -> message
    }
}
