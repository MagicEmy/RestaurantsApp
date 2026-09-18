# Restaurants App

**Restaurants App** is a modern Android application designed to explore and manage a list of restaurants. The project follows Android development best practices, using **Jetpack Compose** and a reactive, offline-first architecture.

## Features
- **Restaurant List:** A dynamic list of restaurants fetched from Firebase and sorted by ID server-side.
- **Favorites:** Ability to mark restaurants as favorites with state persistence across app restarts.
- **Details and Deep Links:** Support for opening specific restaurant details directly via URL (e.g., `https://www.restaurantsapp.details.com/2`).
- **Offline Caching:** High-performance data management where API results are cached in a local Room database, allowing the app to work even without an internet connection.


## Tech Stack
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for a modern, declarative UI.
- **Networking:** [Retrofit 3.0](https://square.github.io/retrofit/) with GSON Converter for Firebase Realtime Database integration.
- **Local Database:** [Room](https://developer.android.com/training/data-storage/room) for robust local persistence and offline support.
- **Concurrency:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for non-blocking asynchronous operations.
- **Navigation:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) for screen flow and deep link handling.
- **DI/Management:** Gradle Version Catalog (`libs.versions.toml`) for centralized dependency management.

## Architecture
The app implements the **MVVM (Model-View-ViewModel)** pattern with **Unidirectional Data Flow (UDF)**:
- **Model:** Data repository logic combining Retrofit for remote data and Room for local caching.
- **View:** Declarative UI screens built with Compose.
- **ViewModel:** Encapsulates business logic and exposes UI state using **Kotlin Property Delegation (`by`)** and `private set` for better encapsulation.

## Setup
To run the project locally:
1. Clone the repository.
2. Open the project in **Android Studio (Ladybug or later)**.
3. Ensure you have your own Firebase database configured or use the existing URL in the code.

---
*Project created for educational purposes to explore modern Android development.*
