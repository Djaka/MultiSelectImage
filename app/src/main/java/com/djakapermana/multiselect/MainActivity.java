package com.djakapermana.multiselect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button buttonSelectImage, buttonSaveImage;
    private ArrayList<Image> images = new ArrayList<>();
    public int REQUEST_CODE_PICKER;
    RecyclerView recImageViewer;
    ImageAdapter adapter;
    private static String EXTRA_IMAGE = "extra_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        buttonSelectImage = findViewById(R.id.btnSelectImage);
        buttonSaveImage = findViewById(R.id.btnSave);
        recImageViewer = findViewById(R.id.recImage);

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

        buttonSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
            }
        });

        if(savedInstanceState != null){
            images = savedInstanceState.getParcelableArrayList(EXTRA_IMAGE);
            printImages(images);
        }

        loadImage();
    }

    private void saveImage() {
        File imagedir = new File(Environment.getExternalStorageDirectory() + "/OFU");
        File thumbnailsPath = new File(imagedir + "/thumbnails/");
        if (!imagedir.exists()) {
            File pathImage = new File("/sdcard/OFU/");
            pathImage.mkdirs();

            thumbnailsPath = new File(pathImage+"/thumbnails/");
            thumbnailsPath.mkdir();

        }

        for(Image img: images){
            Tblimage tblimage = new Tblimage();

            Bitmap bitmap = BitmapFactory.decodeFile(img.getPath());
            Bitmap bitmap1 = scaleDown(bitmap,false);
            Bitmap bitmap2 = createThumbnails(bitmap, true);

            String UUID = java.util.UUID.randomUUID().toString();

            File fileImg = new File(imagedir,UUID+".jpeg");
            File fileThb = new File(thumbnailsPath, UUID+".jpeg");

            OutputStream outImage = null, outThb = null;

            try {
                outImage = new FileOutputStream(fileImg);
                outThb = new FileOutputStream(fileThb);
                bitmap1.compress(Bitmap.CompressFormat.JPEG,50,outImage);
                outImage.flush();
                outImage.close();

                bitmap2.compress(Bitmap.CompressFormat.JPEG,50,outThb);
                outThb.flush();
                outThb.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            tblimage.setRes(UUID + ".jpeg");
            tblimage.save();
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, boolean filter) {

        final int maxSize = 1024;
        int width;
        int height;
        int inWidth = realImage.getWidth();
        int inHeight = realImage.getHeight();
        if(inWidth > inHeight){
            width = maxSize;
            height = (inHeight * maxSize) / inWidth;
        } else {
            height = maxSize;
            width = (inWidth * maxSize) / inHeight;
        }


        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }

    public static Bitmap createThumbnails(Bitmap realImage, boolean filter) {

        final int maxSize = 500;
        int width;
        int height;

        int inWidth = realImage.getWidth();
        int inHeight = realImage.getHeight();
        if(inWidth > inHeight){
            width = maxSize;
            height = (inHeight * maxSize) / inWidth;
        } else {
            height = maxSize;
            width = (inWidth * maxSize) / inHeight;
        }

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }

    private void loadImage(){
        List<Tblimage> tblimage = Tblimage.listAll(Tblimage.class);

        for(int i = 0; i<tblimage.size(); i++){
            Log.d("Djaka", "loadImage: " + tblimage.get(i).getRes());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            images = (ArrayList<Image>) ImagePicker.getImages(data);
            printImages(images);
        }
    }

    private void printImages(ArrayList<Image> images) {

        adapter = new ImageAdapter(images,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recImageViewer.setLayoutManager(linearLayoutManager);
        recImageViewer.setAdapter(adapter);
    }

    public void start(){
        ImagePicker picker = ImagePicker.create(this);
        picker.imageDirectory(Environment.getExternalStorageDirectory() + "/OFU");
        picker.folderMode(true);
        picker.showCamera(true);
        picker.limit(4);
        picker.origin(images);
        picker.imageFullDirectory(Environment.getExternalStorageDirectory().getPath());
        picker.start(REQUEST_CODE_PICKER);

//        ImagePicker.create(MainActivity.this)
//                .folderMode(false)
//                .limit(4) // max images can be selected (99 by default)
//                .showCamera(true) // show camera or not (true by default)
//                .imageDirectory("Pictures")   // captured image directory name ("Camera" folder by default)
//                .imageFullDirectory(Environment.getExternalStorageDirectory().getPath()) // can be full path
//                .origin(images) // original selected images, used in multi mode
//                .start(REQUEST_CODE_PICKER); // start image picker activity with request code
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_IMAGE,images);
    }
}
