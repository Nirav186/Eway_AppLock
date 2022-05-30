package com.nirav.applock.activities.lock;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.nirav.applock.R;
import com.nirav.applock.activities.main.MainActivity;
import com.nirav.applock.activities.setting.LockSettingActivity;
import com.nirav.applock.base.AppConstants;
import com.nirav.applock.base.BaseActivity;
import com.nirav.applock.db.CommLockInfoManager;
import com.nirav.applock.receiver.PowerConnectionReceiver;
import com.nirav.applock.utils.LockPatternUtils;
import com.nirav.applock.utils.SpUtil;
import com.nirav.applock.utils.SystemBarHelper;
import com.nirav.applock.widget.LockPatternView;
import com.nirav.applock.widget.LockPatternViewPattern;

import java.util.List;

public class GestureSelfUnlockActivity extends BaseActivity {

    private LockPatternView mLockPatternView;
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;
    private String actionFrom;
    private String pkgName;
    private CommLockInfoManager mManager;
    private LinearLayout mTopLayout;

    private TextureView mTextureView;
    @NonNull
    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_gesture_self_unlock;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mLockPatternView = findViewById(R.id.unlock_lock_view);
        mTopLayout = findViewById(R.id.top_layout);
        mTextureView = findViewById(R.id.texture_view);
        mTopLayout.setPadding(0, SystemBarHelper.getStatusBarHeight(this), 0, 0);
    }

    @Override
    protected void initData() {
        mManager = new CommLockInfoManager(this);
        pkgName = getIntent().getStringExtra(AppConstants.LOCK_PACKAGE_NAME);
        actionFrom = getIntent().getStringExtra(AppConstants.LOCK_FROM);
        initLockPatternView();
    }

    private void initLockPatternView() {
//        mLockPatternView.setLineColorRight(ContextCompat.getColor(this,R.color.appBarColor));
        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(pattern -> {
            if (mLockPatternUtils.checkPattern(pattern)) {
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                switch (actionFrom) {
                    case AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY:
                        Intent intent = new Intent(GestureSelfUnlockActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case AppConstants.LOCK_FROM_FINISH:
                        mManager.unlockCommApplication(pkgName);
                        finish();
                        break;
                    case AppConstants.LOCK_FROM_SETTING:
                        startActivity(new Intent(GestureSelfUnlockActivity.this, LockSettingActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case AppConstants.LOCK_FROM_UNLOCK:
                        mManager.setIsUnLockThisApp(pkgName, true);
                        mManager.unlockCommApplication(pkgName);
                        sendBroadcast(new Intent(GestureUnlockActivity.FINISH_UNLOCK_THIS_APP));
                        finish();
                        break;
                    case AppConstants.LOCK_FROM_CHARGE_REMOVAL:
                        PowerConnectionReceiver.mediaPlayer.release();
                        finish();
                        break;
                    case AppConstants.LOCK_FROM_LOCK_HOME_ACITVITY:
                        finish();
                        break;
                }
            } else {
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                    mFailedPatternAttemptsSinceLastTimeout++;
                    int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
                    if (retry >= 0) {
                    }
                } else {
                }
                if (mFailedPatternAttemptsSinceLastTimeout >= 3) {
                    if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_RECORD_PIC, false)) {

                    }
                }
                if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) { //The number of failures is greater than the maximum number of incorrect attempts before blocking the use

                } else {
                    mLockPatternView.postDelayed(mClearPatternRunnable, 500);
                }
            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
    }

    @Override
    protected void initAction() {

    }


}
