package com.example.mypetproject2.features.ui.games.stress.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.R
import com.example.mypetproject2.data.database.GameItemDb
import com.example.mypetproject2.features.ui.games.Rules
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel

class RepetitionWordsAdapter(
     var deletedPosition: Int? = null,
    private val deleteListener: (GameItemDb) -> Unit,


) : RecyclerView.Adapter<RepetitionWordsAdapter.ViewHolder>() {
    /**
     *Список элементов для отображения в RecyclerView
     * */
    var items: MutableList<GameItemDb> = mutableListOf()



    /**
     *Внутренний класс ViewHolder для элементов списка
     * */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordTextView: TextView = itemView.findViewById(R.id.tv_right_word)
        val tvRulesBd: TextView = itemView.findViewById(R.id.tv_rules_repit_bd)
         val ivRules: ImageView = itemView.findViewById(R.id.iv_rules_bd)
        val ivDelete: ImageView = itemView.findViewById(R.id.iv_gelete)

        var isRulesVisible = false

        init {
            ivRules.setOnClickListener {
                isRulesVisible = !isRulesVisible
                tvRulesBd.visibility = if (isRulesVisible) View.VISIBLE else View.GONE
            }

        }
    }

    fun removeItem(position: Int) {
        if (position < 0 || position >= items.size) return
        val removedItem = items[position] // Получаем элемент для удаления
        items.removeAt(position) // Удаляем элемент из списка
        notifyItemRemoved(position)
        deleteListener(removedItem) // Вызываем обработчик удаления с удаленным элементом
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_repit_ruls, parent, false)
        return ViewHolder(itemView)
    }

    /**
     *Получение количества элементов в списке
     * */
    override fun getItemCount(): Int {
        return items.size
    }
    /**
     *Получение элемента по его позиции в списке
     * */

    fun getItemAtPosition(position: Int): GameItemDb {
        return items[position]
    }
    /**
     *Получение идентификатора элемента по его позиции в списке
     * */

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    /**
     *  Привязка данных элемента к ViewHolder
     * */
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.wordTextView.text = currentItem.rightAnswer

        holder.ivDelete.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                /**
                 *Вызов функции обратного вызова при касании элемента
                 * */
                //
                deleteListener(currentItem)
            }
            false
        }

        holder.tvRulesBd.text = Rules.rules[currentItem.rightAnswer.lowercase()]

        /**
         * Установка обработчика касания для элемента списка
         * */

    }
    /**
     *Обновление данных списка и уведомление адаптера об изменениях
     * */
    //
    fun updateData(newItems: List<GameItemDb>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}