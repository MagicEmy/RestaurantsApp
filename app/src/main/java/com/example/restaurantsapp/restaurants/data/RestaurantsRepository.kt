package com.example.restaurantsapp.restaurants.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.restaurantsapp.restaurants.data.di.IoDispatcher
import com.example.restaurantsapp.restaurants.data.local.LocalRestaurant
import com.example.restaurantsapp.restaurants.data.local.PartialLocalRestaurant
import com.example.restaurantsapp.restaurants.data.local.RestaurantsDao
import com.example.restaurantsapp.restaurants.data.remote.RestaurantsApiService
import com.example.restaurantsapp.restaurants.domain.Restaurant
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

@Singleton
class RestaurantsRepository @Inject constructor(
    private val restInterface: RestaurantsApiService,
    private val restaurantsDao: RestaurantsDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
){
    suspend fun loadRestaurants() {
        return withContext(dispatcher) {
            try {
                refreshCash()
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        if (restaurantsDao.getAll().isEmpty())
                            throw Exception(
                                "Something went wrong." + "We have no data."
                            )
                    }
                    else -> throw e
                }
            }
        }
    }

    private suspend fun refreshCash() {
        val remoteRestaurants = restInterface.getRestaurants()
        val favoriteRestaurants = restaurantsDao.getAllFavorite()
        restaurantsDao.addAll(remoteRestaurants.map {
            LocalRestaurant(it.id, it.title, it.description, false)
        })
        restaurantsDao.updateAll(
            favoriteRestaurants.map{
                PartialLocalRestaurant(it.id, true)
            }
        )
    }

    suspend fun toggleFavoriteRestaurant (id: Int, value: Boolean) =
        withContext(dispatcher) {
            restaurantsDao.update(
                PartialLocalRestaurant(id = id, isFavorite = value)
            )
        }
    fun getPagedRestaurants(): Flow<PagingData<Restaurant>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 5,
                prefetchDistance = 1,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                restaurantsDao.getRestaurantsPagingSource()
            }
        ).flow.map { pagingData ->
            pagingData.map { localRestaurant ->
                Restaurant(
                    id = localRestaurant.id,
                    title = localRestaurant.title,
                    description = localRestaurant.description,
                    isFavorite = localRestaurant.isFavorite
                )
            }
        }
    }
}