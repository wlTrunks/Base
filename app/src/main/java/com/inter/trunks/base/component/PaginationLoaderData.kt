package ru.mos.polls.kbase.component

import android.content.Context
import android.support.annotation.CallSuper
import io.reactivex.subjects.Subject


interface PaginationLoaderData : LoaderData {

    @CallSuper
    fun refreshData() {
        getPaginationLoaderComponent().refreshData()
        loadData()
    }

    fun getPaginationLoaderComponent() = loadingListener as PaginationLoaderComponent

    fun isPaginationEnable() = getPaginationLoaderComponent().isPaginationEnable()

    fun getPageNumber() = getPaginationLoaderComponent().getPageNum()

    val subjects: Subject<List<Any>>

}
