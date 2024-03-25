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
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var viewModel: GameCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initializeViewModel()
        initGame()

        initObserve()
        //при нажатии на кнопку добавить вариант ответа в checkWord
        homeAdapter = HomeAdapter { text, bNumber ->
            Log.i("homeAdapter", "$text $bNumber}")
            val r = text.replace("_", bNumber)
            Log.i("text", " $r")
            viewModel.checkWord(r)

        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = homeAdapter

        return binding.root
    }
    //Как обычно вешаю observe. С помощью submit отправляю данные в Adapter
    private fun initObserve() {
        viewModel.gameState.observe(viewLifecycleOwner) {
            when (it) {
                is GameStateCard.NewWord -> {
                    submitMiniGame(it.word, it.letters)
                }
                is GameStateCard.CheckedAnswer -> {
                    val answer = it.state.answers.last()
                        Log.i("text", "${answer.first}, ${answer.second}")
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
    private fun submitMiniGame(word: String, letters: List<String>) {
//        viewModel.updateScreen.observe(viewLifecycleOwner) {
//            homeAdapter.submitList(it)
//        }
        val submitList = listOf(HomeItemsList.HomeMiniGame(MiniGame(word, letters, false)))
        homeAdapter.submitList(submitList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

