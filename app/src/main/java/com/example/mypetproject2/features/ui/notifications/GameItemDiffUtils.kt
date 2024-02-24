package com.example.mypetproject2.features.ui.notifications

import androidx.recyclerview.widget.DiffUtil
import com.example.mypetproject2.data.database.gamedb.GameItemDb

class GameItemDiffUtils(
    private val oldList: List<GameItemDb>,
    private val newList: List<GameItemDb>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}