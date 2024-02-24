package com.example.mypetproject2.features.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.R

class HomeAdapter(): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvGame: CardView = itemView.findViewById( R.id.cv_game)
        val tvWord: TextView = itemView.findViewById(R.id.tv_word_item)
        val b1: Button = itemView.findViewById(R.id.b_1_item)
        val b2: Button = itemView.findViewById(R.id.b_2_item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val gameCard = LayoutInflater.from(parent.context).inflate( R.layout.fragment_game_card_view, parent, false)
        return ViewHolder(gameCard)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cvGame
        holder.b1.text = "Н"
        holder.b2.text = "НН"
        holder.tvWord.text = "серебрянный "

    }
}