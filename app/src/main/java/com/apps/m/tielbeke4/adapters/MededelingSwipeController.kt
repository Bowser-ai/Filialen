package com.apps.m.tielbeke4.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.apps.m.tielbeke4.filialen.Filiaal
import com.apps.m.tielbeke4.viewmodel.FilialenDao
import com.apps.m.tielbeke4.viewmodel.FilialenRepository

class MededelingSwipeController(
    private val list: List<Filiaal>,
) : ItemTouchHelper.Callback() {
    interface MededelingenSwipeCalbacks {
        fun deleteMededelingConfirmation(filiaal: Filiaal)
    }

    private var swipeCallbacks: MededelingenSwipeCalbacks? = null

    fun setSwipeCallbacks(P0: MededelingenSwipeCalbacks) {
        swipeCallbacks = P0
    }

    private val repo = FilialenRepository.getInstance(FilialenDao())

    override fun getMovementFlags(
        p0: RecyclerView,
        p1: RecyclerView.ViewHolder,
    ): Int = makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)

    override fun onMove(
        p0: RecyclerView,
        p1: RecyclerView.ViewHolder,
        p2: RecyclerView.ViewHolder,
    ): Boolean = false

    override fun onSwiped(
        p0: RecyclerView.ViewHolder,
        p1: Int,
    ) {
        if (p1 == ItemTouchHelper.END) {
            val index = p0.adapterPosition
            val filiaal = list.filter { it.mededeling != "" }[index]
            swipeCallbacks?.deleteMededelingConfirmation(filiaal)
        }
    }
}
