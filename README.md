# MinLish Mobile

Ứng dụng học từ vựng Android sử dụng thuật toán lặp lại ngắt quãng SM-2.

---

## Tech Stack

| Layer | Công nghệ |
|---|---|
| UI | Jetpack Compose + Material3 |
| Architecture | Clean Architecture (MVVM) |
| Network | Retrofit + OkHttp |
| Local DB | Room Database |
| DI | Hilt |
| Async | Coroutines + Flow |
| Background | WorkManager |
| Security | EncryptedSharedPreferences |

---

## Cấu trúc Project

```
MinlishMobile/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       │
│       ├── java/com/minlish/app/
│       │   │
│       │   ├── MinlishApp.kt                         # Application class
│       │   ├── MainActivity.kt                       # Entry point
│       │   │
│       │   ├── di/                                   # Dependency Injection (Hilt)
│       │   │   ├── AppModule.kt
│       │   │   ├── NetworkModule.kt                  # Retrofit + OkHttp
│       │   │   ├── DatabaseModule.kt                 # Room
│       │   │   ├── RepositoryModule.kt
│       │   │   └── SyncModule.kt                     # WorkManager
│       │   │
│       │   ├── data/                                 # Data Layer
│       │   │   │
│       │   │   ├── remote/                           # API calls
│       │   │   │   ├── AuthApi.kt                    # Login, Register, Refresh token
│       │   │   │   ├── VocabApi.kt                   # Vocab sets, Words
│       │   │   │   ├── LearningApi.kt                # SM-2 endpoints
│       │   │   │   └── interceptor/
│       │   │   │       ├── AuthInterceptor.kt        # Đính kèm JWT vào request
│       │   │   │       └── RefreshInterceptor.kt     # Tự động refresh token
│       │   │   │
│       │   │   ├── local/                            # Offline storage (Room)
│       │   │   │   ├── MinlishDatabase.kt
│       │   │   │   ├── TypeConverters.kt
│       │   │   │   ├── TokenStorage.kt               # EncryptedSharedPreferences
│       │   │   │   ├── dao/
│       │   │   │   │   ├── VocabSetDao.kt
│       │   │   │   │   ├── WordDao.kt
│       │   │   │   │   ├── LearningProgressDao.kt
│       │   │   │   │   └── UserDao.kt
│       │   │   │   └── entity/
│       │   │   │       ├── VocabSetEntity.kt
│       │   │   │       ├── WordEntity.kt
│       │   │   │       ├── LearningProgressEntity.kt
│       │   │   │       └── UserEntity.kt
│       │   │   │
│       │   │   ├── dto/                              # Data Transfer Objects (khớp backend)
│       │   │   │   ├── LoginRequest.kt
│       │   │   │   ├── LoginResponse.kt
│       │   │   │   ├── ApiResponse.kt
│       │   │   │   ├── AuthResponse.kt
│       │   │   │   ├── VocabSetDto.kt
│       │   │   │   ├── WordDto.kt
│       │   │   │   ├── LearningProgressDto.kt
│       │   │   │   └── PaginationDto.kt
│       │   │   │
│       │   │   ├── mapper/                           # Chuyển đổi DTO ↔ Entity ↔ Domain
│       │   │   │   ├── VocabSetMapper.kt
│       │   │   │   ├── WordMapper.kt
│       │   │   │   └── LearningProgressMapper.kt
│       │   │   │
│       │   │   ├── preferences/
│       │   │   │   └── UserPreferences.kt
│       │   │   │
│       │   │   ├── sync/                             # Đồng bộ dữ liệu offline
│       │   │   │   ├── SyncManager.kt
│       │   │   │   ├── SyncWorker.kt
│       │   │   │   └── SyncConflictResolver.kt
│       │   │   │
│       │   │   └── repository/                       # Implement business logic
│       │   │       ├── AuthRepository.kt
│       │   │       ├── VocabRepository.kt
│       │   │       ├── LearningRepository.kt
│       │   │       └── UserRepository.kt
│       │   │
│       │   ├── domain/                               # Domain Layer
│       │   │   │
│       │   │   ├── model/                            # Domain entities
│       │   │   │   ├── User.kt
│       │   │   │   ├── VocabSet.kt
│       │   │   │   ├── Word.kt
│       │   │   │   └── LearningProgress.kt
│       │   │   │
│       │   │   ├── usecase/
│       │   │   │   ├── auth/
│       │   │   │   │   ├── LoginUseCase.kt
│       │   │   │   │   ├── RegisterUseCase.kt
│       │   │   │   │   └── RefreshTokenUseCase.kt
│       │   │   │   ├── vocab/
│       │   │   │   │   ├── FetchVocabSetsUseCase.kt
│       │   │   │   │   ├── CreateVocabSetUseCase.kt
│       │   │   │   │   └── CloneVocabSetUseCase.kt
│       │   │   │   └── learning/
│       │   │   │       ├── GetLearningQueueUseCase.kt
│       │   │   │       ├── SubmitReviewUseCase.kt
│       │   │   │       └── CalculateSM2UseCase.kt
│       │   │   │
│       │   │   └── Result.kt                         # Sealed class xử lý kết quả
│       │   │
│       │   ├── presentation/                         # UI Layer
│       │   │   │
│       │   │   ├── theme/
│       │   │   │   ├── Color.kt
│       │   │   │   ├── Type.kt
│       │   │   │   ├── Theme.kt
│       │   │   │   └── Dimensions.kt
│       │   │   │
│       │   │   ├── components/                       # Reusable Composables
│       │   │   │   ├── MinlishButton.kt
│       │   │   │   ├── MinlishTextField.kt
│       │   │   │   ├── LoadingIndicator.kt
│       │   │   │   ├── ErrorView.kt
│       │   │   │   └── EmptyState.kt
│       │   │   │
│       │   │   ├── navigation/
│       │   │   │   ├── NavDestinations.kt
│       │   │   │   ├── MinlishNavHost.kt
│       │   │   │   └── BottomNavGraph.kt
│       │   │   │
│       │   │   └── screens/
│       │   │       ├── welcome/
│       │   │       │   ├── WelcomeScreen.kt
│       │   │       │   └── WelcomeViewModel.kt
│       │   │       │
│       │   │       ├── auth/
│       │   │       │   ├── LoginScreen.kt            # ← Đang làm
│       │   │       │   ├── RegisterScreen.kt
│       │   │       │   ├── VerifyEmailScreen.kt
│       │   │       │   └── AuthViewModel.kt
│       │   │       │
│       │   │       ├── dashboard/
│       │   │       │   ├── DashboardScreen.kt
│       │   │       │   └── DashboardViewModel.kt
│       │   │       │
│       │   │       ├── vocab/
│       │   │       │   ├── VocabSetListScreen.kt
│       │   │       │   ├── VocabSetDetailScreen.kt
│       │   │       │   ├── CreateVocabSetScreen.kt
│       │   │       │   ├── WordListScreen.kt
│       │   │       │   └── VocabViewModel.kt
│       │   │       │
│       │   │       ├── learning/
│       │   │       │   ├── FlashcardScreen.kt
│       │   │       │   ├── FlashcardViewModel.kt
│       │   │       │   ├── ReviewQueueScreen.kt
│       │   │       │   └── SM2RatingBottomSheet.kt
│       │   │       │
│       │   │       ├── explore/
│       │   │       │   ├── ExploreScreen.kt
│       │   │       │   └── ExploreViewModel.kt
│       │   │       │
│       │   │       └── profile/
│       │   │           ├── ProfileScreen.kt
│       │   │           ├── SettingsScreen.kt
│       │   │           └── ProfileViewModel.kt
│       │   │
│       │   └── util/
│       │       ├── NetworkResult.kt
│       │       ├── Constants.kt
│       │       ├── Extensions.kt
│       │       ├── SM2Algorithm.kt
│       │       └── ConnectivityObserver.kt
│       │
│       └── res/
│           ├── drawable/
│           │   └── ic_google.xml                     # Google logo SVG
│           ├── values/
│           │   ├── colors.xml
│           │   ├── strings.xml
│           │   └── themes.xml
│           └── font/
│
├── gradle.properties
├── settings.gradle.kts
└── build.gradle.kts
```

