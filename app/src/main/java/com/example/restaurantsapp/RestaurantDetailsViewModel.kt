package com.example.restaurantsapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantDetailsViewModel(private val stateHandle: SavedStateHandle): ViewModel() {
    private var restInterface: RestaurantsApiService
    private var restaurantsDao = RestaurantsDb.getDaoInstance(RestaurantsApplication.getAppContext())
    
    var state by mutableStateOf<Restaurant?>(null)
        private set

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://restaurants-85734-default-rtdb.europe-west1.firebasedatabase.app/")
            .build()
        restInterface = retrofit.create(RestaurantsApiService::class.java)
        
        val id = stateHandle.get<Int>("restaurant_id") ?: 0
        
        viewModelScope.launch {
            state = restaurantsDao.getRestaurantById(id)
        }
        
        getRemoteRestaurant(id)
    }

    private fun getRemoteRestaurant(id: Int) {
        viewModelScope.launch(errorHandler) {
            refreshDetailsCache(id)
        }
    }

    private suspend fun refreshDetailsCache(id: Int) {
        withContext(Dispatchers.IO) {
            val responseMap = restInterface.getRestaurant(id)
            val remoteRestaurant = responseMap.values.first()
            val wasFavorite = restaurantsDao.getRestaurantById(id)?.isFavorite ?: false
            
            restaurantsDao.addAll(listOf(remoteRestaurant))
            if (wasFavorite) {
                restaurantsDao.update(PartialRestaurant(id, true))
            }

            state = restaurantsDao.getRestaurantById(id)
        }
    }
}
