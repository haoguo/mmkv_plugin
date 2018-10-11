package me.haog.mmkv;

import android.content.Context;

import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * MmkvPlugin
 */
public final class MmkvPlugin implements MethodCallHandler {

  private MmkvPlugin(Context context) {
    rootDir = MMKV.initialize(context);
    instances = new HashMap<String, MMKV>();
  }

  private final String rootDir;

  private final Map<String, MMKV> instances;

  /**
   * Plugin registration.
   */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "mmkv");
    channel.setMethodCallHandler(new MmkvPlugin(registrar.context()));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if ("putBoolean".equals(call.method)) {
      instances
          .get(call.argument("id"))
          .encode(call.argument("key"), call.<Boolean>argument("value"));
    } else if ("putInt".equals(call.method)) {
      instances
          .get(call.argument("id"))
          .encode(call.argument("key"), call.<Integer>argument("value"));
    } else if ("putLong".equals(call.method)) {
      instances
          .get(call.argument("id"))
          .encode(call.argument("key"), call.<Long>argument("value"));
    } else if ("putString".equals(call.method)) {
      instances
          .get(call.argument("id"))
          .encode(call.argument("key"), call.<String>argument("value"));
    } else if ("putFloat".equals(call.method)) {
      instances
          .get(call.argument("id"))
          .encode(call.argument("key"), call.<Float>argument("value"));
    } else if ("putBytes".equals(call.method)) {
      instances
          .get(call.argument("id"))
          .encode(call.argument("key"), call.<byte[]>argument("value"));
    } else if ("getBoolean".equals(call.method)) {
      boolean value = instances
          .get(call.argument("id"))
          .decodeBool(
              call.argument("key"),
              call.hasArgument("defaultValue") ? call.argument("defaultValue") : false);
    } else if ("getInt".equals(call.method)) {
      int value = instances
          .get(call.argument("id"))
          .decodeInt(
              call.argument("key"),
              call.hasArgument("defaultValue") ? call.argument("defaultValue") : 0);
    } else if ("getLong".equals(call.method)) {
      long value = instances
          .get(call.argument("id"))
          .decodeLong(
              call.argument("key"),
              call.hasArgument("defaultValue") ? call.argument("defaultValue") : 0L);
    } else if ("getString".equals(call.method)) {
      String value = instances
          .get(call.argument("id"))
          .decodeString(
              call.argument("key"),
              call.hasArgument("defaultValue") ? call.argument("defaultValue") : null);
    } else if ("getFloat".equals(call.method)) {
      float value = instances
          .get(call.argument("id"))
          .decodeFloat(
              call.argument("key"),
              call.hasArgument("defaultValue") ? call.argument("defaultValue") : 0.0F);
    } else if ("getBytes".equals(call.method)) {
      byte[] value = instances
          .get(call.argument("id"))
          .decodeBytes(call.argument("key"));
    } else if ("containsKey".equals(call.method)) {
      instances
          .get(call.argument("id"))
          .containsKey(call.argument("key"));
    } else if ("remove".equals(call.method)) {
      instances
          .get(call.argument("id"))
          .removeValueForKey(call.argument("key"));
    } else if ("clear".equals(call.method)) {
      instances
          .get(call.argument("id"))
          .clearAll();
    } else if ("keys".equals(call.method)) {
      instances
          .get(call.argument("id"))
          .allKeys();
    } else {
      result.notImplemented();
    }
  }
}
