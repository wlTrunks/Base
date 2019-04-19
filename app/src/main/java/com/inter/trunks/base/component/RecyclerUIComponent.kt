package ru.mos.polls.kbase.component

import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.findOptional
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import ru.mos.polls.R
import ru.mos.polls.base.component.UIComponent
import ru.mos.polls.kbase.utils.show

/**
 * Компонент для работы со списком RecyclerView.
 *
 * RecyclerView - R.id.list.
 * EmptyView - android.R.id.empty. Будет показываться, если список пуст.
 *
 */
class RecyclerUIComponent<Adapter : RecyclerView.Adapter<*>>(

        var adapter: Adapter? = null

) : UIComponent(), KoinComponent {

    var recycler: RecyclerView? = null
        private set

    /**
     * Отображается View-в случае пустого результата.
     */
    private var emptyView: View? = null

    var recyclerItemDecoration: MutableSet<RecyclerView.ItemDecoration> = mutableSetOf()

    var layoutManager: RecyclerView.LayoutManager? = null

    override fun onViewCreated(layout: View?) {
        super.onViewCreated(layout)
        layout?.let { view ->
            recycler = view.findOptional(R.id.list)
            emptyView = view.findOptional(android.R.id.empty)
        }
        emptyView?.show(false)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recycler?.let { list ->
            with(list) {
                if (this@RecyclerUIComponent.layoutManager == null)
                    this@RecyclerUIComponent.layoutManager =
                            get<LinearLayoutManager> { parametersOf(context) }

                layoutManager = this@RecyclerUIComponent.layoutManager

                adapter = this@RecyclerUIComponent.adapter

                recyclerItemDecoration.forEach { addItemDecoration(it) }
            }
        }
    }

    fun refreshUI() {
        adapter?.let { adapter ->
            if (adapter.itemCount > 0)
                hideEmptyView()
            else
                showEmptyView()
        } ?: showEmptyView()
    }

    private fun showEmptyView() {
        recycler?.show(false)
        emptyView?.show(true)
    }

    private fun hideEmptyView() {
        recycler?.show(true)
        emptyView?.show(false)
    }

    fun hideViews() {
        recycler?.show(false)
        emptyView?.show(false)
    }

    override fun onDestroy() {
        clear()
        recyclerItemDecoration.clear()
        adapter = null
        layoutManager = null
        super.onDestroy()
    }

    override fun onDestroyView() {
        clear()
        super.onDestroyView()
    }

    private fun clear() {
        recycler?.let {
            it.adapter = null
            it.layoutManager = null
        }
        recycler = null
    }

    fun setEmptyViewText(@StringRes stringId: Int) {
        if (emptyView is TextView) {
            (emptyView as TextView).text = context.getString(stringId)
        }
    }

    fun scrollToPosition(position: Int) = recycler?.scrollToPosition(position)

}
