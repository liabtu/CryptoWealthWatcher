package com.example.cryptowealthwatcher2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptowealthwatcher2.databinding.FragmentHomeBinding
import com.example.cryptowealthwatcher2.ui.adapter.CoinAdapter
import com.example.cryptowealthwatcher2.utils.PreferenceManager
import com.example.cryptowealthwatcher2.viewmodel.CoinViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CoinViewModel by viewModels()
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var coinAdapter: CoinAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        preferenceManager = PreferenceManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        // აქ გადავცემთ დიდხანს დაჭერის ლოგიკას
        coinAdapter = CoinAdapter { coin ->
            val isAdded = preferenceManager.toggleFavorite(coin.id)
            if (isAdded) {
                Toast.makeText(requireContext(), "${coin.name} დაემატა ფავორიტებში! ⭐️", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "${coin.name} ამოიშალა ფავორიტებიდან!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.rvCoins.apply {
            adapter = coinAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.coins.observe(viewLifecycleOwner) { coinsList ->
            if (coinsList != null) {
                coinAdapter.setData(coinsList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // ყოველ ჯერზე როცა Home ეკრანზე დავბრუნდებით, სია განახლდება მონიშვნების დასანახად
        if (::coinAdapter.isInitialized) {
            coinAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}