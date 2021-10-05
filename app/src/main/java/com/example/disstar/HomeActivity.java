package com.example.disstar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    CarouselView carouselView;
    ArrayList<Integer> recommend=new ArrayList<Integer>();
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public ImageButton appbar;
    public TextView pointsView;

   public TextView textView;
    public RecyclerView top_video,new_video,history_video;
    private VideoListAdapter top_video_adapter,new_video_adapter,history_video_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recommend.add(R.raw.a);
        recommend.add(R.raw.b);
        recommend.add(R.raw.c);
        recommend.add(R.raw.d);
        carouselView=findViewById(R.id.carouselView);
        carouselView.setPageCount(recommend.size());
        carouselView.setImageListener(imageListener);
        drawerLayout=findViewById(R.id.drawer);
        appbar=findViewById(R.id.imageButton);
        top_video=findViewById(R.id.top_videos_recycler);
        new_video=findViewById(R.id.new_videos_recycler);
        history_video=findViewById(R.id.videos_recycler);
        top_video_adapter=new VideoListAdapter(this,recommend);
        new_video_adapter=new VideoListAdapter(this,recommend);
        history_video_adapter=new VideoListAdapter(this,recommend);
        top_video.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.HORIZONTAL,false));
        new_video.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.HORIZONTAL,false));
        history_video.setLayoutManager(new GridLayoutManager(getBaseContext(),3));
         top_video.setAdapter(top_video_adapter);
         new_video.setAdapter(new_video_adapter);
         history_video.setAdapter(history_video_adapter);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        View headerView = navigationView.getHeaderView(0);
         textView=headerView.findViewById(R.id.profileName);
        pointsView=headerView.findViewById(R.id.point);
        appbar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.history)
                {
                    Intent intent=new Intent(HomeActivity.this,HistoryActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });



    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(HomeActivity.this).asBitmap().load("android.resource://"+getPackageName()+"/"+recommend.get(position)).centerCrop().into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(HomeActivity.this,VideoPlayingActivity.class);
                    intent.putExtra("path",recommend.get(position));
                    startActivity(intent);

                }
            });
        }
    };
    void fetchPoint()
    {

       textView.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://star-4c25f-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference("records").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("point");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long points=(long)snapshot.getValue();
                 pointsView.setText(String.valueOf(points));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("error",error.toString());

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchPoint();
    }
}