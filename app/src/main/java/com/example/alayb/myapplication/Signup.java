package com.example.alayb.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alayb.myapplication.asyncfiles.ServiceAsync;

import org.json.JSONException;
import org.json.JSONObject;

public class Signup extends AppCompatActivity {

    EditText fname, lname, email, pass, repass;
    TextView back;
    Button btnsignup;
    ProgressDialog dialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        back=findViewById(R.id.backtologin);
        pass = findViewById(R.id.pass);
        dialog = new ProgressDialog(this);
        toolbar=findViewById(R.id.toolbarsignup);
        setSupportActionBar(toolbar);
        repass = findViewById(R.id.repass);
        btnsignup = findViewById(R.id.btnsignup);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidSignup()){
                    dialog.setTitle("Creating Account");
                    dialog.setMessage("Please wait");
                    dialog.setCancelable(false);
                    dialog.show();
                    String username=fname.getText().toString().trim()+" "+lname.getText().toString().trim();
                    apiCallForSignup(fname.getText().toString().trim(),lname.getText().toString().trim(),username,email.getText().toString().trim(),pass.getText().toString().trim());
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler=new Handler();
                back.setTextColor(Color.parseColor("Black"));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        back.setTextColor(Color.parseColor("#0B98E4"));
                    }

                },1000);

                MainActivity.sendActivity().finish();
                MainActivity.activity=null;
                Intent intent =new Intent(Signup.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void apiCallForSignup(String firstName , String lastName, String userName,String email, String password) {
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("firstName",firstName);
        builder.appendQueryParameter("lastName",lastName);
        builder.appendQueryParameter("userName",userName);
        builder.appendQueryParameter("email",email);
        builder.appendQueryParameter("password",password);

        ServiceAsync async= new ServiceAsync(AllUrls.Sign_up,new ServiceAsync.OnAsyncResult() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object=new JSONObject(result);
                    String status=object.getString("status");
                    String statuscode=object.getString("statusCode");
                    if (statuscode.equals("1")&&status.equals("valid")){
                        Toast.makeText(Signup.this, "Account Created, Please Login", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(Signup.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else Toast.makeText(Signup.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.cancel();
            }

            @Override
            public void onFailure(String result) {
                Toast.makeText(Signup.this, result, Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        },builder);

    async.execute();


    }

    public Boolean ValidSignup() {
        if (fname.getText().toString().trim().equals("")){
            Toast.makeText(this, "Enter Valid First Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (lname.getText().toString().trim().equals("")){
            Toast.makeText(this, "Enter Valid Last name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
            Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (repass.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please Re-Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!repass.getText().toString().trim().equals(pass.getText().toString().trim())) {
            Toast.makeText(this, "Password and Re-Entered Password do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;


    }
}