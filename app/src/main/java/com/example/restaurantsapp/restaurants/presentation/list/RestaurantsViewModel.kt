package com.example.restaurantsapp.restaurants.presentation.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.restaurantsapp.restaurants.data.di.MainDispatcher
import com.example.restaurantsapp.restaurants.domain.GetInitialRestaurantsUseCase
import com.example.restaurantsapp.restaurants.domain.GetPagedRestaurantsUseCase
import com.example.restaurantsapp.restaurants.domain.Restaurant
import com.example.restaurantsapp.restaurants.domain.ToggleRestaurantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class RestaurantsViewModel @Inject constructor (
    private val getRestaurantsUseCase: GetInitialRestaurantsUseCase,
    private val getPagedRestaurantsUseCase: GetPagedRestaurantsUseCase,
    private val toggleRestaurantUseCase: ToggleRestaurantUseCase,
    @MainDispatcher private val dispatcher : CoroutineDispatcher
): ViewModel() {
    private val _state =
        mutableStateOf(
            RestaurantsScreenState(
                isLoading = true
            )
        )
    val state: State<RestaurantsScreenState>
        get() = _state

    val pageRestaurants: Flow<PagingData<Restaurant>> =
        getPagedRestaurantsUseCase()
            .cachedIn(viewModelScope)


    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        _state.value = _state.value.copy(
            error = exception.message,
            isLoading = false
        )
    }

    init {
        getRestaurants()
    }

    private fun getRestaurants() {
        viewModelScope.launch(errorHandler + dispatcher) {
            getRestaurantsUseCase()
            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }

    fun retryInitialLoad() {
        _state.value = _state.value.copy(
            isLoading = true,
            error = null
        )
        getRestaurants()
    }

    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch(errorHandler + dispatcher ) {
            toggleRestaurantUseCase(id, oldValue)
        }
    }

}