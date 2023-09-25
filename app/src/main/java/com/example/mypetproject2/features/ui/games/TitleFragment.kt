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

        val tvTitle = binding.root.findViewById<TextView>(R.id.tv_title)
        val tvDescriptions = binding.root.findViewById<TextView>(R.id.tv_descriptions)
        val title = arguments?.getString("title")
        val descriptions = arguments?.getString("descriptions")
        tvTitle.text = title
        tvDescriptions.text = descriptions
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.bStartGame.setOnClickListener {
//            launchGameFragment()
        val gameNumber = arguments?.getInt("gameNumber") ?: 0

            binding.bStartGame.setOnClickListener {
                when (gameNumber) {
                    1 -> {
                        val action = TitleFragmentDirections.actionTitleFragmentToGamesFragment()
                        findNavController().navigate(action)
                    }
                    2 -> {
                        val action = TitleFragmentDirections.actionTitleFragmentToSpellingNNFragment()
                        findNavController().navigate(action)
                    }
                    3-> {
                        val action = TitleFragmentDirections.actionTitleFragmentToSpellingPrefFragment()
                        findNavController().navigate(action)
                    }
                    4 -> {
                        val action = TitleFragmentDirections.actionTitleFragmentToSpellingRootFragment()
                        findNavController().navigate(action)
                    }
                    5 -> {
                        val action = TitleFragmentDirections.actionTitleFragmentToSpellingSuffixFragment()
                        findNavController().navigate(action)
                    }
                    6-> {
                        val action = TitleFragmentDirections.actionTitleFragmentToChooseWordFragment()
                        findNavController().navigate(action)
                    }
                    else -> {
                        //TODO
                    }
                }

        }
        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navView.visibility = View.GONE
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
                navView.visibility = View.VISIBLE

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}