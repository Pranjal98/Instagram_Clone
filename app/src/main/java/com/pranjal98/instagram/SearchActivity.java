package com.pranjal98.instagram;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private DatabaseReference mRef;
    private Query searchQuery;

    //a list to store all the products
    List<searchContents> dataLists;

    //the recyclerview
    RecyclerView recyclerView;

    SearchRecyclerView adapter;

    private LinearLayoutManager mLayoutManager;

    public void Search(View view){

        EditText searchValue = findViewById(R.id.SearchItem);

        String searchItem = searchValue.getText().toString();

        mRef = FirebaseDatabase.getInstance().getReference("Users");

        searchQuery = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("UserName")
                .startAt(searchItem)
                .endAt(searchItem + "\uf8ff");

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recyclerSearch);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(SearchActivity.this);

        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        //initializing the productlist
        dataLists = new ArrayList<>();

        searchQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                searchContents artist = dataSnapshot.getValue(searchContents.class);
                dataLists.add(artist);

                //creating recyclerview adapter
                SearchRecyclerView adapter = new SearchRecyclerView(getApplicationContext(), dataLists);

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
        setContentView(R.layout.activity_search);

        ImageView search = findViewById(R.id.searchNewsFeed);
        search.setImageResource(R.drawable.searchclked);
    }



//    ValueEventListener valueEventListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            dataLists.clear();
//            if (dataSnapshot.exists()) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//                    searchContents artist = snapshot.getValue(searchContents.class);
//                    dataLists.add(artist);
//
//                    //creating recyclerview adapter
//                    SearchRecyclerView adapter = new SearchRecyclerView(getApplicationContext(), dataLists);
//
//                    //setting adapter to recyclerview
//                    recyclerView.setAdapter(adapter);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            else {
//
//                Toast.makeText(SearchActivity.this, "Empty", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    };
}
