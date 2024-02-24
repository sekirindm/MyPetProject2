package com.example.mypetproject2.features.ui.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.example.mypetproject2.R

class CustomDialogFragment {

    fun customDialogWindow(fullRightAnswer: String, fullUserAnswer: String, context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvFullRightAnswer: TextView? = dialog.findViewById(R.id.tv_full_right_answer)
        val tvFullUserAnswer: TextView? = dialog.findViewById(R.id.tv_full_user_answer)

        tvFullRightAnswer?.text = fullRightAnswer
        tvFullUserAnswer?.text = fullUserAnswer

        dialog.show()
    }
    fun customDialogWindowBd(fullRightAnswer: String, context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_custom_dialog_bd)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvFullRightAnswer: TextView? = dialog.findViewById(R.id.tv_full_right_answer)

        tvFullRightAnswer?.text = fullRightAnswer

        dialog.show()
    }
}