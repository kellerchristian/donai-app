# AGENTS.md - AI Coding Assistant Guide for DonAI Mobile

## Project Overview

**DonAI Mobile** is a Kotlin Multiplatform (KMP) blood donation platform connecting donors and recipients. The architecture enforces strict Clean Architecture principles with shared business logic (Kotlin) across platforms and native UI per platform.

- **Status:** MVP stage, Android UI implemented (Jetpack Compose), iOS planned (SwiftUI)
- **Key Constraint:** Shared module must NOT reference Android APIs, navigation, or UI frameworks
- **Backend:** REST API (Ktor Client) - see https://github.com/kellerchristian/donai-api

---

## Critical Architecture Rules (Non-Negotiable)

### Dependency Flow (Enforce Strictly)
```
UI (androidApp) → ViewModel → UseCase → Repository Interface → Data Layer → Ktor API
```

**Forbidden patterns:**
- Domain layer depending on Data or Android libraries
- Composables calling repositories directly
- Raw exceptions propagating to UI
- DTOs exposed outside data layer
- Shared module importing `android.*` packages

### Layer Responsibilities

| Layer | Location | Owns | Never Has |
|-------|----------|------|-----------|
| **Domain** | `shared/domain/` | Models, Repository interfaces, UseCases, business rules | Android, Ktor, Serialization, DTOs |
| **Data** | `shared/data/` | DTOs, Mappers, Repository implementations, Ktor API calls, caching | UI logic, domain directly |
| **Presentation** | `shared/presentation/` | ViewModels, UiState (sealed/data classes), state emissions | Business logic |
| **UI** | `androidApp/screens/` | Jetpack Compose, Navigation Compose, event callbacks | State mutations, business logic |

---

## Essential Patterns & Examples

### 1. StateFlow-Based UiState (Immutable)
```kotlin
// ✅ Correct pattern
data class HomeUiState(
    val isLoading: Boolean = false,
    val requests: List<BloodRequest> = emptyList(),
    val error: String? = null
)

// In ViewModel:
private val _uiState = MutableStateFlow(HomeUiState())
val uiState = _uiState.asStateFlow()

// Update pattern:
_uiState.value = _uiState.value.copy(isLoading = true)
```

### 2. UseCase with `operator invoke`
```kotlin
class GetBloodRequestsUseCase(
    private val repository: RequestRepository
) {
    suspend operator fun invoke(userId: String): List<BloodRequest> {
        return repository.fetchRequests(userId)
    }
}
```

### 3. Repository Pattern (Interface in Domain, Implementation in Data)
```kotlin
// domain/repository/UserRepository.kt
interface UserRepository {
    suspend fun getUser(id: String): User
}

// data/repository/UserRepositoryImpl.kt
class UserRepositoryImpl(private val api: UserApi) : UserRepository {
    override suspend fun getUser(id: String): User {
        return api.getUser(id).toDomain()  // DTO → Domain mapping
    }
}
```

### 4. Mandatory DTO → Domain Mapping
```kotlin
// data/remote/dto/UserDto.kt
@Serializable
data class UserDto(
    @SerialName("user_id") val userId: String,
    val name: String
)

// data/remote/mapper/UserMapper.kt
fun UserDto.toDomain(): User = User(id = userId, name = name)

// ✅ Always: api.getUser().toDomain()
// ❌ Never: direct DTO exposure
```

### 5. Stateless Composables with Events
```kotlin
@Composable
fun LoginScreen(
    uiState: LoginUiState,
    events: LoginEvents,  // Data class holding callbacks
    modifier: Modifier = Modifier
) {
    Button(onClick = { events.onLoginClick(email, password) }) {
        Text("Login")
    }
}

data class LoginEvents(
    val onEmailChange: (String) -> Unit,
    val onPasswordChange: (String) -> Unit,
    val onLoginClick: (String, String) -> Unit
)
```

---

## Build & Development Commands

```bash
# Build shared module (KMP)
./gradlew :shared:build

# Build Android app
./gradlew :androidApp:build

# Run Android app on emulator
./gradlew :androidApp:installDebug

# Run tests
./gradlew :shared:test
./gradlew :androidApp:test

# Clean build
./gradlew clean build
```

**IDE Integration:** Use Android Studio with KMP plugin (Kotlin Multiplatform Mobile)

