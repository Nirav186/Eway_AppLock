package com.nirav.applock.mvp.contract;

import android.content.Context;

import com.nirav.applock.base.BasePresenter;
import com.nirav.applock.base.BaseView;
import com.nirav.applock.model.CommLockInfo;

import java.util.List;

public interface MainContract {
    interface View extends BaseView<Presenter> {
        void loadAppInfoSuccess(List<CommLockInfo> list);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context, boolean isSort);

        void loadLockAppInfo(Context context);

        void onDestroy();
    }
}
