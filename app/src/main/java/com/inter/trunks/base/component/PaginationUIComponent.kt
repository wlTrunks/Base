package ru.mos.polls.kbase.component

import android.content.Context
import android.support.annotation.IntDef
import android.support.v4.view.ViewCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import org.jetbrains.anko.findOptional
import ru.mos.polls.R
import ru.mos.polls.base.component.UIComponent
import ru.mos.polls.base.ui.RecyclerScrollableController
import ru.mos.polls.kbase.utils.show
import ru.mos.polls.rxhttp.rxapi.progreessable.Progressable

/**
 * Копонент пагинации списка. Каждый экран должен реализовать интерфейс PaginationUIInterface.
 *
 * RecyclerView должен иметь идентификатор R.id.list.
 * NestedScrollView должен иметь идентификатор R.id.nestedScroll.
 */
class PaginationUIComponent(

        private val paginationUIInterface: PaginationUIInterface = PaginationUIInterface.STUB

) : UIComponent(), Progressable {

    private var nestedScrollView: NestedScrollView? = null
    private var loader: ProgressBar? = null

    // Идет загрузка данных или нет.
    var isRequestLoading: Boolean = false

    /**
     * Скроллинг нестедвью при загрузке новой страницы
     */
    private var nestedScrollViewFocus = FOCUS_NONE

    private val scrollable: RecyclerView.OnScrollListener
        get() {
            val onLastItemVisibleListener =
                    RecyclerScrollableController.OnLastItemVisibleListener { proceedPaging() }

            return RecyclerScrollableController(onLastItemVisibleListener)
        }

    private val nestedScrollable: NestedScrollView.OnScrollChangeListener =
            NestedScrollView.OnScrollChangeListener { nestedScrollView1, _, scrollY, _, oldScrollY ->
                if (nestedScrollView1.getChildAt(nestedScrollView1.childCount - 1) != null) {
                    if (((scrollY >= (nestedScrollView1.getChildAt(nestedScrollView1.childCount - 1).measuredHeight - nestedScrollView1.measuredHeight)) &&
                                    scrollY > oldScrollY)
                    ) {
                        proceedPaging()
                    }
                }
            }

    override fun onViewCreated(layout: View) {
        super.onViewCreated(layout)
        val recyclerView = layout.findOptional<RecyclerView>(R.id.list)
        nestedScrollView = layout.findOptional(R.id.nestedScroll)
        loader = layout.findOptional(R.id.recyclerLoader)
        if (nestedScrollView != null) {
            recyclerView?.let {
                ViewCompat.setNestedScrollingEnabled(it, false)
            }
            nestedScrollView?.setOnScrollChangeListener(nestedScrollable)
        } else {
            recyclerView?.addOnScrollListener(scrollable)
        }
    }

    override fun begin() {
        isRequestLoading = true
        loader?.show(true)
    }

    override fun end() {
        loader?.show(false)
        isRequestLoading = false
    }

    /**
     * В случае, если у экрана реализован интерфейс для PaginationViewLoader, то проверяем,
     * что идет запрос на получение данных. В самих запросах при получении информации
     * необходимо переключить флаг для isRequestLoading = false.
     */
    private fun proceedPaging() {
        if (paginationUIInterface.checkPagingEnable() && !isRequestLoading) {
            begin()
            paginationUIInterface.onLoadingNextPage(context)
            proceedNestedScroll()
        }
    }

    fun setNestedScrollViewFocus(
            @NestedScrollFocusDirection nestedScrollViewFocus: Int
    ): PaginationUIComponent {
        this.nestedScrollViewFocus = nestedScrollViewFocus
        return this
    }

    private fun proceedNestedScroll() = nestedScrollView?.let {
        if (nestedScrollViewFocus != FOCUS_NONE)
            nestedScrollView?.post {
                nestedScrollView?.fullScroll(nestedScrollViewFocus)
            }
    }


    @Retention(AnnotationRetention.SOURCE)
    @IntDef(View.FOCUS_DOWN, View.FOCUS_UP, FOCUS_NONE)
    annotation class NestedScrollFocusDirection

    /**
     * Интерфейс, который нужно реализовывать всем классам, которые будут использовать
     * PaginationUIComponent.
     */
    interface PaginationUIInterface {

        fun onLoadingNextPage()

        fun checkPagingEnable(): Boolean

        companion object {

            val STUB: PaginationUIInterface = object : PaginationUIInterface {

                override fun onLoadingNextPage() {}

                override fun checkPagingEnable(): Boolean = true

            }
        }
    }

    companion object {
        const val FOCUS_NONE = -1
    }
}
