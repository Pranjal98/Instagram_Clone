package com.pranjal98.instagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewsFeed extends AppCompatActivity {

    private String Key;

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference likerRef, userRef, postRef;
    private Query query, uery;

    //a list to store all the products
    List<Post> dataLists;

    //the recyclerview
    RecyclerView recyclerView;

    private LinearLayoutManager mLayoutManager;

    public void camera(View view){

        Toast.makeText(this, "This features is currently UnAvailable !", Toast.LENGTH_SHORT).show();
    }

    public void message(View view){

        Toast.makeText(this, "This features is currently UnAvailable !", Toast.LENGTH_SHORT).show();
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

    public void profileActivity(View view){

        Intent profileActivity = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(profileActivity);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        ImageView home = findViewById(R.id.homeNewsFeed);
        home.setImageResource(R.drawable.homeclked);

        mAuth = FirebaseAuth.getInstance();

        final String UID = mAuth.getUid();

        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("PostUpload").child(UID);
//        myRef.keepSynced(true);

        userRef = database.getReference("Users").child(UID).child("Followings");

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(NewsFeed.this);

        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        //initializing the productlist
        dataLists = new ArrayList<>();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Key = snapshot.getKey();

                    query = database.getReference("PostUpload").child(Key).orderByChild("UserID").equalTo(Key);
                    query.keepSynced(true);

                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshots, String s) {

                            if(dataSnapshots.exists()){

                                Post post = dataSnapshots.getValue(Post.class);

                                dataLists.add(post);

                                //creating recyclerview adapter
                                RecyclerViewAdapter adapter = new RecyclerViewAdapter(getApplicationContext(), dataLists);

                                //setting adapter to recyclerview
                                recyclerView.setAdapter(adapter);
                            }

                            else {

                                Toast.makeText(NewsFeed.this, "Error !!", Toast.LENGTH_SHORT).show();
                            }

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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
