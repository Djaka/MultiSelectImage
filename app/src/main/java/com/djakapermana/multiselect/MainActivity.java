package com.djakapermana.multiselect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button buttonSelectImage;
    private ArrayList<Image> images = new ArrayList<>();
    public int REQUEST_CODE_PICKER;
    RecyclerView recImageViewer;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        buttonSelectImage = findViewById(R.id.btnSelectImage);
        recImageViewer = findViewById(R.id.recImage);

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
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
//        if (images == null){
//            return;
//        }

//        StringBuilder stringBuffer = new StringBuilder();
//        for (int i = 0, l = images.size(); i < l; i++) {
//            stringBuffer.append(images.get(i).getPath()).append("\n");
//        }
//        textView.setText(stringBuffer.toString());

        adapter = new ImageAdapter(images,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recImageViewer.setLayoutManager(linearLayoutManager);
        recImageViewer.setAdapter(adapter);
    }

    public void start(){
        ImagePicker.create(MainActivity.this)
                .folderMode(false)
                .limit(4) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .imageFullDirectory(Environment.getExternalStorageDirectory().getPath()) // can be full path
                .origin(images) // original selected images, used in multi mode
                .start(REQUEST_CODE_PICKER); // start image picker activity with request code
    }
}
