package com.example.mypetproject2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.databinding.FragmentTitleBinding

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
                1 -> TitleFragmentDirections.actionTitleFragmentToStressFragment()
                2 -> TitleFragmentDirections.actionTitleFragmentToSpellingNNFragment()
                3 -> TitleFragmentDirections.actionTitleFragmentToSpellingPrefFragment()
                4 -> TitleFragmentDirections.actionTitleFragmentToSpellingRootFragment()
                5 -> TitleFragmentDirections.actionTitleFragmentToSpellingSuffixFragment()
                6 -> TitleFragmentDirections.actionTitleFragmentToChooseWordFragment()
                7 -> TitleFragmentDirections.actionTitleFragmentToChooseSeparateSpellingWordFragment()
                8 -> TitleFragmentDirections.actionTitleFragmentToParonymGameFragment(1)
                9 -> TitleFragmentDirections.actionTitleFragmentToGameTwelveFragment()
                10 -> TitleFragmentDirections.actionTitleFragmentToPunctuationGameOneFragment(1)
                11 -> TitleFragmentDirections.actionTitleFragmentToPunctuationGameOneFragment(2)
                12 -> TitleFragmentDirections.actionTitleFragmentToPunctuationGameOneFragment(3)
                13 -> TitleFragmentDirections.actionTitleFragmentToPunctuationGameOneFragment(4)
                14 -> TitleFragmentDirections.actionTitleFragmentToParonymGameFragment(2)
                15 -> TitleFragmentDirections.actionTitleFragmentToGameFourteenFragment()
                16 -> TitleFragmentDirections.actionTitleFragmentToSearchLexicalErrorsFragment()
                else -> {
                    TitleFragmentDirections.actionTitleFragmentToChooseWordFragment()
                }
            }
            findNavController().navigate(action)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}