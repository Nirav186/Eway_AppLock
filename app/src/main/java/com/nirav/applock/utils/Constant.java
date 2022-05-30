package com.nirav.applock.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.nirav.applock.services.CamerSelfiService;

public class Constant {

    public static boolean isFirstTime = true;

    public static String home_path =
            (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Eway");

    public static String intruder_path =
            (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/.Eway/Intruder");


    public static void StartCameraService(Activity activity) {
        if (activity == null) {
            return;
        }

        if (!activity.isFinishing()) {
            Intent front_translucent = new Intent(activity, CamerSelfiService.class);
            front_translucent.putExtra("Front_Request", true);
            front_translucent.putExtra("Quality_Mode", 0);
            activity.startService(front_translucent);
        } else {
            Log.e("CameraService", "No Start");
        }
    }
}
