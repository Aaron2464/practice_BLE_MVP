package com.kerker.practice_ble_mvp;

import com.clj.fastble.data.BleDevice;

public class BlePresenter implements Contract.Presenter {

    @Override
    public void scanBle(String ssid, String pwd) {
        BleModel.setSsidAndPwd(ssid, pwd);
        BleModel.BleScan(new ScanSuccessCallback() {
            @Override
            public void onSuccess(BleDevice device) {
                BleModel.BleConnect(device);
            }

            @Override
            public void onFail() {

            }
        });
    }
}
