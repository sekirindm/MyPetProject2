package com.example.mypetproject2.features.ui.home.minigame

import androidx.recyclerview.widget.DiffUtil

object HomeDIffUtils: DiffUtil.ItemCallback<HomeItemsList>(){

    override fun areItemsTheSame(oldItem: HomeItemsList, newItem: HomeItemsList): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: HomeItemsList, newItem: HomeItemsList): Boolean {
        return oldItem == newItem
    }
}