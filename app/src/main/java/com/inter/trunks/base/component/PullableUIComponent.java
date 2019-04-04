package com.inter.trunks.base.component;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

public class PullableUIComponent extends UIComponent implements Progressable {
    protected SwipeRefreshLayout swipeLayout;

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    public PullableUIComponent(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    @Override
    public void onViewCreated(View layout) {
        super.onViewCreated(layout);
        swipeLayout = layout.findViewById(R.id.swipe);
        swipeLayout.setOnRefreshListener(onRefreshListener);
    }

    @Override
    public void begin() {
        swipeLayout.setRefreshing(true);
    }

    @Override
    public void end() {
        swipeLayout.setRefreshing(false);
    }
}
