package com.tamersarioglu.biometricauthtest.domain.usecase

import com.tamersarioglu.biometricauthtest.domain.repository.BiometricRepository
import javax.inject.Inject

class OpenBiometricEnrollmentUseCase @Inject constructor(
    private val repository: BiometricRepository
) {
    operator fun invoke() = repository.openEnrollment()
}
