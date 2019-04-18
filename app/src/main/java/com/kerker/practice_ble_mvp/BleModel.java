package com.kerker.practice_ble_mvp;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.util.List;

public class BleModel {

    private static String mSsid;
    private static String mPwd;
    private static String mTimeStamp = "0002540BE3FF";
    private static String w1 = "7731";
    private static String w0 = "7730";
    private static String w9 = "7739";

    public static void BleScan(final ScanSuccessCallback scanSuccessCallback) {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {

            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                Log.d("TEST", "BLE bleDevice: " + bleDevice);
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                Log.d("TEST", "scan sucess");
                Log.d("TEST", "scan sucess: " + scanResultList);
                BleDevice device = null;
                if (scanResultList.size() != 0) {
                    device = scanResultList.get(0);
                    for (int i = 0; i < scanResultList.size(); i++) {
                        if (device.getRssi() < scanResultList.get(i).getRssi()) {
                            device = scanResultList.get(i);

                        }
                    }
                    scanSuccessCallback.onSuccess(device);
                }
            }
        });
    }

    public static void BleConnect(BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice.getMac(), new BleGattCallback() {
            @Override
            public void onStartConnect() {

            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {

            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                Log.d("TEST", "connect sucess");
                gatt.getServices();
                BluetoothGattService service = getGattService(gatt);
                BluetoothGattCharacteristic characteristic = getCharacteristic(service);
                BleNotify(bleDevice, characteristic);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            }
        });
    }

    public static void BleNotify(final BleDevice bleDevice, final BluetoothGattCharacteristic characteristic) {
        BleManager.getInstance().notify(bleDevice, characteristic.getService().getUuid().toString(), characteristic.getUuid().toString(), new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
                Log.d("TEST", "notify sucess");
                new BleAsyncTask(bleDevice,characteristic,CreateHex24(mSsid)).doInBackground();
                new BleAsyncTask(bleDevice,characteristic,CreateHex24(mPwd)).doInBackground();
                //                             7d969922a951001554175420
//                            "4557158cc237001553852782"   D7C1
//                            "77314e55554f4c696e6b5f5341540002540BE3FF"  B835
//                            "7731" + "4e55554f4c696e6b5f534154"+"0002540BE3FF"
//                            getStringToHex("NUUOLink_SAT")
            }

            @Override
            public void onNotifyFailure(BleException exception) {

            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
            }
        });
    }

    private static String CreateHex24(String hex24){
        String i, j, k;
        Log.d("TEST", "mSsid: " + hex24.toCharArray().length);
        Log.d("TEST", "mSsid: " + hex24);
        if (hex24.toCharArray().length <= 24) {
            i = hex24;
            if (i.length() != 24) {
                i = i + String.format("%1$0" + (24 - i.length()) + "d", 0);
            }
            return w1 + i + mTimeStamp;
        } else if (48 >= hex24.toCharArray().length && hex24.toCharArray().length > 24) {
            i = hex24.substring(0, 24);
            j = hex24.substring(24);
            if (j.length() != 24) {
                j = j + String.format("%1$0" + (24 - j.length()) + "d", 0);
            }
            return w1 + i + mTimeStamp + w9 + j + mTimeStamp;
        } else {
            i = hex24.substring(0, 24);
            j = hex24.substring(24, 48);
            k = hex24.substring(48);
            if (k.length() != 24) {
                k = k + String.format("%1$0" + (24 - k.length()) + "d", 0);
            }
            return w1 + i + mTimeStamp + w0 + j + mTimeStamp + w9 + k + mTimeStamp;
        }
    }

    public static void setSsidAndPwd(String ssid, String pwd) {
        mSsid = ssid;
        mPwd = pwd;
    }

    private static BluetoothGattCharacteristic getCharacteristic(BluetoothGattService service) {
        BluetoothGattCharacteristic gattCharacteristic = null;
        for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
            if (characteristic.getUuid().toString().contains("0000fe51")) {
                gattCharacteristic = characteristic;
            }
        }
        return gattCharacteristic;
    }

    private static BluetoothGattService getGattService(BluetoothGatt gatt) {
        BluetoothGattService gattService = null;
        for (BluetoothGattService service : gatt.getServices()) {
            String uuid = service.getUuid().toString();
            if (uuid != null) {
                if (uuid.contains("0000fe50")) {
                    gattService = service;
                }
            }
        }
        return gattService;
    }
}
