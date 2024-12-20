package com.apps.m.tielbeke4.data

import com.apps.m.tielbeke4.filialen.Filiaal
import com.apps.m.tielbeke4.getIndex
import com.apps.m.tielbeke4.viewmodel.FilialenDao
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object FilialenIO {
    var mededelingAddedCallback: (() -> Unit)? = null
    var mededelingDeletedCallback: (() -> Unit)? = null
    var dataLoaded = false

    val dao = FilialenDao()

    private var myRef: DatabaseReference

    private val listener =
        object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(
                p0: DataSnapshot,
                p1: String?,
            ) {}

            override fun onChildChanged(
                p0: DataSnapshot,
                p1: String?,
            ) {
                if (p0.exists()) {
                    CoroutineScope(Dispatchers.Default).launch {
                        dao.retrieveFromDb { daoList ->
                            val filiaal = getFiliaalfromDataSnapShot(p0)
                            synchronized(daoList) {
                                val index = getIndex(filiaal, daoList)
                                if (index >= 0) {
                                    daoList[index] = filiaal
                                }
                            }
                        }
                    }
                }
            }

            override fun onChildAdded(
                p0: DataSnapshot,
                p1: String?,
            ) {
                if (p0.exists() && dataLoaded) {
                    CoroutineScope(Dispatchers.Default).launch {
                        dao.retrieveFromDb { daoList ->
                            val filiaal = getFiliaalfromDataSnapShot(p0)
                            synchronized(daoList) {
                                if (daoList.none { element ->
                                        filiaal.filiaalnummer == element.filiaalnummer
                                    }
                                ) {
                                    daoList.apply {
                                        add(filiaal)
                                        sortBy { it.filiaalnummer }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                CoroutineScope(Dispatchers.Default).launch {
                    dao.retrieveFromDb { daoList ->
                        val filiaal = getFiliaalfromDataSnapShot(p0)
                        synchronized(daoList) {
                            daoList.remove(filiaal)
                        }
                    }
                }
            }
        }

    private val eventListener =
        object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (!dataLoaded && p0.exists()) {
                    CoroutineScope(Dispatchers.Default).launch {
                        val deferredResults = mutableListOf<Deferred<Filiaal>>()
                        p0.children.forEach {
                            deferredResults +=
                                async {
                                    getFiliaalfromDataSnapShot(
                                        it,
                                    )
                                }
                        }
                        dao.retrieveFromDb { daoList ->
                            daoList.addAll(
                                deferredResults
                                    .map {
                                        it.await()
                                    }.distinctBy {
                                        it.filiaalnummer
                                    }.sortedBy {
                                        it.filiaalnummer
                                    },
                            )
                        }
                        dataLoaded = true
                    }
                }
            }
        }

    init {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        myRef = FirebaseDatabase.getInstance().reference
    }

    fun attachListener() {
        myRef.apply {
            addChildEventListener(listener)
            addListenerForSingleValueEvent(eventListener)
        }
    }

    fun detachListener() {
        myRef.apply {
            removeEventListener(listener)
        }
    }

    fun addMededeling(filiaal: Filiaal) {
        val query = myRef.orderByChild("filiaalnummer").equalTo(filiaal.filiaalnummer.toDouble())
        query.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists() && p0.childrenCount == 1L) {
                        p0.children.forEach {
                            it.child("mededeling").ref.setValue(filiaal.mededeling).addOnSuccessListener {
                                mededelingAddedCallback?.invoke()
                            }
                        }
                    }
                }
            },
        )
    }

    fun deleteMededeling(filiaal: Filiaal) {
        val query = myRef.orderByChild("filiaalnummer").equalTo(filiaal.filiaalnummer.toDouble())
        query.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists() && p0.childrenCount == 1L) {
                        p0.children.forEach {
                            it.child("mededeling").ref.setValue("").addOnSuccessListener {
                                mededelingDeletedCallback?.invoke()
                            }
                        }
                    }
                }
            },
        )
    }

    suspend fun queryDb(filiaalnummer: Short): Filiaal =
        withContext(Dispatchers.Default) {
            val channel = Channel<Filiaal>(Channel.CONFLATED)
            myRef.orderByChild("filiaalnummer").equalTo(filiaalnummer.toDouble()).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        CoroutineScope(Dispatchers.Default).launch {
                            if (p0.exists() && p0.childrenCount == 1L) {
                                p0.children.forEach {
                                    channel.send(getFiliaalfromDataSnapShot(it))
                                }
                                channel.close()
                            } else {
                                channel.send(Filiaal())
                                channel.close()
                                coroutineContext.cancel()
                            }
                        }
                    }
                },
            )
            channel.receive()
        }

    private suspend fun getFiliaalfromDataSnapShot(dataSnapshot: DataSnapshot): Filiaal =
        withContext(Dispatchers.Default) {
            Filiaal(
                (dataSnapshot.child("filiaalnummer").value as? Long ?: 0).toShort(),
                dataSnapshot.child("address").value as? String ?: "",
                dataSnapshot.child("postcode").value as? String ?: "",
                dataSnapshot.child("telnum").value as? String ?: "",
                dataSnapshot.child("info").value as? String ?: "",
                dataSnapshot.child("mededeling").value as? String ?: "",
            )
        }
}
