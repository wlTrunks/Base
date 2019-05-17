package com.inter.trunks.base.component;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.View;


public abstract class UIComponent {
    private Context context;

    public final void onViewCreated(Context context, View layout) {
        this.context = new WeakReference(context);
        onViewCreated(layout);
    }

    public void onViewCreated(View layout) {
    }

    public void onDestroyView() {
    }

    @CallSuper
    public void onDestroy() {
        this.context = null;
    }

    protected final Context getContext() {
        return context;
    }
}
