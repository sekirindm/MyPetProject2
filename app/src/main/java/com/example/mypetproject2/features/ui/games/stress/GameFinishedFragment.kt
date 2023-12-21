package com.example.mypetproject2.features.ui.games.stress

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypetproject2.R
import com.example.mypetproject2.databinding.FragmentGameFinishedBinding
import com.example.mypetproject2.features.*
import com.example.mypetproject2.features.ui.games.stress.adapters.ContainerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class GameFinishedFragment : Fragment() {

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding get() = _binding!!

    private var score: Int = 0
    private var answers: List<Boolean> = emptyList()
    private var percentage: Float = 0f
    private var gameType: String = ""
    private lateinit var viewModel: GamesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        val rootView = binding.root


        score = arguments?.getInt("score") ?: 0
        percentage = arguments?.getFloat("percentage") ?: 0f
        answers = arguments?.getBooleanArray("answers")?.toList() ?: emptyList()
        gameType = arguments?.getString("gameType") ?: ""
        val answersHistory = (arguments?.getStringArray("uswerAnswerHistory") ?: arrayOf()).toList()
        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]
        val pair = when (gameType) {
            "stress" -> getPair(answersHistory)
            "spelling" -> getPairSpelling(answersHistory)
            "spellingpref" -> getPairSpellingPref(answersHistory)
            "spellingroot" -> getPairSpellingRoot(answersHistory)
            "spellingsuffix" -> getPairSpellingSuffix(answersHistory)
            "chooseWord" -> getPairChooseWord(answersHistory)
            "chooseSeparateWord" -> getPairChooseSeparateWord(answersHistory)
            "paronym" -> getPairParonym(answersHistory)
            "spellingtwelve" -> getPairSpellingTwelve(answersHistory)
            else -> emptyList()
        }

        Log.d("onCreateView", "answersHistory $pair")

        val layoutManager = LinearLayoutManager(requireContext())

        binding.rvContainer.layoutManager = layoutManager
        binding.rvContainer.adapter = ContainerAdapter(viewModel, score, percentage, answers, pair)


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                navController.popBackStack(R.id.navigation_dashboard, false)
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
