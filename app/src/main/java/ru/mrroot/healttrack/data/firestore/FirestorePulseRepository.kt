package ru.mrroot.healttrack.data.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class FirestorePulseRepository : IPulseRepository {

    companion object {
        private const val NAME_TABLE = "pulse"
    }

    private val remoteDB = FirebaseFirestore.getInstance().apply {
        firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
    }

    private val changeObservable =
        BehaviorSubject.create { emitter: ObservableEmitter<List<DocumentSnapshot>> ->
            val listeningRegistration = remoteDB.collection(NAME_TABLE)
                .addSnapshotListener { value, error ->
                    if (value == null || error != null) {
                        return@addSnapshotListener
                    }

                    if (!emitter.isDisposed) {
                        emitter.onNext(value.documents)
                    }

                    value.documentChanges.forEach {
                        Log.d(
                            "FirestorePulseRepository",
                            "Data changed type ${it.type} document ${it.document.id}"
                        )
                    }
                }

            emitter.setCancellable { listeningRegistration.remove() }
        }

    override fun getAllPulse(): Single<List<Pulse>> {
        return Single.create<List<DocumentSnapshot>> { emitter ->
            remoteDB.collection(NAME_TABLE)
                .get()
                .addOnSuccessListener {
                    if (!emitter.isDisposed) {
                        emitter.onSuccess(it.documents)
                    }
                }
                .addOnFailureListener {
                    if (!emitter.isDisposed) {
                        emitter.onError(it)
                    }
                }
        }
            .observeOn(Schedulers.io())
            .flatMapObservable { Observable.fromIterable(it) }
            .map(::mapDocumentToRemotePulse)
            .toList()
    }

    private fun mapDocumentToRemotePulse(document: DocumentSnapshot) =
        document.toObject(Pulse::class.java)!!.apply { id = document.id }

    override fun addPulse(pulse: Pulse): Completable {
        return Completable.create { emitter ->
            remoteDB.collection(NAME_TABLE)
                .add(pulse)
                .addOnSuccessListener {
                    if (!emitter.isDisposed) {
                        emitter.onComplete()
                    }
                }
                .addOnFailureListener {
                    if (!emitter.isDisposed) {
                        emitter.onError(it)
                    }
                }
        }
    }


    override fun deletePulse(id: String): Completable {
        return Completable.create { emitter ->
            remoteDB.collection(NAME_TABLE)
                .document(id)
                .delete()
                .addOnSuccessListener {
                    if (!emitter.isDisposed) {
                        emitter.onComplete()
                    }
                }
                .addOnFailureListener {
                    if (!emitter.isDisposed) {
                        emitter.onError(it)
                    }
                }
        }
    }

    override fun getChangeObservable(): Observable<List<Pulse>> =
        changeObservable.hide()
            .observeOn(Schedulers.io())
            .map { list -> list.map(::mapDocumentToRemotePulse) }
}