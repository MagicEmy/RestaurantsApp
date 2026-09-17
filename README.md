# Restaurants App

**Restaurants App** is a modern Android application designed to explore and manage a list of restaurants. The project was developed following Android development best practices, using the **Jetpack Compose** framework and a reactive architecture.

## Features
- **Restaurant List:** Display a list of restaurants fetched in real-time from Firebase.
- **Details and Deep Links:** Support for opening restaurant details directly via URL (e.g., `https://www.restaurantsapp.details.com/2`).
- **Favorites:** Ability to mark restaurants as favorites with state persistence.
- **Server-side Ordering:** Data is fetched from the backend already sorted by ID.

## Tech Stack
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for a modern, declarative UI.
- **Networking:** [Retrofit 3.0](https://square.github.io/retrofit/) with GSON Converter for API calls to Firebase Realtime Database.
- **Local Database:** [Room](https://developer.android.com/training/data-storage/room) for local persistence.
- **Concurrency:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) and `viewModelScope` for efficient asynchronous operations.
- **Navigation:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) for handling screen flow and deep links.
- **Dependency Management:** Gradle Version Catalog (`libs.versions.toml`) for centralized library management.

## Architecture
The app follows the **MVVM (Model-View-ViewModel)** pattern:
- **Model:** Data management via Retrofit and Room.
- **View:** Screens built entirely in Compose (`RestaurantsScreen`, `RestaurantDetailsScreen`).
- **ViewModel:** UI state management using `MutableState` (property delegation) and business logic (`RestaurantsViewModel`).

## Setup
To run the project locally:
1. Clone the repository.
2. Open the project in **Android Studio (Ladybug or later)**.
3. Ensure you have your own Firebase database configured or use the existing URL in the code.

---
*Project created for educational purposes to explore modern Android development.*
