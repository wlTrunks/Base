package com.inter.trunks.base.component;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.View;

import java.lang.ref.WeakReference;

public abstract class UIComponent {
    private WeakReference<Context> context;

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
        context.clear();
    }

    protected final Context getContext() {
        return context.get();
    }
}
