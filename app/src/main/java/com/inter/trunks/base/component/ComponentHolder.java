package com.inter.trunks.base.component;

public interface ComponentHolder {
    <T extends UIComponent> T getComponent(Class<T> componentClass);
}
