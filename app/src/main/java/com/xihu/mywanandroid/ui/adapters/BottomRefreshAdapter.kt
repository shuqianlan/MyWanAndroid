package com.xihu.mywanandroid.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class BottomRefreshAdapter<T> private constructor(clazz: Class<T>, cb:InstanceBeansCallBack<T>): RecyclerView.Adapter<ViewHolder>() {
    private val FOOTER_VIEW_TAG = -1
    public var beans: SortedList<T>? = null

    protected var onBindView: (view:View, bean:T) -> Unit = {view, bean ->  }
    protected var onClickItemView: ((view:View, bean:T) -> Unit)?=null
    protected var onLoadData: (suspend (adapter:BottomRefreshAdapter<T>) -> Unit)?=null
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
            R.layout.bottomrefreshlayout
        } else {
            layoutResource?.invoke(viewType)
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(layout!!, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        if (position < beans!!.size()) {
            onBindView?.invoke(holder.itemView, beans!![position])
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
        print(">>>>>>>>>>>>>>>>>>>>>. $beans")
        this.beans!!.addAll(beans)
        notifyDataSetChanged()
    }

    class Builder<B>(
        clazz: Class<B>,
        bindToView:((view:View, bean:B) -> Unit),
        viewLayout:((viewType:Int) -> Int),
        onLoadData: (suspend (BottomRefreshAdapter<B>) -> Unit),
        instance: InstanceBeansCallBack<B>

    ) {
        val adapter = BottomRefreshAdapter<B>(clazz, instance)

        init {
            adapter.layoutResource = viewLayout
            adapter.onBindView = bindToView
            adapter.onLoadData = onLoadData
        }

        fun setDatas(beans: List<B>?): Builder<B> {
            adapter.beans!!.addAll(beans!!)
            return this
        }

        fun build(): BottomRefreshAdapter<*> {
            return adapter
        }

    }

    @FunctionalInterface
    interface InstanceBeansCallBack<T> {
        fun instance(adapter: BottomRefreshAdapter<T>): SortedListAdapterCallback<T>?
    }

}