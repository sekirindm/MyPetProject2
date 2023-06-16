package com.example.mypetproject2.features.ui.games.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.R
import com.example.mypetproject2.features.createCustomResultSpannableStringBuilder
import com.example.mypetproject2.features.ui.games.GamesViewModel

class WordAnswerHistoryAdapter(private val wordPairs: List<Pair<String, String>>, private val viewModel: GamesViewModel, private val historyItems: List<Boolean>) :
        RecyclerView.Adapter<WordAnswerHistoryAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_full_answer_history, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val wordPair = wordPairs[position]
            holder.bind(wordPair)
            val item = historyItems[position]

            if (item) {
                holder.ivUserAnswer.setImageResource(R.drawable.rectangle_63)
            } else {
                holder.ivUserAnswer.setImageResource(R.drawable.rectangle_incorrect_answer)
            }
        }

        override fun getItemCount(): Int {
            return wordPairs.size
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvRightAnswer: TextView = itemView.findViewById(R.id.tv_right_answer)
        private val tvAnswerUser: TextView = itemView.findViewById(R.id.tv_user_answer)
         val ivUserAnswer: ImageView = itemView.findViewById(R.id.iv_user_answer)

        fun bind(wordPair: Pair<String, String>) {
            val formattedWordPair = formatWordPair(wordPair)
            tvRightAnswer.text = formattedWordPair.first
            tvAnswerUser.text = formattedWordPair.second
        }


        private fun formatWordPair(wordPair: Pair<String, String>): Pair<CharSequence, CharSequence> {
            val rightAnswer = wordPair.first
            val userAnswer = wordPair.second

            val correctIndex = rightAnswer.indexOfFirst { it.isUpperCase() }
            val selectedVowelIndex = viewModel.selectedVowelIndex.value
            val selectedVowelChar = viewModel.selectedVowelChar.value

            val builder = createCustomResultSpannableStringBuilder(
                userAnswer,
                correctIndex,
                selectedVowelIndex,
                selectedVowelChar
            )
            return rightAnswer to builder
        }
    }
}