---

## Naming Conventions (Strictly Enforced)

| Component | Pattern | Example |
|-----------|---------|---------|
| DTO | `XxxDto` | `UserDto`, `BloodRequestDto` |
| ViewModel | `XxxViewModel` | `HomeViewModel` |
| UseCase | `XxxUseCase` | `GetBloodRequestsUseCase` |
| Repository (interface) | `XxxRepository` | `UserRepository` |
| Repository (impl) | `XxxRepositoryImpl` | `UserRepositoryImpl` |
| UiState (sealed/data class) | `XxxUiState` | `HomeUiState` |
| Events (callbacks) | `XxxEvents` | `LoginEvents` |
| Screen Composable | `XxxScreen` | `HomeScreen` |
| Component Composable | `XxxComponent` | `UserCard` |
| Constant object | `Routes`, `AppConstants` | `object Routes { const val HOME = "home" }` |

---

## Shared Module Structure

```
shared/src/commonMain/kotlin/com/donai/app/
├── core/                      # Framework setup
│   ├── di/                    # Koin modules (planned)
│   ├── network/               # Ktor client config
│   └── constants/
├── domain/                    # Pure business logic
│   ├── model/                 # Domain entities
│   ├── repository/            # Repository interfaces only
│   └── usecase/               # Use case implementations
├── data/                      # Data layer
│   ├── remote/
│   │   ├── api/               # API interface definitions
│   │   ├── dto/               # DTO definitions with @Serializable
│   │   └── mapper/            # toDomain() extension functions
│   └── repository/            # Repository implementations
└── presentation/              # Platform-shared state management
    ├── auth/
    ├── requests/
    └── [feature]/
        ├── XxxViewModel.kt
        └── XxxUiState.kt
```

**CRITICAL:** No navigation, UI components, or Android imports in shared module. Those stay in `androidApp/`.

---

## Key File References

| What To Do | Where To Look |
|-----------|----------------|
| Understand architecture | `ARCHITECTURE.md` |
| Coding standards | `CODING_GUIDELINES.md` |
| Add a new feature authenticated request | `shared/src/commonMain/data/remote/api/` + `domain/usecase/` + `androidApp/screens/` |
| Modify API integration | `shared/src/commonMain/data/remote/api/` (interface) + mapper + DTO |
| Add state to screen | Extend `XxxUiState` in `shared/presentation/`, update ViewModel, pass to Composable |
| Configure Ktor | `shared/src/commonMain/core/network/` |
| Dependency injection (planned) | `shared/src/commonMain/core/di/` |

---

## Common Pitfalls to Avoid

1. ❌ **Never** use `var` for mutable state in domain/data layers
2. ❌ **Never** call `repository.fetchData()` directly from Composables—route through ViewModel + UseCase
3. ❌ **Never** skip DTO→Domain mapping; it enforces layer separation
4. ❌ **Never** use `GlobalScope` for coroutines; use viewModelScope
5. ❌ **Never** import `android.*` in shared module
6. ❌ **Never** mix platform code in shared module
7. ❌ **Never** throw raw exceptions in UI; map to sealed classes or Result types
8. ❌ **Never** expose mutable state from ViewModel; always use `.asStateFlow()`

---

## Testing Strategy

- **Unit tests** for UseCases, Mappers, and business logic (in `commonTest/`)
- **Integration tests** for Repository implementations with mock Ktor client
- **UI tests** for Composables (in `androidApp/` with Robolectric or Compose Testing)
- **No database tests yet** (in-memory caching only at MVP stage)

---

## External Resources

- **Backend:** https://github.com/kellerchristian/donai-api
- **Kotlin Multiplatform:** https://kotlinlang.org/docs/multiplatform.html
- **Jetpack Compose:** https://developer.android.com/compose
- **Ktor Client:** https://ktor.io/docs/client.html

---

## When in Doubt

1. Read `ARCHITECTURE.md` + `CODING_GUIDELINES.md`
2. Check existing implementations in the same layer (e.g., `UserRepositoryImpl` for new repos)
3. Follow the **StateFlow → UiState → Composable** pattern for UI
4. Enforce layer separation: if an import feels wrong across layers, it probably is
5. Ask: "Should this live in `shared/` or `androidApp/`?" → If it's UI/navigation → `androidApp/`

