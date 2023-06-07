package com.example.mypetproject2.features.ui.games.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.R

 class ContainerAdapter(
    private val score: Int,
    private val percentage: Float,
    private val answers: List<Boolean>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SCORE = 0
    private val VIEW_TYPE_PERCENTAGE = 2
    private val VIEW_TYPE_ANSWER_HISTORY = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_SCORE -> {
                val view = inflater.inflate(R.layout.item_score, parent, false)
                ScoreViewHolder(view)
            }
            VIEW_TYPE_PERCENTAGE -> {
                val view = inflater.inflate(R.layout.item_percents, parent, false)
                PercentageViewHolder(view)
            }
            VIEW_TYPE_ANSWER_HISTORY -> {
                val view = inflater.inflate(R.layout.item_answer_history_rv, parent, false)
                AnswerHistoryViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_SCORE -> {
                val scoreViewHolder = holder as ScoreViewHolder
                scoreViewHolder.bind(score)
            }
            VIEW_TYPE_PERCENTAGE -> {
                val percentageViewHolder = holder as PercentageViewHolder
                percentageViewHolder.bind(percentage)
            }
            VIEW_TYPE_ANSWER_HISTORY -> {
                val answerHistoryViewHolder = holder as AnswerHistoryViewHolder
                answerHistoryViewHolder.bind(answers)
            }
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_SCORE
            1 -> VIEW_TYPE_ANSWER_HISTORY
            2 -> VIEW_TYPE_PERCENTAGE
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    inner class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvScore: TextView = itemView.findViewById(R.id.tv_score)

        fun bind(score: Int) {
            tvScore.text = score.toString()
        }
    }

     inner class PercentageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         private val tvPercentage: TextView = itemView.findViewById(R.id.tv_percentage)

         fun bind(percentage: Float) {
             val formattedPercentage = String.format("%.0f%%", percentage)
             tvPercentage.text = formattedPercentage
         }
     }

    inner class AnswerHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val rvHistoryAnswer: RecyclerView = itemView.findViewById(R.id.rv_history_answer)

        fun bind(answers: List<Boolean>) {
            val answerHistoryAdapter = AnswerHistoryAdapter(answers)
            rvHistoryAnswer.adapter = answerHistoryAdapter
            rvHistoryAnswer.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}