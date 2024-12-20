package com.apps.m.tielbeke4.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.apps.m.tielbeke4.data.FilialenIO
import com.apps.m.tielbeke4.filialen.Filiaal

class FiliaalViewModel(
    private val filiaalRepository: FilialenRepository,
) : ViewModel() {
    init {
        FilialenIO.attachListener()
    }

    override fun onCleared() {
        super.onCleared()
        FilialenIO.detachListener()
    }

    interface OnButtonListener {
        fun zoekButtonListener()

        fun clickTelListener()

        fun clickKaartListener()

        fun addMededelingListener()
    }

    var mededelingAddedCallback
        get() = filiaalRepository.mededelingAddedCallback
        set(value) {
            filiaalRepository.mededelingAddedCallback = value
        }

    var mededelingDeletedCallback
        get() = filiaalRepository.mededelingAddedCallback
        set(value) {
            filiaalRepository.mededelingDeletedCallback = value
        }

    fun setOnButtonListener(P0: OnButtonListener) {
        this.onButtonClick = P0
    }

    private var onButtonClick: OnButtonListener? = null

    var filiaal = ObservableField<Filiaal>(Filiaal())

    var filiaalNummer = ObservableField<String>("")

    var filiaalTitle = ObservableField<String>("")

    var mededelingenButtonVisibility: Int? = null

    val filiaalMededelingTitle = ObservableField<String>("")
        get() {
            field.set(
                "${filiaal.get()?.filiaalnummer}\n" +
                    "${filiaal.get()?.Address}",
            )
            return field
        }
    var mededeling = ObservableField<String>("")

    fun getFilialen() = filiaalRepository.getFilialen()

    suspend fun queryDb(filiaalnummer: Short) = filiaalRepository.queryDb(filiaalnummer)

    fun zoek() {
        onButtonClick?.zoekButtonListener()
    }

    fun tel() {
        onButtonClick?.clickTelListener()
    }

    fun kaart() {
        onButtonClick?.clickKaartListener()
    }

    fun addMededeling() {
        onButtonClick?.addMededelingListener()
    }
}
