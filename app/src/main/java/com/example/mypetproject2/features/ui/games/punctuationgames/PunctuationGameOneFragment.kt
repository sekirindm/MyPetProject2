package com.example.mypetproject2.features.ui.games.punctuationgames

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mypetproject2.R
import com.example.mypetproject2.databinding.FragmentChooseSeparateSpellingWordBinding
import com.example.mypetproject2.databinding.FragmentPunctuationBinding
import com.example.mypetproject2.databinding.FragmentPunctuationGameOneBinding
import com.example.mypetproject2.features.ui.games.choosespelling.GameSeparateWordViewModel

class PunctuationGameOneFragment : Fragment() {

    private var _binding: FragmentPunctuationGameOneBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPunctuationGameOneBinding.inflate(inflater, container, false)

        val text = binding.tvWord
        val gameNumber = arguments?.getInt("gameNumber") ?: 0

        when (gameNumber) {
            1 -> text.text = "Первая"
            2 -> text.text = "Вторая"
            3 -> text.text = "Третья"
            4 -> text.text = "Четвертая"
        }
        return binding.root
    }
}