package com.pranjal98.instagram;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SinglePost extends AppCompatActivity {

    private String UID, PostID, UserID, yourName, currentDate, time="", caption="";

    Calendar calendar = Calendar.getInstance();

    private int currentHour = Integer.parseInt(new SimpleDateFormat("HH").format(calendar.getTime()));
    private int currentMin = Integer.parseInt(new SimpleDateFormat("mm").format(calendar.getTime()));
    private int currentSec = Integer.parseInt(new SimpleDateFormat("ss").format(calendar.getTime()));
    private int Dhour, Dmin, Dsec;

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;

    private DatabaseReference postDataRef, dp_Uri_Ref, userNameRef, descRef, captionRef, likeRef, hourRef, minRef, secRef, dateRef, likerRef, lNameRef, yourNameRef, deleteRef, postRef;
    private Query uery, query;

    private ImageButton buttonViewOption;

    public void backFromSinglePost(View view){

        Intent backFromSinglePost = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(backFromSinglePost);
        finish();
    }

    public void SinglePostLikeButton(View view){

        likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                uery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshots) {

                        if(!dataSnapshots.exists()){

                            lNameRef.child("ID").setValue(UID);
                            lNameRef.child("Name").setValue(yourName);

//                                                            holder.likeButton.setImageResource(R.drawable.notifyclked);

                            String likeCount = dataSnapshot.getValue(String.class);
                            int likes = Integer.parseInt(likeCount);
                            likes++;
                            likeCount = Integer.toString(likes);
                            likeRef.setValue(likeCount);

                            uery.removeEventListener(this);
                        }

                        else {

                            lNameRef.child("ID").removeValue();
                            lNameRef.child("Name").removeValue();

//                                                            holder.likeButton.setImageResource(R.drawable.notify);

                            String likeCount = dataSnapshot.getValue(String.class);
                            int likes = Integer.parseInt(likeCount);
                            likes--;
                            likeCount = Integer.toString(likes);
                            likeRef.setValue(likeCount);

                            uery.removeEventListener(this);
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

    public void SinglePostCommentButton(View view){

        Intent comments = new Intent(getApplicationContext(), CommentsActivity.class);

        comments.putExtra("PostID", PostID);
        comments.putExtra("UserID", UserID);

        startActivity(comments);
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

    public void profileActivity(View view){

        Intent profileActivity = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(profileActivity);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        ImageView prof = findViewById(R.id.profNewsFeed);
        prof.setImageResource(R.drawable.profclked);

        getIncomingIntent();

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();

        UID = mAuth.getUid();

        final ImageView likeButton = findViewById(R.id.SinglePostLikeButton);

        postDataRef = database.getReference("PostUpload").child(UserID).child(PostID);

        dp_Uri_Ref = postDataRef.child("dp_url");
        dp_Uri_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String dp_url = dataSnapshot.getValue(String.class);

                de.hdodenhof.circleimageview.CircleImageView Dp_url = findViewById(R.id.SinglePostuserDpId);
                Picasso.get().load(dp_url).into(Dp_url);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userNameRef = postDataRef.child("userName");
        userNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                String userName = dataSnapshot.getValue(String.class);

                TextView UserName = findViewById(R.id.SinglePostuserNameId);
                UserName.setText(userName);

                TextView UserNameI = findViewById(R.id.SinglePostuserNameIId);
                UserNameI.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        descRef = postDataRef.child("descUri");
        descRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                String descUri = dataSnapshot.getValue(String.class);

                ImageView DescUri = findViewById(R.id.SinglePostDescImgId);
                Picasso.get().load(descUri).into(DescUri);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        likerRef = postDataRef.child("Likers");
        lNameRef = likerRef.child(UID);

        uery = likerRef.orderByChild("ID").equalTo(UID);

        uery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {

                if(dataSnapshots.exists()){

                    likeButton.setImageResource(R.drawable.notifyclked);
                }

                else {

                    likeButton.setImageResource(R.drawable.notify);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        likeRef = postDataRef.child("likeCount");
        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                String likeCount = dataSnapshot.getValue(String.class);

                TextView LikeCount = findViewById(R.id.SinglePostLikeCount);
                LikeCount.setText(likeCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        captionRef = postDataRef.child("desc");
        captionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                caption = dataSnapshot.getValue(String.class);

                TextView Desc = findViewById(R.id.SinglePostcontentDescId);
                Desc.setText(caption);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        Calendar calendar = Calendar.getInstance();

//        final int currentHour = Integer.parseInt(new SimpleDateFormat("HH").format(calendar.getTime()));
//        final int currentMin = Integer.parseInt(new SimpleDateFormat("mm").format(calendar.getTime()));
//        final int currentSec = Integer.parseInt(new SimpleDateFormat("ss").format(calendar.getTime()));
        final String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

//        final int Dhour;
//        final int Dmin;
//        final int Dsec;
//        final String time = "";
//        Calendar calendar = Calendar.getInstance();
//
//        final int[] currentHour = {Integer.parseInt(new SimpleDateFormat("HH").format(calendar.getTime()))};
//        final int[] currentMin = {Integer.parseInt(new SimpleDateFormat("mm").format(calendar.getTime()))};
//        final int currentSec = Integer.parseInt(new SimpleDateFormat("ss").format(calendar.getTime()));
//        final String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
//
//        final int[] Dhour = new int[1];
//        final int[] Dmin = new int[1];
//        final int[] Dsec = new int[1];
//        final String[] time = {""};

        hourRef = database.getReference("PostUpload").child(UserID).child(PostID).child("uploadHour");
        hourRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int hour = Integer.parseInt(dataSnapshot.getValue(String.class));

                Dhour = currentHour - hour;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        minRef = database.getReference("PostUpload").child(UserID).child(PostID).child("uploadMin");
        minRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int min = Integer.parseInt(dataSnapshot.getValue(String.class));

                if(currentMin >= min){
                    Dmin = currentMin - min;
                }
                else {
                    Dmin = (currentMin + 60) - min;
                    currentHour--;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        secRef = database.getReference("PostUpload").child(UserID).child(PostID).child("uploadSec");
        secRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int sec = Integer.parseInt(dataSnapshot.getValue(String.class));

                if(currentSec >= sec){
                    Dsec = currentSec - sec;
                }
                else {
                    Dsec = (currentSec + 60) - sec;
                    currentMin--;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dateRef = postDataRef.child("uploadDate");
        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String date = dataSnapshot.getValue(String.class);

                if (currentDate.equals(date)){

                    if(Dhour == 0){

                        if(Dmin == 0){

                            if(Dsec == 0){

                                time = "Just Now";
                            }
                            else {

                                time = Dsec + " sec ago";
                            }
                        }
                        else {

                            time = Dmin + " min ago";
                        }
                    }
                    else {

                        time = Dhour + " hours ago";
                    }
                }

                else {

                    time = date;
                }

                TextView Time = findViewById(R.id.SinglePosttime);
                Time.setText(time);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        yourNameRef = database.getReference("Users").child(UID).child("UserName");
        yourNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                yourName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        buttonViewOption = findViewById(R.id.moreMenu);

        buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(getApplicationContext(), buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.post_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.DeleteOption:

//                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
//                                final AlertDialog dialog = alertDialog.create();
//
//                                alertDialog.setTitle("Confirm Deletion");
//                                alertDialog.setMessage("Delete this post?");
//                                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                                Toast.makeText(getApplicationContext(), "Delete working properly", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                alertDialog.setNegativeButton("Don't delete", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                                dialog.cancel();
//                                            }
//                                        });
//
//                                dialog.show();

                                query = database.getReference("PostUpload").child(UID).orderByChild("PostID").equalTo(PostID);

                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                            if(snapshot.exists()){

                                                deleteRef = database.getReference("PostUpload").child(UID).child(PostID);

                                                Intent backToProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                                                startActivity(backToProfile);
                                                finish();

                                                postRef = database.getReference("Users").child(UID).child("Post");

                                                postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        String postCount = dataSnapshot.getValue(String.class);

                                                        int pCount = Integer.parseInt(postCount);
                                                        pCount--;

                                                        postCount = Integer.toString(pCount);

                                                        postRef.setValue(postCount);
                                                        deleteRef.removeValue();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                                Toast.makeText(SinglePost.this, "Item Deleted.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                return true;

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }

    private void getIncomingIntent(){

        if(getIntent().hasExtra("UserID") && getIntent().hasExtra("PostID")){

            UserID = getIntent().getStringExtra("UserID");
            PostID = getIntent().getStringExtra("PostID");
        }
    }
}
