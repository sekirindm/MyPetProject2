package com.example.mypetproject2.features.ui.ui

import android.content.Context
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.example.mypetproject2.R
//private val onClickListener: () -> Unit
open class NoUnderlineClickableSpan(private val textColor: Int) : ClickableSpan() {

    override fun onClick(widget: View) {
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
        ds.color = textColor
    }
}