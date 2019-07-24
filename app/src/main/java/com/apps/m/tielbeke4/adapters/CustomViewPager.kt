package com.apps.m.tielbeke4.adapters

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class CustomViewPager(context: Context, attr: AttributeSet) :
    ViewPager(context,attr) {

    private var swipeEnabled = false

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (swipeEnabled) {
            super.onTouchEvent(ev)
        } else {
            false
        }

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (swipeEnabled) {
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }
    }

    fun setSwipeEnabled (isEnabled: Boolean) {
        swipeEnabled = isEnabled
    }

}