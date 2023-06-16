package com.example.mypetproject2.features.ui.games

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.features.ui.games.adapters.AnswerHistoryAdapter
import com.example.mypetproject2.R
import com.example.mypetproject2.data.stress
import com.example.mypetproject2.databinding.FragmentGameFinishedBinding
import com.example.mypetproject2.features.getPair
import com.example.mypetproject2.features.isVowel
import com.example.mypetproject2.features.ui.games.adapters.ContainerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class GameFinishedFragment : Fragment() {

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding get() = _binding!!

    private var score: Int = 0
    private var answers: List<Boolean> = emptyList()
    private var percentage: Float = 0f
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
        val answersHistory = (arguments?.getStringArray("uswerAnswerHistory") ?: arrayOf()).toList()
        val pair = getPair(answersHistory)
        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]

        Log.d("onCreateView", "answersHistory $answersHistory")

        val layoutManager = LinearLayoutManager(requireContext())

        binding.rvContainer.layoutManager = layoutManager
        binding.rvContainer.adapter = ContainerAdapter(viewModel, score, percentage, answers, pair)


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navView.visibility = View.VISIBLE
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                navController.navigate(R.id.navigation_home)

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
