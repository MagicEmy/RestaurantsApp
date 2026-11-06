package com.example.restaurantsapp

import com.example.restaurantsapp.restaurants.data.RestaurantsRepository
import com.example.restaurantsapp.restaurants.domain.GetInitialRestaurantsUseCase
import com.example.restaurantsapp.restaurants.domain.GetPagedRestaurantsUseCase
import com.example.restaurantsapp.restaurants.domain.ToggleRestaurantUseCase
import com.example.restaurantsapp.restaurants.presentation.list.RestaurantsScreenState
import com.example.restaurantsapp.restaurants.presentation.list.RestaurantsViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RestaurantsViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    private fun getViewModel(
        apiService: FakeApiService = FakeApiService()
    ): RestaurantsViewModel {
        val restaurantsRepository =
            RestaurantsRepository(
                apiService,
                FakeRoomDao(),
                dispatcher
            )
        val getInitialRestaurantsUseCase =
            GetInitialRestaurantsUseCase(
                restaurantsRepository
            )
        val getPagedRestaurantsUseCase =
            GetPagedRestaurantsUseCase(
                restaurantsRepository
            )
        val toggleRestaurantUseCase =
            ToggleRestaurantUseCase(
                restaurantsRepository
            )
        return RestaurantsViewModel(
            getInitialRestaurantsUseCase,
            getPagedRestaurantsUseCase = getPagedRestaurantsUseCase,
            toggleRestaurantUseCase = toggleRestaurantUseCase,
            dispatcher = dispatcher
        )
    }

    @Test
    fun initialState_isProduced() = scope.runTest {
        val viewModel = getViewModel()
        val initialState = viewModel.state.value
        assertEquals(
            RestaurantsScreenState(
                isLoading = true,
                error = null
            ),
            initialState
        )
    }

    @Test
    fun successfulInitialLoad_updatesLoadingState() =
        scope.runTest {
            val viewModel = getViewModel()

            advanceUntilIdle()

            assertEquals(
                RestaurantsScreenState(
                    isLoading = false,
                    error = null
                ),
                viewModel.state.value
            )
        }

    @Test
    fun failedInitialLoad_producesErrorState() =
        scope.runTest {
            val apiService = FakeApiService().apply {
                shouldThrowException = true
            }
            val viewModel = getViewModel(apiService)

            advanceUntilIdle()

            assertEquals(
                RestaurantsScreenState(
                    isLoading = false,
                    error = "Something went wrong."
                ),
                viewModel.state.value
            )
        }
    @Test
    fun retryInitialLoad_afterError_producesSuccessState() =
        scope.runTest {
            val apiService = FakeApiService(
                shouldThrowException = true
            )
            val viewModel = getViewModel(
                apiService = apiService
            )
            advanceUntilIdle()
            assertEquals(
                RestaurantsScreenState(
                    isLoading = false,
                    error = "Something went wrong."
                ),
                viewModel.state.value
            )
            apiService.shouldThrowException = false
            viewModel.retryInitialLoad()

            assertEquals(
                RestaurantsScreenState(
                    isLoading = true,
                    error = null
                ),
                viewModel.state.value
            )

            advanceUntilIdle()

            assertEquals(
                RestaurantsScreenState(
                    isLoading = false,
                    error = null
                ),
                viewModel.state.value
            )
        }
}