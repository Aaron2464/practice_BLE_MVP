package com.kerker.practice_ble_mvp;

import com.clj.fastble.data.BleDevice;

public interface ScanSuccessCallback {
    void onSuccess(BleDevice device);

    void onFail();
}
