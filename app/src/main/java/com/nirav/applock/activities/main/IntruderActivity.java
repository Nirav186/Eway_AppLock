package com.nirav.applock.activities.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;

import com.nirav.applock.R;
import com.nirav.applock.adapters.ImageAdapter;
import com.nirav.applock.adapters.Intruder_Adapter;
import com.nirav.applock.utils.Constant;

import java.io.File;
import java.util.ArrayList;

public class IntruderActivity extends AppCompatActivity {

    ArrayList<String> imgList = new ArrayList<>();

    RecyclerView intruder_recycler;
    LinearLayout no_intruder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intruder);

        intruder_recycler = findViewById(R.id.intruder_recycler);
        no_intruder = findViewById(R.id.no_intruder);

        getImages();
//        setAdapter();
        if (imgList.size()==0) {
            no_intruder.setVisibility(View.VISIBLE);
            intruder_recycler.setVisibility(View.GONE);
        }else {
            setAdapter();
            no_intruder.setVisibility(View.GONE);
            intruder_recycler.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter() {
        ImageAdapter intruder_adapter = new ImageAdapter(IntruderActivity.this,
                imgList);
        intruder_recycler.setLayoutManager(new GridLayoutManager(IntruderActivity.this, 3,
                RecyclerView.VERTICAL, false));
        intruder_recycler.setAdapter(intruder_adapter);
    }

    private void getImages() {
        File file;
        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.Q){
            file = new File(Constant.home_path);
        }else {
            file = new File(Constant.intruder_path);
        }
        if (file.exists()) {
            if (file.list() != null) {
                imgList.clear();
                String[] names = file.list();
                for (String name : names) {
                    imgList.add(file.getPath() + "/" + name);
                }
            }
        }
    }


}