package com.mobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Navigate extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    API api;
    Button btnOnCinema;

    //vars
     ArrayList<String> mNames = new ArrayList<>();
     ArrayList<String> mImageUrls = new ArrayList<>();
     ArrayList<String> mDesc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        SharedPreferences settings = getSharedPreferences("Profile", MODE_PRIVATE);
        String useN = settings.getString("Username", "");
        String Pword = settings.getString("Password", "");

        if (!Objects.equals(useN, "") && !Objects.equals(Pword, "")){
            api = new API(this);
            Log.d(TAG, "onCreate: started.");

            UserDetails.username = useN;
            UserDetails.password = Pword;

            btnOnCinema = findViewById(R.id.btnOnCinema);
            btnOnCinema.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageUrls.clear();
                    mNames.clear();

                    String res = api.getOnCinema();
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        JSONArray jsonArray = jsonObject.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject result = jsonArray.getJSONObject(i);
                            String title = result.getString("title");
                            String imageUrl = "https://image.tmdb.org/t/p/w185" + result.getString("poster_path");
                            String desc = result.getString("overview");
                            initImageBitmaps(title, imageUrl, desc);
                            Log.d(TAG, "title: " + title);
                            Log.d(TAG, "imageurl: " + imageUrl);
                            Log.d(TAG, "desc: " + desc);
                        }
                        initRecyclerView();
                    } catch (JSONException e) {
                        e.getStackTrace();
                    }

                }
            });
        }else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Intent intent = new Intent(this, Search.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                Intent intent2 = new Intent(this, Profile.class);
                startActivity(intent2);
                return true;
            case R.id.logOut:
                SharedPreferences.Editor editor = getSharedPreferences("Profile", MODE_PRIVATE).edit();
                editor.putString("Username", "");
                editor.putString("Password", "");
                editor.apply();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initImageBitmaps(String title, String img, String desc) {
        Log.d("initImageBitmaps: ", "preparing bitmaps.");
        mImageUrls.add(img);
        mNames.add(title);
        mDesc.add(desc);
        Log.d(TAG, "initImageBitmaps: image: " + title);
        Log.d(TAG, "initImageBitmaps: image: " + img);
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls, mDesc);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
