package com.example.alayb.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class PasswordChange extends AppCompatActivity{
    EditText oldp,newp,renewp;
    Button btn;
    Toolbar toolbar;
    ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        oldp=findViewById(R.id.password_old);
        newp=findViewById(R.id.password_new);
        renewp=findViewById(R.id.password_renew);
        toolbar=findViewById(R.id.toolbarchange);
        setSupportActionBar(toolbar);
        btn=findViewById(R.id.pass_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!oldp.getText().toString().trim().equals("")&&!newp.getText().toString().trim().equals("")){
                    if (renewp.getText().toString().equals(newp.getText().toString())){
                        dialog=new ProgressDialog(PasswordChange.this);
                        dialog.setTitle("Loading");
                        dialog.setMessage("Please wait");
                        dialog.setCancelable(false);
                        dialog.show();

                    Uri.Builder builder=new Uri.Builder();
                    builder.appendQueryParameter("uId",getSharedPreferences("Login",MODE_PRIVATE).getString("userId",""));
                    builder.appendQueryParameter("oPassword",oldp.getText().toString());
                    builder.appendQueryParameter("nPassword",newp.getText().toString());
                    new ServiceAsync(AllUrls.Cpass, new ServiceAsync.OnAsyncResult() {
                        @Override
                        public void onSuccess(String result) {

                            try {
                            JSONObject object = new JSONObject(result);
                                if(object.getString("status").equals("valid")&& object.getString("statusCode").equals("1")){
                                    Toast.makeText(PasswordChange.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(PasswordChange.this,DashboardActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else
                                Toast.makeText(PasswordChange.this, "Check Password and Try again", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.cancel();
                        }
                        @Override
                        public void onFailure(String result) {
                            dialog.cancel();
                            Toast.makeText(PasswordChange.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    },builder).execute();
                }
                else
                        Toast.makeText(PasswordChange.this, "Re-Entered Password Doesn't Match", Toast.LENGTH_SHORT).show();
              }

            }
        });



    }
}