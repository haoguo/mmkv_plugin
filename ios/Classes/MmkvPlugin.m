#import "MmkvPlugin.h"

@interface FLTMmkvPlugin ()
@property(nonatomic) MMKV* defaultInstance;
@property(nonatomic) NSMutableDictionary<NSString*, MMKV*>* instances;
@end

@implementation FLTMmkvPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"mmkv"
            binaryMessenger:[registrar messenger]];
  FLTMmkvPlugin* instance = [[FLTMmkvPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"default" isEqualToString:call.method]) {
    if (_defaultInstance == NULL) {
      _defaultInstance = [MMKV defaultMMKV];
    }
    result(NULL);
  } else if ([@"withId" isEqualToString:call.method]) {
    _instances[call.arguments[@"id"]] = [MMKV mmkvWithID:call.arguments[@"id"]];
    result(NULL);
  } else if ([@"withCryptKey" isEqualToString:call.method]) {
    _instances[call.arguments[@"id"]] = [MMKV mmkvWithID:call.arguments[@"id"] cryptKey:call.arguments[@"cryptKey"]];
    result(NULL);
  } else {
    MMKV* inst = NULL;
    if ([(NSNumber*)call.arguments[@"default"] boolValue]) {
      inst = _defaultInstance;
    } else {
      inst = _instances[call.arguments[@"id"]];
    }
    if ([@"putBoolean" isEqualToString:call.method]) {
      [inst setBool:call.arguments[@"value"] forKey:call.arguments[@"key"]];
      result(NULL);
    } else if ([@"putLong" isEqualToString:call.method]) {
      [inst setInt64:[(NSNumber*)call.arguments[@"value"] longValue] forKey:call.arguments[@"key"]];
      result(NULL);
    } else if ([@"putString" isEqualToString:call.method]) {
      [inst setObject:call.arguments[@"value"] forKey:call.arguments[@"key"]];
      result(NULL);
    } else if ([@"putDouble" isEqualToString:call.method]) {
      [inst setDouble:[(NSNumber*)call.arguments[@"value"] doubleValue] forKey:call.arguments[@"key"]];
      result(NULL);
    } else if ([@"putBytes" isEqualToString:call.method]) {
      [inst setObject:call.arguments[@"value"] forKey:call.arguments[@"key"]];
      result(NULL);
    } else if ([@"getBoolean" isEqualToString:call.method]) {
      bool value = [inst getBoolForKey:call.arguments[@"key"] defaultValue:call.arguments[@"defaultValue"]];
      result([NSNumber numberWithBool:value]);
    } else if ([@"getLong" isEqualToString:call.method]) {
      long value = [inst getInt64ForKey:call.arguments[@"key"] defaultValue:[(NSNumber*)call.arguments[@"defaultValue"] longValue]];
      result([NSNumber numberWithLong:value]);
    } else if ([@"getString" isEqualToString:call.method]) {
      NSString* value = [inst getObjectOfClass:NSString.class forKey:call.arguments[@"key"]];
      result(value);
    } else if ([@"getDouble" isEqualToString:call.method]) {
      double value = [inst getDoubleForKey:call.arguments[@"key"] defaultValue:[(NSNumber*)call.arguments[@"defaultValue"] doubleValue]];
      result([NSNumber numberWithDouble:value]);
    } else if ([@"getBytes" isEqualToString:call.method]) {
      NSData* value = [inst getObjectOfClass:NSData.class forKey:call.arguments[@"key"]];
      result([FlutterStandardTypedData typedDataWithBytes:value]);
    } else if ([@"containsKey" isEqualToString:call.method]) {
      result([NSNumber numberWithBool:[inst containsKey:call.arguments[@"key"]]]);
    } else if ([@"remove" isEqualToString:call.method]) {
      [inst removeValueForKey:call.arguments[@"key"]];
      result(NULL);
    } else if ([@"clear" isEqualToString:call.method]) {
      [inst clearAll];
      result(NULL);
    } else if ([@"keys" isEqualToString:call.method]) {
      NSMutableArray<NSString*>* keys = [NSMutableArray arrayWithCapacity:[inst count]];
      [inst enumerateKeys:^(NSString *key, BOOL *stop) {
        [keys addObject:key];
      }];
      result(keys);
    } else if ([@"count" isEqualToString:call.method]) {
      result([NSNumber numberWithLong:[inst count]]);
    } else if ([@"removeValuesForKeys" isEqualToString:call.method]) {
      NSArray* keys = call.arguments[@"keys"];
      [inst removeValuesForKeys:keys];
      result(NULL);
    } else {
      result(FlutterMethodNotImplemented);
    }
  }
}

@end
