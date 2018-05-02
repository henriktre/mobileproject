package com.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 1/1/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mDesc = new ArrayList<>();
    private Context mContext;


    //Constructor
    public RecyclerViewAdapter(Context context, ArrayList<String> imageNames, ArrayList<String> images, ArrayList<String> desc ) {
        mImageNames = imageNames;
        mDesc = desc;
        mImages = images;
        mContext = context;
    }

    //No touching everyone, this is just a inflater.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //This is where we have the Listener for pressing each
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        //This is for images, we use Glide, can be found on github, with that we can get images from
        //the nett by using the images Url.
        Glide.with(mContext)
                .asBitmap()     //Sett as bitmap
                .load(mImages.get(position))
                .into(holder.image);

        //This sets the name of the
        holder.imageName.setText(mImageNames.get(position));


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mImageNames.get(position));

                //Just used to check if buttons listen.
                //Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_SHORT).show();
                //Toast.makeText(mContext, mDesc.get(position), Toast.LENGTH_SHORT).show();

                //Set the Movidetails class to what we clicked
                MovieDetails.movNam = mImageNames.get(position);
                MovieDetails.postUrl = mImages.get(position);
                MovieDetails.description = mDesc.get(position);
                Intent intent = new Intent(mContext, Chat.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }


    //Just your every day viewholder.
    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView desc;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.movie_name);
            desc = itemView.findViewById(R.id.movie_description);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}