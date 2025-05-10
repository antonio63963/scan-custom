package com.example.native_insert.ScannerListen;

public interface ScannerCallback {
    void onComplete(String result);
    void onError(String message);
}
