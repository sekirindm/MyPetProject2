package com.example.mypetproject2.features.ui.punctuation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.R
import com.example.mypetproject2.databinding.FragmentPunctuationBinding
import com.example.mypetproject2.features.ui.dashboard.DashboardFragmentDirections


class PunctuationFragment : Fragment() {

    private var _binding: FragmentPunctuationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _binding = FragmentPunctuationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            bPunctuation1.setOnClickListener {
                launchGame9()
            }
            bPunctuation2.setOnClickListener {
                launchGame10()
            }
            bPunctuation3.setOnClickListener {
                launchGame11()
            }
            bPunctuation4.setOnClickListener {
                launchGame12()
            }
        }
    }

    private fun launchGameFragment(gameNumber: Int, gameDescription: String, title: String) {
        val action = PunctuationFragmentDirections.actionPunctuationFragment2ToTitleFragment(
            title,
            gameDescription,
            gameNumber
        )
        findNavController().navigate(action)
    }

    private fun launchGameWithDescription(gameNumber: Int, gameDescription: String, title: String) {
        launchGameFragment(gameNumber, gameDescription, title)
    }

    private fun launchGameWithDescription(gameNumber: Int, gameDescription: String) {
        val title = when (gameNumber) {
            9 -> "хряч"
            10 -> "Игра Спеллинг с буквой 'Н' - это увлекательная игра, в которой вам нужно правильно составить слова, используя одну или две буквы Н. Вам будет представлено случайно выбранное слово, в котором одна или две буквы Н заменены символами подчеркивания _. Ваша задача - угадать пропущенные буквы и правильно составить слово"
            11 -> "чики пики"
            12 -> "рики пики"
            else -> "Игра" // Заголовок по умолчанию, если нет соответствующего gameNumber
        }
        launchGameWithDescription(gameNumber, gameDescription, title)
    }

    private fun launchGame9() {
        launchGameWithDescription(9, "срааака")
    }
    private fun launchGame10() {
        launchGameWithDescription(10, "еее")
    }
    private fun launchGame11() {
        launchGameWithDescription(11, "ииии")
    }
    private fun launchGame12() {
        launchGameWithDescription(12, "ннннн")
    }

}