package com.pranjal98.instagram;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchRecyclerView extends RecyclerView.Adapter<SearchRecyclerView.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<searchContents> dataList;

    //getting the context and product list with constructor
    public SearchRecyclerView(Context mCtx, List<searchContents> dataList) {
        this.mCtx = mCtx;
        this.dataList = dataList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.search_content, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        //getting the product of the specified position
        final searchContents product = dataList.get(position);

        //binding the data with the viewholder views


        Picasso.get().load(product.getDp_url()).into(holder.dp_url);
        holder.UserName.setText(product.getUserName());

        holder.SearchItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent publicProfile = new Intent(mCtx, PublicProfileActivity.class);
                publicProfile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                publicProfile.putExtra("Key", product.getKey());

                mCtx.startActivity(publicProfile);
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

        RelativeLayout SearchItems;

        public ProductViewHolder(View itemView) {
            super(itemView);

            dp_url = itemView.findViewById(R.id.userDpIdSearch);
            UserName = itemView.findViewById(R.id.userNameIdSearch);

            SearchItems = itemView.findViewById(R.id.SearchItems);
        }
    }
}
