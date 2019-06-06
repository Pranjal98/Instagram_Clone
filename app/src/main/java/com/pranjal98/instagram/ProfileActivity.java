package com.pranjal98.instagram;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth myAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef, youRef, imgRef, postRef, postCountRef, followersRef, followingRef;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Uri selectImg, imgUri;

    private String userName, UID, dpUri, PostCount, Followers, Following;

    //a list to store all the products
    List<Post> dataLists;

    //the recyclerview
    RecyclerView recyclerView;



    public void uploadDp(View view){

        Intent dp = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(dp, 1);
    }

    public void logOut(View view){

        myAuth = FirebaseAuth.getInstance();

        myAuth.signOut();

        Intent logOut = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(logOut);
        finish();
    }

    public void upload(View view){

        Toast.makeText(this, "This features is currently UnAvailable !", Toast.LENGTH_SHORT).show();
    }

    public void homeActivity(View view){

        Intent homeActivity = new Intent(getApplicationContext(), NewsFeed.class);
        startActivity(homeActivity);
        finish();
    }

    public void searchActivity(View view){

        Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(searchActivity);
        finish();
    }

    public void uploadActivity(View view){

        Intent uploadActivity = new Intent(getApplicationContext(), AddActivity.class);
        startActivity(uploadActivity);
        finish();
    }

    public void notify(View view){

        Intent notificationAcitvity = new Intent(getApplicationContext(), NotificationActivity.class);
        startActivity(notificationAcitvity);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView prof = findViewById(R.id.profNewsFeed);
        prof.setImageResource(R.drawable.profclked);

        myAuth = FirebaseAuth.getInstance();

        UID = myAuth.getUid();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(UID);

        youRef = myRef.child("UserName");
        imgRef = myRef.child("dp_url");
        postCountRef = myRef.child("Post");
        followersRef = myRef.child("Followers");
        followingRef = myRef.child("Following");

        postRef = database.getReference("PostUpload").child(UID);
        postRef.keepSynced(true);



        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.profileGallery);

        GridLayoutManager mLayoutManager = new GridLayoutManager(ProfileActivity.this,3);


        recyclerView.setLayoutManager(mLayoutManager);

        //initializing the productlist
        dataLists = new ArrayList<>();


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final TextView UserName = findViewById(R.id.userNameProfile);
        final TextView realName = findViewById(R.id.realNameProfile);
        final TextView postCount = findViewById(R.id.postCount);
        final TextView followers = findViewById(R.id.followers);
        final TextView following = findViewById(R.id.following);

        final ImageView db = findViewById(R.id.userDpId);

        postCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                PostCount = dataSnapshot.getValue(String.class);

                postCount.setText(PostCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Following = dataSnapshot.getValue(String.class);

                following.setText(Following);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        followersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Followers = dataSnapshot.getValue(String.class);

                followers.setText(Followers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dpUri = dataSnapshot.getValue(String.class);

                Picasso.get().load(dpUri).into(db);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        youRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userName = dataSnapshot.getValue(String.class);

                UserName.setText(userName);
                realName.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        postRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Post post = dataSnapshot.getValue(Post.class);

                dataLists.add(post);

                //creating recyclerview adapter
                GalleryRecyclerView adapter = new GalleryRecyclerView(getApplicationContext(), dataLists);

                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode== RESULT_OK && data != null){

            selectImg = data.getData();

            StorageReference ref = storageReference.child("images/"+ UID +"/dp/");
            ref.putFile(selectImg)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imgUri = taskSnapshot.getDownloadUrl();

                            myRef.child("dp_url").setValue(imgUri.toString());

                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(), "Failed !"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
