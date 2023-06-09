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
    private val answers: List<Boolean>,
    private val answersHistory: List<Pair<String, String>>

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SCORE = 0
    private val VIEW_TYPE_PERCENTAGE = 2
    private val VIEW_TYPE_ANSWER_HISTORY = 1
     private val VIEW_TYPE_WORD_ANSWER_HISTORY = 3

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
            VIEW_TYPE_WORD_ANSWER_HISTORY -> {
                val view = inflater.inflate(R.layout.item_full_answer_history_rv, parent, false)
                WordAnswerHistoryViewHolder(view)
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
            VIEW_TYPE_WORD_ANSWER_HISTORY -> {
                val wordAnswerHistoryViewHolder = holder as WordAnswerHistoryViewHolder
                wordAnswerHistoryViewHolder.bind(answersHistory)
            }
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_SCORE
            1 -> VIEW_TYPE_ANSWER_HISTORY
            2 -> VIEW_TYPE_PERCENTAGE
            3 -> VIEW_TYPE_WORD_ANSWER_HISTORY
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

     inner class WordAnswerHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         private val rvWordHistoryAnswer: RecyclerView = itemView.findViewById(R.id.full_answer_history_rv)

         fun bind(answersHistory: List<Pair<String, String>>) {
             val wordAnswerHistoryAdapter = WordAnswerHistoryAdapter(answersHistory)
             rvWordHistoryAnswer.adapter = wordAnswerHistoryAdapter
             rvWordHistoryAnswer.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
         }
     }
}