package com.example.mypetproject2.features.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.databinding.FragmentHomeBinding
import com.example.mypetproject2.features.ui.adapters.HomeAdapter
import com.example.mypetproject2.features.ui.home.minigame.GameCardViewModel
import com.example.mypetproject2.features.ui.home.minigame.GameStateCard
import com.example.mypetproject2.features.ui.home.minigame.HomeItemsList
import com.example.mypetproject2.features.ui.home.minigame.MiniGame

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: GameCardViewModel

    //при нажатии на кнопку добавить вариант ответа в checkWord
    private var homeAdapter: HomeAdapter = HomeAdapter { text, bText ->
        Log.i("homeAdapter", "$text $bText}")
        val userAnswer = text.replace("_", bText)
        Log.i("text", " $userAnswer")
        viewModel.checkWord(userAnswer)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initializeViewModel()
        initObserve()
        initGame()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = homeAdapter

        return binding.root
    }
    //Как обычно вешаю observe. С помощью submit отправляю данные в Adapter
    private fun initObserve() {
        viewModel.gameState.observe(viewLifecycleOwner) {
            when (it) {
                is GameStateCard.NewWord -> {
                    submitMiniGame(it.miniGame)
                }
                is GameStateCard.CheckedAnswer -> {
                    submitMiniGame(it.miniGame)
                        Log.i("text", "${it.miniGame}")
                    viewModel.delay()

            }
                is GameStateCard.FinishGame -> {
                    Log.i("finish", "Finish mini game")
                }

                else -> {}
            }
        }
    }

    private fun initializeViewModel() {
        recyclerView = binding.rvHome
        viewModel = ViewModelProvider(this)[GameCardViewModel::class.java]
    }

    private fun initGame() {
        viewModel.initGame()
    }

    private fun submitMiniGame(miniGame: MiniGame) {
//        viewModel.updateScreen.observe(viewLifecycleOwner) {
//            homeAdapter.submitList(it)
//        }
        val submitList = listOf(HomeItemsList.HomeMiniGame(miniGame))
        homeAdapter.submitList(submitList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.i("onDestroyView", "крах")
    }
}

