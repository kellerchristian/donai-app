# DONAI Mobile Architecture

## Overview

DONAI Mobile is built using Kotlin Multiplatform (KMP) with a shared business logic layer and platform-specific UI implementations.

The project follows:

- Clean Architecture
- MVVM (Presentation Layer)
- Repository Pattern
- Unidirectional Data Flow (UDF)

The goal is to maximize shared logic across platforms while keeping UI fully native (Android now, iOS in the future).

---

# Project Structure

shared/
├── core/
│   ├── network/
│   ├── auth/
│   ├── util/
│   ├── di/
│   └── constants/
│
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
│
├── data/
│   ├── remote/
│   │   ├── api/
│   │   ├── dto/
│   │   └── mapper/
│   │
│   └── repository/
│
├── presentation/
│   ├── auth/
│   ├── requests/
│   ├── commitments/
│   ├── profile/
│   ├── history/
│   └── shared/
│
└── platform/

androidApp/
├── navigation/
├── screens/
├── components/
├── theme/
└── MainActivity.kt

---

# Architecture Layers

## UI Layer

- Jetpack Compose (Android)
- Stateless by default
- Only renders state and emits events

Flow:

UI → ViewModel → UseCase → Repository

---

## Presentation Layer

Responsible for:

- ViewModels
- UiState
- UiEvents (if needed)
- UI orchestration

Rules:

- State exposed via StateFlow
- Immutable UiState only
- No business logic inside composables

---

## Domain Layer

Pure business logic layer.

Contains:

- Domain models
- Repository interfaces
- Use cases
- Business rules / policies

Must NOT depend on:

- Android
- Ktor
- Serialization
- DTOs

---

## Data Layer

Responsible for:

- API communication (Ktor)
- DTOs
- Mappers
- Repository implementations
- In-memory caching

Rules:

- DTOs NEVER leave this layer
- Mapping is mandatory (DTO → Domain)

---

# Networking

- Ktor Client
- Kotlinx Serialization

Rules:

- All network models are DTOs
- Domain models are independent
- Explicit mapping required

---

# Caching Strategy

No local database is used initially.

Cache strategy:

- in-memory cache inside repositories
- session persistence via secure storage (tokens only)

Cache is:
- volatile
- reset on app restart

---

# Dependency Injection

- Koin

Rules:

- feature-based modules
- constructor injection only
- no service locator patterns

---

# Navigation

- Android: Navigation Compose
- iOS (future): SwiftUI navigation

Shared module must NOT contain navigation logic.

---

# State Management

- StateFlow
- Coroutines

Pattern:

UI → Intent → ViewModel → UseCase → Repository → StateFlow → UI

Rules:

- single source of truth per screen
- immutable UiState
- no mutable state exposed

---

# Error Handling

- No raw exceptions in UI
- Errors mapped into sealed classes or Result wrappers

---

# Naming Conventions

- DTO → ends with Dto
- UseCase → operator invoke
- RepositoryImpl → implementation suffix
- UiState → screen state holder
- ViewModel → screen ViewModel

---

# Concurrency

- Kotlin Coroutines
- Flow / StateFlow

Rules:

- structured concurrency only
- no GlobalScope
- ViewModels own coroutine scope

---

# Platform Strategy

Shared module:

- domain
- data
- use cases
- networking
- state management

Platform modules:

- UI
- navigation
- permissions
- system integrations

---

# Security

- HTTPS only
- Tokens stored securely per platform
- No sensitive data in logs

---

# Offline Strategy

No offline database at MVP stage.

Only future extension:

- SQLDelight (optional)
- offline sync queue (future)

---

# Scalability Goals

Architecture supports:

- Firebase Auth
- FCM notifications
- geolocation services
- iOS UI
- offline mode (future)