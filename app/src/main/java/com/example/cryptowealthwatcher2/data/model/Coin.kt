package com.example.cryptowealthwatcher2.data.model

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val current_price: Double
)