package com.tamersarioglu.biometricauthtest

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.tamersarioglu.biometricauthtest.presentation.biometric.BiometricScreen
import com.tamersarioglu.biometricauthtest.ui.theme.BiometricAuthTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BiometricAuthTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    BiometricScreen()
                }
            }
        }
    }
}
