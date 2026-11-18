package com.example.restaurantsapp

import androidx.lifecycle.SavedStateHandle
import com.example.restaurantsapp.restaurants.data.RestaurantsRepository
import com.example.restaurantsapp.restaurants.data.local.LocalRestaurant
import com.example.restaurantsapp.restaurants.domain.GetRestaurantByIdUseCase
import com.example.restaurantsapp.restaurants.domain.Restaurant
import com.example.restaurantsapp.restaurants.presentation.details.RestaurantDetailsScreenState
import com.example.restaurantsapp.restaurants.presentation.details.RestaurantDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class RestaurantDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(
        savedStateHandle: SavedStateHandle,
        fakeRoomDao: FakeRoomDao = FakeRoomDao()
    ): RestaurantDetailsViewModel {
        val repository = RestaurantsRepository(
            FakeApiService(),
            fakeRoomDao,
            mainDispatcherRule.testDispatcher
        )

        val getRestaurantByIdUseCase =
            GetRestaurantByIdUseCase(repository)

        return RestaurantDetailsViewModel(
            getRestaurantByIdUseCase,
            savedStateHandle
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun validRestaurantId_producesContentState() = runTest {
        val fakeRoomDao = FakeRoomDao()

        val localRestaurant = LocalRestaurant(
            id = 1,
            title = "Test restaurant",
            description = "Test description",
            isFavorite = false
        )

        fakeRoomDao.addAll(listOf(localRestaurant))

        val viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf("restaurant_id" to localRestaurant.id)
            ),
            fakeRoomDao = fakeRoomDao
        )

        advanceUntilIdle()

        assertEquals(
            RestaurantDetailsScreenState(
                restaurant = Restaurant(
                    id = 1,
                    title = "Test restaurant",
                    description = "Test description",
                    isFavorite = false
                ),
                isLoading = false,
                error = null
            ),
            viewModel.state.value

        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun unknownRestaurantId_producesNotFoundState() = runTest {
        val viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf("restaurant_id" to 999)
            )
        )

        advanceUntilIdle()

        assertEquals(
            RestaurantDetailsScreenState(
                restaurant = null,
                isLoading = false,
                error = "Restaurant not found."
            ),
            viewModel.state.value
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun missingRestaurantId_producedNotFoundState() = runTest {
        val viewModel = createViewModel(
            savedStateHandle = SavedStateHandle()
        )

        advanceUntilIdle()

        assertEquals(
            RestaurantDetailsScreenState(
                restaurant = null,
                isLoading = false,
                error = "Restaurant not found."
            ),
            viewModel.state.value
        )
    }

}
