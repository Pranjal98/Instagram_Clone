package com.pranjal98.instagram;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentRecyclerView extends RecyclerView.Adapter<CommentRecyclerView.ProductViewHolder> {

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;

    private DatabaseReference postDataRef, dp_url_Ref, UserNameRef, CommentRef;

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<CommentContents> dataList;

    //getting the context and product list with constructor
    public CommentRecyclerView(Context mCtx, List<CommentContents> dataList) {
        this.mCtx = mCtx;
        this.dataList = dataList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.comment_content, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        //getting the product of the specified position
        final CommentContents product = dataList.get(position);

        //binding the data with the viewholder views

        mAuth = FirebaseAuth.getInstance();

        final String UID = mAuth.getUid();

        database = FirebaseDatabase.getInstance();

        String PosterUserID = product.getPosterUserID();
        String PostID = product.getPostID();
        String CommenterUserID = product.getCommenterUserID();
        String CommentID = product.getCommentID();

        postDataRef = database.getReference("PostUpload").child(PosterUserID).child(PostID).child("Comments").child(CommenterUserID).child(CommentID);

        dp_url_Ref = postDataRef.child("dp_url");
        dp_url_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String DP = dataSnapshot.getValue(String.class);

                Picasso.get().load(DP).into(holder.dp_url);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        UserNameRef = postDataRef.child("Name");
        UserNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String userName = dataSnapshot.getValue(String.class);

                holder.UserName.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        CommentRef = postDataRef.child("Comment");
        CommentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String comment = dataSnapshot.getValue(String.class);

                holder.Comment.setText(comment);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        de.hdodenhof.circleimageview.CircleImageView dp_url;
        TextView UserName;
        TextView Comment;

        public ProductViewHolder(View itemView) {
            super(itemView);

            dp_url = itemView.findViewById(R.id.userDpIdComment);
            UserName = itemView.findViewById(R.id.userNameIdComment);
            Comment = itemView.findViewById(R.id.userComment);
        }
    }
}
