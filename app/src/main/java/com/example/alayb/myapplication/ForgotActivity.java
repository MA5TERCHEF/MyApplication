package com.example.alayb.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alayb.myapplication.asyncfiles.ServiceAsync;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alayb on 15-Jul-19.
 */

public class ForgotActivity extends AppCompatActivity{


    EditText email;
    Toolbar toolbar;
    Button btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        email=findViewById(R.id.forgotemail);
        btn=findViewById(R.id.forgotbtn);
        toolbar=findViewById(R.id.toolbarforgot);
        setSupportActionBar(toolbar);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    new ServiceAsync(AllUrls.Fpass, new ServiceAsync.OnAsyncResult() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject object=new JSONObject(result);
                                if(object.getString("status").equals("valid")&&object.getString("statusCode").equals("1")){
                                    Toast.makeText(ForgotActivity.this, "Check Email for Further Details", Toast.LENGTH_SHORT).show();
                                    MainActivity.activity.finish();
                                    MainActivity.activity=null;
                                    startActivity( new Intent(ForgotActivity.this,MainActivity.class));
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(String result) {
                            Toast.makeText(ForgotActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                        }
                    }, new Uri.Builder().appendQueryParameter("email", email.getText().toString())).execute();

                }
                else
                    Toast.makeText(ForgotActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}