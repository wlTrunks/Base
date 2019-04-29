package com.inter.trunks.base.component;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public final class UIComponentHolder implements ComponentHolder {
    private final ArrayMap<Class<UIComponent>, UIComponent> hashComponent = new ArrayMap<>();

    private UIComponentHolder(List<UIComponent> components) {
        for (UIComponent component : components) {
            hashComponent.put((Class<UIComponent>) component.getClass(), component);
        }
        components.clear();
    }

    public void onViewCreated(Context context, View view) {
        for (int i = 0; i < hashComponent.size(); i++) {
            hashComponent.valueAt(i).onViewCreated(context, view);
        }
    }

    public void onDestroyView() {
        for (int i = 0; i < hashComponent.size(); i++) {
            hashComponent.valueAt(i).onDestroyView();
        }
    }

    public void onDestroy() {
        for (int i = 0; i < hashComponent.size(); i++) {
            hashComponent.valueAt(i).onDestroy();
        }
        hashComponent.clear();
    }

    public <T extends UIComponent> T getComponent(Class<T> componentClass) {
        T component = getComponentByClass(componentClass);
        if (componentClass.isInstance(component)) {
            return component;
        }
        return null;
    }

    @SuppressWarnings({"Unchecked", "SuspiciousMethodCalls"})
    public <T extends UIComponent> T getComponentByClass(Class<T> componentClass) {
        //noinspection unchecked
        return (T) hashComponent.get(componentClass);
    }

    public List<Progressable> getProgressableComponent() {
        List<Progressable> list = new ArrayList<>();
        for (int i = 0; i < hashComponent.size(); i++) {
            if (hashComponent.valueAt(i) instanceof Progressable) list.add((Progressable) hashComponent.valueAt(i));
        }
        return list;
    }


    public static final class Builder {
        private final List<UIComponent> components = new ArrayList<>();

        public Builder with(UIComponent component) {
            components.add(component);
            return this;
        }

        public Builder addAll(List<UIComponent> components) {
            if (components != null) this.components.addAll(components);
            return this;
        }

        public UIComponentHolder build() {
            return new UIComponentHolder(components);
        }
    }
}

