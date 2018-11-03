import 'package:flutter/material.dart';
import 'package:mmkv_plugin/mmkv_plugin.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  Mmkv mmkv;

  @override
  void initState() {
    super.initState();
    Mmkv.defaultInstance().then((inst) {
      mmkv = inst;
      mmkv.putString('testint', 'hello world');
    });
  }

  void onPressed() {
    mmkv.getString('testint').then((i) => print(i));
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: const Text('Plugin example app'),
        ),
        body: new Center(
          child: new Text('Running on: $_platformVersion\n'),
        ),
        floatingActionButton: RaisedButton(
          onPressed: this.onPressed,
        ),
      ),
    );
  }
}
