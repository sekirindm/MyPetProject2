package com.example.mypetproject2.features.ui.games.stress.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.R
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel

class ContainerAdapter(
    private val viewModel: GamesViewModel,
    private val score: Int,
    private val percentage: Float,
    private val answers: List<Boolean>,
    private val answersHistory: List<Pair<String, String>>

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SCORE = 0
    private val VIEW_TYPE_PERCENTAGE = 2
    private val VIEW_TYPE_ANSWER_HISTORY = 1
    private val VIEW_TYPE_WORD_ANSWER_HISTORY = 3
    private val VIEW_TYPE_REPORT = 4
    private val VIEW_TYPE_NEW_GAME = 5

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
            VIEW_TYPE_REPORT -> {
                val view = inflater.inflate(R.layout.item_report, parent, false)
                ReportViewHolder(view)
            }
            VIEW_TYPE_NEW_GAME -> {
                val view = inflater.inflate(R.layout.item_start_new_game, parent, false)
                StartNewGameViewHolder(view)
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
            VIEW_TYPE_REPORT -> {
                val reportViewHolder = holder as ReportViewHolder
                reportViewHolder.bind()
            }
            VIEW_TYPE_NEW_GAME -> {
                val startNewGameViewHolder = holder as StartNewGameViewHolder
                startNewGameViewHolder.bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_SCORE
            1 -> VIEW_TYPE_ANSWER_HISTORY
            2 -> VIEW_TYPE_PERCENTAGE
            4 -> VIEW_TYPE_WORD_ANSWER_HISTORY
            3 -> VIEW_TYPE_REPORT
            5 -> VIEW_TYPE_NEW_GAME
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
            rvHistoryAnswer.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvReport: TextView = itemView.findViewById(R.id.tv_report)

        fun bind() {
            tvReport.text = "Отчет"
        }
    }
    inner class StartNewGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bNewGame: Button = itemView.findViewById(R.id.b_restart_game)

        fun bind() {
            bNewGame.setOnClickListener {
                val navController = itemView.findNavController()
                itemView.findNavController()
                    .navigate(R.id.action_gameFinishedFragment_to_navigation_dashboard)
                navController.popBackStack()

            }
        }
    }

    inner class WordAnswerHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val rvWordHistoryAnswer: RecyclerView =
            itemView.findViewById(R.id.full_answer_history_rv)

        fun bind(answersHistory: List<Pair<String, String>>) {
            val wordAnswerHistoryAdapter = WordAnswerHistoryAdapter(answersHistory, viewModel, answers)
            rvWordHistoryAnswer.adapter = wordAnswerHistoryAdapter
            rvWordHistoryAnswer.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        }
    }
}