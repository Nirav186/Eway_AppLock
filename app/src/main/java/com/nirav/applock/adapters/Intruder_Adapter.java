package com.nirav.applock.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirav.applock.R;
import com.nirav.applock.activities.main.ImageShowActivity;
import com.nirav.applock.model.SavedImages;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class Intruder_Adapter extends RecyclerView.Adapter<Intruder_Adapter.ViewHolder> {
    Activity activity;
    ArrayList<String> media_datas;
    LayoutInflater inflater;

    public Intruder_Adapter(Activity activity, ArrayList<String> media_datas) {
        this.activity = activity;
        this.media_datas = media_datas;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        int margin = activity.getResources().getDimensionPixelSize(R.dimen._8sdp);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) activity).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int deviceheight = (displaymetrics.widthPixels - margin) / 3;
        holder.images_set.getLayoutParams().height = deviceheight;
        Glide.with(activity).load(media_datas.get(position)).into(holder.images_set);
        holder.images_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, ImageShowActivity.class));
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    SavedImages savedImages = new SavedImages();
                    savedImages.setImages(media_datas);
                    savedImages.setPos(holder.getAdapterPosition());
                    EventBus.getDefault().post(savedImages);
                }, 100);
            }
        });
    }

    @Override
    public int getItemCount() {
        return media_datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images_set;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            images_set = (ImageView) itemView.findViewById(R.id.imaegs);
        }
    }
}
