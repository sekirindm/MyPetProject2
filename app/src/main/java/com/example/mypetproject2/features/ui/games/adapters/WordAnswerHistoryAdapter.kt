package com.example.mypetproject2.features.ui.games.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.R

class WordAnswerHistoryAdapter(private val wordPairs: List<Pair<String, String>>) :
        RecyclerView.Adapter<WordAnswerHistoryAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_full_answer_history, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val wordPair = wordPairs[position]
            holder.bind(wordPair)
        }

        override fun getItemCount(): Int {
            return wordPairs.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(wordPair: Pair<String, String>) {
                itemView.findViewById<TextView>(R.id.tv_right_answer).text = wordPair.first
                itemView.findViewById<TextView>(R.id.tv_answer_user).text = wordPair.second
            }

        }
    }

