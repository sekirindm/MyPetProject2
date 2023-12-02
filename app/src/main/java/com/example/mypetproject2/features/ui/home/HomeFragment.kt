package com.example.mypetproject2.features.ui.home

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mypetproject2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        webViewTest()
        return binding.root
    }
//    A<sup>2</sup> + B<sup>2</sup> = C<sup>2</sup>
//    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
    fun webViewTest() {
    val textView = binding.textView

    val htmlText = "Всем привет<sub>▢</sub> я ваш босс"
    textView.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)


}
//        val webView = binding.textView
//        webView.text = "<style>body { font-size: 20px; }</style>Всем привет<sub>▢</sub>я ваш босс"


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}