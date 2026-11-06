package com.example.restaurantsapp

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.restaurantsapp.restaurants.data.local.LocalRestaurant
import com.example.restaurantsapp.restaurants.data.local.PartialLocalRestaurant
import com.example.restaurantsapp.restaurants.data.local.RestaurantsDao
import kotlinx.coroutines.delay

class FakeRoomDao : RestaurantsDao {
    private var restaurants =
        HashMap<Int, LocalRestaurant>()
    override suspend fun getAll() : List<LocalRestaurant> {
        delay(1000)
        return restaurants.values.toList()
    }

    override fun getRestaurantsPagingSource(): PagingSource<Int, LocalRestaurant> {
        val sortedRestaurants = restaurants.values
            .sortedWith(
                compareBy<LocalRestaurant, String>(
                    String.CASE_INSENSITIVE_ORDER
                ) { it.title }
                    .thenBy { it.id }
            )
        return FakeRestaurantsPagingSource(sortedRestaurants)
    }
    override suspend fun addAll(
        restaurants: List<LocalRestaurant>
    ){
        restaurants.forEach {
            this.restaurants[it.id] = it
        }
    }
    override suspend fun update(
        partialRestaurant: PartialLocalRestaurant
    ) {
        delay(1000)
        updateRestaurant(partialRestaurant)
    }
    override suspend fun updateAll(
        partialRestaurants: List<PartialLocalRestaurant>
    ) {
        delay(1000)
        partialRestaurants.forEach {
            updateRestaurant(it)
        }
    }
    override suspend fun getAllFavorite() :
            List<LocalRestaurant> {
        return restaurants.values.toList()
            .filter { it.isFavorite}
    }

    private fun updateRestaurant(
        partialRestaurant: PartialLocalRestaurant
    ) {
        val restaurant =
            this.restaurants[partialRestaurant.id]
        if (restaurant != null)
            this.restaurants[partialRestaurant.id] =
                restaurant.copy(
                    isFavorite =
                        partialRestaurant.isFavorite
                )
    }
    private class FakeRestaurantsPagingSource(
        private val restaurants: List<LocalRestaurant>
    ): PagingSource<Int, LocalRestaurant>(){

        override suspend fun load(
            params: LoadParams<Int>
        ): LoadResult<Int, LocalRestaurant> {
            val startIndex = params.key ?:0
            if (startIndex >= restaurants.size) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
            val endIndex = minOf(
                startIndex + params.loadSize,
                restaurants.size
            )

            return LoadResult.Page(
                data = restaurants.subList(startIndex, endIndex),
                prevKey = if (startIndex == 0) {
                    null
                } else {
                    maxOf(0, startIndex - params.loadSize)
                },
                nextKey = if (endIndex >= restaurants.size) {
                    null
                } else {
                    endIndex
                }
            )
        }

        override fun getRefreshKey(
            state: PagingState<Int, LocalRestaurant>
        ): Int? {
            val anchorPosition = state.anchorPosition ?: return null
            return maxOf(
                0,
                anchorPosition - state.config.initialLoadSize / 2
            )
        }
    }
}