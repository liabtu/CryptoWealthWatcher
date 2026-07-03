package com.example.cryptowealthwatcher2.data.remote

import com.example.cryptowealthwatcher2.data.model.Coin
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CoinApi {

    @Headers(
        "x-cg-demo-api-key: CG-7gJV1hpJxzK7EaUKNQ7qniu"
    )
    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency")
        currency: String = "usd",

        @Query("order")
        order: String = "market_cap_desc",

        @Query("per_page")
        perPage: Int = 20,

        @Query("page")
        page: Int = 1
    ): List<Coin>
}