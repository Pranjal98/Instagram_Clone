package com.pranjal98.instagram;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class GalleryRecyclerView extends RecyclerView.Adapter<GalleryRecyclerView.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Post> dataList;

    //getting the context and product list with constructor
    public GalleryRecyclerView(Context mCtx, List<Post> dataList) {
        this.mCtx = mCtx;
        this.dataList = dataList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.gallery_data, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        //getting the product of the specified position
        final Post product = dataList.get(position);

        //binding the data with the viewholder views

        Picasso.get().load(product.getDp_url()).into(holder.dp_url);
        holder.userName.setText(product.getUserName());

        Picasso.get().load(product.getDescUri()).into(holder.descUri);
        holder.desc.setText(product.getDesc());
        holder.likeCount.setText(product.getLikeCount());

        holder.galleryPostImgId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent singlePost = new Intent(mCtx, SinglePost.class);
                singlePost.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

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

                singlePost.putExtra("UserID", product.getUserID());
                singlePost.putExtra("PostID", product.getPostID());
//                singlePost.putExtra("dp_url", product.getDp_url());
//                singlePost.putExtra("userName", product.getUserName());
//                singlePost.putExtra("descUri", product.getDescUri());
//                singlePost.putExtra("desc", product.getDesc());
//                singlePost.putExtra("likeCount", product.getLikeCount());
//                singlePost.putExtra("time", time);

                mCtx.startActivity(singlePost);
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
        TextView desc;
        TextView likeCount;
        TextView time;

        RelativeLayout galleryPostImgId;

        public ProductViewHolder(View itemView) {
            super(itemView);

            dp_url = itemView.findViewById(R.id.GalleryuserDpId);
            userName = itemView.findViewById(R.id.GalleryUserName);
            descUri = itemView.findViewById(R.id.GalleryImg);
            desc = itemView.findViewById(R.id.GalleryCaption);
            likeCount = itemView.findViewById(R.id.GalleryLikeCount);
            time = itemView.findViewById(R.id.Gallerytime);

            galleryPostImgId = itemView.findViewById(R.id.galleryPostImgId);

        }
    }
}