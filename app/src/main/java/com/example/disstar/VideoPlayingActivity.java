package com.example.disstar;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class VideoPlayingActivity extends AppCompatActivity {
    ArrayList<Integer> temp=new ArrayList<>();
    int currentVideo=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playing);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        MaterialCardView materialCardView=findViewById(R.id.materialCard);
        VideoView videoView=findViewById(R.id.video_view);
        MediaController mediaController = new MediaController(this);
         mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        currentVideo=getIntent().getIntExtra("path",0);
        String videopath = "android.resource://"+getPackageName()+"/"+currentVideo;
        videoView.setVideoPath(videopath);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(VideoPlayingActivity.this,"Video completed",Toast.LENGTH_LONG).show();
                materialCardView.setVisibility(View.VISIBLE);
                if(!temp.contains(currentVideo))
                incrementPoint();
            }
        });

        ArrayList<Integer> recommend=new ArrayList<Integer>();
        recommend.add(R.raw.a);
        recommend.add(R.raw.b);
        recommend.add(R.raw.c);
        recommend.add(R.raw.d);
        recommend.add(R.raw.e);
        recommend.remove(Integer.valueOf(currentVideo));
        RecommendationAdapter recommendationAdapter=new RecommendationAdapter(this,recommend);
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new  LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                materialCardView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(recommendationAdapter);
            }
        },10000);
        ImageView cancelRecommend=findViewById(R.id.cancel);
        cancelRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialCardView.setVisibility(View.GONE);
            }
        });
        fetchWatchedVideo();
    }
    void incrementPoint()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://star-4c25f-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference("records").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("point");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long points=(long)snapshot.getValue();

                    points+=100;
                    myRef.setValue(points);
                    Toast.makeText(VideoPlayingActivity.this,"Congrats You have got 100 point",Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("error",error.toString());

            }
        });
        database.getReference("records").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("history").push().setValue(getIntent().getIntExtra("path",0));

    }
    void fetchWatchedVideo()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://star-4c25f-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference("records").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("history");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    HashSet<Integer> recommend = new HashSet<>();
                    for (DataSnapshot uniqueUserSnapshot : snapshot.getChildren()) {
                        recommend.add(((Long) uniqueUserSnapshot.getValue()).intValue());
                    }
                  temp= new ArrayList<>();
                    for (int i : recommend)
                        temp.add(i);

                   }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("error",error.toString());


            }
        });
    }



}