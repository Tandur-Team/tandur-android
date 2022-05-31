package com.tandurteam.tandur.core.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tandurteam.tandur.dashboard.myplantlist.ListFragment

class SectionPagerAdapter(
    fragment: Fragment,
    val query: String = ""
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = ListFragment()

        fragment.arguments = Bundle().apply {
            putInt(ListFragment.ARG_SECTION_NUMBER, position)
            putString(ListFragment.ARG_QUERY, query)
        }

        return fragment
    }
}