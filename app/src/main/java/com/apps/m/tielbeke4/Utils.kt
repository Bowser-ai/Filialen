package com.apps.m.tielbeke4

import com.apps.m.tielbeke4.databinding.MainUiFragmentBinding
import com.apps.m.tielbeke4.filialen.Filiaal

fun getIndex(
    filiaal: Filiaal?,
    list: MutableList<Filiaal>,
): Int =
    list.binarySearch(
        filiaal,
        compareBy {
            it?.filiaalnummer?.compareTo(filiaal?.filiaalnummer ?: 0)
        },
    )

fun MainUiFragmentBinding.filiaal() = this.filiaalmodel?.filiaal
