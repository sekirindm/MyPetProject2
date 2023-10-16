package com.example.mypetproject2.features.ui.games.stress.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.R
import com.example.mypetproject2.data.database.GameItemDb
import com.example.mypetproject2.features.createCustomResultSpannableStringBuilder
import com.example.mypetproject2.features.markString
import com.example.mypetproject2.features.ui.games.Rules
import com.example.mypetproject2.features.ui.games.spelling.transformWord
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordAnswerHistoryAdapter(
    private val wordPairs: List<Pair<String, String>>,
    private val viewModel: GamesViewModel,
    private val historyItems: List<Boolean>,
) : RecyclerView.Adapter<WordAnswerHistoryAdapter.ViewHolder>() {

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
        holder.tvRules.visibility = if (holder.isRulesVisible) View.VISIBLE else View.GONE

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvRightAnswer: TextView = itemView.findViewById(R.id.tv_right_answer)
        private val tvAnswerUser: TextView = itemView.findViewById(R.id.tv_user_answer)
        private val ivFavouritesWords: ImageView = itemView.findViewById(R.id.iv_favourites_words)
        val ivUserAnswer: ImageView = itemView.findViewById(R.id.iv_user_answer)
        val tvRules: TextView = itemView.findViewById(R.id.tv_rules)
        private val ivRules: ImageView = itemView.findViewById(R.id.iv_rules)
        var isRulesVisible = false

        init {
            ivRules.setOnClickListener {
                isRulesVisible = !isRulesVisible
                tvRules.visibility = if (isRulesVisible) View.VISIBLE else View.GONE
            }
        }

        fun bind(wordPair: Pair<String, String>) {
            val formattedWordPair = formatWordPair(wordPair)
            val word = formattedWordPair.first.toString()

            tvRightAnswer.text = formattedWordPair.first
            tvAnswerUser.text = formattedWordPair.second

                var isWordAdded = viewModel.isWordAdded(word)

                updateIcon(isWordAdded)

                ivFavouritesWords.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (isWordAdded) {
                            viewModel.deleteItem(word)
                        } else {
                            viewModel.insertWord(word)
                        }
                        isWordAdded = !isWordAdded
                        updateIcon(isWordAdded)
                    }
                }

            val rule = applyRule(markString(formattedWordPair.first.toString()).lowercase())
            tvRules.text = rule
            Log.d(
                "suffixRule",
                "suffixRule: ${applyRule(markString(formattedWordPair.first.toString()).lowercase())}"
            )
        }

        private fun updateIcon(isWordAdded: Boolean) {
            if (isWordAdded) {
                ivFavouritesWords.setImageResource(R.drawable.group_17__2_)
            } else {
                ivFavouritesWords.setImageResource(R.drawable.group_19)
            }
        }

        private fun applyRule(word: String): String {
            val transformedWord = transformWord(word).lowercase()
            val suffixStart = transformedWord.indexOf('!') + 1
            val suffixEnd = transformedWord.lastIndexOf('!')
            if (suffixStart in 1 until suffixEnd) {
                val suffix = transformedWord.substring(suffixStart, suffixEnd)
                val rules = Rules.rules
                val rule = rules[suffix]
                return rule ?: ""
            }
            return ""
        }

        private fun formatWordPair(wordPair: Pair<String, String>): Pair<CharSequence, CharSequence> {
            val rightAnswer = wordPair.first
            val userAnswer = wordPair.second

            val correctIndex = userAnswer == rightAnswer
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

    override fun getItemCount(): Int {
        return wordPairs.size
    }
}
