package com.example.disstar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
private ArrayList<Integer> paths;
private Context context;
public VideoListAdapter(Context context,ArrayList<Integer> paths)
        {
        this.context=context;
        this.paths=paths;
        }


@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.video_list, parent, false);
        return new ViewHolder(listItem);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).asBitmap().load("android.resource://"+context.getPackageName()+"/"+paths.get(position)).centerCrop().into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        Intent intent=new Intent(context,VideoPlayingActivity.class);
        intent.putExtra("path",paths.get(position));
        context.startActivity(intent);

        }
        });

        }



@Override
public int getItemCount() {
        return paths.size();
        }
public static class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView img;
    public ViewHolder(View itemView) {
        super(itemView);
        this.img=itemView.findViewById(R.id.video_image);

    }
}
}
