package com.example.mypetproject2.features.ui.notifications

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
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
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
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
            gameViewModel.deleteGameItem(gameItem)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = repetitionWordsAdapter

        gameViewModel.gameItem.observe(viewLifecycleOwner, Observer { gameItems ->
            Log.d("FavoritesWords", "Observed gameItemsLiveData with ${gameItems.size} items")
            repetitionWordsAdapter.updateData(gameItems)
        })
        gameViewModel.updateRecyclerViewData()

        val dividerDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.divider_drawable)
        val itemDecoration = DividerItemDecoration(requireContext(), dividerDrawable!!)
        recyclerView.addItemDecoration(itemDecoration)


        return rootView
    }

//    fun customAnimations(position: Int) {
//        // Устанавливаем анимацию удаления для элемента
//        recyclerView.layoutManager?.findViewByPosition(position)?.let { itemView ->
//            val animatorList = mutableListOf<Animator>()
//            val deletedItem = repetitionWordsAdapter.items[position]
//
//            // Устанавливаем анимацию альфа-прозрачности для исчезания элемента
//            val alphaAnimator = ObjectAnimator.ofFloat(itemView, "alpha", 1f, 0f)
//            alphaAnimator.duration = 300
//
//            // Устанавливаем анимацию масштабирования для исчезания элемента
//            val scaleXAnimator = ObjectAnimator.ofFloat(itemView, "scaleX", 1f, 0f)
//            val scaleYAnimator = ObjectAnimator.ofFloat(itemView, "scaleY", 1f, 0f)
//            scaleXAnimator.duration = 300
//            scaleYAnimator.duration = 300
//
//            // Добавляем анимации в список
//            animatorList.add(alphaAnimator)
//            animatorList.add(scaleXAnimator)
//            animatorList.add(scaleYAnimator)
//
//            // Создаем и запускаем анимацию удаления элемента
//            val animatorSet = AnimatorSet()
//            animatorSet.playTogether(animatorList)
//
//            animatorSet.addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
//
//                    gameViewModel.deleteGameItem(deletedItem)
//
//                    // Удалить элемент из адаптера после завершения анимации
//                    repetitionWordsAdapter.items.removeAt(position)
//                    repetitionWordsAdapter.notifyItemRemoved(position)
//                }
//
//            })
//
//            animatorSet.start()
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}