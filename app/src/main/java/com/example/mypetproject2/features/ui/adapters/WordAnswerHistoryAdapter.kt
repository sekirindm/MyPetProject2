package com.example.mypetproject2.features.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.R
import com.example.mypetproject2.data.baselist.Rules
import com.example.mypetproject2.data.baselist.separateList
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.features.ui.ui.CustomDialogFragment
import com.example.mypetproject2.utils.createCustomResultSpannableStringBuilder
import com.example.mypetproject2.utils.transformWord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
        holder.tvRules.visibility = if (holder.isRulesVisible) View.VISIBLE else GONE

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val customDialogFragment = CustomDialogFragment()
        private val tvRightAnswer: TextView = itemView.findViewById(R.id.tv_right_answer)
        private val tvAnswerUser: TextView = itemView.findViewById(R.id.tv_user_answer)
        private val ivFavouritesWords: ImageView = itemView.findViewById(R.id.iv_favourites_words)
        val ivUserAnswer: ImageView = itemView.findViewById(R.id.iv_user_answer)
        val tvRules: TextView = itemView.findViewById(R.id.tv_rules)
        private val ivRules: ImageView = itemView.findViewById(R.id.iv_rules)
        val ivFull: ImageView = itemView.findViewById(R.id.iv_full)
        var isRulesVisible = false

        init {
            ivRules.setOnClickListener {
                isRulesVisible = !isRulesVisible
                tvRules.visibility = if (isRulesVisible) VISIBLE else GONE
            }

        }

        //TODO Подсветка символов не работает из-за того что first an second типа toString()
        fun bind(wordPair: Pair<String, String>) {
            val formattedWordPair = formatWordPair(wordPair)
            val word = formattedWordPair.first.toString().take(23)
            val context = Job() + Dispatchers.Default
            val scope = CoroutineScope(context)

            if (formattedWordPair.first.split(' ').size > 1) {
                tvRightAnswer.text = word
                tvAnswerUser.text = formattedWordPair.second.toString().take(23)
            } else {
                tvRightAnswer.text = formattedWordPair.first
                tvAnswerUser.text = formattedWordPair.second
            }


            if (formattedWordPair.first.length < 25) {
                ivFull.visibility = GONE
            } else {
                ivFull.setOnClickListener {
                    customDialogFragment.customDialogWindow(
                        formattedWordPair.first.toString().replace("|", ""),
                        formattedWordPair.second.toString(),
                        itemView.context
                    )
                }
            }

            var isWordAdded = viewModel.isWordAdded(word)

            updateIcon(isWordAdded)

            ivFavouritesWords.setOnClickListener {
                scope.launch {
                    if (isWordAdded) viewModel.deleteItem(word) else viewModel.insertWord(word)
                    isWordAdded = !isWordAdded
                    updateIcon(isWordAdded)
                }
            }
            val dopWord = separateList.map {
                val wordFirst = it.first
                when (it.second) {
                    0 -> wordFirst.replace("(", "").replace(")", " ")
                    1 -> wordFirst.replace("(", "").replace(")", "")
                    2 -> wordFirst.replace("(", "").replace(")", "-")
                    else -> wordFirst
                }
            }

            val correctDisplay = dopWord.find { it.replace("!", "") == word }?.let {
                separateList[dopWord.indexOf(it)].second
            }
            Log.d("separateList", "$dopWord")
            Log.d("faweaf", "${word.trim()}")

            tvRightAnswer.text = word + when (correctDisplay) {
                0 -> " (раздельно)"
                1 -> " (слитно)"
                else -> ""
            }


            val rule = applyRule(markString(formattedWordPair.first.toString()).lowercase())
            tvRules.text = rule
            Log.d(
                "suffixRule",
                "suffixRule: ${applyRule(markString(formattedWordPair.first.toString()).lowercase())}"
            )
        }

        private fun markString(string: String) = "!$string!"

        private fun updateIcon(isWordAdded: Boolean) {
            if (isWordAdded) {
                ivFavouritesWords.setImageResource(R.drawable.frame_98)
            } else {
                ivFavouritesWords.setImageResource(R.drawable.frame_110)
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
