package com.example.mypetproject2.features.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.R
import com.example.mypetproject2.features.ui.home.minigame.HomeDIffUtils
import com.example.mypetproject2.features.ui.home.minigame.HomeItemsList
import com.example.mypetproject2.features.ui.home.minigame.MiniGame


class HomeAdapter(
    //Лямбда вызывается при нажатии на кнопку
    val handleWords: (String, String) -> Unit
) : ListAdapter<HomeItemsList, RecyclerView.ViewHolder>(HomeDIffUtils) {

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position) is HomeItemsList.HomeMiniGame -> 0
            else -> {
                1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> HomeViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_game_card_view, parent, false)
            )

            else -> {
                HomeViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.fragment_game_card_view, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HomeViewHolder -> {
                //Получаем экзэмпляр miniGame, и передаем его при создании bind
                val item = getItem(position) as HomeItemsList.HomeMiniGame
                val miniGame = item.miniGame
                holder.bind(miniGame)
            }
        }
    }

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWord: TextView = itemView.findViewById(R.id.tv_word_item)
        private val b1: Button = itemView.findViewById(R.id.b_1_item)
        private val b2: Button = itemView.findViewById(R.id.b_2_item)

        //При нажатии на одну из этих кнопок срабатывает лямбда, куда мы отправляем tvWord и b1, b2
        init {
            b1.setOnClickListener { handleWords(tvWord.text.toString(), b1.text.toString()) }
            b2.setOnClickListener { handleWords(tvWord.text.toString(),  b2.text.toString()) }
        }

        // Принимает Data class MiniGame, в tvWord вставляем word из miniGame
        // в b1 и b2 вставляем рандомные варианты ответа

        fun bind(miniGame: MiniGame) {
            tvWord.text = miniGame.word
            b1.text = miniGame.letters[0]
            b2.text = miniGame.letters[1]
            
        }

    }
}