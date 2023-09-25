package com.example.mypetproject2.features.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.databinding.FragmentDashboardBinding
import com.example.mypetproject2.features.ui.home.HomeViewModel

class DashboardFragment : Fragment() {

    private var gameNumber = 0

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            bStartSpelling.setOnClickListener {
                launchGame2()
            }
            bStartStress.setOnClickListener {
                launchGame1()
            }
            bStartSpellingPref.setOnClickListener {
                launchGame3()
            }
            bStartSpellingRoot.setOnClickListener {
                launchGame4()
            }
            bStartSpellingSuffix.setOnClickListener {
                launchGame5()
            }
            bStartChooseWord.setOnClickListener {
                launchGame6()
            }
        }
    }

    private fun launchGameFragment(gameNumber: Int, gameDescription: String, title: String) {
        val action = DashboardFragmentDirections.actionNavigationDashboardToTitleFragment(
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
            3 -> "чики пики"
            4 -> "рики пики"
            5 -> "куки пукки"
            6 -> "Выбери привильное слово!"
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
    private fun launchGame3() {
        launchGameWithDescription(3, "Правописание приставок")
    }

    private fun launchGame4() {
        launchGameWithDescription(4, "Правописание корней")
    }
    private fun launchGame5() {
        launchGameWithDescription(5, "Правописание суффиксов")
    }
    private fun launchGame6() {
        launchGameWithDescription(6, "Граматика")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}