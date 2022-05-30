package com.nirav.applock.activities.main;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.nirav.applock.R;
import com.nirav.applock.activities.lock.GestureSelfUnlockActivity;
import com.nirav.applock.activities.pwd.CreatePwdActivity;
import com.nirav.applock.base.AppConstants;
import com.nirav.applock.prefs.PrefManager;
import com.nirav.applock.receiver.PowerConnectionReceiver;
import com.nirav.applock.utils.LockUtil;
import com.nirav.applock.utils.SpUtil;
import com.nirav.applock.utils.ToastUtil;
import com.nirav.applock.widget.DialogPermission;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout appLock,intruder;
    private Switch shakeMoveSwitch, batteryRemovalSwitch;
    private SensorManager sensorManager;
    private static final int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1;

    PowerConnectionReceiver receiver;

    Boolean isMainOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        appLock = findViewById(R.id.appLock);
        intruder = findViewById(R.id.intruder);
        shakeMoveSwitch = findViewById(R.id.shakeMoveSwitch);
        batteryRemovalSwitch = findViewById(R.id.batteryRemovalSwitch);

        askPermissions();

        PrefManager manager = new PrefManager(this);
        receiver = new PowerConnectionReceiver(HomeActivity.this);

        appLock.setOnClickListener(v -> {
            boolean isFirstLock = SpUtil.getInstance().getBoolean(AppConstants.LOCK_IS_FIRST_LOCK, true);
            if (isFirstLock) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    isMainOpen = true;
                    showDialog();
                }
            } else {
                Intent intent = new Intent(HomeActivity.this, GestureSelfUnlockActivity.class);
                intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME);
                intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY);
                startActivity(intent);
            }
        });

        intruder.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,IntruderActivity.class);
            startActivity(intent);
        });

        batteryRemovalSwitch.setChecked(manager.isChargeRemoval());

        batteryRemovalSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                manager.setChargeRemoval(true);
                IntentFilter ifilter = new IntentFilter();
                ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
                ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
                registerReceiver(receiver, ifilter);
            } else {
                manager.setChargeRemoval(false);
                if(PowerConnectionReceiver.mediaPlayer!=null){
                    PowerConnectionReceiver.mediaPlayer.release();
                }
                try {
                    unregisterReceiver(receiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void askPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                boolean isFirstLock = SpUtil.getInstance().getBoolean(AppConstants.LOCK_IS_FIRST_LOCK, true);
                if (isFirstLock) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showDialog();
                    }
                }
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 150);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showDialog() {
        // If you do not have access to view usage rights and the phone exists to view usage this interface
        if (!LockUtil.isStatAccessPermissionSet(HomeActivity.this) && LockUtil.isNoOption(HomeActivity.this)) {
            DialogPermission dialog = new DialogPermission(HomeActivity.this);
            dialog.show();
            dialog.setOnClickListener(new DialogPermission.onClickListener() {
                @Override
                public void onClick() {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent = null;
                        intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivityForResult(intent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                    }
                }
            });
        } else {
            gotoCreatePwdActivity();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ACTION_USAGE_ACCESS_SETTINGS) {
            if (LockUtil.isStatAccessPermissionSet(HomeActivity.this)) {
                gotoCreatePwdActivity();
            } else {
                ToastUtil.showToast("Permission denied");
                finish();
            }
        } else if (requestCode == 101) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    gotoCreatePwdActivity();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case 100:
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        boolean isFirstLock = SpUtil.getInstance().getBoolean(AppConstants.LOCK_IS_FIRST_LOCK, true);
                        if (isFirstLock) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                showDialog();
                            }
                        }
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA}, 150);
                    }
                    break;
                case 150:
                    boolean isFirstLock = SpUtil.getInstance().getBoolean(AppConstants.LOCK_IS_FIRST_LOCK, true);
                    if (isFirstLock) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            showDialog();
                        }
                    }
                    break;
            }
        } else {
            switch (requestCode) {
                case 100:
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                    break;
                case 150:
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA}, 150);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void gotoCreatePwdActivity() {
        if (Settings.canDrawOverlays(this)) {
            Intent intent2 = new Intent(HomeActivity.this, CreatePwdActivity.class);
            if (!isMainOpen){
                intent2.putExtra("isMainOpen",false);
            }
            startActivity(intent2);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            checkDrawOverlayPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        Log.v("App", "Package Name: " + getApplicationContext().getPackageName());

        // Check if we already  have permission to draw over other apps
        if (!Settings.canDrawOverlays(this)) {
            Log.v("App", "Requesting Permission" + Settings.canDrawOverlays(this));
            // if not construct intent to request permission
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getApplicationContext().getPackageName()));
            // request permission via start activity for result
            startActivityForResult(intent, 101); //It will call onActivityResult Function After you
            // press Yes/No and go Back after giving permission
        } else {
            Log.v("App", "We already have permission for it.");
            // disablePullNotificationTouch();
            // Do your stuff, we got permission captain
        }
    }
}