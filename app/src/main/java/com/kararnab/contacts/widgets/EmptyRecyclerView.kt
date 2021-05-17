package com.kararnab.contacts.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EmptyRecyclerView : RecyclerView {
    private var mEmptyView: View? = null

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    ) {
    }

    private fun initEmptyView() {
        if (mEmptyView != null) {
            if (adapter == null || adapter!!.itemCount == 0) {
                mEmptyView!!.visibility = VISIBLE
                this@EmptyRecyclerView.visibility = GONE
                //TODO: UiUtils.crossFade(mEmptyView,EmptyRecyclerView.this,timeout);
            } else {
                this@EmptyRecyclerView.visibility = VISIBLE
                mEmptyView!!.visibility = GONE
                //TODO: UiUtils.crossFade(EmptyRecyclerView.this,mEmptyView,timeout);
            }
            /*mEmptyView.setVisibility(
                    getAdapter() == null || getAdapter().getItemCount() == 0 ? VISIBLE : GONE);
            EmptyRecyclerView.this.setVisibility(
                    getAdapter() == null || getAdapter().getItemCount() == 0 ? GONE : VISIBLE);*/
        }
    }

    val observer: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            initEmptyView()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            initEmptyView()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            initEmptyView()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        val oldAdapter = getAdapter()
        super.setAdapter(adapter)
        oldAdapter?.unregisterAdapterDataObserver(observer)
        adapter?.registerAdapterDataObserver(observer)
    }

    fun setEmptyView(view: View?) {
        mEmptyView = view
        initEmptyView()
    }
}