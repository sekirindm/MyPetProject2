package com.example.mypetproject2.features.ui.notifications

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class CustomItemAnimation : DefaultItemAnimator() {


    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        val animatorList = mutableListOf<Animator>()

        // Устанавливаем анимацию альфа-прозрачности для исчезания элемента
        val alphaAnimator = ObjectAnimator.ofFloat(holder.itemView, "alpha", 1f, 0f)
        alphaAnimator.duration = 300

        // Устанавливаем анимацию масштабирования для исчезания элемента
        val scaleXAnimator = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 1f, 0f)
        val scaleYAnimator = ObjectAnimator.ofFloat(holder.itemView, "scaleY", 1f, 0f)
        scaleXAnimator.duration = 300
        scaleYAnimator.duration = 300

        // Добавляем анимации в список
        animatorList.add(alphaAnimator)
        animatorList.add(scaleXAnimator)
        animatorList.add(scaleYAnimator)

        // Создаем и запускаем анимацию удаления элемента
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animatorList)
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // Действия при начале анимации
            }

            override fun onAnimationEnd(animation: Animator) {
                // Действия при завершении анимации
                dispatchRemoveFinished(holder)
            }

            override fun onAnimationCancel(animation: Animator) {
                // Действия при отмене анимации
            }

            override fun onAnimationRepeat(animation: Animator) {
                // Действия при повторе анимации
            }
        })

        animatorSet.start()
        return true
    }
}




