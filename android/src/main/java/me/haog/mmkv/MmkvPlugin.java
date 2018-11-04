package me.haog.mmkv;

import android.content.Context;
import android.util.Base64;

import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
      result.success(null);
    } else if ("withCryptKey".equals(call.method)) {
      String id = call.argument("id");
      byte[] cryptKey = call.argument("cryptKey");
      String cryptKeySz = Base64.encodeToString(cryptKey, Base64.DEFAULT);
      instances.put(id, MMKV.mmkvWithID(id, MMKV.SINGLE_PROCESS_MODE, cryptKeySz));
      result.success(null);
    } else {
      MMKV inst;
      if (call.hasArgument("default")) {
        inst = defaultInstance;
      } else {
        inst = instances.get(call.argument("id"));
      }
      if ("putBoolean".equals(call.method)) {
        inst.encode(call.<String>argument("key"), call.<Boolean>argument("value"));
        result.success(null);
      } else if ("putLong".equals(call.method)) {
        Object value = call.argument("value");
        Long longValue;
        if (value instanceof Integer) {
          longValue = ((Integer) value).longValue();
        } else {
          longValue = (Long) value;
        }
        inst.encode(call.<String>argument("key"), longValue);
        result.success(null);
      } else if ("putString".equals(call.method)) {
        inst.encode(call.<String>argument("key"), call.<String>argument("value"));
        result.success(null);
      } else if ("putDouble".equals(call.method)) {
        inst.encode(call.<String>argument("key"), call.<Double>argument("value"));
        result.success(null);
      } else if ("putBytes".equals(call.method)) {
        inst.encode(call.<String>argument("key"), call.<byte[]>argument("value"));
        result.success(null);
      } else if ("getBoolean".equals(call.method)) {
        boolean value = inst.decodeBool(
            call.<String>argument("key"),
            call.hasArgument("defaultValue") ? call.<Boolean>argument("defaultValue") : false);
        result.success(value);
      } else if ("getLong".equals(call.method)) {
        long value = inst.decodeLong(
            call.<String>argument("key"),
            call.hasArgument("defaultValue") ? call.<Long>argument("defaultValue") : 0L);
        result.success(value);
      } else if ("getString".equals(call.method)) {
        String value = inst.decodeString(
            call.<String>argument("key"),
            call.hasArgument("defaultValue") ? call.<String>argument("defaultValue") : null);
        result.success(value);
      } else if ("getDouble".equals(call.method)) {
        double value = inst.decodeDouble(
            call.<String>argument("key"),
            call.hasArgument("defaultValue") ? call.<Double>argument("defaultValue") : 0.0D);
        result.success(value);
      } else if ("getBytes".equals(call.method)) {
        byte[] value = inst.decodeBytes(call.<String>argument("key"));
        result.success(value);
      } else if ("containsKey".equals(call.method)) {
        result.success(inst.containsKey(call.<String>argument("key")));
      } else if ("remove".equals(call.method)) {
        inst.removeValueForKey(call.<String>argument("key"));
        result.success(null);
      } else if ("clear".equals(call.method)) {
        inst.clearAll();
        result.success(null);
      } else if ("keys".equals(call.method)) {
        String[] keys = inst.allKeys();
        result.success(Arrays.asList(keys));
      } else if ("count".equals(call.method)) {
        long count = inst.count();
        result.success(count);
      } else if ("removeValuesForKeys".equals(call.method)) {
        List<?> keyList = call.argument("keys");
        String[] keys = new String[keyList.size()];
        keyList.toArray(keys);
        inst.removeValuesForKeys(keys);
        result.success(null);
      } else {
        result.notImplemented();
      }
    }
  }
}
