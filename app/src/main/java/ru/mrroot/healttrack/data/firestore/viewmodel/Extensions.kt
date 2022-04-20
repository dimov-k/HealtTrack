package ru.mrroot.healttrack.data.firestore.viewmodel

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addTo(compositeDisposable: CompositeDisposable){
    compositeDisposable.add(this)
}