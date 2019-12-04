package com.xihu.mywanandroid.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.xihu.mywanandroid.R
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import java.security.InvalidParameterException

/*
* 支持下拉更新的RecyclerView.Adapter
* 1. 支持自定义创建View
* 2. 支持不同的viewtype
* 3. 支持异步的数据加载
*
* */
class BottomRefreshAdapter<T, DB:ViewDataBinding> private constructor(clazz: Class<T>, cb:InstanceBeansCallBack<T, DB>): RecyclerView.Adapter<ViewHolder>() {
    private val FOOTER_VIEW_TAG = -1
    public var beans: SortedList<T>? = null

    protected var onBindView: ((view:View, bean:T) -> Unit)?= null
    protected var onClickItemView: ((view:View, bean:T) -> Unit)?=null
    protected var onLoadData: (suspend (adapter:BottomRefreshAdapter<T,DB>) -> Unit)?=null
    protected var getViewType: (bean: T) -> Int = { bean -> 0 }
    protected var layoutResource: ((viewType:Int)->Int)?=null

    init {
        beans = SortedList(clazz, cb.instance(this)!!)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layout = if (viewType == FOOTER_VIEW_TAG) {

            val databinding = DataBindingUtil.inflate<DB>(LayoutInflater.from(parent.context), R.layout.bottomrefreshlayout, parent, false)
            return ViewHolder(databinding)
        } else {

            val databinding = DataBindingUtil.inflate<DB>(LayoutInflater.from(parent.context), layoutResource?.invoke(viewType)!!, parent, false)
            return ViewHolder(databinding)
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        if (position < beans!!.size()) {
            onBindView?.invoke(holder.itemView, beans!![position]) ?: holder.bindr.setVariable(1, beans!![position])
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
        return beans!!.size() + 1
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val viewtype = holder.itemViewType
        if (viewtype == FOOTER_VIEW_TAG) {
            GlobalScope.launch { onLoadData?.invoke(this@BottomRefreshAdapter) }
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
        notifyDataSetChanged()
    }

    class Builder<B,VB:ViewDataBinding>(
        clazz: Class<B>,
        viewLayout:((viewType:Int) -> Int),
        onLoadData: (suspend (BottomRefreshAdapter<B,VB>) -> Unit),
        instance: InstanceBeansCallBack<B,VB>

    ) {
        val adapter = BottomRefreshAdapter<B,VB>(clazz, instance)

        init {
            adapter.layoutResource = viewLayout
            adapter.onLoadData = onLoadData
        }

        fun build(): BottomRefreshAdapter<B,VB> {
            return adapter
        }

    }

    @FunctionalInterface
    interface InstanceBeansCallBack<T,S:ViewDataBinding> {
        fun instance(adapter: BottomRefreshAdapter<T,S>): SortedListAdapterCallback<T>?
    }

}