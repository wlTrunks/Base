package com.inter.trunks.base.component;


public interface Progressable {

    Progressable STUB = new Progressable() {

        @Override
        public void begin() {
        }

        @Override
        public void end() {
        }

    };

    void begin();

    void end();

}
