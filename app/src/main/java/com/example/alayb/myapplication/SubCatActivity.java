package com.example.alayb.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.alayb.myapplication.adapters.SubCat_Adapter;
import com.example.alayb.myapplication.asyncfiles.ServiceAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubCatActivity extends AppCompatActivity {

    RecyclerView subcat_rv;
    ArrayList<String> sub_id;
    ArrayList<String> sub_name;
    ProgressDialog dialog;
    ArrayList<String> sub_image;
    android.support.v7.widget.Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        dialog=new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        sub_id = new ArrayList<>();
        sub_name = new ArrayList<>();
        sub_image = new ArrayList<>();
        subcat_rv = findViewById(R.id.subcat_rv);
        final SubCat_Adapter adapter = new SubCat_Adapter(this, sub_id, sub_name, sub_image);
        subcat_rv.setLayoutManager(new LinearLayoutManager(this));
        subcat_rv.setAdapter(adapter);
        toolbar=findViewById(R.id.toolbarsubcat);
        setSupportActionBar(toolbar);
        String id = getSharedPreferences("Login", Context.MODE_PRIVATE).getString("Cat_Id","");
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("action", "subcategory");
        builder.appendQueryParameter("catId", id);
        ServiceAsync async = new ServiceAsync(AllUrls.Category, new ServiceAsync.OnAsyncResult() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i = 1; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        sub_id.add(object.getString("subCategoryId"));
                        sub_name.add(object.getString("subCategoryName"));
                        sub_image.add(object.getString("categoryImage"));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.cancel();

            }

            @Override
            public void onFailure(String result) {
                dialog.cancel();
                Toast.makeText(SubCatActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }, builder);
        async.execute();
    }
}
