package com.example.disstar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {


    private TextInputEditText userId,email,password;
    private  MaterialButton register,login;
    private FirebaseAuth firebaseAuth;
    private  TextView error;
    private ProgressDialog progressBar;
    private  Editable emailText,userIdText,passwardText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userId=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.button_register);
        login=findViewById(R.id.button_login);
        error=findViewById(R.id.error);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailText = email.getText();
                 passwardText = password.getText();
                   userIdText=userId.getText();

               if (passwardText.length() < 8) {
                    error.setText("Passward must have atleast 8 characters");
                }
                else if(userIdText.length()<2|| userIdText.toString().matches("\\d"))
                {
                    error.setText("User Id is Invalid");
                }
                else {
                    progressBar = new ProgressDialog(v.getContext());
                    progressBar.setMessage("Signing Up");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.show();
                    register();

                }

            }
        });

    }










    void register()
    {

            FirebaseDatabase database = FirebaseDatabase.getInstance("https://star-4c25f-default-rtdb.firebaseio.com/");
            DatabaseReference myRef = database.getReference("users");
            Log.v("hello","ref");
            myRef.orderByChild("userId").equalTo(userIdText.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        error.setText("UserId Already Taken");
                        progressBar.dismiss();
                    } else
                        registerUser(emailText, passwardText, userIdText);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.v("error",error.toString());

                }
            });


    }
    void registerUser(Editable username,Editable passward,Editable UserId)
    {
        Log.v("hello","shivam");
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(username.toString(), passward.toString())
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {


                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    try {
                                        throw task.getException();
                                    }
                                    // if user enters wrong email.
                                    catch (FirebaseAuthWeakPasswordException weakPassword) {

                                        progressBar.dismiss();
                                        error.setText("Weak Passward");
                                        // TODO: take your actions!
                                    }
                                    // if user enters wrong password.
                                    catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                        progressBar.dismiss();
                                        error.setText("Invalid E-mail ");

                                        // TODO: Take your action
                                    } catch (FirebaseAuthUserCollisionException existEmail) {
                                        progressBar.dismiss();
                                        error.setText("E-mail already Exist");
                                    } catch (Exception e) {
                                        progressBar.dismiss();
                                        error.setText(e.getMessage());

                                    }
                                } else {

                                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://star-4c25f-default-rtdb.firebaseio.com/");
                                    DatabaseReference myRef = database.getReference("users");
                                    User user=new User(emailText.toString(),userIdText.toString());

                                    myRef.push().setValue(user);
                                    progressBar.dismiss();
                                    FirebaseUser userr=FirebaseAuth.getInstance().getCurrentUser();
                                    database.getReference("records").child(userr.getUid()).child("point").push().setValue(0);
                                    userr.sendEmailVerification();
                                    firebaseAuth.signOut();
                                    Toast.makeText(getBaseContext(),"Email sent for verification",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        }
                );
    }

}