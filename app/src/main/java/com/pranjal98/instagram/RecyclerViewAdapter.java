package com.pranjal98.instagram;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ProductViewHolder> {

    private String yourName;

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;

    private DatabaseReference keyRef, likeRef, likerRef, likeyRef, lNameRef, yourNameRef, userRef, deleteRef, postRef, postDataRef, dp_Uri_Ref, userNameRef, captionRef, descRef;
    private Query query, uery, duery;


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Post> dataList;

    //getting the context and product list with constructor
    public RecyclerViewAdapter(Context mCtx, List<Post> dataList) {
        this.mCtx = mCtx;
        this.dataList = dataList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.post, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        //getting the product of the specified position
        final Post product = dataList.get(position);

        mAuth = FirebaseAuth.getInstance();

        final String UID = mAuth.getUid();

        database = FirebaseDatabase.getInstance();

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

        //binding the data with the viewholder views

        String UserId = product.getUserID();
        String PostId = product.getPostID();

        postDataRef = database.getReference("PostUpload").child(UserId).child(PostId);

        dp_Uri_Ref = postDataRef.child("dp_url");
        dp_Uri_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String dp_url = dataSnapshot.getValue(String.class);

                Picasso.get().load(dp_url).into(holder.dp_url);
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

                holder.userName.setText(userName);
                holder.userNameA.setText(userName);
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

                Picasso.get().load(descUri).into(holder.descUri);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        keyRef = database.getReference("Users").child(UID).child("Followings");
        keyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    final String key = snapshot.getKey();


                    likerRef = database.getReference("PostUpload").child(key).child(product.getPostID()).child("Likers");

                    uery = likerRef.orderByChild("ID").equalTo(UID);

                    uery.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshots) {

                            if(dataSnapshots.exists()){

                                holder.likeButton.setImageResource(R.drawable.notifyclked);
                            }

                            else {

                                holder.likeButton.setImageResource(R.drawable.notify);
                            }
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

        likeRef = postDataRef.child("likeCount");
        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                String likeCount = dataSnapshot.getValue(String.class);

                holder.likeCount.setText(likeCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        captionRef = postDataRef.child("desc");
        captionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                String caption = dataSnapshot.getValue(String.class);

                holder.desc.setText(caption);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        int hour = Integer.parseInt(product.getUploadHour());
        int min = Integer.parseInt(product.getUploadMin());
        int sec = Integer.parseInt(product.getUploadSec());
        String date = product.getUploadDate();

        Calendar calendar = Calendar.getInstance();

        int currentHour = Integer.parseInt(new SimpleDateFormat("HH").format(calendar.getTime()));
        int currentMin = Integer.parseInt(new SimpleDateFormat("mm").format(calendar.getTime()));
        int currentSec = Integer.parseInt(new SimpleDateFormat("ss").format(calendar.getTime()));
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        int Dhour, Dmin, Dsec;
        String time = "";

        if(currentSec >= sec){
            Dsec = currentSec - sec;
        }
        else {
            Dsec = (currentSec + 60) - sec;
            currentMin--;
        }

        if(currentMin >= min){
            Dmin = currentMin - min;
        }
        else {
            Dmin = (currentMin + 60) - min;
            currentHour--;
        }

        Dhour = currentHour - hour;

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

        holder.time.setText(time);

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                likeRef = database.getReference("PostUpload").child(product.getUserID()).child(product.getPostID()).child("likeCount");

                likerRef = database.getReference("PostUpload").child(product.getUserID()).child(product.getPostID()).child("Likers");
                lNameRef = likerRef.child(UID);

                uery = likerRef.orderByChild("ID").equalTo(UID);

                likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        uery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshots) {

                                if(!dataSnapshots.exists()){

                                    lNameRef.child("ID").setValue(UID);
                                    lNameRef.child("Name").setValue(yourName);

//                                    holder.likeButton.setImageResource(R.drawable.notifyclked);

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

//                                    holder.likeButton.setImageResource(R.drawable.notify);

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
        });

        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent comments = new Intent(mCtx, CommentsActivity.class);
                comments.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                comments.putExtra("PostID", product.getPostID());
                comments.putExtra("UserID", product.getUserID());

                mCtx.startActivity(comments);
            }
        });

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.post_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.DeleteOption:
                                //handle menu1 click

//                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mCtx);
//                                final AlertDialog dialog = alertDialog.create();
//
//                                alertDialog.setTitle("Confirm Deletion");
//                                alertDialog.setMessage("Delete this post?");
//                                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                                Toast.makeText(mCtx, "Delete working properly", Toast.LENGTH_SHORT).show();
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

                                query = database.getReference("PostUpload").child(UID).orderByChild("PostID").equalTo(product.getPostID());

                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                            if(snapshot.exists()){

                                                deleteRef = database.getReference("PostUpload").child(UID).child(product.getPostID());
                                                deleteRef.removeValue();

                                                postRef = database.getReference("Users").child(UID).child("Post");

                                                postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        String postCount = dataSnapshot.getValue(String.class);

                                                        int pCount = Integer.parseInt(postCount);
                                                        pCount--;

                                                        postCount = Integer.toString(pCount);

                                                        postRef.setValue(postCount);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                                Intent backTonewsfeed = new Intent(mCtx, NewsFeed.class);
                                                backTonewsfeed.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                mCtx.startActivity(backTonewsfeed);

                                                Toast.makeText(mCtx, "Item Deleted.", Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        de.hdodenhof.circleimageview.CircleImageView dp_url;
        TextView userName;
        ImageView descUri;
        TextView userNameA;
        TextView desc;
        TextView likeCount;
        TextView time;

        ImageButton likeButton;
        ImageView commentButton;

        ImageButton buttonViewOption;

        public ProductViewHolder(View itemView) {
            super(itemView);

            dp_url = itemView.findViewById(R.id.userDpId);
            userName = itemView.findViewById(R.id.userNameId);
            descUri = itemView.findViewById(R.id.DescImgId);
            userNameA = itemView.findViewById(R.id.userNameIId);
            desc = itemView.findViewById(R.id.contentDescId);
            likeCount = itemView.findViewById(R.id.LikeCount);
            time = itemView.findViewById(R.id.time);

            likeButton = itemView.findViewById(R.id.LikeButton);
            commentButton = itemView.findViewById(R.id.CommentButton);

            buttonViewOption = itemView.findViewById(R.id.moreMenu);
        }
    }
}