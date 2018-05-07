package com.mobileproject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class API {
    Context ctx;

    public API(Context context){
        ctx=context;
    }

//    @Override
//    protected void onStop () {
//        super.onStop();
//        if (queue != null) {
//            queue.cancelAll(TAG);
//        }
//    }

    public static final String TAG = "tag";

    String movieDbUrl = "https://api.themoviedb.org/";
        String movieDbToken = "19f4374cfb6313cca307d1196d53501d";
        String response = "";
//        String example = "https://api.themoviedb.org/3/movie/550?api_key=19f4374cfb6313cca307d1196d53501d";


        public String getOnCinema () {
            RequestQueue queue = Volley.newRequestQueue(ctx);
            String discover = "3/discover/movie?api_key=";
            String onCinema = "&language=en-US&sort_by=popularity.desc&include_adult=true&include_video=false&page=1";
            String url = movieDbUrl + discover + movieDbToken + onCinema;

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

        public String search (String userSearch, int type) {
            RequestQueue queue = Volley.newRequestQueue(ctx);

            String search;
            /*
            * 1: movie search
            * 2: tv show search
            * 3: company search
            * 4: collection search
            * 5: keyword search
            * 6: person search
            * 7: multi search (movies, tv shows and people)
            * */
            switch (type) {
                case 1:  search = "3/search/movie?api_key=";
                    break;
                case 2:  search = "3/search/tv?api_key=";
                    break;
                case 3:  search = "3/search/company?api_key=";
                    break;
                case 4:  search = "3/search/collection?api_key=";
                    break;
                case 5:  search = "3/search/keyword?api_key=";
                    break;
                case 6:  search = "3/search/person?api_key=";
                    break;
                case 7:  search = "3/search/multi?api_key=";
                    break;
                default: search = "3/search/multi?api_key=";
                    break;
            }

            String q = userSearch.replaceAll(" ", "%20");
            String query = "&query=" + q;

            String url = movieDbUrl + search + movieDbToken + query;
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
