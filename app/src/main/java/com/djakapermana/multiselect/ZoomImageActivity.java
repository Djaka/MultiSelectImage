package com.djakapermana.multiselect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.model.Image;
import com.github.chrisbanes.photoview.PhotoView;

public class ZoomImageActivity extends AppCompatActivity {

    PhotoView photoView;
    public static String IMAGE_EXTRA = "image_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        photoView = findViewById(R.id.zoomImage);

        String imagePath = getIntent().getStringExtra(IMAGE_EXTRA);

        Glide.with(this)
                .load(imagePath)
                .into(photoView);

    }
}
