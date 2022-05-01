package com.example.projectem8.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.projectem8.Glide.SlidingAdapter;
import com.example.projectem8.R;

import java.util.ArrayList;

public class PageViewSlider extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_view);

        Intent intent = getIntent();
        ArrayList<String> urls = intent.getStringArrayListExtra("urls");

        Log.i("test", "Urls -get- (Glide) --->" + urls.size());

        ViewPager mPager = findViewById(R.id.vpager);
        mPager.setAdapter(new SlidingAdapter(this, urls));

    }
}