package com.nirav.applock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.nirav.applock.base.AppConstants;
import com.nirav.applock.services.BackgroundManager;
import com.nirav.applock.services.LoadAppListService;
import com.nirav.applock.services.LockService;
import com.nirav.applock.utils.LogUtil;
import com.nirav.applock.utils.SpUtil;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(@NonNull Context context, Intent intent) {
        LogUtil.i("Boot service....");
        Toast.makeText(context, "gjfjfkfggkh", Toast.LENGTH_SHORT).show();
        //TODO: 11 compatable done
       // context.startService(new Intent(context, LoadAppListService.class));
        BackgroundManager.getInstance().init(context).startService(LoadAppListService.class);
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE, false)) {
            BackgroundManager.getInstance().init(context).startService(LockService.class);
            BackgroundManager.getInstance().init(context).startAlarmManager();
        }
    }
}
