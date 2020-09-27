package com.apps.m.tielbeke4.filialen

import java.io.Serializable

data class Filiaal(
    val filiaalnummer: Short = 0,
    val Address: String = "",
    val Postcode: String = "",
    val telnum: String = "",
    val Info: String = "",
    var mededeling: String = ""


) : Serializable {

    override fun toString(): String {
        return "$filiaalnummer\nADRES:$Address\nPOSTCODE:$Postcode\nTEL:$telnum\nINFO:$Info\nMEDEDELING:$mededeling"
    }
}