---

## Luồng dữ liệu

```
UI (Screen)
    ↓ ↑
ViewModel
    ↓ ↑
UseCase          ← domain layer, không biết về Android
    ↓ ↑
Repository       ← quyết định lấy data từ remote hay local
    ↓ ↑
Remote / Local   ← Retrofit API hoặc Room Database
```

---

## Luồng Authentication

```
LoginScreen → AuthViewModel.login()
                  ↓
             LoginUseCase
                  ↓
             AuthRepository.login()
                  ↓
             AuthApi.login()  ──→  POST /auth/login
                  ↓
             TokenStorage.save(token)
                  ↓
             Navigate → Dashboard
```

---

## Dependencies chính

```kotlin
// Jetpack Compose
implementation("androidx.compose.material3:material3:1.2.1")
implementation("androidx.compose.material:material-icons-extended:1.6.7")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

// Network
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// WorkManager
implementation("androidx.work:work-runtime-ktx:2.9.0")
```

---

## Tiến độ

| Screen | Status |
|---|---|
| LoginScreen | 🔄 Đang làm |
| RegisterScreen | ⬜ Chưa làm |
| DashboardScreen | ⬜ Chưa làm |
| VocabSetListScreen | ⬜ Chưa làm |
| FlashcardScreen | ⬜ Chưa làm |
| ExploreScreen | ⬜ Chưa làm |
| ProfileScreen | ⬜ Chưa làm |