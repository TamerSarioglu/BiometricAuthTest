package com.tamersarioglu.biometricauthtest.data.biometric

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import com.tamersarioglu.biometricauthtest.domain.model.BiometricAuthError
import com.tamersarioglu.biometricauthtest.domain.model.BiometricAvailability
import com.tamersarioglu.biometricauthtest.domain.repository.BiometricRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidBiometricRepository @Inject constructor(
    @param:ApplicationContext
    private val context: Context
) : BiometricRepository {

    override fun checkAvailability(): BiometricAvailability {
        val biometricManager = BiometricManager.from(context)

        return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAvailability.Available
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                BiometricAvailability.Unavailable(BiometricAuthError.NoneEnrolled)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                BiometricAvailability.Unavailable(BiometricAuthError.NoHardware)
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                BiometricAvailability.Unavailable(BiometricAuthError.HardwareUnavailable)
            }
            else -> BiometricAvailability.Unavailable(
                BiometricAuthError.Unknown("An unknown error occurred.")
            )
        }
    }

    override fun openEnrollment() {
        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
            putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(enrollIntent)
    }
}
