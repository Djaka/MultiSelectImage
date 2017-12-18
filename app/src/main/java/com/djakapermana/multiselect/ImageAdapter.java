package com.djakapermana.multiselect;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.model.Image;

import java.util.ArrayList;

/**
 * Created by Djaka on 16/12/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    ArrayList<Image> images;
    MainActivity activity;

    public ImageAdapter(ArrayList<Image> images) {
        this.images = images;
    }

    public ImageAdapter(ArrayList<Image> images, MainActivity activity) {
        this.images = images;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Image image = images.get(position);
        final String imagePath = image.getPath();

        Glide.with(activity)
                .load(imagePath)
                .into(holder.imageView);

        holder.imageRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                images.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,images.size());
                Toast.makeText(activity, "Remove: " + imagePath, Toast.LENGTH_SHORT).show();
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ZoomImageActivity.class);
                intent.putExtra(ZoomImageActivity.IMAGE_EXTRA, imagePath);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageRemove, imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgSelected);
            imageRemove = itemView.findViewById(R.id.icDelete);

        }
    }
}
