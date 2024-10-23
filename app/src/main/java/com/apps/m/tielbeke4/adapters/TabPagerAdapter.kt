package com.apps.m.tielbeke4.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.apps.m.tielbeke4.MainUiFragment
import com.apps.m.tielbeke4.filialen.FilialenLijstFragment
import com.apps.m.tielbeke4.mededelingen.MededelingenList
import java.lang.RuntimeException

class TabPagerAdapter(fm: FragmentManager, private val tabCount: Int):
FragmentPagerAdapter(fm){
    override fun getItem(p0: Int): Fragment {
        return when (p0) {
            0 -> MainUiFragment()
            1 -> FilialenLijstFragment()
            2-> MededelingenList()
            else-> throw RuntimeException("X")
        }
    }

    override fun getCount() = tabCount
}