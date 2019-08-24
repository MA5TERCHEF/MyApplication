package com.example.alayb.myapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FirstActivity extends AppCompatActivity {

    ImageView first;
    TextView txtfirst;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        first=findViewById(R.id.first);
        txtfirst=findViewById(R.id.txtfirst);
        first.startAnimation(AnimationUtils.loadAnimation(this,R.anim.animation_first));
        txtfirst.startAnimation(AnimationUtils.loadAnimation(this,R.anim.animation_first_text));

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(FirstActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3500);

    }
}