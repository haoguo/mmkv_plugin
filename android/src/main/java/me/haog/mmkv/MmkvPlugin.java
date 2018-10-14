package me.haog.mmkv;

import android.content.Context;
import android.util.Base64;

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
    defaultInstance = MMKV.defaultMMKV();
  }

  private final String rootDir;

  private final Map<String, MMKV> instances;

  private final MMKV defaultInstance;

  /**
   * Plugin registration.
   */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "mmkv");
    channel.setMethodCallHandler(new MmkvPlugin(registrar.context()));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if ("default".equals(call.method)) {
      result.success(null); // dummy because init in constructor.
    } else if ("withId".equals(call.method)) {
      String id = call.argument("id");
      instances.put(id, MMKV.mmkvWithID(id));
    } else if ("withCryptKey".equals(call.method)) {
      String id = call.argument("id");
      byte[] cryptKey = call.argument("cryptKey");
      String cryptKeySz = Base64.encodeToString(cryptKey, Base64.DEFAULT);
      instances.put(id, MMKV.mmkvWithID(id, MMKV.SINGLE_PROCESS_MODE, cryptKeySz));
    } else {
      MMKV inst;
      if (call.argument("default")) {
        inst = defaultInstance;
      } else {
        inst = instances.get(call.argument("id"));
      }
      if ("putBoolean".equals(call.method)) {
        inst.encode(call.<String>argument("key"), call.<Boolean>argument("value"));
      } else if ("putInt".equals(call.method)) {
        inst.encode(call.<String>argument("key"), call.<Integer>argument("value"));
      } else if ("putLong".equals(call.method)) {
        inst.encode(call.<String>argument("key"), call.<Long>argument("value"));
      } else if ("putString".equals(call.method)) {
        inst.encode(call.<String>argument("key"), call.<String>argument("value"));
      } else if ("putDouble".equals(call.method)) {
        inst.encode(call.<String>argument("key"), call.<Double>argument("value"));
      } else if ("putBytes".equals(call.method)) {
        inst.encode(call.<String>argument("key"), call.<byte[]>argument("value"));
      } else if ("getBoolean".equals(call.method)) {
        boolean value = inst.decodeBool(
            call.<String>argument("key"),
            call.hasArgument("defaultValue") ? call.<Boolean>argument("defaultValue") : false);
      } else if ("getInt".equals(call.method)) {
        int value = inst.decodeInt(
            call.<String>argument("key"),
            call.hasArgument("defaultValue") ? call.<Integer>argument("defaultValue") : 0);
      } else if ("getLong".equals(call.method)) {
        long value = inst.decodeLong(
            call.<String>argument("key"),
            call.hasArgument("defaultValue") ? call.<Long>argument("defaultValue") : 0L);
      } else if ("getString".equals(call.method)) {
        String value = inst.decodeString(
            call.<String>argument("key"),
            call.hasArgument("defaultValue") ? call.<String>argument("defaultValue") : null);
      } else if ("getDouble".equals(call.method)) {
        double value = inst.decodeDouble(
            call.<String>argument("key"),
            call.hasArgument("defaultValue") ? call.<Double>argument("defaultValue") : 0.0D);
      } else if ("getBytes".equals(call.method)) {
        byte[] value = inst.decodeBytes(call.<String>argument("key"));
      } else if ("containsKey".equals(call.method)) {
        inst.containsKey(call.<String>argument("key"));
      } else if ("remove".equals(call.method)) {
        inst.removeValueForKey(call.<String>argument("key"));
      } else if ("clear".equals(call.method)) {
        inst.clearAll();
      } else if ("keys".equals(call.method)) {
        inst.allKeys();
      } else if ("count".equals(call.method)) {
        inst.count();
      } else {
        result.notImplemented();
      }
    }
  }
}
