package com.mobileproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Stian on 18.04.2018.
 */

public class Navigate extends AppCompatActivity {
    API api = new API();

    Button getResponse;
    TextView viewResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);

        getResponse = (Button)findViewById(R.id.btnGetRes);
        viewResponse = (TextView) findViewById(R.id.viewResponse);

        getResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewResponse.setText(api.getOnCinema());
            }
        });
    }
}
