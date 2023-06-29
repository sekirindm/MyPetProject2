package com.example.mypetproject2.features.ui.games

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.R
import com.example.mypetproject2.databinding.FragmentGameFinishedBinding
import com.example.mypetproject2.databinding.FragmentGamesBinding
import com.example.mypetproject2.databinding.FragmentHomeBinding
import com.example.mypetproject2.databinding.FragmentTitleBinding
import com.example.mypetproject2.features.ui.home.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class TitleFragment : Fragment() {

    private var _binding: FragmentTitleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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
                        // Здесь запускаем первую игру
                        val action = TitleFragmentDirections.actionTitleFragmentToNavigationDashboard()
                        findNavController().navigate(action)
                    }
                    2 -> {
                        // Здесь запускаем вторую игру
                        val action = TitleFragmentDirections.actionTitleFragmentToSpellingNNFragment()
                        findNavController().navigate(action)
                    }
                    else -> {
                        //TODO
                    }
                }
//            }

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

    private fun launchGameFragment() {
        findNavController().navigate(R.id.action_titleFragment_to_spellingNNFragment)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}