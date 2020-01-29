package com.kararnab.contacts.widgets;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import org.jetbrains.annotations.Nullable;

public class EmptyRecyclerView extends RecyclerView {
    private View mEmptyView;
    public EmptyRecyclerView(Context context) {
        super(context);
    }
    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    private void initEmptyView() {
        if (mEmptyView != null) {
            long timeout = 500;
            if(getAdapter() == null || getAdapter().getItemCount() == 0){
                mEmptyView.setVisibility(VISIBLE);
                EmptyRecyclerView.this.setVisibility(GONE);
                //TODO: UiUtils.crossFade(mEmptyView,EmptyRecyclerView.this,timeout);
            }else{
                EmptyRecyclerView.this.setVisibility(VISIBLE);
                mEmptyView.setVisibility(GONE);
                //TODO: UiUtils.crossFade(EmptyRecyclerView.this,mEmptyView,timeout);
            }
            /*mEmptyView.setVisibility(
                    getAdapter() == null || getAdapter().getItemCount() == 0 ? VISIBLE : GONE);
            EmptyRecyclerView.this.setVisibility(
                    getAdapter() == null || getAdapter().getItemCount() == 0 ? GONE : VISIBLE);*/
        }
    }
    final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            initEmptyView();
        }
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            initEmptyView();
        }
        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            initEmptyView();
        }
    };
    @Override
    public void setAdapter(Adapter adapter) {
        Adapter oldAdapter = getAdapter();
        super.setAdapter(adapter);
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }
    public void setEmptyView(View view) {
        this.mEmptyView = view;
        initEmptyView();
    }
}