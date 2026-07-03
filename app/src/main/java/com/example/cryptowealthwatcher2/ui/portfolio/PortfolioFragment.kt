package com.example.cryptowealthwatcher2.ui.portfolio

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowealthwatcher2.data.model.Coin
import com.example.cryptowealthwatcher2.databinding.FragmentPortfolioBinding
import com.example.cryptowealthwatcher2.ui.adapter.PortfolioAdapter
import com.example.cryptowealthwatcher2.ui.adapter.PortfolioItem
import com.example.cryptowealthwatcher2.utils.PreferenceManager
import com.example.cryptowealthwatcher2.viewmodel.CoinViewModel

class PortfolioFragment : Fragment() {

    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CoinViewModel by viewModels()
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var portfolioAdapter: PortfolioAdapter
    private var marketCoins: List<Coin> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPortfolioBinding.inflate(inflater, container, false)
        preferenceManager = PreferenceManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // ვაკვირდებით კრიპტოების სიას API-დან მიმდინარე ფასების მისაღებად
        viewModel.coins.observe(viewLifecycleOwner) { coins: List<Coin>? ->
            if (coins != null) {
                marketCoins = coins
                loadPortfolio()
            }
        }

        // ახალი აქტივის დამატების ღილაკის მართვა
        binding.fabAddAsset.setOnClickListener {
            if (marketCoins.isNotEmpty()) {
                showAddAssetDialog()
            } else {
                Toast.makeText(requireContext(), "მონაცემები იტვირთება, გთხოვთ მოიცადოთ...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        portfolioAdapter = PortfolioAdapter { item ->
            showDeleteConfirmation(item)
        }
        val recyclerView: RecyclerView = binding.rvPortfolio
        recyclerView.adapter = portfolioAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
    }

    // პორტფოლიოს ჩატვირთვა და საერთო ბალანსის გამოთვლა
    private fun loadPortfolio() {
        val savedAssets = preferenceManager.getPortfolioAssets()
        val portfolioItems = mutableListOf<PortfolioItem>()
        var totalBalance = 0.0

        for ((coinId, amount) in savedAssets) {
            val matchingCoin = marketCoins.find { it.id == coinId }
            if (matchingCoin != null) {
                portfolioItems.add(PortfolioItem(matchingCoin, amount))
                totalBalance += amount * matchingCoin.current_price
            }
        }

        portfolioAdapter.setData(portfolioItems)
        // ბალანსის ფორმატირება ორ ათწილადამდე
        binding.tvTotalBalance.text = "$${String.format("%.2f", totalBalance)}"
    }

    // აქტივის დამატების დიალოგური ფანჯარა (AlertDialog)
    private fun showAddAssetDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("კრიპტო აქტივის დამატება")

        // ვქმნით დინამიურ ლეიაუთს დიალოგისთვის
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        // კრიპტოვალუტის ასარჩევი Spinner
        val spinner = Spinner(requireContext())
        val coinNames = marketCoins.map { "${it.name} (${it.symbol.uppercase()})" }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, coinNames)
        spinner.adapter = spinnerAdapter
        layout.addView(spinner)

        // ცარიელი სივრცე დაშორებისთვის
        val spacer = View(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(1, 30)
        }
        layout.addView(spacer)

        // რაოდენობის შესაყვანი ველი (მხოლოდ ციფრები და წერტილი)
        val input = EditText(requireContext()).apply {
            hint = "შეიყვანეთ რაოდენობა (მაგ: 0.5)"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        layout.addView(input)

        builder.setView(layout)

        builder.setPositiveButton("დამატება") { dialog, _ ->
            val selectedIndex = spinner.selectedItemPosition
            val amountStr = input.text.toString()

            if (amountStr.isNotEmpty()) {
                val amount = amountStr.toDoubleOrNull()
                if (amount != null && amount > 0) {
                    val selectedCoin = marketCoins[selectedIndex]
                    // მონაცემების შენახვა SharedPreferences-ში
                    preferenceManager.savePortfolioAsset(selectedCoin.id, amount)
                    loadPortfolio()
                    Toast.makeText(requireContext(), "${selectedCoin.name} დაემატა პორტფოლიოში!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "არასწორი რაოდენობა!", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("გაუქმება") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    // პორტფოლიოდან აქტივის წაშლის დადასტურება
    private fun showDeleteConfirmation(item: PortfolioItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("აქტივის წაშლა")
            .setMessage("ნამდვილად გსურთ ${item.coin.name}-ის წაშლა პორტფოლიოდან?")
            .setPositiveButton("წაშლა") { _, _ ->
                preferenceManager.removePortfolioAsset(item.coin.id)
                loadPortfolio() // სიის განახლება
                Toast.makeText(requireContext(), "წაიშალა!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("გაუქმება", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadPortfolio()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}