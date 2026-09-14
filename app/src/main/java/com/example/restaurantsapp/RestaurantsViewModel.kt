package com.example.restaurantsapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantsViewModel(private val stateHandle: SavedStateHandle): ViewModel() {
    private var restInterface: RestaurantsApiService
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }
    val state = mutableStateOf(emptyList<Restaurant>())

    init {
        val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(
            GsonConverterFactory.create()
        )
            .baseUrl(
                "https://restaurants-85734-default-rtdb.europe-west1.firebasedatabase.app"
            )
            .build()
        restInterface = retrofit.create(
            RestaurantsApiService::class.java
        )
        getRestaurants()
    }

    private suspend fun getRemoteRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            restInterface.getRestaurants()
        }
    }
    private fun getRestaurants() {
        viewModelScope.launch(errorHandler) {
            try {
                val restaurants = getRemoteRestaurants()
                state.value = restaurants.restoreSelections()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun toggleFavorite(id: Int) {
        val restaurants = state.value.toMutableList()
        val itemIndex = restaurants.indexOfFirst { it.id == id }
        val item = restaurants[itemIndex]
        restaurants[itemIndex] =
            item.copy(isFavorite = !item.isFavorite)
        storeSelection(restaurants[itemIndex])
        state.value = restaurants
    }

    private fun storeSelection(item: Restaurant) {
        val savedToggled = stateHandle
            .get<List<Int>?>(FAVORITES )
            .orEmpty() .toMutableList()
        if (item.isFavorite) savedToggled.add(item.id)
        else savedToggled.remove(item.id)
        stateHandle[FAVORITES] = savedToggled
    }

    private fun List<Restaurant>.restoreSelections(): List<Restaurant> {
        stateHandle.get<List<Int>?>(FAVORITES)?.let { selectedIds ->
            val restaurantsMap = this.associateBy { it.id }
            selectedIds.forEach { id ->
                restaurantsMap[id]?.isFavorite = true
            }
            return restaurantsMap.values.toList()
        }
        return this
    }
    companion object {
        const val FAVORITES = "favorites"
    }
}