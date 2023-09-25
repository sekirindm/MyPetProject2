package com.example.mypetproject2.features.ui.notifications

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.R
import com.example.mypetproject2.databinding.FavouritesWordsRvBinding
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.features.ui.games.stress.adapters.DividerItemDecoration
import com.example.mypetproject2.features.ui.games.stress.adapters.RepetitionWordsAdapter
import kotlinx.coroutines.*


class FavoritesWords : Fragment() {
    private var _binding: FavouritesWordsRvBinding? = null
    private val binding get() = _binding!!

    private lateinit var gameViewModel: GamesViewModel
    private lateinit var repetitionWordsAdapter: RepetitionWordsAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FavouritesWordsRvBinding.inflate(inflater, container, false)
        val rootView = binding.root
        recyclerView = binding.rvRepetition


        gameViewModel = ViewModelProvider(this)[GamesViewModel::class.java]

        repetitionWordsAdapter = RepetitionWordsAdapter { gameItem ->
            CoroutineScope(Dispatchers.IO).launch { gameViewModel.deleteGameItem(gameItem) }
            val positionToRemove = repetitionWordsAdapter.items.indexOf(gameItem)
            repetitionWordsAdapter.removeItem(positionToRemove)
        }
        val customItemAnimator = CustomItemAnimation()
        recyclerView.itemAnimator = customItemAnimator


        repetitionWordsAdapter.setHasStableIds(false)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = repetitionWordsAdapter

        gameViewModel.gameItem.observe(viewLifecycleOwner, Observer { gameItems ->
            Log.d("FavoritesWords", "Observed gameItemsLiveData with ${gameItems.size} items")
            repetitionWordsAdapter.updateData(gameItems)
        })

        gameViewModel.updateRecyclerViewData()

        val dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.divider_drawable)
        val itemDecoration = DividerItemDecoration(requireContext(), dividerDrawable!!)
        recyclerView.addItemDecoration(itemDecoration)


        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}