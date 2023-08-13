package com.example.mypetproject2.features.ui.games.spelling

import android.app.AlertDialog
import android.text.TextUtils.replace
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.R
import com.google.android.material.bottomnavigation.BottomNavigationView

fun transformWord(word: String): String {
    var upperCaseCount = 0
    var transformedWord = ""

    for (char in word) {
        if (char.isUpperCase()) {
            upperCaseCount++
            if (upperCaseCount == 2) {
                continue
            }
        }
        transformedWord += char
    }

    return transformedWord
}

fun transformWordSuf(word: String): String {
    var upperCaseCount = 0
    var transformedWord = ""

    for (char in word) {
        if (char.isUpperCase()) {
            upperCaseCount++
            if (upperCaseCount == 2) {
                continue
            }
        }
        transformedWord += char
    }

    return transformedWord.replace(Regex("[!@]")) {""}
}

fun Fragment.setupOnBackPressedCallback() {
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        })
}

private fun Fragment.hideNavView() {
    val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
    navView.visibility = View.GONE
}

private fun Fragment.showExitConfirmationDialog() {
    val builder = AlertDialog.Builder(requireContext())
    builder.setTitle("Выход из игры")
    builder.setMessage("Вы точно хотите выйти из игры?")
    builder.setPositiveButton("Да") { dialog, _ ->
        dialog.dismiss()
        findNavController().popBackStack()
        hideNavView()
    }
    builder.setNegativeButton("Нет") { dialog, _ ->
        dialog.dismiss()
    }
    val dialog = builder.create()
    dialog.show()
}

