package com.kerker.practice_ble_mvp;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;

import java.util.ArrayList;
import java.util.UUID;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BlePresenter implements Contract.Presenter {

    private Contract.View mView;

    public BlePresenter(Contract.View view) {
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    public void initBle() {
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);
    }

    public void scanBle(final String ssid, final String pwd) {
        BleModel.setSsidAndPwd(ssid, pwd);
        BleModel.BleScan(new ScanSuccessCallback() {
            @Override
            public void onSuccess(BleDevice device) {
                if (BleModel.getmBeanMap().get(device.getName()).getRssiArray().size() < 5) {
                    setScanRule();
                    scanBle(ssid, pwd);
                } else {
                    if (calculateAverage(BleModel.getmBeanMap().get(device.getName()).getRssiArray()) > -65) {
                        BleModel.getmBeanMap().get(device.getName()).setScanFlag(true);
                        BleModel.BleConnect(device);
                    } else {
                        setScanRule();
                        scanBle(ssid, pwd);
                    }
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    public void setScanRule() {
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(new UUID[]{UUID.fromString("0000fe50-0000-1000-8000-00805f9b34fb")})      // 只扫描指定的服务的设备，可选
                .setDeviceName(false, null)   // 只扫描指定广播名的设备，可选
                //        "BTKM-02D7A97CB835"
                .setDeviceMac(null)                  // 只扫描指定mac的设备，可选
                //        "02:D7:A9:7C:B8:35"
//                "02:94:24:67:D7:C1"
                .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(5000)              // 扫描超时时间
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    public String getStringToHex(String strValue) {
        byte byteData[] = null;
        int intHex = 0;
        String strHex = "";
        String strReturn = "";
        try {
            byteData = strValue.getBytes("ISO8859-1");
            for (int intI = 0; intI < byteData.length; intI++) {
                intHex = (int) byteData[intI];
                if (intHex < 0)
                    intHex += 256;
                if (intHex < 16)
                    strHex += "0" + Integer.toHexString(intHex).toUpperCase();
                else
                    strHex += Integer.toHexString(intHex).toUpperCase();
            }
            strReturn = strHex;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strReturn;
    }


    private double calculateAverage(ArrayList<Integer> marks) {
        Integer sum = 0;
        if (!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }
}
