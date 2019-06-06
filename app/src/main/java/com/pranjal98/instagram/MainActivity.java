package com.pranjal98.instagram;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private  FirebaseUser currentUser;

    public void newsFeed(){

        Intent feed = new Intent(MainActivity.this, NewsFeed.class);

        startActivity(feed);
        finish();
    }

    public void fbClked(View view){

        Toast.makeText(this, "Sorry! This service is not available till now!", Toast.LENGTH_SHORT).show();
    }

    public  void  signUp(View view){

        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }


    public void logIn(View view){

        EditText email = (EditText) findViewById(R.id.editId);

        EditText password = (EditText) findViewById(R.id.editIId);

        if(!email.getText().toString().equals("") && !password.getText().toString().equals("")){

            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(!task.isSuccessful())
                    {
                        Toast.makeText(MainActivity.this, "Failed to Signed in!", Toast.LENGTH_SHORT).show();
                    }

                    else {

                        newsFeed();

//                        Toast.makeText(MainActivity.this, "Successfully signed in!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        else {

            Toast.makeText(this, "Pls give the valid input!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();


        if(currentUser != null){

            newsFeed();

//            Toast.makeText(MainActivity.this, "Already Logged In !!", Toast.LENGTH_SHORT).show();
        }

        else {

//            Toast.makeText(MainActivity.this, "Pls Log in First !!", Toast.LENGTH_SHORT).show();
        }
    }
}
