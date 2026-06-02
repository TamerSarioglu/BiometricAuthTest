# BiometricAuthTest

BiometricAuthTest is a small Android app that demonstrates how to protect a screen with Android biometric authentication. The app starts in a locked state, checks whether strong biometric authentication is available, shows the system biometric prompt, and displays a protected screen only after a successful fingerprint authentication.

## What the App Does

- Shows a locked Compose screen with an "Unlock with Fingerprint" action.
- Checks the device with `BiometricManager.canAuthenticate(BIOMETRIC_STRONG)`.
- Opens the Android biometric prompt through `BiometricPrompt`.
- Unlocks the protected screen when authentication succeeds.
- Shows error messages for failed authentication, unavailable hardware, missing enrollment, prompt cancellation, and unknown biometric errors.
- Opens the Android biometric enrollment settings when no biometric credential is enrolled.
- Lets the user lock the protected screen again.

## Tech Stack

- Kotlin
- Android Jetpack Compose
- Material 3
- AndroidX Biometric
- Hilt dependency injection
- Coroutines and `StateFlow`
- Minimum SDK 30

## Project Structure

```text
app/src/main/java/com/tamersarioglu/biometricauthtest
|-- data/biometric
|   |-- AndroidBiometricRepository.kt
|   |-- BiometricPromptAuthenticator.kt
|-- domain
|   |-- model
|   |-- repository
|   |-- usecase
|-- presentation/biometric
|   |-- BiometricContract.kt
|   |-- BiometricMessages.kt
|   |-- BiometricScreen.kt
|   |-- BiometricViewModel.kt
|-- di
|   |-- BiometricModule.kt
|-- BiometricAuthTestApplication.kt
`-- MainActivity.kt
```

The code is split into presentation, domain, and data layers:

- `presentation` owns the Compose UI, UI state, user intents, and one-shot UI effects.
- `domain` defines biometric models, repository contracts, and use cases.
- `data` connects the domain contract to Android framework APIs.
- `di` binds the repository implementation with Hilt.

## Authentication Flow

1. The app launches `MainActivity`, which renders `BiometricScreen`.
2. `BiometricViewModel` starts with `BiometricUiState.Locked`.
3. The locked UI shows a fingerprint unlock button.
4. When the button is tapped, the UI sends `BiometricIntent.AuthenticateClicked` to the ViewModel.
5. The ViewModel calls `CheckBiometricAvailabilityUseCase`.
6. The use case delegates to `AndroidBiometricRepository.checkAvailability()`.
7. The repository calls `BiometricManager.from(context).canAuthenticate(BIOMETRIC_STRONG)`.
8. If strong biometric auth is available, the ViewModel emits `BiometricEffect.ShowBiometricPrompt`.
9. `BiometricScreen` collects the effect and calls `BiometricPromptAuthenticator.authenticate(...)`.
10. `BiometricPromptAuthenticator` builds a `BiometricPrompt` with title `Secure Sign In`, subtitle `Sign in to the test app`, allowed authenticators `BIOMETRIC_STRONG`, negative button `Cancel`, and confirmation required set to `false`.
11. The system biometric prompt handles fingerprint verification.
12. Prompt callbacks are mapped back into domain results: success becomes `BiometricAuthResult.Success`, failed scans become `AuthenticationFailed`, and prompt errors become `PromptError`.
13. The UI converts the result to a `BiometricIntent` and sends it to the ViewModel.
14. On success, the ViewModel changes state to `BiometricUiState.Authenticated`.
15. Compose reacts to the state change and displays the protected screen.
16. Pressing `Lock` sends `BiometricIntent.LogoutClicked`, returning the app to the locked state.

## Availability and Error Handling

The repository maps Android biometric availability results into app-level domain models:

- `BIOMETRIC_SUCCESS` means authentication can be attempted.
- `BIOMETRIC_ERROR_NONE_ENROLLED` opens biometric enrollment settings.
- `BIOMETRIC_ERROR_NO_HARDWARE` means the device has no biometric hardware.
- `BIOMETRIC_ERROR_HW_UNAVAILABLE` means biometric hardware is temporarily unavailable.
- Any other result is shown as an unknown biometric error message.

When no biometric credential is enrolled, the app starts `Settings.ACTION_BIOMETRIC_ENROLL` with `BIOMETRIC_STRONG` as the requested authenticator type.

## Running the App

Open the project in Android Studio and run the `app` configuration on a device or emulator that supports strong biometric authentication.

You can also build from the command line:

```powershell
.\gradlew.bat assembleDebug
```

For emulator testing, enroll a fingerprint in the emulator settings before testing the unlock flow.

## Tests

Run unit tests:

```powershell
.\gradlew.bat test
```

Run instrumented tests on a connected device or emulator:

```powershell
.\gradlew.bat connectedAndroidTest
```
