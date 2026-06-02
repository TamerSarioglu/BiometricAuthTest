package com.tamersarioglu.biometricauthtest.domain.repository

import com.tamersarioglu.biometricauthtest.domain.model.BiometricAvailability

interface BiometricRepository {
    fun checkAvailability(): BiometricAvailability
    fun openEnrollment()
}
