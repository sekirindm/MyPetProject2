package com.example.mypetproject2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnswerHistoryAdapter(private val historyItems: List<AnswerHistoryItem>) : RecyclerView.Adapter<AnswerHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val answerIndicator: ImageView = itemView.findViewById(R.id.iv_incorrect)
//        val answerWord: TextView = itemView.findViewById(R.id.tvAnswerWord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.answer_history_item,
            parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = historyItems[position]

        if (item.isCorrect) {
            holder.answerIndicator.setImageResource(R.drawable.ellipse_5)
        } else {
            holder.answerIndicator.setImageResource(R.drawable.frame_24)
        }

//        holder.answerWord.text = item.word
    }

    override fun getItemCount(): Int {
        return historyItems.size
    }
}