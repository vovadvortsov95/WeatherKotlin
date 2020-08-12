package com.example.vladimirdvortsov.weatherkotlin.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo

abstract class BaseViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    protected fun disposable(action: () -> Disposable) {
        action.invoke().addTo(disposables)
    }

}