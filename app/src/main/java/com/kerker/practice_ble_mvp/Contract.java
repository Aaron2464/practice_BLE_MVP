package com.kerker.practice_ble_mvp;

public interface Contract {
    interface View {
        void writeSuccess();
    }

    interface Presenter {
        void scanBle(String ssid, String pwd);
    }
}
