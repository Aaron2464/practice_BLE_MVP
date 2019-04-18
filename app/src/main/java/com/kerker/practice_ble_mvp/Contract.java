package com.kerker.practice_ble_mvp;

public interface Contract {
    interface View {
        void setPresenter(BlePresenter presenter);

        void writeSuccess();
    }

    interface Presenter {
        void initBle();

        void scanBle(String ssid, String pwd);

        void setScanRule();

        String getStringToHex(String strValue);
    }
}
