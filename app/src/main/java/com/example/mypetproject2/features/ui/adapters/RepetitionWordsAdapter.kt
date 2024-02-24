package com.example.mypetproject2.features.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.R
import com.example.mypetproject2.data.database.gamedb.GameItemDb
import com.example.mypetproject2.data.baselist.Rules
import com.example.mypetproject2.features.ui.notifications.GameItemDiffUtils
import com.example.mypetproject2.features.ui.ui.CustomDialogFragment

class RepetitionWordsAdapter(
    private val deleteListener: (GameItemDb) -> Unit,
) : RecyclerView.Adapter<RepetitionWordsAdapter.ViewHolder>() {

    private var items: MutableList<GameItemDb> = mutableListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val customDialogFragment = CustomDialogFragment()
        private val wordTextView: TextView = itemView.findViewById(R.id.tv_right_word)
        private val tvRulesBd: TextView = itemView.findViewById(R.id.tv_rules_repit_bd)
        private val ivRules: ImageView = itemView.findViewById(R.id.iv_rules_bd)
        private val ivDelete: ImageView = itemView.findViewById(R.id.iv_gelete)
        private val ivFull: ImageView = itemView.findViewById(R.id.iv_full_bd)

        private var isRulesVisible = false

        init {
            ivRules.setOnClickListener {
                isRulesVisible = !isRulesVisible
                tvRulesBd.visibility = if (isRulesVisible) View.VISIBLE else View.GONE
            }
            adapterPosition
        }

        fun bind(currentItem: GameItemDb) {
            if (currentItem.rightAnswer.length < 20) {
                ivFull.visibility = GONE
            } else {
                ivFull.setOnClickListener {
                    customDialogFragment.customDialogWindowBd(
                        currentItem.rightAnswer,
                        itemView.context
                    )
                }

            }
            wordTextView.text = currentItem.rightAnswer
            ivDelete.setOnClickListener {

                deleteListener(currentItem)
            }
            tvRulesBd.text = Rules.rules[currentItem.rightAnswer.lowercase()]
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

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    fun updateData(newItems: List<GameItemDb>) {
        val diffResult = DiffUtil.calculateDiff(GameItemDiffUtils(items, newItems))
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }
}