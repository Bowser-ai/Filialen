package com.apps.m.tielbeke4.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.apps.m.tielbeke4.MainUiFragment
import com.apps.m.tielbeke4.filialen.FilialenLijstFragment
import com.apps.m.tielbeke4.mededelingen.MededelingenList

class TabPagerAdapter(
    fm: FragmentManager,
    liveCycle: Lifecycle,
    private val tabCount: Int,
) : FragmentStateAdapter(fm, liveCycle) {
    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> MainUiFragment()
            1 -> FilialenLijstFragment()
            2 -> MededelingenList()
            else -> throw RuntimeException("X")
        }

    override fun getItemCount(): Int = tabCount
}
