package com.example.mypetproject2.features.ui.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.features.ui.games.adapters.AnswerHistoryAdapter
import com.example.mypetproject2.R
import com.example.mypetproject2.data.stress
import com.example.mypetproject2.databinding.FragmentGameFinishedBinding
import com.example.mypetproject2.features.isVowel
import com.example.mypetproject2.features.ui.games.adapters.ContainerAdapter

class GameFinishedFragment : Fragment() {

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding get() = _binding!!

    private var score: Int = 0
    private var answers: List<Boolean> = emptyList()
    private var percentage: Float = 0f
    private var answersHistory: List<Pair<String, String>> = emptyList()


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
        answersHistory = arguments?.getStringArrayList("answersHistory")?.map {
            val pair = it.split(":")
            Pair(pair[0], pair[1])
        } ?: emptyList()

        val layoutManager = LinearLayoutManager(requireContext())

        binding.rvContainer.layoutManager = layoutManager
        binding.rvContainer.adapter = ContainerAdapter(score, percentage, answers, answersHistory)


        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
