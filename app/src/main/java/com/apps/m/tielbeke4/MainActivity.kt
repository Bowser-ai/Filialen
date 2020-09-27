package com.apps.m.tielbeke4

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.apps.m.tielbeke4.adapters.TabPagerAdapter
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {
    fun onToolBar() {
        setSupportActionBar(tool_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tool_bar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
        supportFragmentManager.popBackStack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onToolBar()
        configureTabLayout()
    }

    private fun configureTabLayout() {
        val tabtitles = listOf("HoofdScherm", "FilialenLijst", "Mededelingen")
        for (index in 0..2)
            tabTalyout.addTab(tabTalyout.newTab().setText(tabtitles[index]))

        pager.addOnPageChangeListener(
                TabLayout.TabLayoutOnPageChangeListener(tabTalyout)
        )
        val adapter = TabPagerAdapter(supportFragmentManager, tabTalyout.tabCount)
        pager.adapter = adapter

        tabTalyout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                pager.currentItem = p0?.position ?: 0
            }
        })
    }
}
