package com.example.mypetproject2.features.ui.home

import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypetproject2.data.listPunctuationGameTwo
import com.example.mypetproject2.databinding.FragmentHomeBinding
import com.example.mypetproject2.features.spannableStringBuilderUnicode
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.features.ui.games.stress.logic.GamesLogic

class HomeFragment : Fragment() {
    private lateinit var spannableStringBuilder: SpannableStringBuilder

    private lateinit var viewModel: GamesViewModel
    private var _binding: FragmentHomeBinding? = null
private val gamesLogic = GamesLogic()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]
        spannableStringBuilder = SpannableStringBuilder()
        binding.textView .movementMethod = LinkMovementMethod.getInstance()
        webViewTest()
//        setupWordClick()
//        updateSelectedVowelFormatting()
        return binding.root
    }

    fun webViewTest() {
        val textView = binding.textView

        val list = listPunctuationGameTwo.random()
        val htmlText = "▢"
        val modifiedListToUnicode = list.replace(",", htmlText)
//        val unicode = Html.fromHtml(modifiedListToUnicode, Html.FROM_HTML_MODE_LEGACY)
        val span = spannableStringBuilderUnicode(modifiedListToUnicode, requireContext(), textView)

        textView.text = span
//        Log.d("span", "$span")
//
    }

//    fun updateSelectedVowelFormatting() {
//        val selectedVowelIndex = viewModel.selectedVowelIndex.value ?: -1
//        val selectedVowelChar = viewModel.selectedVowelChar.value
//
//        val spannableLength = spannableStringBuilder.length
//        if (selectedVowelIndex in 0 until spannableLength && selectedVowelChar != null) {
//           gamesLogic.updateSelectedVowelFormatting(
//                spannableStringBuilder,
//                selectedVowelIndex,
//                selectedVowelChar
//            )
//            binding.textView.text = spannableStringBuilder
//        }
//    }

//    private fun setupWordClick() {
//        val list = listPunctuationGameTwo.random()
//        val htmlText = "<sub>▢</sub>"
//        val modifiedListToUnicode = list.replace(",", htmlText)
//        val unicode = Html.fromHtml(modifiedListToUnicode, Html.FROM_HTML_MODE_LEGACY)
//        spannableStringBuilder =
//            spannableStringBuilderUnicode(list, requireContext(), te)
////                handleVowelClick(characterIndex, character)
//
//        binding.textView.text = spannableStringBuilder
//    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}