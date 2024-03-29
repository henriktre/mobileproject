package com.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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
    Firebase reference;

    //Just constructor.
    public RecyclerViewAdapter(Context context, ArrayList<String> imageNames, ArrayList<String> images, ArrayList<String> desc ) {
        mImageNames = imageNames;
        mDesc = desc;
        mImages = images;
        mContext = context;
    }

    //Don't touch this, it's just a view holder.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);

        Firebase.setAndroidContext(mContext);
        reference = new Firebase("https://mobileproject-3b6d7.firebaseio.com/users");



        return holder;
    }

    //This is what we use to set the views.
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        reference.child(UserDetails.username).child("favourites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot chld : dataSnapshot.getChildren() ){
                    if(chld.getValue().equals(mImageNames.get(position))) {
                        holder.btnFav.setImageResource(R.drawable.fav);
                        holder.btnFav.setTag("fav");
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });

        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);

        holder.imageName.setText(mImageNames.get(position));
        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Sett favourites(with the hearty emoji or some shit.)

                Log.d(TAG, "onClick:  fav button clicked");

                if(holder.btnFav.getTag() == "fav"){
                    holder.btnFav.setImageResource(R.drawable.nfav);
                    holder.btnFav.setTag("nfav");


                    reference.child(UserDetails.username).child("favourites").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot chld : dataSnapshot.getChildren() ){
                                if(chld.getValue().equals(mImageNames.get(position))) {
                                    chld.getRef().removeValue();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });


                } else {
                    holder.btnFav.setImageResource(R.drawable.fav);
                    holder.btnFav.setTag("fav");
                    reference.child(UserDetails.username).child("favourites").push().setValue(mImageNames.get(position));

                }

            }
        });


                holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mImageNames.get(position));

                //When you click on a view you put info in movieDetails and are sent to navigation.

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


    //The viewHolder.
    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView desc;
        TextView imageName;
        RelativeLayout parentLayout;
        ImageButton btnFav;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.movie_name);
            desc = itemView.findViewById(R.id.movie_description);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            btnFav = itemView.findViewById(R.id.btnFav);
        }
    }


}