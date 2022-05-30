package com.nirav.applock.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nirav.applock.R
import com.nirav.applock.adapters.ViewPagerAdapter
import com.nirav.applock.model.SavedImages
import kotlinx.android.synthetic.main.activity_image_show.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ImageShowActivity : AppCompatActivity() {

    private var images:ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_show)

        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onImagesGet(event: SavedImages) {
        images = event.images
        var mViewPagerAdapter = ViewPagerAdapter(this@ImageShowActivity, images)
        vpImage.adapter = mViewPagerAdapter
        vpImage.setCurrentItem(event.pos,false)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}