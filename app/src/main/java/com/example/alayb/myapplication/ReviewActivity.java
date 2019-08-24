package com.example.alayb.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alayb.myapplication.asyncfiles.ServiceAsync;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alayb on 23-Jul-19.
 */

public class ReviewActivity extends AppCompatActivity{

    TextView name,cat;
    ImageView img;
    EditText text;
    RatingBar bar;
    Button btn;
    Toolbar toolbar;
    ProgressDialog dialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_review);
        name=findViewById(R.id.review_name);
        cat=findViewById(R.id.review_cat);
        img=findViewById(R.id.review_image);
        toolbar=findViewById(R.id.toolbarreview);
        setSupportActionBar(toolbar);
        text=findViewById(R.id.review_text);
        bar=findViewById(R.id.review_rating);
        btn=findViewById(R.id.review_submit);
        final Intent i=getIntent();
        i.getStringExtra("id");
        name.setText(i.getStringExtra("name"));
        Glide.with(this).load(i.getStringExtra("img")).into(img);
        cat.setText(i.getStringExtra("cat"));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bar.getRating()==0f)
                Toast.makeText(ReviewActivity.this, "Please Rate The Product Before Submitting The Review", Toast.LENGTH_SHORT).show();
                else{

                    dialog=new ProgressDialog(ReviewActivity.this);
                    dialog.setTitle("Submitting Review");
                    dialog.setMessage("Please wait");
                    dialog.setCancelable(false);
                    dialog.show();

                Uri.Builder builder=new Uri.Builder();
                builder.appendQueryParameter("productId",i.getStringExtra("id"));
                builder.appendQueryParameter("userId",getSharedPreferences("Login",MODE_PRIVATE).getString("userId",""));
                builder.appendQueryParameter("ratings",bar.getRating()+"");
                builder.appendQueryParameter("action","submitRatings");
                builder.appendQueryParameter("description",text.getText().toString());

                new ServiceAsync(AllUrls.Edit, new ServiceAsync.OnAsyncResult() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject object=new JSONObject(result);
                            if (object.getString("status").equals("true")&&object.getString("msg").equals("Ratings submitted")){
                                Toast.makeText(ReviewActivity.this, "Review Submitted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ReviewActivity.this,DashboardActivity.class));
                                finish();
                            }
                            else
                                Toast.makeText(ReviewActivity.this, "Can't submit review for same product multiple times", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.cancel();

                    }

                    @Override
                    public void onFailure(String result) {
                        dialog.cancel();
                        Toast.makeText(ReviewActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }, builder).execute();



                }
            }
        });
    }
}
