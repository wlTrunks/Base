package ru.mos.polls.kbase.component

import android.content.Context
import android.support.annotation.CallSuper
import io.reactivex.disposables.Disposable

interface LoaderData {

    val loadingListener: LoadingStateListener

    @CallSuper
    fun loadData() {
        loadingListener.startLoading()
    }

    fun launch(job: () -> Disposable)

}
