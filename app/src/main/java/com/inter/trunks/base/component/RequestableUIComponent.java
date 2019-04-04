package com.inter.trunks.base.component;

import android.view.View;


public class RequestableUIComponent extends UIComponent {
    public RequestableUIComponent(RequestInterface requestInterface) {
        this.requestInterface = requestInterface;
    }

    protected RequestInterface requestInterface;

    protected View rootConnectionError; //R.id.rootConnectionError

    @Override
    public void onViewCreated(View layout) {
        super.onViewCreated(layout);
        rootConnectionError = layout.findViewById(R.id.rootConnectionError);
    }

    public void showErrorConnetionView() {
        rootConnectionError.setVisibility(View.VISIBLE);
    }

    public void hideErrorConnectionView() {
        rootConnectionError.setVisibility(View.GONE);
    }

    public void onReloadClick() {
        if (requestInterface != null) requestInterface.reload();
    }

    public boolean isRootConnectionVisible() {
        return rootConnectionError.getVisibility() == View.VISIBLE;
    }
}
