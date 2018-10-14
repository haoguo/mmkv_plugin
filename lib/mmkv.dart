import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/services.dart';

class Mmkv {
  static const MethodChannel _channel = const MethodChannel('mmkv');

  Mmkv._(this.isDefault, this.id);

  final bool isDefault;
  final String id;

  static Future<Mmkv> defaultInstance() {
    Completer<Mmkv> completer = Completer();
    _channel.invokeMethod('default').then((value) => completer.complete(Mmkv._(true, null))).catchError((error) => completer.completeError(error));
    return completer.future;
  }

  static Future<Mmkv> withId(String id) {
    Completer<Mmkv> completer = Completer();
    _channel.invokeMethod('withId', {'id': id}).then((value) => completer.complete(Mmkv._(false, id))).catchError((error) => completer.completeError(error));
    return completer.future;
  }

  static Future<Mmkv> withCryptKey(String id, Uint8List cryptKey) {
    Completer<Mmkv> completer = Completer();
    _channel
        .invokeMethod('withCryptKey', {'id': id, 'cryptKey': cryptKey})
        .then((value) => completer.complete(Mmkv._(false, id)))
        .catchError((error) => completer.completeError(error));
    return completer.future;
  }

  Future _invoke(String method, Map<String, dynamic> arguments) {
    Map<String, dynamic> params = {};
    if (this.isDefault) {
      params['default'] = true;
    } else {
      params['id'] = this.id;
    }
    params.addAll(arguments);
    return _channel.invokeMethod(method, params);
  }

  void putBoolean(String key, bool value) async => await _invoke('putBoolean', {'key': key, 'value': value});

  void putInt(String key, int value) async => await _invoke('putLong', {'key': key, 'value': value});

  void putString(String key, String value) async => await _invoke('putString', {'key': key, 'value': value});

  void putBytes(String key, Uint8List value) async => await _invoke('putBytes', {'key': key, 'value': value});

  void putDouble(String key, double value) async => await _invoke('putDouble', {'key': key, 'value': value});

  Future<bool> getBoolean(String key) => _invoke('getBoolean', {'key': key});

  Future<int> getInt(String key) => _invoke('getInt', {'key': key});

  Future<String> getString(String key) => _invoke('getString', {'key': key});

  Future<Uint8List> getBytes(String key) => _invoke('getBytes', {'key': key});

  Future<double> getDouble(String key) => _invoke('getDouble', {'key': key});

  Future<bool> containsKey(String key) => _invoke('containsKey', {'key': key});

  Future<void> remove(String key) => _invoke('remove', {'key': key});

  Future<void> clear() => _invoke('clear', {});

  Future<List<String>> keys() => _invoke('keys', {});

  Future<int> count() => _invoke('count', {});
}
