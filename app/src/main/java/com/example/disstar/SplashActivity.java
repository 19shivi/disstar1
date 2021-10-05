package com.example.disstar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

                new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()==null ||FirebaseAuth.getInstance().getCurrentUser().getDisplayName().isEmpty())
                    intent=new Intent(SplashActivity.this,MobileActivity.class);
                else
                intent=new Intent(SplashActivity.this,HomeActivity.class);

               startActivity(intent);
                finish();
            }
        },3000);

    }
}