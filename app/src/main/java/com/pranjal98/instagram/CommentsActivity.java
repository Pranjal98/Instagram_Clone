package com.pranjal98.instagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommentsActivity extends AppCompatActivity {

    private String UID, UserID, PostID, uuid, yourName, dpUri;

    private EditText comment;

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;

    private DatabaseReference myRef, forUuidRef, mRef, imgRef, yourNameRef, commentRef, cRef;

    //a list to store all the products
    List<CommentContents> dataLists;

    //the recyclerview
    RecyclerView recyclerView;

    SearchRecyclerView adapter;

    private LinearLayoutManager mLayoutManager;

    public void back(View view){

        Intent backFromSinglePost = new Intent(getApplicationContext(), NewsFeed.class);
        startActivity(backFromSinglePost);
        finish();
    }

    public void PostComment(View view){

        uuid = UUID.randomUUID().toString();
        forUuidRef = myRef.child(uuid);

        forUuidRef.child("dp_url").setValue(dpUri);
        forUuidRef.child("Name").setValue(yourName);
        forUuidRef.child("CommenterUserID").setValue(UID);
        forUuidRef.child("PosterUserID").setValue(UserID);
        forUuidRef.child("PostID").setValue(PostID);
        forUuidRef.child("Comment").setValue(comment.getText().toString());
        forUuidRef.child("CommentID").setValue(uuid);

        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent comments = getIntent();

        if(comments.hasExtra("UserID") && comments.hasExtra("PostID")){

            UserID = comments.getStringExtra("UserID");
            PostID = comments.getStringExtra("PostID");
        }

        comment = findViewById(R.id.enteredComment);

        final ImageView db = findViewById(R.id.myDp);

        mAuth = FirebaseAuth.getInstance();

        UID = mAuth.getUid();

        database = FirebaseDatabase.getInstance();

        mRef = database.getReference("Users").child(UID);

        yourNameRef = mRef.child("UserName");
        imgRef = mRef.child("dp_url");



        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recyclerComment);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(CommentsActivity.this);

//        mLayoutManager.setReverseLayout(true);
//        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        //initializing the productlist
        dataLists = new ArrayList<>();

        commentRef = database.getReference("PostUpload").child(UserID).child(PostID).child("Comments");

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    String Key = snapshot.getKey();

                    cRef = commentRef.child(Key);

                    cRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            CommentContents artist = dataSnapshot.getValue(CommentContents.class);
                            dataLists.add(artist);

                            //creating recyclerview adapter
                            CommentRecyclerView adapter = new CommentRecyclerView(getApplicationContext(), dataLists);

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

                commentRef.removeEventListener(this);
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

        yourNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                yourName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef = database.getReference("PostUpload").child(UserID).child(PostID).child("Comments").child(UID);
    }
}
