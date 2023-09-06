package com.example.mypetproject2.features.ui.notifications

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
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

    init {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FavouritesWordsRvBinding.inflate(inflater, container, false)
        val rootView = binding.root
        recyclerView = binding.rvRepetition

        gameViewModel = ViewModelProvider(this)[GamesViewModel::class.java]
        repetitionWordsAdapter = RepetitionWordsAdapter(requireContext()) { item ->
            CoroutineScope(Dispatchers.IO).launch {
                gameViewModel.deleteGameItem(item)
            }


        }

        repetitionWordsAdapter.setHasStableIds(false)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = repetitionWordsAdapter

        gameViewModel.gameItemsLiveData.observe(viewLifecycleOwner, Observer { gameItems ->
            Log.d("FavoritesWords", "Observed gameItemsLiveData with ${gameItems.size} items")
            repetitionWordsAdapter.updateData(gameItems)
            recyclerView.scrollToPosition(0)


        })


        gameViewModel.updateRecyclerViewData()
        val dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.divider_drawable)
        val itemDecoration = DividerItemDecoration(requireContext(), dividerDrawable!!)
        recyclerView.addItemDecoration(itemDecoration)


        setupSwipeListener(recyclerView)

        return rootView
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val item = repetitionWordsAdapter.getItemAtPosition(position)
                CoroutineScope(Dispatchers.IO).launch {
                    gameViewModel.deleteGameItem(item)
                }


                val handler = Handler(Looper.getMainLooper())

                handler.postDelayed({
                        gameViewModel.deleteAndQueryAllItems(item)

                    },100)

                }
            }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}