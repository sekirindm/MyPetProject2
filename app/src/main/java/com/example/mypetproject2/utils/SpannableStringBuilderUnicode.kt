package com.example.mypetproject2.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.mypetproject2.R
import com.example.mypetproject2.data.baselist.listPunctuationGameFour
import com.example.mypetproject2.features.ui.ui.NoUnderlineClickableSpan
import java.lang.Exception

fun main() {
    val word = listPunctuationGameFour[15]
    val startIndex = word.indexOf("|")
    val endIndex = word.lastIndexOf(">")
    print("$startIndex, ${endIndex - 1}")
}

fun isSubUnicode(s: Char): Boolean {
    val htmlText = "▢"
    return s in htmlText

}

fun isComma(s: Char): Boolean {
    val comma = ","
    return s in comma

}

@SuppressLint("ResourceType")
fun spannableStringBuilderUnicode(
    word: String,
    context: Context,
    textView: TextView,
    imageView: ImageView
): SpannableStringBuilder {

    val startIndex = word.indexOf("|")
    val endIndex = word.indexOf("|", startIndex + 1)
    val builder = SpannableStringBuilder(word.replace("|", ""))


    for (char in word.indices) {
        val character = word[char]
//        if (isComma(character)) {
//            imageView.setOnClickListener {
//                Log.d("imageView", "click")
//                builder.replace(char, char + 1, "▢") //
//
//                val spans = builder.getSpans(char, char + 1, Any::class.java)
//                for (span in spans) {
//                    builder.removeSpan(span)
//                }
//                textView.text = builder
//            }
//        }
        if (isSubUnicode(character)) {
            val clickableSpan =
                object : NoUnderlineClickableSpan(ContextCompat.getColor(context, R.color.gray)) {
                    override fun onClick(widget: View) {

                        val spans = builder.getSpans(char, char + 1, Any::class.java)
                        for (span in spans) {
                            builder.removeSpan(span)
                        }
                        textView.text = builder

                    }

                }
            builder.setSpan(
                clickableSpan,
                char,
                char + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            builder.setSpan(
                SubscriptSpan(),
                char,
                char + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            builder.setSpan(
                RelativeSizeSpan(1.1f),
                char, // start
                char + 1, // end
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE

            )
            builder.setSpan(
                StyleSpan(Typeface.BOLD),
                char, // start
                char + 1, // end
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (startIndex != -1 && endIndex != -1) {
                builder.setSpan(
                    StyleSpan(Typeface.BOLD),
                    startIndex,
                    endIndex - 1,
                    0
                )
            }

        }
    }
    return builder
}