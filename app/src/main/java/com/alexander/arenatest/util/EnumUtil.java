package com.alexander.arenatest.util;

public class EnumUtil {

    public enum NetworkState {
        LOADING(0), LOADED(1), FAILED(2);

        private final int type;

        NetworkState(int type) {
            this.type = type;
        }

        public int getValor() {
            return type;
        }
    }
}