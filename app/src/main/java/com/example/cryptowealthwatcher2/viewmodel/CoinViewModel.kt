package com.example.cryptowealthwatcher2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptowealthwatcher2.data.model.Coin
import com.example.cryptowealthwatcher2.repository.CoinRepository
import kotlinx.coroutines.launch

class CoinViewModel : ViewModel() {

    private val repository = CoinRepository()

    private val _coins = MutableLiveData<List<Coin>>()
    val coins: LiveData<List<Coin>> = _coins

    init {
        fetchCoins()
    }

    private fun fetchCoins() {
        viewModelScope.launch {
            try {
                val response = repository.getCoins()
                if (response.isNotEmpty()) {
                    _coins.postValue(response)
                } else {
                    loadMockData()
                }
            } catch (e: Exception) {
                loadMockData()
            }
        }
    }

    private fun loadMockData() {
        val mockList = listOf(
            Coin("bitcoin", "btc", "Bitcoin", "https://assets.coingecko.com/coins/images/1/large/bitcoin.png", 65000.0),
            Coin("ethereum", "eth", "Ethereum", "https://assets.coingecko.com/coins/images/279/large/ethereum.png", 3200.0),
            Coin("solana", "sol", "Solana", "https://assets.coingecko.com/coins/images/4128/large/solana.png", 145.0),
            Coin("ripple", "xrp", "Ripple", "https://assets.coingecko.com/coins/images/44/large/xrp-symbol-white-branded.png", 0.52)
        )
        _coins.postValue(mockList) // 👈 შეცვლილია postValue-ით
    }
}