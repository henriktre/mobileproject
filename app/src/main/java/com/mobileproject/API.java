package com.mobileproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class API extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    public static final String TAG = "tag";

    String movieDbUrl = "https://api.themoviedb.org/3/discover/movie?api_key=";
        String movieDbToken = "19f4374cfb6313cca307d1196d53501d";
        String response = "";
        String example = "https://api.themoviedb.org/3/movie/550?api_key=19f4374cfb6313cca307d1196d53501d";


    RequestQueue queue = Volley.newRequestQueue(this);

        public String getOnCinema () {

            RequestQueue queue = Volley.newRequestQueue(this);
            String onCinema = "&language=en-US&sort_by=popularity.desc&include_adult=true&include_video=false&page=1";
            String url = movieDbUrl + movieDbToken + onCinema;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String res) {
                            response = res;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    response = error.getMessage();
                }

            });

            stringRequest.setTag(TAG);
            queue.add(stringRequest);
            return response;
        }
    }
