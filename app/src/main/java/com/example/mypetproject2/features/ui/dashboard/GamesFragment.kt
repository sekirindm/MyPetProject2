package com.example.mypetproject2.features.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypetproject2.R
import com.example.mypetproject2.databinding.FragmentGamesBinding
import com.example.mypetproject2.databinding.FragmentHomeBinding

class GamesFragment : Fragment() {

    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!

    private lateinit var tvWord: TextView
    private var selectedVowelIndex: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        val rootView = binding.root

        tvWord = binding.tvWord
        val buttonCheck = binding.bCheck
        buttonCheck.setOnClickListener { checkWord() }

        val word = "Привет"
        val spannableStringBuilder = SpannableStringBuilder(word)

        for (i in 0 until word.length) {
            val character = word[i]
            if (isVowel(character)) {
                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(view: View) {
                        if (selectedVowelIndex != -1) {
                            spannableStringBuilder.setSpan(
                                ForegroundColorSpan(Color.BLACK),
                                selectedVowelIndex,
                                selectedVowelIndex + 1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            val lowercaseChar =
                                spannableStringBuilder[selectedVowelIndex].lowercaseChar()
                            spannableStringBuilder.replace(
                                selectedVowelIndex,
                                selectedVowelIndex + 1,
                                lowercaseChar.toString()
                            )
                        }

                        spannableStringBuilder.setSpan(
                            ForegroundColorSpan(Color.BLACK),
                            i,
                            i + 1,
                            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        val uppercaseChar = character.uppercaseChar()

                        spannableStringBuilder.replace(i, i + 1, uppercaseChar.toString())

                        selectedVowelIndex = i

                        tvWord.text = spannableStringBuilder
                    }
                }
                spannableStringBuilder.setSpan(clickableSpan, i, i + 1, 0)
            }
        }
        tvWord.text = spannableStringBuilder
        tvWord.movementMethod = LinkMovementMethod.getInstance()

        return rootView
    }

    private fun isVowel(c: Char): Boolean {
        val vowels = listOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я')
        return c.lowercaseChar() in vowels
    }

    private fun checkWord() {
        if (selectedVowelIndex != -1) {
            val word = tvWord.text.toString()
            val selectedVowel = word[selectedVowelIndex]
            val correctVowel = 'е' // Здесь нужно указать правильную гласную букву

            val spannableStringBuilder = SpannableStringBuilder(word)

            for (i in 0 until word.length) {
                val character = word[i]
                if (isVowel(character)) {
                    if (character.equals(correctVowel, ignoreCase = true)) {
                        spannableStringBuilder.setSpan(
                            ForegroundColorSpan(Color.GREEN),
                            i,
                            i + 1,
                            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    } else {
                        spannableStringBuilder.setSpan(
                            ForegroundColorSpan(Color.RED),
                            i,
                            i + 1,
                            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
            tvWord.text = spannableStringBuilder
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}