package com.nirav.applock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.nirav.applock.R;
import com.nirav.applock.activities.lock.GestureSelfUnlockActivity;
import com.nirav.applock.base.AppConstants;
import com.nirav.applock.prefs.PrefManager;

public class PowerConnectionReceiver extends BroadcastReceiver {

    public static MediaPlayer mediaPlayer;

    public PowerConnectionReceiver(Context context) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PrefManager manager = new PrefManager(context);
        if (manager.isChargeRemoval()) {
            mediaPlayer = MediaPlayer.create(context, R.raw.alarm1);
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                mediaPlayer.release();
                Toast.makeText(context, "The device is charging", Toast.LENGTH_SHORT).show();
            } else {
                intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);
                mediaPlayer.start();
                Toast.makeText(context, "The device is not charging", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, GestureSelfUnlockActivity.class);
                i.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_CHARGE_REMOVAL);
                context.startActivity(i);
            }
        }
    }
}