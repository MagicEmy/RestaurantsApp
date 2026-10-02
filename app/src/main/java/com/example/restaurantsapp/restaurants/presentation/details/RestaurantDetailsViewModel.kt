package com.example.restaurantsapp.restaurants.presentation.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantsapp.restaurants.domain.Restaurant
import com.example.restaurantsapp.restaurants.presentation.details.RestaurantDetailsScreenState
import com.example.restaurantsapp.restaurants.data.remote.RestaurantsApiService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantDetailsViewModel(private val stateHandle: SavedStateHandle): ViewModel() {
    private val _state = mutableStateOf(RestaurantDetailsScreenState(isLoading = true))
    val state: State<RestaurantDetailsScreenState> get() = _state

    private var restInterface: RestaurantsApiService = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://restaurants-85734-default-rtdb.europe-west1.firebasedatabase.app/")
        .build()
        .create(RestaurantsApiService::class.java)

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    init {
        val id = stateHandle.get<Int>("restaurant_id") ?: 0
        viewModelScope.launch(errorHandler) {
            val restaurant = getRemoteRestaurant(id)
            _state.value = _state.value.copy(restaurant = restaurant, isLoading = false)
        }
    }

    private suspend fun getRemoteRestaurant(id: Int): Restaurant? {
        return withContext(Dispatchers.IO) {
            val responseMap = restInterface.getRestaurant(id)
            return@withContext responseMap.values.firstOrNull()?.let { remoteRestaurant ->
                Restaurant(
                    id = remoteRestaurant.id,
                    title = remoteRestaurant.title,
                    description = remoteRestaurant.description
                )
            }
        }
    }
}