package com.nirav.applock.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirav.applock.R;
import com.nirav.applock.db.CommLockInfoManager;
import com.nirav.applock.model.CommLockInfo;

import java.util.ArrayList;
import java.util.List;

public class MainAdapterTemp extends RecyclerView.Adapter<MainAdapterTemp.MainViewHolder> {

    @NonNull
    private List<CommLockInfo> mLockInfos = new ArrayList<>();

    private Context mContext;

    private PackageManager packageManager;
    private CommLockInfoManager mLockInfoManager;

    public MainAdapterTemp(Context mContext) {
         this.mContext = mContext;
        packageManager = mContext.getPackageManager();
        mLockInfoManager = new CommLockInfoManager(mContext);
    }

    public void setLockInfos(@NonNull List<CommLockInfo> lockInfos) {
        mLockInfos.clear();
        mLockInfos.addAll(lockInfos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MainAdapterTemp.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.temp, parent, false);
        return new MainAdapterTemp.MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainAdapterTemp.MainViewHolder holder, final int position) {
        final CommLockInfo lockInfo = mLockInfos.get(position);
        holder.mAppName.setText(packageManager.getApplicationLabel(lockInfo.getAppInfo()));
        if (lockInfo.isLocked()) {
            holder.mLockedImageView.setImageResource(R.drawable.ic_lock);
            holder.mLockUnLockText.setText(mContext.getString(R.string.text_locked));
        } else {
            holder.mLockedImageView.setImageResource(R.drawable.ic_unlock);
            holder.mLockUnLockText.setText(mContext.getString(R.string.text_unlocked));
        }
        ApplicationInfo appInfo = lockInfo.getAppInfo();
        holder.mAppIcon.setImageDrawable(packageManager.getApplicationIcon(appInfo));
        holder.mLockedImageView.setOnClickListener(view -> {
            if (lockInfo.isLocked()) {
                lockInfo.setLocked(false);
                mLockInfoManager.unlockCommApplication(lockInfo.getPackageName());
            } else {
                lockInfo.setLocked(true);
                mLockInfoManager.lockCommApplication(lockInfo.getPackageName());
            }
            notifyItemChanged(position);
        });
    }


    private void initData(TextView tvAppName, CheckBox switchCompat, ImageView mAppIcon, CommLockInfo lockInfo) {
        tvAppName.setText(packageManager.getApplicationLabel(lockInfo.getAppInfo()));
        switchCompat.setChecked(lockInfo.isLocked());
        ApplicationInfo appInfo = lockInfo.getAppInfo();
        mAppIcon.setImageDrawable(packageManager.getApplicationIcon(appInfo));
    }

    public void changeItemLockStatus(@NonNull CheckBox checkBox, @NonNull CommLockInfo info, int position) {
        if (checkBox.isChecked()) {
            info.setLocked(true);
            mLockInfoManager.lockCommApplication(info.getPackageName());
        } else {
            info.setLocked(false);
            mLockInfoManager.unlockCommApplication(info.getPackageName());
        }
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return mLockInfos.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        private ImageView mAppIcon;
        private TextView mAppName;
        private TextView mLockUnLockText;
        private ImageView mLockedImageView;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            mAppIcon = itemView.findViewById(R.id.appIcon);
            mAppName = itemView.findViewById(R.id.appName);
            mLockUnLockText = itemView.findViewById(R.id.tvLockUnlock);
            mLockedImageView = itemView.findViewById(R.id.iconLockUnlock);
        }
    }
}
