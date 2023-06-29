package com.example.mypetproject2.features.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var gameNumber = 0

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
         return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bStartSpelling.setOnClickListener {
            launchGame2()
        }
        binding.bStartStress.setOnClickListener {
            launchGame1()
        }
    }

    private fun launchGameFragment(gameNumber: Int, gameDescription: String, title: String) {
        val action = HomeFragmentDirections.actionNavigationHomeToTitleFragment(
            title,
            gameDescription,
            gameNumber
        )
        findNavController().navigate(action)
    }

    private fun launchGameWithDescription(gameNumber: Int, gameDescription: String, title: String) {
        this.gameNumber = gameNumber
        launchGameFragment(gameNumber, gameDescription, title)
    }

    private fun launchGameWithDescription(gameNumber: Int, gameDescription: String) {
        val title = when (gameNumber) {
            1 -> "Добро пожаловать в игру с ударениями! Вам предстоит расставлять ударения в словах. Определите правильное расположение ударения и преуспейте в игре. Улучшайте свои навыки в русском языке и научитесь правильно ставить ударения. Проверьте свои знания и станьте мастером ударений в словах!"
            2 -> "Игра Спеллинг с буквой 'Н' - это увлекательная игра, в которой вам нужно правильно составить слова, используя одну или две буквы Н. Вам будет представлено случайно выбранное слово, в котором одна или две буквы Н заменены символами подчеркивания _. Ваша задача - угадать пропущенные буквы и правильно составить слово"
            else -> "Игра" // Заголовок по умолчанию, если нет соответствующего gameNumber
        }
        launchGameWithDescription(gameNumber, gameDescription, title)
    }

    private fun launchGame1() {
        launchGameWithDescription(1, "Ударение")
    }

    private fun launchGame2() {
        launchGameWithDescription(2, "Н и НН")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}