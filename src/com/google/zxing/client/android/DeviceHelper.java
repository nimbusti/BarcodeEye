package com.google.zxing.client.android;

import android.util.Log;

public class DeviceHelper {
  
  private static final String TAG = DeviceHelper.class.getSimpleName();

  private DeviceHelper() {
  }
  
  public static boolean isGoogleGlass() {
    Log.i(TAG, "BOARD=" + android.os.Build.BOARD);//...
    return android.os.Build.MODEL.contains("Glass");
  }
}
