package com.apps.m.tielbeke4.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.apps.m.tielbeke4.MainUiFragment
import com.apps.m.tielbeke4.filialen.FilialenLijstFragment
import com.apps.m.tielbeke4.mededelingen.MededelingenList

class TabPagerAdapter(fm: FragmentManager, private val tabCount: Int):
FragmentPagerAdapter(fm){
    override fun getItem(p0: Int): Fragment? {
        return when (p0) {
            0 -> MainUiFragment()
            1 -> FilialenLijstFragment()
            2-> MededelingenList()
            else-> null
        }
    }

    override fun getCount() = tabCount


}