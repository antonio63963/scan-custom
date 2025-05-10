package com.example.native_insert.ScannerListen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ScannerListen extends BroadcastReceiver {
    static public String SCAN_EVENT = "device.scanner.EVENT";
    private ScannerCallback callback;

    public ScannerListen() {
    }

    public ScannerListen(ScannerCallback callback) {
        this.callback = callback;
    }

    public void setCallback(ScannerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        byte[] barcode = intent.getByteArrayExtra("EXTRA_EVENT_DECODE_VALUE");
        if (barcode != null) {
            int d = barcode.length - 1;
            String barcodeStr = new String(barcode, 0, d);
            Log.i("barcode1", barcodeStr);
            if(callback != null) {
                callback.onComplete(barcodeStr);
            }
        } else if(callback != null) {
            callback.onError("Fail on scanning...");
        }

    }
}
