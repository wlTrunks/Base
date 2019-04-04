package com.inter.trunks.base.component;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;

public class RecyclerUIComponent<Adapter extends RecyclerView.Adapter> extends UIComponent {
    protected RecyclerView recyclerView;
    @Nullable
    protected View emptyView;

    private Adapter adapter;

    public RecyclerUIComponent(Adapter adapter) {
        this.adapter = adapter;
    }

    private RecyclerView.LayoutManager layoutManager;
    private HashSet<RecyclerView.ItemDecoration> setItemDecorations = new HashSet();

    @Override
    public void onViewCreated(View layout) {
        super.onViewCreated(layout);
        recyclerView = layout.findViewById(R.id.list);
        emptyView = layout.findViewById(android.R.id.empty);
        if (recyclerView.getLayoutManager() == null) {
            if (layoutManager == null) {
                layoutManager = new LinearLayoutManager(getContext());
            }
            recyclerView.setLayoutManager(layoutManager);
        }
        recyclerView.setAdapter(adapter);
        if (setItemDecorations.size() > 0) {
            Iterator<RecyclerView.ItemDecoration> itr = setItemDecorations.iterator();
            while (itr.hasNext()) {
                recyclerView.addItemDecoration(itr.next());
            }
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public void refreshUI() {
        if (adapter.getItemCount() == 0) {
            showEmptyView();
        } else {
            hideEmptyView();
        }
    }

    public void addItemDecorations(HashSet<RecyclerView.ItemDecoration> setItemDecorations) {
        this.setItemDecorations.addAll(setItemDecorations);
    }

    public void showEmptyView() {
        recyclerView.setVisibility(View.GONE);
        if (emptyView != null) emptyView.setVisibility(View.VISIBLE);
    }

    public void hideEmptyView() {
        recyclerView.setVisibility(View.VISIBLE);
        if (emptyView != null) emptyView.setVisibility(View.GONE);
    }

    public void hideViews() {
        recyclerView.setVisibility(View.GONE);
        if (emptyView != null) emptyView.setVisibility(View.GONE);
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setEmptyText(@StringRes int text) {
        setEmptyText(getContext().getString(text));
    }

    public void setEmptyText(String text) {
        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(text);
        } else throw new IllegalStateException("EmptyView must be an instance of TextView");
    }

    @Override
    public void onDestroyView() {
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        recyclerView = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        layoutManager = null;
        setItemDecorations.clear();
        adapter = null;
        super.onDestroy();
    }
}
