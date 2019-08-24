package com.example.alayb.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alayb.myapplication.asyncfiles.ServiceAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView txt1, txt2, forgot;
    Button btn1, btn2;
    Context context;
    CheckBox checked;
    static MainActivity activity;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog dialog;
    boolean staylogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        activity=this;
        txt1 = findViewById(R.id.Edt1);
        checked=findViewById(R.id.checked);
        txt2 = findViewById(R.id.Edt2);
        forgot = findViewById(R.id.forgot);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        dialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        staylogin = sharedPreferences.getBoolean("staylogin", false);
        if (staylogin) {
            Intent i = new Intent(context, DashboardActivity.class);
            startActivity(i);
            finish();
        }
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Signup.class);
                startActivity(i);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidLogin();
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgot.setTextColor(Color.parseColor("cyan"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        forgot.setTextColor(Color.parseColor("#0b98e4"));
                    }
                }, 1000);
                Intent i = new Intent(MainActivity.this, ForgotActivity.class);
                startActivity(i);
            }
        });
    }

    public void ValidLogin() {
        if (!Patterns.EMAIL_ADDRESS.matcher(txt1.getText().toString().trim()).matches())
            Toast.makeText(context, "Enter Valid Email", Toast.LENGTH_SHORT).show();


        else if(txt2.getText().toString().equals(""))
            Toast.makeText(context, "Enter Valid Password", Toast.LENGTH_SHORT).show();

        else {
            dialog.setTitle("Logging In");
            dialog.setMessage("Please wait");
            dialog.setCancelable(false);
            dialog.show();
            Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("email", txt1.getText().toString().trim());
            builder.appendQueryParameter("password", txt2.getText().toString().trim());

            ServiceAsync async = new ServiceAsync(AllUrls.Login, new ServiceAsync.OnAsyncResult() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject object = new JSONObject(result);
                        String status = object.getString("status");
                        String statuscode = object.getString("statusCode");
                        if (statuscode.equals("1") && status.equals("valid")) {
                            String userId = object.getString("uId");
                            String email = txt1.getText().toString().trim();
                            String firstName = object.getString("firstName");
                            String lastName = object.getString("lastName");
                            String userName = firstName + " " + lastName;
                            String address = object.getString("address");
                            String address2 = object.getString("address2");
                            String phone = object.getString("phone");
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            if (checked.isChecked())
                            editor.putBoolean("staylogin", true);
                            Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                            editor.putString("userName", userName);
                            editor.putString("userId", userId);
                            editor.putString("email", email);
                            editor.putString("lastName", lastName);
                            editor.putString("firstName", firstName);
                            editor.putString("phone", phone);
                            editor.putString("address", address);
                            editor.putString("address2", address2);
                            editor.putString("profile", object.getString("profileImg"));
                            editor.commit();
                            startActivity(i);
                            finish();

                        } else
                            Toast.makeText(MainActivity.this, "Wrong Password or Email", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.e("TAG", e + "");
                        e.printStackTrace();
                    }
                    dialog.cancel();
                }

                @Override
                public void onFailure(String result) {
                    Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }, builder);
            async.execute();
        }
    }
    public static Activity sendActivity(){
        return activity;
    }
}