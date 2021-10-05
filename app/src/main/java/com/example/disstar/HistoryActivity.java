package com.example.disstar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class HistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView empty;
    private ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView=findViewById(R.id.history_videos_recycler);
        empty=findViewById(R.id.empty_history);
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),2));
        empty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        setData();

    }

    void setData()
    {
        progressBar = new ProgressDialog(HistoryActivity.this);
        progressBar.setMessage("Fetching data...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://star-4c25f-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference("records").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("history");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    HashSet<Integer> recommend=new HashSet<>();
                    for (DataSnapshot uniqueUserSnapshot : snapshot.getChildren()) {
                        recommend.add(((Long)uniqueUserSnapshot.getValue()).intValue());
                    }
                    ArrayList<Integer> temp=new ArrayList<>();
                    for(int i: recommend)
                        temp.add(i);

                    VideoListAdapter videoListAdapter=new VideoListAdapter(getBaseContext(),temp);
                    recyclerView.setAdapter(videoListAdapter);
                    empty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                else
                {
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                }
                progressBar.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("error",error.toString());
                progressBar.dismiss();

            }
        });

    }
}