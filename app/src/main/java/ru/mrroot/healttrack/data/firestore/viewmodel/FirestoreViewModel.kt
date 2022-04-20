package ru.mrroot.healttrack.data.firestore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.mrroot.healttrack.data.firestore.FirestorePulseRepository
import ru.mrroot.healttrack.data.firestore.IPulseRepository
import ru.mrroot.healttrack.data.firestore.Pulse

class FirestoreViewModel : ViewModel() {

    private val _pulseList = MutableLiveData<List<Pulse>>()

    private val repository: IPulseRepository = FirestorePulseRepository()

    private val disposable = CompositeDisposable()

    init {
        repository.getChangeObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {
                    _pulseList.value = it
                },
                {
                    it.printStackTrace()
                }
            )
            .addTo(disposable)
    }

    fun deletePulse(pulseId: String) {
        repository.deletePulse(pulseId)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {},
                {
                    it.printStackTrace()
                })
            .addTo(disposable)
    }

    fun addPulse(pulse: Pulse) {
        repository.addPulse(pulse)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {},
                {
                    it.printStackTrace()
                })
            .addTo(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun getPulseList() : Single<List<Pulse>> {
        return repository.getAllPulse()
    }
}