package com.tamersarioglu.biometricauthtest.data.biometric

import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.tamersarioglu.biometricauthtest.domain.model.BiometricAuthError
import com.tamersarioglu.biometricauthtest.domain.model.BiometricAuthResult

class BiometricPromptAuthenticator(
    private val activity: FragmentActivity
) {
    fun authenticate(onResult: (BiometricAuthResult) -> Unit) {
        val biometricPrompt = BiometricPrompt(
            activity,
            ContextCompat.getMainExecutor(activity),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onResult(
                        BiometricAuthResult.Error(
                            BiometricAuthError.PromptError(errString.toString())
                        )
                    )
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onResult(BiometricAuthResult.Success)
                }

                override fun onAuthenticationFailed() {
                    onResult(BiometricAuthResult.Error(BiometricAuthError.AuthenticationFailed))
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Secure Sign In")
            .setSubtitle("Sign in to the test app")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .setNegativeButtonText("Cancel")
            .setConfirmationRequired(false)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
