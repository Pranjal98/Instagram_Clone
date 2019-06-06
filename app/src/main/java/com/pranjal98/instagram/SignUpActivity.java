package com.pranjal98.instagram;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef, youRef;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private EditText password,email,fname;

    private String UID;

    private Uri selectImg, imgUri;

    public void addImg(){

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        else {

            intentFunc();
        }
    }

    public void addDp(View view){

        addImg();
    }

    public void intentFunc(){

        Intent dp = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(dp, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                intentFunc();
            }
        }
    }

    public void logInClk(View view){

        Intent backT0LogIn = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backT0LogIn);
        finish();
    }

    public void signUp(View view){

        fname = findViewById(R.id.userNameSignUp);
        email = findViewById(R.id.emailSignUp);
        password = findViewById(R.id.passWordSingUp);

        if(!fname.getText().toString().equals("") && !email.getText().toString().equals("") && !password.getText().toString().equals("")){

            if(password.getText().toString().length() >= 8){

                if(selectImg != null){

                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {

                                UID = mAuth.getUid();
                                database = FirebaseDatabase.getInstance();
                                myRef = database.getReference("Users").child(UID);

                                Map<String, Object> userDesc = new HashMap<>();

                                userDesc.put("Key", UID);
                                userDesc.put("UserName", fname.getText().toString());
                                userDesc.put("Post", "0");
                                userDesc.put("Followers", "0");
                                userDesc.put("Following", "0");

                                myRef.setValue(userDesc);

                                youRef = myRef.child("Followings").child(UID);
                                youRef.child("ID").setValue(UID);
                                youRef.child("Name").setValue(fname.getText().toString());

                                StorageReference ref = storageReference.child("images/"+ UID +"/dp/");
                                ref.putFile(selectImg)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                imgUri = taskSnapshot.getDownloadUrl();

                                                myRef.child("dp_url").setValue(imgUri.toString());

                                                Intent feed = new Intent(SignUpActivity.this, NewsFeed.class);
                                                startActivity(feed);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(getApplicationContext(), "Failed in Uploading Dp !", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(SignUpActivity.this, "Error in Creating a new user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                else {

                    Toast.makeText(this, "You haven't choose your phofile pic yet! pls choose it first!", Toast.LENGTH_SHORT).show();
                }
            }

            else {

                Toast.makeText(this, "Password must be contain 8 characters!", Toast.LENGTH_SHORT).show();
            }
        }

        else {

            Toast.makeText(this, "Enter a valid input First !!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode== RESULT_OK && data != null){

            selectImg = data.getData();

        }
    }
}

