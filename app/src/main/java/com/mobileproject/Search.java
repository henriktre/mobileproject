package com.mobileproject;

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
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Stian on 30.04.2018.
 */

public class Search extends AppCompatActivity {
    String TAG = "Search: ";
    API api;
    Button search;
    EditText searchText;
    ArrayList<String> mNames = new ArrayList<>();
    ArrayList<String> mDesc = new ArrayList<>();
    ArrayList<String> mImageUrls = new ArrayList<>();
    int searchType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        searchType = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchText = findViewById(R.id.txtSearch);

        api = new API(this);

        search = findViewById(R.id.btnSearch);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageUrls.clear();
                mNames.clear();
                mDesc.clear();
                Log.d(TAG, "onClick search type: " + searchType);
                String res = api.search(searchText.getText().toString(), searchType);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String title;
                        JSONObject result = jsonArray.getJSONObject(i);
                        if(searchType == 1) {
                            if(result.getString("title") == null) break;
                            title = result.getString("title");
                        } else {
                            if(result.getString("name") == null) break;
                            title = result.getString("name");
                        }
                        String imageUrl = "https://image.tmdb.org/t/p/w185" + result.getString("poster_path");
                        String desc = result.getString("overview");
                        initImageBitmaps(title, imageUrl, desc);
                    }
                    initRecyclerView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.multi: searchType = 7;
//                return true;
            case R.id.movies: searchType = 1;
                return true;
            case R.id.tv: searchType = 2;
                return true;
//            case R.id.company: searchType = 3;
//                return true;
//            case R.id.person: searchType = 6;
//                return true;
            default: searchType = 1;
                return true;
        }
    }

    private void initImageBitmaps(String title, String img, String desc) {
        Log.d("initImageBitmaps: ", "preparing bitmaps.");
        mImageUrls.add(img);
        mNames.add(title);
        mDesc.add(desc);
    }
    private void initRecyclerView(){
        Log.d("initRecyclerView: ", "init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls, mDesc);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
