package com.example.mypetproject2.features.ui.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

open class CustomClickableSpan(private val textColor: Int): ClickableSpan() {


    override fun onClick(widget: View) {
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
        ds.color = textColor
        ds.bgColor = Color.WHITE
    }
}