package com.nirav.applock.adapters

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nirav.applock.R
import com.nirav.applock.activities.main.ImageShowActivity
import com.nirav.applock.model.SavedImages
import org.greenrobot.eventbus.EventBus
import java.util.*


class ImageAdapter(private var activity: Activity, private var imgList: ArrayList<String>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private var inflater: LayoutInflater? = LayoutInflater.from(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater?.inflate(R.layout.item_img, parent, false)!!
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val margin = activity.resources.getDimensionPixelSize(R.dimen._8sdp)
        val displaymetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
        val deviceheight = (displaymetrics.widthPixels - margin) / 3
        holder.rcImg.getLayoutParams().height = deviceheight
        Glide.with(activity).load(imgList[position]).override(1080, 600)
            .into(holder.rcImg)

        holder.rcImg.setOnClickListener {
            activity.startActivity(Intent(activity, ImageShowActivity::class.java))
            Handler(Looper.getMainLooper()).postDelayed({
                var savedImages = SavedImages()
                savedImages.images = imgList
                savedImages.pos  = holder.adapterPosition
                EventBus.getDefault().post(
                    savedImages
                )
            },100)
        }
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rcImg: ImageView = itemView.findViewById(R.id.rcImg)
    }
}