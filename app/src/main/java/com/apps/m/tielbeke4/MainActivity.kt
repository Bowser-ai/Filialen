package com.apps.m.tielbeke4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apps.m.tielbeke4.adapters.TabPagerAdapter
import com.apps.m.tielbeke4.databinding.ToolbarBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    // Lazy inflation of the toolbar.
    private val toolBarBinding =
        lazy {
            DataBindingUtil.setContentView<ToolbarBinding>(this, R.layout.toolbar)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onToolBar()
        setUpViewPager()
    }

    /**
     * Initializes the main toolbar for the application.
     */
    private fun onToolBar() {
        val toolBar = toolBarBinding.value.toolBar
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolBar.setNavigationOnClickListener {
            executeBackPressed()
        }
    }

    /**
     * Callback for the click back arrow event on the toolbar.
     */
    private fun executeBackPressed(): Unit =
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }

    /**
     * Setup for the main view pager of the application.
     */
    private fun setUpViewPager() {
        val tabLayout = toolBarBinding.value.tabLayout
        val pager = toolBarBinding.value.pager

        val adapter = TabPagerAdapter(supportFragmentManager, lifecycle, 3)
        pager.adapter = adapter

        // Tab titles to the main view pager and initialize.
        (
            TabLayoutMediator(tabLayout, pager) { tab, pos ->
                tab.text =
                    when (pos) {
                        0 -> getString(R.string.main_view)
                        1 -> getString(R.string.branches)
                        2 -> getString(R.string.remarks)
                        else -> throw RuntimeException("Position $pos is out of range for the view pager.")
                    }
            }
        ).attach()
    }
}
