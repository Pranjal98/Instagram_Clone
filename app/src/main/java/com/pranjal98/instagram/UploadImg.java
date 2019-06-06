package com.pranjal98.instagram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class UploadImg extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase database, db;
    private DatabaseReference myRef, youRef, nameRef, imgRef, postRef;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private String UID, userName, PostID, dp_url, likeCount, postCount, uuid, UserID, uploadHour, uploadMin, uploadSec, uploadDate;

    private Uri filePath, imgUri;

    public void backToNewsFeed(){

        Intent back = new Intent(getApplicationContext(), NewsFeed.class);
        startActivity(back);
        finish();
    }

    public void uploaded(View view){

        UID = firebaseAuth.getUid();
        uuid = UUID.randomUUID().toString();


        database = FirebaseDatabase.getInstance();
        db = FirebaseDatabase.getInstance();
        myRef = database.getReference("PostUpload").child(UID).child(uuid);

        youRef = db.getReference("Users").child(UID);

        nameRef = youRef.child("UserName");
        imgRef = youRef.child("dp_url");
        postRef = youRef.child("Post");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final EditText caption = findViewById(R.id.caption);

        Calendar calendar = Calendar.getInstance();

        uploadHour = new SimpleDateFormat("HH").format(calendar.getTime());
        uploadMin = new SimpleDateFormat("mm").format(calendar.getTime());
        uploadSec = new SimpleDateFormat("ss").format(calendar.getTime());

        uploadDate = DateFormat.getDateInstance().format(calendar.getTime());

        if(filePath != null)
        {

            postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    postCount = dataSnapshot.getValue(String.class);

                    int pCount = Integer.parseInt(postCount);
                    pCount++;

                    postCount = Integer.toString(pCount);

                    postRef.setValue(postCount);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    userName = dataSnapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dp_url = dataSnapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            StorageReference ref = storageReference.child("images/"+ UID +"/posts/" + uuid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imgUri = taskSnapshot.getDownloadUrl();

                            myRef.child("UserID").setValue(UID);
                            myRef.child("PostID").setValue(uuid);
                            myRef.child("dp_url").setValue(dp_url);
                            myRef.child("userName").setValue(userName);
                            myRef.child("descUri").setValue(imgUri.toString());
                            myRef.child("desc").setValue(caption.getText().toString());
                            myRef.child("likeCount").setValue("0");
                            myRef.child("uploadHour").setValue(uploadHour);
                            myRef.child("uploadMin").setValue(uploadMin);
                            myRef.child("uploadSec").setValue(uploadSec);
                            myRef.child("uploadDate").setValue(uploadDate);

                            Post post = new Post(UserID, PostID, dp_url, userName, imgUri.toString(), caption.getText().toString(), likeCount, uploadHour, uploadMin, uploadSec, uploadDate);

                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                            backToNewsFeed();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            backToNewsFeed();
                        }
                    });
        }
    }

    public void back(View view) {

        backToNewsFeed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_img);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        Bitmap bmp;

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = (ImageView) findViewById(R.id.uploadImage);
        filePath = Uri.parse(intent.getStringExtra("img"));
        image.setImageBitmap(bmp);
    }
}
