package com.tamersarioglu.biometricauthtest.di

import com.tamersarioglu.biometricauthtest.data.biometric.AndroidBiometricRepository
import com.tamersarioglu.biometricauthtest.domain.repository.BiometricRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BiometricModule {

    @Binds
    @Singleton
    abstract fun bindBiometricRepository(
        repository: AndroidBiometricRepository
    ): BiometricRepository
}
