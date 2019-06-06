package com.pranjal98.instagram;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PublicProfileActivity extends AppCompatActivity {

    private int fCounter = 0;

    private String userName, Key, UID, dpUri, PostCount, Followers, Following, yourName;

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference mRef;
    private DatabaseReference youRef;
    private DatabaseReference imgRef;
    private DatabaseReference postRef;
    private DatabaseReference postCountRef;
    private DatabaseReference followersRef;
    private DatabaseReference followingRef;
    private DatabaseReference myFollowingRef;
    private DatabaseReference followerRef;
    private DatabaseReference myFollowingsRef;
    private DatabaseReference fRef;
    private DatabaseReference eRef;
    private DatabaseReference yourNameRef;

    private Query query;

    private TextView following;
    private TextView followers;

    //a list to store all the products
    List<Post> dataLists;

    //the recyclerview
    RecyclerView recyclerView;

    public void follow(View view){

        followersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshots) {

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.exists()) {

                            fRef.child("ID").setValue(UID);
                            fRef.child("Name").setValue(yourName);
//
                            Followers = dataSnapshots.getValue(String.class);

                            int followersCounter = Integer.parseInt(Followers);
                            followersCounter++;

                            Followers = Integer.toString(followersCounter);

                            followersRef.setValue(Followers);

                            query.removeEventListener(this);
                        }

                        else {

                            fRef.child("ID").removeValue();
                            fRef.child("Name").removeValue();

                            Followers = dataSnapshots.getValue(String.class);

                            int followersCounter = Integer.parseInt(Followers);
                            followersCounter--;

                            Followers = Integer.toString(followersCounter);

                            followersRef.setValue(Followers);

                            query.removeEventListener(this);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myFollowingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshots) {

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            eRef.child("Name").removeValue();
                            eRef.child("ID").removeValue();

                            Following = dataSnapshots.getValue(String.class);

                            int followersCounter = Integer.parseInt(Following);
                            followersCounter--;

                            Following = Integer.toString(followersCounter);

                            myFollowingRef.setValue(Following);

                            fCounter--;

                            query.removeEventListener(this);
                        }

                        else {

                            eRef.child("Name").setValue(userName);
                            eRef.child("ID").setValue(Key);

                            Following = dataSnapshots.getValue(String.class);

                            int followeingCounter = Integer.parseInt(Following);
                            followeingCounter++;

                            Following = Integer.toString(followeingCounter);

                            myFollowingRef.setValue(Following);

                            fCounter++;

                            query.removeEventListener(this);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void homeActivity(View view){

        Intent homeActivity = new Intent(getApplicationContext(), NewsFeed.class);
        startActivity(homeActivity);
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

    public void profileActivity(View view){

        Intent profileActivity = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(profileActivity);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);

        ImageView search = findViewById(R.id.searchNewsFeed);
        search.setImageResource(R.drawable.searchclked);
        Intent publicProfile = getIntent();

        if(publicProfile.hasExtra("Key")){

            Key = publicProfile.getStringExtra("Key");
        }

        mAuth = FirebaseAuth.getInstance();

        UID = mAuth.getUid();

        database = FirebaseDatabase.getInstance();

        myRef = database.getReference("Users").child(Key);
        mRef = database.getReference("Users").child(UID);

        youRef = myRef.child("UserName");
        imgRef = myRef.child("dp_url");
        postCountRef = myRef.child("Post");
        followersRef = myRef.child("Followers");
        followingRef = myRef.child("Following");

        followerRef = myRef.child("Follower");
        fRef = followerRef.child(UID);
        yourNameRef = mRef.child("UserName");

        myFollowingRef = mRef.child("Following");

        myFollowingsRef = mRef.child("Followings");
        eRef = myFollowingsRef.child(Key);

        query = followerRef.orderByChild("ID").equalTo(UID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    Button followed = findViewById(R.id.Follow);
                    followed.setText("unfollow");
                }

                else {

                    Button followed = findViewById(R.id.Follow);
                    followed.setText("follow");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        postRef = database.getReference("PostUpload").child(Key);
        postRef.keepSynced(true);


        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.publicProfileGallery);

        GridLayoutManager mLayoutManager = new GridLayoutManager(PublicProfileActivity.this,3);


        recyclerView.setLayoutManager(mLayoutManager);

        //initializing the productlist
        dataLists = new ArrayList<>();

        final TextView UserName = findViewById(R.id.userNameProfile);
        final TextView realName = findViewById(R.id.realNameProfile);
        final TextView postCount = findViewById(R.id.postCount);
        followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);

        final ImageView db = findViewById(R.id.userDpId);


        yourNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                yourName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        followersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Followers = dataSnapshot.getValue(String.class);

                followers.setText(Followers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Following = dataSnapshot.getValue(String.class);

                following.setText(Following);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        imgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dpUri = dataSnapshot.getValue(String.class);

                Picasso.get().load(dpUri).into(db);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

//        postRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


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
}
