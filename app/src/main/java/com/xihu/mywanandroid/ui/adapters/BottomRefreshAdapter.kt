package com.xihu.mywanandroid.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.xihu.mywanandroid.R
import java.security.InvalidParameterException

class BottomRefreshAdapter<T, DB:ViewDataBinding> private constructor(clazz: Class<T>, cb:InstanceBeansCallBack<T, DB>): RecyclerView.Adapter<ViewHolder>() {
    private val FOOTER_VIEW_TAG = -1
    var beans: SortedList<T>? = null

    protected var onBindView: ((binder:DB, bean:T) -> Unit) ?= null
    protected var onClickItemView: ((view:View, bean:T) -> Unit)?=null
    protected var onLoadData: (() -> Unit)?=null
    protected var getViewType: (bean: T) -> Int = { _ -> 0 }
    protected var layoutResource: ((viewType:Int)->Int)?=null

    init {
        beans = SortedList(clazz, cb.instance(this)!!)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = if (viewType == FOOTER_VIEW_TAG) {
            ViewHolder(DataBindingUtil.inflate<DB>(LayoutInflater.from(parent.context), R.layout.bottomrefreshlayout, parent, false))
        } else {
            ViewHolder(DataBindingUtil.inflate<DB>(LayoutInflater.from(parent.context), layoutResource?.invoke(viewType)!!, parent, false))
        }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val maxIndex = if (isEnd) beans!!.size() else beans!!.size()-1
        if (position < maxIndex) {
            onBindView?.invoke(
                holder.bindr as DB,
                beans!![position]
            ) ?: holder.bindr.setVariable(1, beans!![position])

            if (onClickItemView != null) {
                holder.itemView.setOnClickListener {
                    onClickItemView?.invoke(
                        holder.itemView,
                        beans!![position]
                    )
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return (beans!!.size() + if (isEnd) 0 else 1)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if ((holder.itemViewType == FOOTER_VIEW_TAG) and !isEnd) {
            onLoadData?.invoke()
        }
    }

    override fun getItemViewType(position: Int): Int {
        var viewType = FOOTER_VIEW_TAG
        if (position < beans!!.size()) {
            viewType = getViewType(beans!![position])
            if (viewType < 0) {
                InvalidParameterException("viewType must > 0")
            }
        }
        return viewType
    }

    inline fun<reified B:T> extendDatas(beans: List<B>) {
        this.beans!!.addAll(beans)
    }

    fun clearDatas() {
        this.beans!!.clear()
    }

    class Builder<B,VB:ViewDataBinding>(
        clazz: Class<B>,
        viewLayout:((viewType:Int) -> Int),
        onLoadData: (() -> Unit),
        instance: InstanceBeansCallBack<B,VB>

    ) {
        private val adapter = BottomRefreshAdapter<B,VB>(clazz, instance)

        init {
            adapter.layoutResource = viewLayout
            adapter.onLoadData = onLoadData
        }

        fun bindView(bindTo:((binder:VB, bean:B) -> Unit)?=null):Builder<B, VB> {
            adapter.onBindView = bindTo
            return this
        }

        fun callOnClick(onClick:((view:View, bean:B)->Unit)): Builder<B, VB> {
            adapter.onClickItemView = onClick
            return this
        }

        fun build(): BottomRefreshAdapter<B,VB> {
            return adapter
        }

    }

    @FunctionalInterface
    interface InstanceBeansCallBack<T,S:ViewDataBinding> {
        fun instance(adapter: BottomRefreshAdapter<T,S>): SortedListAdapterCallback<T>?
    }

    private var isEnd = false
    fun setToEnd(isEnd:Boolean) {
        this.isEnd = isEnd
    }
}