package com.example.cryptowealthwatcher2.repository

import com.example.cryptowealthwatcher2.data.model.Coin
import com.example.cryptowealthwatcher2.data.remote.RetrofitInstance

class CoinRepository {

    suspend fun getCoins(): List<Coin> {
        return RetrofitInstance.api.getCoins()
    }
}