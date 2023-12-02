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
import kotlinx.coroutines.CoroutineScope

class RepetitionWordsAdapter(
    private val deleteListener: (GameItemDb, Int) -> Unit,
) : RecyclerView.Adapter<RepetitionWordsAdapter.ViewHolder>() {
    var items: MutableList<GameItemDb> = mutableListOf()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_repit_ruls, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

//    fun getItemAtPosition(position: Int): GameItemDb {
//        return items[position]
//    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    fun removeItem(position: Int): GameItemDb {
        val deletedItem = items.removeAt(position)
        notifyItemRemoved(position)
        return deletedItem
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.wordTextView.text = currentItem.rightAnswer
        holder.ivDelete.setOnClickListener {

            // Вызывайте колбэк удаления, анимацию добавим вручную
            deleteListener(currentItem, position)
        }
        holder.tvRulesBd.text = Rules.rules[currentItem.rightAnswer.lowercase()]
    }


    fun updateData(newItems: List<GameItemDb>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}