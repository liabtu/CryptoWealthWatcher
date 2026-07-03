package com.example.cryptowealthwatcher2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptowealthwatcher2.data.model.Coin
import com.example.cryptowealthwatcher2.databinding.ItemCoinBinding
import com.example.cryptowealthwatcher2.utils.PreferenceManager

class CoinAdapter(
    // კონსტრუქტორში გადავცემთ ფუნქციას, რომელიც გამოიძახება დიდხანს დაჭერისას
    private val onFavoriteClick: (Coin) -> Unit
) : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    private var coinList = emptyList<Coin>()
    private lateinit var preferenceManager: PreferenceManager

    fun setData(newList: List<Coin>) {
        coinList = newList
        notifyDataSetChanged()
    }

    inner class CoinViewHolder(val binding: ItemCoinBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding = ItemCoinBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        // SharedPreferences-ის მენეჯერის შექმნა კონტექსტის გამოყენებით
        preferenceManager = PreferenceManager(parent.context)
        return CoinViewHolder(binding)
    }

    override fun getItemCount(): Int = coinList.size

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = coinList[position]

        // ტექსტების შევსება
        holder.binding.tvCoinName.text = coin.name
        holder.binding.tvCoinSymbol.text = coin.symbol.uppercase()
        holder.binding.tvCoinPrice.text = "$${coin.current_price}"

        // სურათის ჩატვირთვა Glide ბიბლიოთეკით
        Glide.with(holder.itemView.context)
            .load(coin.image)
            .into(holder.binding.ivCoinIcon)

        // ვამოწმებთ, არის თუ არა ეს მონეტა ფავორიტებში და ვიზუალურად ვნიშნავთ
        val isFav = preferenceManager.isFavorite(coin.id)
        if (isFav) {
            // თუ ფავორიტია, ბარათს ვუკეთებთ ოქროსფერ ჩარჩოს (stroke)
            holder.binding.root.strokeColor = android.graphics.Color.parseColor("#FFD700")
            holder.binding.root.strokeWidth = 4
        } else {
            // თუ არ არის, ჩარჩოს ვაქრობთ
            holder.binding.root.strokeWidth = 0
        }

        // დიდხანს დაჭერისას (Long Click) ვიძახებთ ფავორიტების ფუნქციას
        holder.itemView.setOnLongClickListener {
            onFavoriteClick(coin)
            notifyItemChanged(position)
            true
        }
    }
}