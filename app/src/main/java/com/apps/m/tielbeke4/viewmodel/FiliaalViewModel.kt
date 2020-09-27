package com.apps.m.tielbeke4.viewmodel


import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.view.View
import com.apps.m.tielbeke4.QueryDatabase
import com.apps.m.tielbeke4.filialen.Filiaal



class FiliaalViewModel(
    private val filiaalRepository: FilialenRepository) : ViewModel() {

            init {
                QueryDatabase.attachListener()
            }

    override fun onCleared() {
        super.onCleared()
        QueryDatabase.detachListener()
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

    val filiaalMededelingTitle = ObservableField<String>("")
        get() {
            field.set(
                "${filiaal.get()?.filiaalnummer}\n" +
                        "${filiaal.get()?.Address}"
            )
            return field
        }
    var mededeling = ObservableField<String>("")

    /* fun addFiliaal(filiaal: Filiaal) {
         filiaalRepository.addFiliaal(filiaal)
     }

     fun deleteFiliaal(filiaal: Filiaal) {
         filiaalRepository.deleteFiliaal(filiaal)
     }*/
/*
    fun changeFiliaal(filiaal: Filiaal) {
        filiaalRepository.changeFiliaal(filiaal)
    }*/

    fun getFilialen() = filiaalRepository.getFilialen()

    suspend fun queryDb(filiaalnummer:Short) = filiaalRepository.queryDb(filiaalnummer)


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