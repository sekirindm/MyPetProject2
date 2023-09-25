package com.example.mypetproject2.features.ui.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.R
import com.example.mypetproject2.databinding.FragmentTitleBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class TitleFragment : Fragment() {

    private var _binding: FragmentTitleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTitleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title")
        val descriptions = arguments?.getString("descriptions")
        val gameNumber = arguments?.getInt("gameNumber") ?: 0

        with(binding) {
            tvTitle.text = title
            tvDescriptions.text = descriptions
        }

        binding.bStartGame.setOnClickListener {
            val action = when (gameNumber) {
                1 -> { TitleFragmentDirections.actionTitleFragmentToGamesFragment() }
                2 -> { TitleFragmentDirections.actionTitleFragmentToSpellingNNFragment() }
                3 -> { TitleFragmentDirections.actionTitleFragmentToSpellingPrefFragment() }
                4 -> { TitleFragmentDirections.actionTitleFragmentToSpellingRootFragment() }
                5 -> { TitleFragmentDirections.actionTitleFragmentToSpellingSuffixFragment() }
                6 -> { TitleFragmentDirections.actionTitleFragmentToChooseWordFragment() }
                else -> { TitleFragmentDirections.actionTitleFragmentToChooseWordFragment() }
            }
            findNavController().navigate(action)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}