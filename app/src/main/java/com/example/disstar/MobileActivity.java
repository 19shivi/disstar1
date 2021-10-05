package com.example.disstar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MobileActivity extends AppCompatActivity {
    private ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        Button button=findViewById(R.id.button_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText name,mobile;
                mobile=findViewById(R.id.mobile);
                name=findViewById(R.id.name_full);
                if(mobile.getText().length()!=10 || name.getText().length()==0)
                {
                    Toast.makeText(MobileActivity.this,"Please enter correct mobile / username ",Toast.LENGTH_LONG).show();
                }
                else
                    saveData( name.getText().toString(), mobile.getText().toString());
            }
        });
    }
    void saveData(String name,String mobile)
    {
        progressBar = new ProgressDialog(MobileActivity.this);
        progressBar.setMessage("Saving");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setphone(mobile);
            }
        });

    }
    void  setphone(String mobile)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://star-4c25f-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference("records").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mobile");
         myRef.setValue(mobile).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 Intent intent=new Intent(MobileActivity.this,HomeActivity.class);
                 progressBar.dismiss();
                 startActivity(intent);
                 finish();
             }
         });
    }

}