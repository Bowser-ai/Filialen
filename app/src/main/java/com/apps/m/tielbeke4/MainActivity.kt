package com.apps.m.tielbeke4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apps.m.tielbeke4.adapters.TabPagerAdapter
import com.apps.m.tielbeke4.databinding.ToolbarBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ToolbarBinding
    fun onToolBar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolBar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

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
        binding = ToolbarBinding.inflate(layoutInflater)
        configureTabLayout()
        onToolBar()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainUiFragment()).commit()
    }

    private fun configureTabLayout() {
        val tabLayout = binding.tabLayout
        val pager = binding.pager
        val tabtitles = listOf("HoofdScherm", "FilialenLijst", "Mededelingen")
        for (index in 0..2)
            tabLayout.addTab(tabLayout.newTab().setText("Hmm"))

        pager.addOnPageChangeListener(
                TabLayout.TabLayoutOnPageChangeListener(tabLayout)
        )
        val adapter = TabPagerAdapter(supportFragmentManager,  tabLayout.tabCount)
        pager.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
