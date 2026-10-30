# Restaurants App

**Restaurants App** is a modern Android application designed to explore and manage a list of restaurants. The project follows Android development best practices, applying **Clean Architecture**, **Dagger Hilt**, **Jetpack Compose**, and a reactive, offline-first strategy.

## Features
- **Offline Caching:** High-performance data management where API results are cached in a local Room database, allowing the app to work even without an internet connection.
- **Restaurant List:** A dynamic list of restaurants fetched from Firebase, sorted server-side, and organized using domain Use Cases.
- **Details and Deep Links:** Support for opening specific restaurant details directly via URL (e.g., `https://www.restaurantsapp.details.com/2`).
- **Favorites Management:** Ability to mark restaurants as favorites with state persistence across app restarts.
- **Comprehensive Testing:** Full coverage including UI Compose tests, ViewModel coroutine tests, UseCase business logic tests, and Repository data layer tests.

## Tech Stack
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for a modern, declarative UI.
- **Dependency Injection:** [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) with KSP for compile-time dependency injection.
- **Networking:** [Retrofit 3.0](https://square.github.io/retrofit/) with GSON Converter for Firebase Realtime Database integration.
- **Local Database:** [Room](https://developer.android.com/training/data-storage/room) for robust local persistence and offline support.
- **Concurrency:** [Kotlin Coroutines & Flow](https://kotlinlang.org/docs/coroutines-overview.html) for non-blocking asynchronous operations.
- **Navigation:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) with `hiltViewModel()` integration and deep link support.
- **Testing:** JUnit 4, Kotlinx Coroutines Test (`runTest`, `StandardTestDispatcher`), Compose UI Test JUnit4, and custom Fakes.
- **Dependency Management:** Gradle Version Catalog (`libs.versions.toml`) for centralized library management.

## Architecture
The app follows **Clean Architecture** principles combined with **MVVM** and **Unidirectional Data Flow (UDF)**, organized in a **package-by-feature** structure:

- **Presentation Layer:**
  - **View:** Declarative UI screens built with Compose, decoupled from ViewModels for testability and Preview support.
  - **ViewModel:** Encapsulates business logic and exposes UI state using the **Backing Property pattern** (`private val _state` and `public val state: State<T>`) to enforce encapsulation.

- **Domain Layer:**
  - **Use Cases:** Encapsulate isolated business rules (`GetInitialRestaurantsUseCase`, `ToggleRestaurantUseCase`, `GetSortedRestaurantsUseCase`).
  - **Domain Model:** Pure Kotlin model (`Restaurant`) representing app domain logic.

- **Data Layer:**
  - **Repository Pattern:** `RestaurantsRepository` serves as the Single Source of Truth, unifying remote network calls (`Retrofit`) and local caching (`Room`).
  - **Model Separation:** Clean mapping between Remote DTOs (`RemoteRestaurant`), Room Local Entities (`LocalRestaurant`), and Domain Models (`Restaurant`).

## Testing Suite
- **UI Tests (`androidTest`):** `RestaurantsScreenTest` verifies initial loading states, content rendering, error states, and semantics descriptions.
- **ViewModel Tests (`test`):** `RestaurantsViewModelTest` verifies state emissions and coroutine exception handling using `StandardTestDispatcher` and `TestScope`.
- **Domain Tests (`test`):** `GetSortedRestaurantsUseCaseTest` verifies business logic sorting behavior.
- **Data Tests (`test`):** `RestaurantsRepositoryTest` tests data flow, offline caching, and favorites updates using fakes (`FakeApiService`, `FakeRoomDao`).

---
*Project created for educational purposes to explore modern Android development.*
