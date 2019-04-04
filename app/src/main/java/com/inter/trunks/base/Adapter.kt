package com.inter.trunks.base

import android.database.Observable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import java.lang.RuntimeException

open class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val listenersList = HashSet<ItemsClickEventsListener>()
    open var itemsClickEventsObservable = ItemsClickEventsObservable()
    private val itemList: MutableList<Any> = mutableListOf()
    protected open var createViewHolder: ((ViewGroup, Int) -> RecyclerView.ViewHolder)? = null
    protected open var getItemViewType: ((Int, Any) -> Int)? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (createViewHolder == null) throw RuntimeException("createViewHolder must be initialized")
        val vh = createViewHolder?.invoke(parent, viewType)
                ?: throw RuntimeException("ViewHolder not have been setted in createViewHolder")
        (vh as ItemViewHolderBinder<Any, RecyclerView.ViewHolder>).processOnClickListener(vh, itemsClickEventsObservable)
        (vh as ItemViewHolderBinder<Any, RecyclerView.ViewHolder>).processOnLongClickListener(vh, itemsClickEventsObservable)
        return vh
    }


    fun setVHFunction(createViewHolder: ((ViewGroup, Int) -> RecyclerView.ViewHolder), getItemViewType: ((Int, Any) -> Int)? = null) {
        this@Adapter.createViewHolder = createViewHolder
        this@Adapter.getItemViewType = getItemViewType
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as ItemViewHolderBinder<Any, RecyclerView.ViewHolder>).onBindViewHolder(viewHolder, itemList[viewHolder.adapterPosition])
    }

    /**
     * Регистрация слушателей
     */
    protected open fun registerItemsEventsListener() {
        for (rvmItemsClickEventsListener in listenersList) {
            itemsClickEventsObservable.registerObserver(rvmItemsClickEventsListener)
        }
    }

    protected open fun unRegisterAllObservers() {
        itemsClickEventsObservable.unregisterAll()
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return getItemViewType?.invoke(position, itemList[position]) ?: 0
    }

    fun setData(list: List<Any>) {
        this.itemList.clear()
        this.itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(list: List<Any>) {
        val positionStart = itemList.size + 1
        this.itemList.addAll(list)
        notifyItemRangeInserted(positionStart, itemList.size)
    }

    fun addData(position: Int, list: List<Any>) {
        this.itemList.addAll(position, list)
        notifyItemRangeInserted(position, position + list.size)
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    fun remove(item: Any): Boolean {
        val isRemoved = itemList.remove(item)
        if (isRemoved) notifyDataSetChanged()
        return isRemoved
    }

    fun removeAt(position: Int): Any {
        val item: Any = itemList.removeAt(position)
        notifyItemRemoved(position)
        return item
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getList(): List<T> {
        return itemList as List<T>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getItem(position: Int): T {
        return itemList[position] as T
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        unRegisterAllObservers()
        itemList.clear()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        registerItemsEventsListener()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as ItemViewHolderBinder<Any, RecyclerView.ViewHolder>).onViewDetachedFromWindow(holder)
    }

    /**
     * Базовый VH с использованием синтетики
     */
    abstract class BaseViewHolder<T, VH : RecyclerView.ViewHolder>(val layoutId: Int, val parent: ViewGroup) :
            RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)), Adapter.ItemViewHolderBinder<T, VH>, LayoutContainer {
        override val containerView: View = itemView
    }

    interface ItemViewHolderBinder<T, VH : RecyclerView.ViewHolder> {
        fun onBindViewHolder(holder: VH, item: T)
        fun processOnClickListener(viewHolder: VH, itemsClickEventsObservable: ItemsClickEventsObservable) {
            viewHolder.itemView.setOnClickListener { v -> itemsClickEventsObservable.notifyItemClick(viewHolder.adapterPosition) }
        }

        fun processOnLongClickListener(viewHolder: VH, itemsClickEventsObservable: ItemsClickEventsObservable) {
            viewHolder.itemView.setOnLongClickListener { v -> itemsClickEventsObservable.notifyItemLongClick(viewHolder.adapterPosition) }
        }

        fun onViewDetachedFromWindow(viewHolder: VH) {}
    }

    open class ItemsClickEventsObservable : Observable<ItemsClickEventsListener>() {
        fun notifyItemClick(position: Int) {
            synchronized(mObservers) {
                for (l in mObservers) {
                    l.onItemClick(position)
                }
            }
        }

        fun notifyItemLongClick(position: Int): Boolean {
            synchronized(mObservers) {
                var consumed = false
                for (l in mObservers) {
                    if (l.onItemLongClick(position)) {
                        consumed = true
                    }
                }
                return consumed
            }
        }
    }
}

interface ItemsClickEventsListener {
    fun onItemClick(position: Int)
    /**
     * @return возращать true для получения
     */
    fun onItemLongClick(position: Int): Boolean = false
}
