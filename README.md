# Restaurants App

**Restaurants App** is a modern Android application designed to explore and manage a list of restaurants. The project follows Android development best practices, applying **Clean Architecture**, **Dagger Hilt**, **Jetpack Compose**, and a reactive, locally cached data strategy.

## Features
- **Local Caching:** Restaurant data fetched from Firebase is persisted in Room, which provides the local data source used by the UI.
- **Paginated Restaurant List:** Paging 3 loads restaurants from Room and exposes refresh, append, retry, loading, and error states to the Compose UI.
- **Details and Deep Links:** Restaurant details are loaded from Room through the repository and a dedicated use case. Specific restaurants can also be opened directly via URL (e.g., `https://www.restaurantsapp.details.com/2`).
- **Favorites Management:** Ability to mark restaurants as favorites with state persistence across app restarts.
- **Lifecycle-Aware State:** ViewModels expose immutable `StateFlow` state, collected in Compose with `collectAsStateWithLifecycle()`.
- **Automated Testing:** Compose UI, ViewModel, use case, and repository behavior is verified with focused tests and custom fakes.

## Tech Stack
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for a modern, declarative UI.
- **Dependency Injection:** [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) with KSP for compile-time dependency injection.
- **Networking:** [Retrofit 3.0](https://square.github.io/retrofit/) with GSON Converter for Firebase Realtime Database integration.
- **Local Database:** [Room](https://developer.android.com/training/data-storage/room) for robust local persistence and offline support.
- **Concurrency:** [Kotlin Coroutines & Flow](https://kotlinlang.org/docs/coroutines-overview.html) for non-blocking asynchronous operations.
- **Pagination:** [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) with Room-backed `PagingSource` integration.
- **Navigation:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) with `hiltViewModel()` integration and deep link support.
- **Testing:** JUnit 4, Kotlinx Coroutines Test (`runTest`, `StandardTestDispatcher`, and a custom `MainDispatcherRule`), Compose UI Test JUnit4, and custom fakes.
- **Dependency Management:** Gradle Version Catalog (`libs.versions.toml`) for centralized library management.

## Architecture
The app follows **Clean Architecture** principles combined with **MVVM** and **Unidirectional Data Flow (UDF)**, organized in a **package-by-feature** structure:

- **Presentation Layer:**
  - **View:** Declarative UI screens built with Compose, decoupled from ViewModels for testability and Preview support.
  - **ViewModel:** Coordinates use cases and exposes immutable UI state using the **Backing Property pattern** (`MutableStateFlow` internally and `StateFlow` publicly). Compose collects this state in a lifecycle-aware manner.

- **Domain Layer:**
  - **Use Cases:** Represent focused application operations (`GetInitialRestaurantsUseCase`, `GetPagedRestaurantsUseCase`, `GetRestaurantByIdUseCase`, and `ToggleRestaurantUseCase`).
  - **Domain Model:** Pure Kotlin model (`Restaurant`) representing app domain logic.

- **Data Layer:**
  - **Repository Pattern:** `RestaurantsRepository` coordinates remote network calls through Retrofit and local persistence through Room while keeping data-source details out of the presentation layer.
  - **Model Separation:** Clean mapping between Remote DTOs (`RemoteRestaurant`), Room Local Entities (`LocalRestaurant`), and Domain Models (`Restaurant`).

## Testing Suite
- **UI Tests (`androidTest`):** `RestaurantsScreenTest` verifies initial loading states, content rendering, error states, and semantics descriptions.
- **ViewModel Tests (`test`):** `RestaurantsViewModelTest` verifies initial loading, success, failure, and retry behavior. `RestaurantDetailsViewModelTest` covers successful loading, unknown restaurant IDs, and missing navigation arguments.
- **Coroutine Testing:** `MainDispatcherRule` replaces `Dispatchers.Main` during local JVM tests, while `runTest` and `StandardTestDispatcher` provide deterministic coroutine execution.
- **Domain Tests (`test`):** Focused use case tests verify restaurant and favorite-related behavior.
- **Data Tests (`test`):** `RestaurantsRepositoryTest` verifies repository behavior and persisted favorite updates using `FakeApiService` and `FakeRoomDao`.

---
*Project created for educational purposes to explore modern Android development.*
