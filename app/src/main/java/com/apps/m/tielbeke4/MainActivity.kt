package com.apps.m.tielbeke4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.apps.m.tielbeke4.adapters.TabPagerAdapter
import com.apps.m.tielbeke4.databinding.ToolbarBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
        supportFragmentManager.popBackStack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolBar = findViewById<Toolbar>(R.id.tool_bar)
        onToolBar(toolBar)
        configureTabLayout()
    }

    private fun onToolBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun configureTabLayout() {
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val pager = findViewById<ViewPager2>(R.id.pager)

        val adapter = TabPagerAdapter(supportFragmentManager,  lifecycle, 3)
        pager.adapter = adapter

        (TabLayoutMediator(tabLayout, pager){ tab, pos ->
            tab.text = when (pos) {
                0 -> "Hoofdscherm"
                1 -> "Filialen"
                2 -> "Mededelingen"
                else -> throw RuntimeException("Out of range for viewpager tablayout.")
            }
        }).attach()
    }
}
