package com.example.cryptowealthwatcher2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptowealthwatcher2.data.model.Coin
import com.example.cryptowealthwatcher2.databinding.ItemCoinBinding

// დამხმარე მონაცემთა კლასი პორტფოლიოს სიისთვის
data class PortfolioItem(
    val coin: Coin,
    val amount: Double
)

class PortfolioAdapter(
    // ლამბდა ფუნქცია ელემენტის წასაშლელად დიდხანს დაჭერისას
    private val onLongClick: (PortfolioItem) -> Unit
) : RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder>() {

    private var portfolioList = emptyList<PortfolioItem>()

    fun setData(newList: List<PortfolioItem>) {
        portfolioList = newList
        notifyDataSetChanged()
    }

    inner class PortfolioViewHolder(val binding: ItemCoinBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        val binding = ItemCoinBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PortfolioViewHolder(binding)
    }

    override fun getItemCount(): Int = portfolioList.size

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        val item = portfolioList[position]

        // ვითვლით ჯამურ ღირებულებას: რაოდენობა გამრავლებული მიმდინარე ფასზე
        val totalValue = item.amount * item.coin.current_price

        holder.binding.tvCoinName.text = item.coin.name

        // სიმბოლოს ნაცვლად ვაჩვენებთ რამდენი მონეტა გვაქვს (მაგ: 0.5 BTC)
        holder.binding.tvCoinSymbol.text = "${item.amount} ${item.coin.symbol.uppercase()}"

        // ჯამურ ღირებულებას ვაჩვენებთ 2 ათწილადის სიზუსტით
        holder.binding.tvCoinPrice.text = "$${String.format("%.2f", totalValue)}"

        // სურათის ჩატვირთვა Glide ბიბლიოთეკით
        Glide.with(holder.itemView.context)
            .load(item.coin.image)
            .into(holder.binding.ivCoinIcon)

        // დიდხანს დაჭერით მომხმარებელს ვთავაზობთ ელემენტის წაშლას
        holder.itemView.setOnLongClickListener {
            onLongClick(item)
            true
        }
    }
}