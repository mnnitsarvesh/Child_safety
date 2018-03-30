package com.example.root.child;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class RequestResult extends AppCompatActivity {
    private ImageView ResultRequestImageView;
    private Button ResultRequestAccept,ResultRequestDecline;
    private TextView EmailRequset,NameRequest;
    private Toolbar requestResultoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_result);
        Bundle bundle = getIntent().getExtras();
        final String demo[] = bundle.getStringArray("userInfo");
        ResultRequestImageView = findViewById(R.id.requestResultImageView);
        ResultRequestAccept = findViewById(R.id.acceptRequest);
        ResultRequestDecline = findViewById(R.id.declineRequest);
        EmailRequset = findViewById(R.id.requestResultEmail);
        NameRequest = findViewById(R.id.requestResultName);
        requestResultoolbar = findViewById(R.id.requestResultToolbar);
        setSupportActionBar(requestResultoolbar);
        getSupportActionBar().setTitle("Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Picasso.with(this).load(demo[2]).into(ResultRequestImageView);
        EmailRequset.setText(demo[1]);
        NameRequest.setText(demo[0]);
        ResultRequestDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"button decline",Toast.LENGTH_LONG).show();
            }
        });
        ResultRequestAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"button accept",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
