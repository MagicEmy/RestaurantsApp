# Restaurants App

**Restaurants App** is a modern Android application designed to explore and manage a list of restaurants. The project follows Android development best practices, using **Jetpack Compose** and a reactive, offline-first architecture.

## Features
- **Offline Caching:** High-performance data management where API results are cached in a local Room database, allowing the app to work even without an internet connection.
- **Restaurant List:** A dynamic list of restaurants fetched from Firebase and sorted by ID server-side.
- **Details and Deep Links:** Support for opening specific restaurant details directly via URL (e.g., `https://www.restaurantsapp.details.com/2`).
- **Favorites:** Ability to mark restaurants as favorites with state persistence across app restarts.

## Tech Stack
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for a modern, declarative UI.
- **Networking:** [Retrofit 3.0](https://square.github.io/retrofit/) with GSON Converter for Firebase Realtime Database integration.
- **Local Database:** [Room](https://developer.android.com/training/data-storage/room) for robust local persistence and offline support.
- **Concurrency:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for non-blocking asynchronous operations.
- **Navigation:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) for screen flow and deep link handling.
- **DI/Management:** Gradle Version Catalog (`libs.versions.toml`) for centralized dependency management.

## Architecture
The app implements the **MVVM (Model-View-ViewModel)** pattern with strict **Unidirectional Data Flow (UDF)**:
- **Model:** Data repository logic combining Retrofit for remote data and Room for local caching via the **Repository Pattern**.
- **View:** Declarative UI screens built with Compose that observe state and emit events.
- **ViewModel:** Encapsulates business logic and manages UI state using the **Backing Property pattern** (`_state` and `state`). This ensures that only the ViewModel can modify the state, providing robust encapsulation and preventing accidental side effects from the UI layer.

## Planned Refactoring & Roadmap
- [ ] **Details Screen Architecture**: Refactor `RestaurantDetailsViewModel` to use a dedicated `RestaurantDetailsRepository` (and Use Cases) to match the main screen's architecture for complete codebase consistency once the book is completed.

---
*Project created for educational purposes to explore modern Android development.*
