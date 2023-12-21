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
            10 -> "Первая"
            11 -> "Вторая"
            12 -> "Третья"
            13 -> "Четвертая"
            else -> "Игра" // Заголовок по умолчанию, если нет соответствующего gameNumber
        }
        launchGameWithDescription(gameNumber, gameDescription, title)
    }

    private fun launchGame9() {
        launchGameWithDescription(10, "Первая")
    }
    private fun launchGame10() {
        launchGameWithDescription(11, "Вторая")
    }
    private fun launchGame11() {
        launchGameWithDescription(12, "Третья")
    }
    private fun launchGame12() {
        launchGameWithDescription(13, "Четвертая")
    }

}