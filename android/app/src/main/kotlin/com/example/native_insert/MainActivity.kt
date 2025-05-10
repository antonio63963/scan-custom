package com.example.native_insert

import com.example.native_insert.ScannerListen.ScannerCallback
import com.example.native_insert.ScannerListen.ScannerListen
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel

class MainActivity : FlutterActivity() {
    private val eventChannelName = "scanner_listener_channel"
    private var eventSink: EventChannel.EventSink? = null
    private var myJavaReceiver =
        ScannerListen() // Создаем экземпляр вашего Java BroadcastReceiver

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        EventChannel(flutterEngine.dartExecutor.binaryMessenger, eventChannelName).setStreamHandler(
            object : EventChannel.StreamHandler {
                override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
                    eventSink = events
                    // Здесь можно отправлять начальные данные во Flutter, если необходимо
                }

                override fun onCancel(arguments: Any?) {
                    eventSink = null
                }
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        // Создаем IntentFilter для действий, которые хочет перехватывать ваш BroadcastReceiver
        val filter = IntentFilter(ScannerListen.SCAN_EVENT) // Пример: отслеживание заряда батареи

        // прослушка сканера
        val callback: ScannerCallback = object :
            ScannerCallback {
            override fun onComplete(result: String) {
                onScannerCallback(result)
            }

            override fun onError(message: String) {
                Toast.makeText(
                    applicationContext,
                    "Scanning has failed...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        myJavaReceiver.setCallback(callback)

        registerReceiver(myJavaReceiver, filter, RECEIVER_NOT_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myJavaReceiver) // Важно не забыть отписаться от приемника
    }

    fun onScannerCallback(barcode: String?) {
        Log.i("barcode", barcode!!)
        if (barcode != null) {
            Log.i("barcode", barcode)
        }
        if(barcode != null && eventSink is EventChannel.EventSink) {
            Log.i("barcode", barcode)
            eventSink!!.success(barcode)
        }
    }
}