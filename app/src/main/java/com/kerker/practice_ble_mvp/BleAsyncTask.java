package com.kerker.practice_ble_mvp;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.AsyncTask;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;

public class BleAsyncTask extends AsyncTask<Void, Void, Void> {
    private BleDevice mBleDevice;
    private BluetoothGattCharacteristic mCharacteristic;
    private String hex24;

    public BleAsyncTask(BleDevice bleDevice, BluetoothGattCharacteristic characteristic, String hex24) {
        mBleDevice = bleDevice;
        mCharacteristic = characteristic;
        this.hex24 = hex24;
    }

    @Override
    protected Void doInBackground(Void... objects) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BleManager.getInstance().write(mBleDevice, mCharacteristic.getService().getUuid().toString(), mCharacteristic.getUuid().toString(), HexUtil.hexStringToBytes(hex24), false, new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {

            }

            @Override
            public void onWriteFailure(BleException exception) {

            }
        });
        return null;
    }
}
