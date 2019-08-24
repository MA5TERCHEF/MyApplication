package com.example.alayb.myapplication;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.alayb.myapplication.asyncfiles.ServiceAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by alayb on 10-Jul-19.
 */

public class ProductDetails extends AppCompatActivity {

    ImageView img;
    Button btn;
    TextView name, desc, amount, disc;
    Spinner spin;
    String quantity;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    android.support.v7.widget.Toolbar toolbar;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Intent i = getIntent();
        final String id = i.getStringExtra("productid");
        img = findViewById(R.id.product_details_image);
        name = findViewById(R.id.product_details_name);
        desc = findViewById(R.id.product_details_descrip);
        toolbar = findViewById(R.id.toolbar2);

        dialog=new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        amount = findViewById(R.id.product_details_amount);
        disc = findViewById(R.id.product_details_discount);
        btn = findViewById(R.id.btn_cart);
        preferences = getSharedPreferences("Login", MODE_PRIVATE);
        editor = preferences.edit();
        spin = findViewById(R.id.cart_spin);
        String qty[] = {"1", "2", "3", "4", "5"};
        spin.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, qty));
        spin.setSelection(0);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                quantity = i + 1 + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        new ServiceAsync(AllUrls.Product, new ServiceAsync.OnAsyncResult() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.e("tag", result);
                    JSONObject object = new JSONObject(result);
                    name.setText("Name: " + object.getString("productName"));
                    desc.setText(object.getString("productDesc"));
                    amount.setText("₹. " + object.getString("amount"));
                    disc.setText("₹. " + object.getString("orgAmount"));
                    JSONArray array = object.getJSONArray("productImages");
                    array.getJSONArray(0);
                    Glide.with(ProductDetails.this).load(array.getJSONArray(0).getString(0)).into(img);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.cancel();

            }

            @Override
            public void onFailure(String result) {
                dialog.cancel();

            }
        }, new Uri.Builder().appendQueryParameter("pId", id)).execute();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.setTitle("Adding Item to Cart");
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();

                Uri.Builder builder = new Uri.Builder();
                builder.appendQueryParameter("action", "add-to-cart");
                builder.appendQueryParameter("uId", getSharedPreferences("Login", MODE_PRIVATE).getString("userId", ""));
                builder.appendQueryParameter("pId", id);
                builder.appendQueryParameter("qty", quantity);
                new ServiceAsync(AllUrls.Cart, new ServiceAsync.OnAsyncResult() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONArray array = new JSONArray(result);
                            JSONObject object = array.getJSONObject(0);
                            JSONObject object2 = array.getJSONObject(1);
                            editor.putString("cartId", object2.getString("cartId"));
                            Toast.makeText(ProductDetails.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            Toast.makeText(ProductDetails.this, "" + e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        dialog.cancel();
                    }

                    @Override
                    public void onFailure(String result) {
                        dialog.cancel();
                        Toast.makeText(ProductDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }, builder).execute();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cart_icon) {
            Intent i = new Intent(this, CartActivity.class);
            startActivity(i);
        }
        return true;
    }
}