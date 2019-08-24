package com.example.alayb.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alayb.myapplication.asyncfiles.ServiceAsync;
import com.example.alayb.myapplication.asyncfiles.UploadImageAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;


public class EditProfile extends AppCompatActivity {

    EditText fname, lname, phone, add1, add2;
    ImageView img,change;
    Button btn;
    SharedPreferences shared;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    String path;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        dialog = new ProgressDialog(this);
        fname = findViewById(R.id.edt_fname);
        lname = findViewById(R.id.edt_lname);
        phone = findViewById(R.id.edt_phone);
        toolbar=findViewById(R.id.toolbaredit);
        setSupportActionBar(toolbar);
        add1 = findViewById(R.id.edt_add1);
        add2 = findViewById(R.id.edt_add2);
        img = findViewById(R.id.edt_image);
        change = findViewById(R.id.edt_image_click);
        btn = findViewById(R.id.edt_btn);
        shared = getSharedPreferences("Login", MODE_PRIVATE);
        editor = shared.edit();
        path = shared.getString("profile", "");
        Glide.with(EditProfile.this).load(path).into(img);

        fname.setText(shared.getString("firstName",""));
        lname.setText(shared.getString("lastName",""));
        phone.setText(shared.getString("phone",""));
        add1.setText(shared.getString("address",""));
        add2.setText(shared.getString("address2",""));


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkManualPermission();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("Updating Profile");
                dialog.setMessage("Please Wait");
                dialog.show();
                dialog.setCancelable(false);
                Uri.Builder builder = new Uri.Builder();
                builder.appendQueryParameter("action", "updateProfile");
                builder.appendQueryParameter("userid", shared.getString("userId", ""));
                builder.appendQueryParameter("firstName", fname.getText().toString().trim());
                builder.appendQueryParameter("lastName", lname.getText().toString().trim());
                builder.appendQueryParameter("phone", phone.getText().toString().trim());
                builder.appendQueryParameter("address", add1.getText().toString().trim());
                builder.appendQueryParameter("address2", add2.getText().toString().trim());
                ServiceAsync async = new ServiceAsync(AllUrls.Edit, new ServiceAsync.OnAsyncResult() {
                    @Override
                    public void onSuccess(String result) {
                        dialog.cancel();
                        try {
                            JSONObject object = new JSONObject(result);
                            String status = object.getString("status");
                            String message = object.getString("message");
                            if (status.equals("true")) {
                                editor.putString("userName", fname.getText().toString().trim() + " " + lname.getText().toString().trim());
                                editor.putString("firstName", fname.getText().toString().trim());
                                editor.putString("lastName", lname.getText().toString().trim());
                                editor.putString("phone", phone.getText().toString().trim());
                                editor.putString("address", add1.getText().toString().trim());
                                editor.putString("address2", add2.getText().toString().trim());
                                editor.commit();
                                Toast.makeText(EditProfile.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(EditProfile.this,DashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("isfromEdit",true);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG", e + "");
                        }
                    }
                    @Override
                    public void onFailure(String result) {
                        dialog.cancel();
                        Toast.makeText(EditProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }, builder);
                async.execute();
            }
        });
    }

    private void checkManualPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1234);
        }
        else{
            openGallery();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1234){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
            else
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && data != null) {
            Uri uri = data.getData();
            Log.e("TAG", "Uri Path : " + uri);
            path = findRealPath(uri);
            if (!path.equals("")){
                apiCallForUploadImage(path);
            }
            img.setImageURI(uri);
            Log.e("TAG", "Real Path : " + path);
        }
    }

    private void apiCallForUploadImage(final String path) {
        dialog.setTitle("Updating Profile Picture");
        dialog.setMessage("Please Wait");
        dialog.show();
        dialog.setCancelable(false);

        HashMap<String, String> mapVal = new HashMap<>();
        mapVal.put("action", "changeProfilePic");
        mapVal.put("userid", shared.getString("userId", ""));

        HashMap<String, File> mapFile = new HashMap<>();
        mapFile.put("profilepic", new File(path));
        UploadImageAsync imageAsync = new UploadImageAsync(AllUrls.Edit, mapVal, mapFile, new UploadImageAsync.OnAsyncResult() {
            @Override
            public void onSuccess(String result) {
                dialog.cancel();
                try {
                    JSONObject object = new JSONObject(result);

                    if (object.getString("status").equals("true")) {
                        editor.putString("profile", object.getString("profilepic"));
                        editor.commit();
                        Glide.with(EditProfile.this).load(path).into(img);
                        Toast.makeText(EditProfile.this, "Profile Pic Successfully Changed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("TAG",e+"");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String result) {
                Toast.makeText(EditProfile.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();;
                dialog.cancel();
            }
        });
        imageAsync.execute();
    }

    private String findRealPath(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturepath = cursor.getString(columnIndex);
        cursor.close();
        return picturepath;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(EditProfile.this,DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isfromEdit",true);
        startActivity(intent);
        finish();

    }
}