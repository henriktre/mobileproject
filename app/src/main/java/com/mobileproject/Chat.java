package com.mobileproject;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stian on 02.05.2018.
 */

public class Chat extends AppCompatActivity {

    private static final String TAG = "Chat";

    LinearLayout layout;
    RelativeLayout layout_2;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference;
    ImageView sendButton;
    Long timestamp;
    public long timeNow;


    String theWatched = MovieDetails.movNam;
    String moviePoster = MovieDetails.postUrl;
    String description = MovieDetails.description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Log.d(TAG, "onCreate: started.");

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        sendButton = (ImageView)findViewById(R.id.sendButton);


        setImage(moviePoster, theWatched, description);

        timeNow = new Date().getTime();

        Firebase.setAndroidContext(this);

        reference = new Firebase("https://mobileproject-3b6d7.firebaseio.com/messages");


        sendButton.setOnClickListener(new android.view.View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(android.view.View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    timestamp = new Date().getTime();
                    map.put("movie", theWatched);
                    map.put("user", UserDetails.username);
                    map.put("time", timestamp.toString());
                    map.put("message", messageText);
                    reference.push().setValue(map);

                    messageArea.setText("");
                }
            }
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String movieName = map.get("movie").toString();
                String time = map.get("time").toString();
                if(userName.equals(UserDetails.username) && movieName.equals(theWatched)){
                    addMessageBox(userName,time,message, 1);
                }
                else if(!userName.equals(UserDetails.username) && movieName.equals(theWatched)){
                    addMessageBox(userName,time,message, 2);
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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    public void addMessageBox(String user, String time, String message, int type){
        TextView textView = new TextView(this);

        long timeDate = Long.parseLong(time);
        Date date=new Date(timeDate);
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        sfd.format(date);

        // Format the date before showing it
        textView.setText("\n" + user + "\nTime: " + date.toString() + "\nMessage: "+ message + "\n");

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
        }
        else{
            lp2.gravity = Gravity.RIGHT;
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(android.view.View.FOCUS_DOWN);
    }



    private void setImage(String imageUrl, String imageName, String description){
        Log.d(TAG, "setImage: setting the image and name to widgets.");

        TextView name = findViewById(R.id.m_name);
        name.setText(imageName);

        TextView desc = findViewById(R.id.movie_description);
        desc.setText(description);

        ImageView image = findViewById(R.id.movie_image);
        Glide.with(this)
                .asBitmap()
                .load(moviePoster)
                .into(image);
    }
}
