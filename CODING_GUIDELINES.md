# DONAI Coding Guidelines

## General Principles

- Keep code simple and explicit
- Prefer readability over abstraction
- Avoid overengineering

---

# Kotlin Style

Follow official Kotlin conventions.

Prefer:

- data classes
- immutability
- sealed classes for states
- named parameters for clarity

Avoid:

- unnecessary inheritance
- deep nesting
- mutable shared state

---

# Architecture Rules

Allowed dependency flow:

UI → Presentation → Domain → Data

Forbidden:

- Domain depending on Data
- UI calling repositories directly
- Shared module depending on Android APIs

---

# UI Rules

## Composables

- Stateless by default
- No business logic inside UI
- Receive state via parameters
- Emit events via callbacks

Example:

@Composable
fun FeedScreen(
state: FeedUiState,
onRefresh: () -> Unit
)

---

# ViewModels

- Expose immutable StateFlow
- Own UI state
- Call use cases only

Pattern:

private val _uiState = MutableStateFlow(FeedUiState())
val uiState = _uiState.asStateFlow()

---

# State Management

- Single source of truth
- Immutable UiState only
- No direct mutation of lists/objects

---

# Use Cases

- Single responsibility
- Framework independent
- Use operator invoke

Example:

class LoginUseCase(
private val repository: AuthRepository
) {
suspend operator fun invoke(email: String, password: String)
}

---

# Repository Rules

Interfaces in domain:

domain/repository

Implementations in data:

data/repository

Naming:

AuthRepository / AuthRepositoryImpl

---

# DTO Rules

- Only in data layer
- Must end with Dto
- Never exposed to domain or UI

---

# Mappers

- Mandatory DTO ↔ Domain mapping
- Keep mapping explicit

Naming:

toDomain()
toDto()

---

# Coroutines Rules

- Use suspend functions for async work
- No GlobalScope
- Structured concurrency only
- ViewModels manage lifecycle

---

# Flow Rules

- StateFlow → UI state
- SharedFlow → events
- No LiveData

---

# Error Handling

- Never expose raw exceptions to UI
- Map errors to sealed classes or Result types

---

# Dependency Injection

- Use Koin only
- Constructor injection required
- No service locator pattern

---

# Naming Conventions

| Type | Naming |
|------|--------|
| DTO | XxxDto |
| UseCase | XxxUseCase |
| ViewModel | XxxViewModel |
| Repository | XxxRepository |
| Impl | XxxRepositoryImpl |
| UiState | XxxUiState |

---

# File Organization

- One public class per file
- No mixed responsibilities

---

# Constants

Avoid magic strings.

Use:

object Routes {
const val FEED = "feed"
}

---

# Models

- Prefer immutable data classes
- Avoid nullable types unless necessary
- No !! operator usage

---

# Logging

Never log:

- passwords
- tokens
- personal medical data

---

# Functions

- Single responsibility
- Prefer small functions (<40 lines)
- Clear naming over comments

---

# Comments

Only for:

- business rules
- non-obvious logic
- architectural decisions

Avoid obvious comments.

---

# Testing Philosophy

Prioritize testing:

- Use cases
- Mappers
- Business logic
- Validation rules

---

# Future Compatibility

Code must remain compatible with:

- Android UI
- future iOS UI
- shared KMP module