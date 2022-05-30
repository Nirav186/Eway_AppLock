package com.nirav.applock.mvp.contract;

import android.content.Context;

import com.nirav.applock.base.BasePresenter;
import com.nirav.applock.base.BaseView;
import com.nirav.applock.model.CommLockInfo;
import com.nirav.applock.mvp.p.LockMainPresenter;

import java.util.List;

public interface LockMainContract {
    interface View extends BaseView<Presenter> {

        void loadAppInfoSuccess(List<CommLockInfo> list);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context);

        void searchAppInfo(String search, LockMainPresenter.ISearchResultListener listener);

        void onDestroy();
    }
}
