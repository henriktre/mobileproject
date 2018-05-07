package com.mobileproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stian on 18.04.2018.
 */

public class Register extends AppCompatActivity {
    EditText username, password;
    Button registerButton;
    String user, pass;
    TextView login;
    String randUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        registerButton = (Button)findViewById(R.id.registerButton);
        login = (TextView)findViewById(R.id.login);
        //To set a random username.
        randUse = randomIdentifier().replaceAll("\\s+","");

        Toast.makeText(Register.this, "If you don't choose a username, one will be set for you.", Toast.LENGTH_LONG).show();

        Firebase.setAndroidContext(this);

        //To go to mainactivity/login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, MainActivity.class));
            }
        });

        //Button for registering user.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = username.getText().toString();
                pass = password.getText().toString();

                //Checks if password and name is good.
                if(user.equals("")){
                    username.setText(randUse);
                    Toast.makeText(Register.this, "Push Register again.", Toast.LENGTH_LONG).show();
                }
                else if(pass.equals("")){
                    password.setError("can't be blank");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("only alphabet or number allowed");
                }
                else if(user.length()<5){
                    username.setError("at least 5 characters long");
                }
                else if(pass.length()<5){
                    password.setError("at least 5 characters long");
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(Register.this);

                    pd.setMessage("Loading...");
                    pd.show();
                    //String to get users.
                    String url = "https://mobileproject-3b6d7.firebaseio.com/users.json";

                    //Checking if it exists and if not, saves it.
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://mobileproject-3b6d7.firebaseio.com/users");

                            if(s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }

                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(request);
                }
            }
        });
    }


    //Used to make random UserName.
    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz12345674890";

    final java.util.Random rand = new java.util.Random();

    public String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
        }
        return builder.toString();
    }
}
