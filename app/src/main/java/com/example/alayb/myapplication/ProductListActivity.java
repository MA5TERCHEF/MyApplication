package com.example.alayb.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.alayb.myapplication.adapters.ProductList_Adapter;
import com.example.alayb.myapplication.asyncfiles.ServiceAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<String> product_id;
    ArrayList<String> product_name;
    ArrayList<String> product_image;
    ArrayList<String> product_amount;
    ArrayList<String> product_rating;
    ProgressDialog dialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        dialog=new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        product_id=new ArrayList<>();
        product_name=new ArrayList<>();
        product_image=new ArrayList<>();
        product_amount=new ArrayList<>();
        product_rating=new ArrayList<>();
        toolbar=findViewById(R.id.toolbarproduct);
        setSupportActionBar(toolbar);

        rv=findViewById(R.id.product_rv);
        final ProductList_Adapter adapter=new ProductList_Adapter(this,product_id,product_name,product_image,product_amount,product_rating);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        String id=getSharedPreferences("Login", Context.MODE_PRIVATE).getString("subcatid","");
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("action","product");
        builder.appendQueryParameter("subcatId",id);
        ServiceAsync async= new ServiceAsync(AllUrls.Category, new ServiceAsync.OnAsyncResult() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object=new JSONObject(result);
                    JSONArray array=object.getJSONArray("products");
                    for (int j = 0; j <array.length() ; j++) {
                        JSONObject product =array.getJSONObject(j);
                        product_id.add(product.getString("id"));
                        product_name.add(product.getString("productName"));
                        product_amount.add(product.getString("amount"));
                        product_rating.add(product.getString("average_rating"));
                        product_image.add(product.getString("image"));
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("TAG",e+"");
                }
                dialog.cancel();

            }

            @Override
            public void onFailure(String result) {
                dialog.cancel();
                Toast.makeText(ProductListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }, builder);
        async.execute();



    }
}
