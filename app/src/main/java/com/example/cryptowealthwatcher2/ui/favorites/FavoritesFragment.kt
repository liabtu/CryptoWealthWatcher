package com.example.cryptowealthwatcher2.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptowealthwatcher2.databinding.FragmentFavoritesBinding
import com.example.cryptowealthwatcher2.ui.adapter.CoinAdapter
import com.example.cryptowealthwatcher2.utils.PreferenceManager
import com.example.cryptowealthwatcher2.viewmodel.CoinViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CoinViewModel by viewModels()
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var favAdapter: CoinAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        preferenceManager = PreferenceManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // ვაკვირდებით კრიპტოვალუტების მთლიან სიას API-დან
        viewModel.coins.observe(viewLifecycleOwner) { allCoins ->
            if (allCoins != null) {
                loadFavorites()
            }
        }
    }

    private fun setupRecyclerView() {
        // ფავორიტების სიიდან დიდხანს დაჭერისას ის ამოიშლება სიიდან
        favAdapter = CoinAdapter { coin ->
            preferenceManager.toggleFavorite(coin.id)
            Toast.makeText(requireContext(), "${coin.name} ამოიშალა ფავორიტებიდან!", Toast.LENGTH_SHORT).show()
            loadFavorites()
        }

        binding.rvFavorites.apply {
            adapter = favAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    // ფავორიტი მონეტების გაფილტვრა და ადაპტერისთვის გადაცემა
    private fun loadFavorites() {
        val allCoins = viewModel.coins.value ?: emptyList()
        val favIds = preferenceManager.getFavorites()

        // ვტოვებთ მხოლოდ იმ მონეტებს, რომელთა ID-ც შენახულია მეხსიერებაში
        val filteredList = allCoins.filter { coin ->
            favIds.contains(coin.id)
        }

        favAdapter.setData(filteredList)
    }

    // როცა მომხმარებელი სხვა ეკრანიდან ბრუნდება, სია ყოველთვის უნდა განახლდეს
    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}