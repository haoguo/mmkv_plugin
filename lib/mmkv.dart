import 'dart:async';

import 'package:flutter/services.dart';

class Mmkv {
  static const MethodChannel _channel =
      const MethodChannel('mmkv');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    final a = {};
    return version;
  }
}
