package ru.mos.polls.kbase.component

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.annotation.ColorRes
import android.support.annotation.NonNull
import android.support.constraint.ConstraintLayout
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.AsyncSubject
import ru.mos.polls.base.component.PagerUIComponent
import ru.mos.polls.base.component.ProgressableUIComponent
import ru.mos.polls.base.component.PullableUIComponent
import ru.mos.polls.base.component.UIComponentHolder
import ru.mos.polls.profile.ui.adapter.PagerAdapter
import ru.mos.polls.rxhttp.rxapi.model.Page

/**
 * Фукнции-расширения для работы с ComponentHolder-ом.
 */
fun UIComponentHolder.hideProgress() = getComponent(ProgressableUIComponent::class.java)?.end()

fun UIComponentHolder.showProgress() = getComponent(ProgressableUIComponent::class.java)?.begin()

fun UIComponentHolder.hidePullRefresh() = getComponent(PullableUIComponent::class.java)?.end()

fun UIComponentHolder.endProgress() = progressableComponent.forEach { it.end() }

fun UIComponentHolder.refreshUI() = getComponent(RecyclerUIComponent::class.java)?.refreshUI()

fun UIComponentHolder.setPagerList(list: List<PagerAdapter.Page>) =
        getComponent(PagerUIComponent::class.java)?.setPages(list)

fun UIComponentHolder.hidePaginationLoader() =
        getComponent(PaginationUIComponent::class.java)?.end()

fun UIComponentHolder.setProgressViewColor(@ColorRes color: Int) =
        getComponent(ProgressableUIComponent::class.java)?.setProgressViewColor(color)

fun UIComponentHolder.scrollToPosition(position: Int) =
        getComponent(RecyclerUIComponent::class.java)?.scrollToPosition(position)

fun UIComponentHolder.setNeedHideRootView(needHideRootView: Boolean) =
        getComponent(ProgressableUIComponent::class.java)?.setNeedHideRootView(needHideRootView)

/**
 * Меняет позицию ProgressBar вниз экрана
 */
fun UIComponentHolder.setProgressPaginationPosition(toDown: Boolean) {
    val recycler = getComponent(RecyclerUIComponent::class.java)?.recycler
    val loader = getComponent(ProgressableUIComponent::class.java)?.progressView
    if (recycler != null && loader != null) {
        if (loader.layoutParams is ConstraintLayout.LayoutParams) {
            val layoutParams = loader.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topToBottom = recycler.id
            layoutParams.topToTop = ConstraintLayout.LayoutParams.UNSET
            loader.requestLayout()
        }
    }
}

open class LoaderComponent : LoadingStateListener {
    protected var dataLoaded: DataLoaded = DataLoaded.SUCCESS
    override val loadingStateLD = MutableLiveData<LoadingState>()
    val loadingStatePS: AsyncSubject<LoadingState> = AsyncSubject.create()
    protected var _loadingState = LoadingState.IDLE
        set(value) {
            field = value
            loadingStatePS.onNext(value)
            loadingStateLD.value = value
        }

    override fun getLoadingState(): LoadingState = _loadingState

    fun subscribeToLoadingState(@NonNull owner: LifecycleOwner, componentHolder: UIComponentHolder) {
        loadingStateLD.observe(owner, Observer {
            processStates(componentHolder, it!!)
        })
    }

    fun subscribeToLoadingState(componentHolder: UIComponentHolder): Disposable =
            loadingStatePS.subscribe {
                processStates(componentHolder, it)
            }

    override fun loadingDone(isSuccess: Boolean) {
        dataLoaded = if (isSuccess) DataLoaded.SUCCESS else DataLoaded.ERROR
        _loadingState = LoadingState.DONE
    }

    override fun startLoading() {
        _loadingState = LoadingState.ON_START
    }

    override fun processStates(uiComponentHolder: UIComponentHolder, loadingState: LoadingState) {
        when (loadingState) {
            LoadingState.ON_START -> {
                uiComponentHolder.showProgress()
            }
            LoadingState.DONE -> {
                uiComponentHolder.endProgress()
                uiComponentHolder.refreshUI()
                _loadingState = LoadingState.IDLE
            }
            else -> Unit
        }
    }
}

class PaginationLoaderComponent : LoaderComponent() {

    private val page = Page()

    private var pagingState = PagingState.REFRESH

    override fun processStates(uiComponentHolder: UIComponentHolder, loadingState: LoadingState) {
        when (loadingState) {
            LoadingState.ON_START -> {
                if (pagingState != PagingState.REFRESH)
                    uiComponentHolder.showProgress()
            }
            LoadingState.DONE -> {
                uiComponentHolder.endProgress()
                if (pagingState == PagingState.REFRESH) {
                    if (dataLoaded == DataLoaded.SUCCESS)
                        uiComponentHolder.setProgressPaginationPosition(true)
                    pagingState = PagingState.PAGING
                }
                uiComponentHolder.refreshUI()
                _loadingState = LoadingState.IDLE
            }
            else -> Unit
        }
    }

    fun proceedPagingState(isSuccess: Boolean, onReload: (() -> Unit)? = null, onPaging: (() -> Unit)? = null) {
        if (isSuccess) {
            when (pagingState) {
                PagingState.REFRESH -> onReload?.invoke()
                PagingState.PAGING -> onPaging?.invoke()
            }
        }
        loadingDone(isSuccess)
    }

    override fun loadingDone(isSuccess: Boolean) {
        if (isSuccess)
            page.increment()
        super.loadingDone(isSuccess)
    }

    fun getPageNum(): Int = page.num

    fun refreshData() {
        page.reset()
        pagingState = PagingState.REFRESH
    }

    fun isPaginationEnable() = _loadingState == LoadingState.IDLE
}

interface LoadingStateListener {

    fun processStates(uiComponentHolder: UIComponentHolder, loadingState: LoadingState)

    fun getLoadingState(): LoadingState

    fun loadingDone(isSuccess: Boolean)

    fun startLoading()

    val loadingStateLD: MutableLiveData<LoadingState>

}

enum class PagingState {
    REFRESH,
    PAGING;
}

enum class LoadingState {
    IDLE,
    ON_START,
    DONE;
}

enum class DataLoaded {
    SUCCESS,
    ERROR;
}