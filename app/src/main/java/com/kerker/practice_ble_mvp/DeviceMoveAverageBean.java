package com.kerker.practice_ble_mvp;

import com.clj.fastble.data.BleDevice;

import java.util.ArrayList;

public class DeviceMoveAverageBean {
    private BleDevice mBleDevice;
    private boolean mScanFlag;
    private int mRssiMa;
    private ArrayList<Integer> mRssiArray = new ArrayList<>();

    public DeviceMoveAverageBean(BleDevice bleDevice, boolean scanFlag, int rssiMa) {
        mBleDevice = bleDevice;
        mScanFlag = scanFlag;
        mRssiMa = rssiMa;
    }

    public BleDevice getBleDevice() {
        return mBleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        mBleDevice = bleDevice;
    }

    public boolean isScanFlag() {
        return mScanFlag;
    }

    public void setScanFlag(boolean scanFlag) {
        mScanFlag = scanFlag;
    }

    public int getRssiMa() {
        return mRssiMa;
    }

    public void setRssi(int rssiMa) {
        mRssiMa = rssiMa;
        if (mRssiArray.size() < 5) {
            mRssiArray.add(rssiMa);
        } else {
            mRssiArray.remove(0);
            mRssiArray.add(rssiMa);
        }
    }

    public ArrayList<Integer> getRssiArray() {
        return mRssiArray;
    }
}
