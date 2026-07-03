package com.example.cryptowealthwatcher2.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    // SharedPreferences ფაილის ინიციალიზაცია სახელად "crypto_prefs"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("crypto_prefs", Context.MODE_PRIVATE)

    // --- ფავორიტების სექცია ---

    // ფავორიტი კრიპტოების ID-ების სიის (Set) წაკითხვა
    fun getFavorites(): Set<String> {
        return sharedPreferences.getStringSet("favorite_coins", emptySet()) ?: emptySet()
    }

    // ფავორიტის სტატუსის გადართვა (თუ არის - წაშლის, თუ არ არის - დაამატებს)
    fun toggleFavorite(coinId: String): Boolean {
        val currentFavorites = getFavorites().toMutableSet()
        val isNowFavorite: Boolean

        if (currentFavorites.contains(coinId)) {
            currentFavorites.remove(coinId)
            isNowFavorite = false
        } else {
            currentFavorites.add(coinId)
            isNowFavorite = true
        }

        sharedPreferences.edit().putStringSet("favorite_coins", currentFavorites).apply()
        return isNowFavorite
    }

    // ამოწმებს, არის თუ არა კონკრეტული კრიპტო ფავორიტებში
    fun isFavorite(coinId: String): Boolean {
        return getFavorites().contains(coinId)
    }

    // --- პორტფოლიოს სექცია ---

    // აქტივის რაოდენობის შენახვა ტელეფონის მეხსიერებაში (მაგ: "portfolio_bitcoin" -> "0.5")
    fun savePortfolioAsset(coinId: String, amount: Double) {
        sharedPreferences.edit().putString("portfolio_$coinId", amount.toString()).apply()
    }

    // ყველა შენახული აქტივის წაკითხვა (ID -> რაოდენობა)
    fun getPortfolioAssets(): Map<String, Double> {
        val assets = mutableMapOf<String, Double>()
        val allEntries = sharedPreferences.all

        for ((key, value) in allEntries) {
            if (key.startsWith("portfolio_")) {
                val coinId = key.substringAfter("portfolio_")
                val amount = value.toString().toDoubleOrNull() ?: 0.0
                if (amount > 0) {
                    assets[coinId] = amount
                }
            }
        }
        return assets
    }

    // აქტივის წაშლა პორტფოლიოდან
    fun removePortfolioAsset(coinId: String) {
        sharedPreferences.edit().remove("portfolio_$coinId").apply()
    }
}