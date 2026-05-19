# DonAI App

Mobile application for a blood donation platform.

This project is part of a full-stack system designed to connect donors and recipients through an organized, scalable and real-time matching platform.

It works in conjunction with the **DonAI API backend**, which handles data management, matching logic, and system orchestration.

---

## 🚀 Tech Stack

- Kotlin Multiplatform (KMP)
- Kotlin Coroutines
- Ktor Client
- Jetpack Compose (Android UI)
- SwiftUI (planned iOS UI integration)
- Gradle (Kotlin DSL)
- Clean Architecture principles

---

## 🧩 Architecture Overview

The project is structured using a Kotlin Multiplatform approach:

- **shared/** → Business logic, networking, models
- **androidApp/** → Android-specific UI (Jetpack Compose)
- **iosApp/** → iOS-specific UI (SwiftUI, planned)
- **data layer** → API communication (Ktor Client)
- **domain layer** → business rules and use cases
- **presentation layer** → UI state management

The goal is to maximize shared logic while keeping native UI experiences per platform.

---

## 📱 Platforms

### Android
- Jetpack Compose UI
- Fully native integration with shared logic

### iOS (planned)
- SwiftUI-based interface
- Shared Kotlin business logic via KMP

---

## 🔗 Backend Integration

This app consumes the **DonAI API**:

👉 https://github.com/kellerchristian/donai-api

The backend handles:
- Donor management
- Blood donation tracking
- Request coordination
- Matching logic between donors and recipients

---

## 🧠 Planned Features

- User authentication (donors & recipients)
- Donation matching system
- Location-based donor search
- Real-time notifications
- Donation history tracking
- Profile management

---

## 📂 Project Structure
shared/
├── data/
├── domain/
├── network/
├── models/

androidApp/
├── ui/
├── screens/
├── viewmodel/

iosApp/ (planned)
├── ui/
├── viewmodels/


---

## 📡 Design Principles

- Clean Architecture
- Separation of concerns
- Shared business logic (KMP)
- Platform-specific UI layers
- Scalable and testable structure

---

## 📌 Status

Work in progress 🚧  
Initial architecture and base setup completed.

---

## 🎯 Purpose

This project is part of my full-stack development portfolio, focused on building real-world scalable systems using Kotlin, Clean Architecture, and cross-platform mobile development.

It demonstrates:
- Backend engineering (DonAI API)
- Mobile engineering (KMP App)
- System design thinking