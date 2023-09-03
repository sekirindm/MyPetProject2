package com.example.mypetproject2.features.ui.games.stress.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mypetproject2.features.ui.notifications.CommonMistakesWords
import com.example.mypetproject2.features.ui.notifications.FavoritesWords

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoritesWords()
            1 -> CommonMistakesWords()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
