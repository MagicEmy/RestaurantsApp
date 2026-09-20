package com.example.restaurantsapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantDetailsRepository {
    private var restInterface: RestaurantsApiService =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://restaurants-85734-default-rtdb.europe-west1.firebasedatabase.app/")
            .build()
            .create(RestaurantsApiService::class.java)

    var restaurantsDao = RestaurantsDb.getDaoInstance(
        RestaurantsApplication.getAppContext()
    )

    suspend fun getRestaurantById(id: Int): Restaurant? = withContext(Dispatchers.IO)
    {
        try {
            refreshDetailsCache(id)
        } catch (e: Exception) {}
        return@withContext restaurantsDao.getRestaurantById(id) }

    suspend fun refreshDetailsCache(id: Int) {
        withContext(Dispatchers.IO) {
            val responseMap = restInterface.getRestaurant(id)
            val remoteRestaurant = responseMap.values.first()
            val wasFavorite = restaurantsDao.getRestaurantById(id)?.isFavorite ?: false

            restaurantsDao.addAll(listOf(remoteRestaurant))
            if (wasFavorite) {
                restaurantsDao.update(PartialRestaurant(id, true))
            }
        }
    }



}