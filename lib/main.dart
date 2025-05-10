import 'package:flutter/material.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _uiMessage = 'Unknown message';
  // // 1. создаем канал
  // static const platform = MethodChannel("scanner_listener_channel");

  // void _getMessageFromNative() async {
  //   String receivedMessage;
  //   try {
  //     receivedMessage = await platform.invokeMethod('getBarcode');
  //   } on PlatformException catch (e) {
  //     receivedMessage = 'Failed massage ${e.message}';
  //   }
  //   setState(() {
  //     _uiMessage = receivedMessage;
  //   });
  // }

  // Event
  static const EventChannel _eventChannel =
      EventChannel('scanner_listener_channel');
  String _javaData = 'Ожидание данных...';

  @override
  void initState() {
    super.initState();
    _eventChannel.receiveBroadcastStream().listen(_onEvent, onError: _onError);
  }

  void _onEvent(Object? event) {
    print("EVENT: $event");
    setState(() {
      _javaData = 'Данные из Java: $event';
    });
  }

  void _onError(Object error) {
    setState(() {
      _javaData = 'Ошибка получения данных из Java: $error';
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text('BroadcastReceiver во Flutter'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text(
                'Полученные данные:',
              ),
              Text("JAVA: $_javaData"),
              Text(
                _uiMessage,
                style: TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
              ),
              ElevatedButton(
                onPressed: () {},
                // _getMessageFromNative, // Пример отправки (если нужно)
                child: Text('Отправить Broadcast (пример)'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
