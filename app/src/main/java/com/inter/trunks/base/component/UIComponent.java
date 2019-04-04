package com.inter.trunks.base.component;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.View;
import butterknife.ButterKnife;

import java.lang.ref.WeakReference;

public abstract class UIComponent implements ComponentHolder {
    private WeakReference<Context> context;
    private UIComponentHolder uiComponentHolder;

    public final void onViewCreated(Context context, View layout) {
        this.context = new WeakReference(context);
        uiComponentHolder = createComponentHolder();
        uiComponentHolder.onViewCreated(context, layout);
        onViewCreated(layout);
    }

    @CallSuper
    public void onViewCreated(View layout) {
    }

    @CallSuper
    public void onDestroyView() {
        uiComponentHolder.onDestroyView();
    }

    @CallSuper
    public void onDestroy() {
        context.clear();
        uiComponentHolder.onDestroy();
        uiComponentHolder = null;
    }

    protected final Context getContext() {
        return context.get();
    }

    protected UIComponentHolder createComponentHolder() {
        return new UIComponentHolder.Builder().build();
    }

    @Override
    public <T extends UIComponent> T getComponent(Class<T> componentClass) {
        return uiComponentHolder.getComponent(componentClass);
    }

    public final UIComponentHolder getUiComponentHolder() {
        return uiComponentHolder;
    }
}
