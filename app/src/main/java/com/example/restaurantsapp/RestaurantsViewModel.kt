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
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.UnknownHostException

class RestaurantsViewModel(): ViewModel() {
    private var restInterface: RestaurantsApiService
    private var RestaurantsDao = RestaurantsDb.getDaoInstance(
            RestaurantsApplication.getAppContext())
    var state by mutableStateOf(emptyList<Restaurant>())

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory( GsonConverterFactory.create())
            .baseUrl( "https://restaurants-85734-default-rtdb.europe-west1.firebasedatabase.app/")
            .build()
        restInterface = retrofit.create(RestaurantsApiService::class.java)
        getRestaurants()
    }
    private suspend fun getAllRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            try {
                refreshCash()
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        if (RestaurantsDao.getAll().isEmpty())
                            throw Exception(
                                "Something went wrong." + "We have no data.")
                    }
                    else -> throw e
                }
            }
            return@withContext RestaurantsDao.getAll()
        }
    }

    private suspend fun refreshCash() {
        val remoteRestaurants = restInterface.getRestaurants()
        val favoriteRestaurants = RestaurantsDao.getAllFavorited()
        RestaurantsDao.addAll(remoteRestaurants)
        RestaurantsDao.updateAll(
            favoriteRestaurants.map{
                PartialRestaurant(it.id, true)
            }
        )
    }
    private fun getRestaurants() {
        viewModelScope.launch(errorHandler) {
            state = getAllRestaurants()
        }
    }
    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch(errorHandler) {
            val updatedRestaurants =
                toggleFavoriteRestaurant(id, oldValue)
            state = updatedRestaurants
        }
    }
    private suspend fun toggleFavoriteRestaurant (id: Int, oldValue: Boolean) =
        withContext(Dispatchers.IO) {
            RestaurantsDao.update(PartialRestaurant(id = id, isFavorite = !oldValue))
            RestaurantsDao.getAll()
        }
}