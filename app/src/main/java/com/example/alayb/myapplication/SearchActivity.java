package com.example.alayb.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alayb.myapplication.adapters.SearchAdapter;
import com.example.alayb.myapplication.asyncfiles.ServiceAsync;
import com.example.alayb.myapplication.models.SearchResultModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alayb on 19-Jul-19.
 */

public class SearchActivity extends AppCompatActivity{

    RecyclerView rv;
    SearchAdapter adapter;
    EditText searchtxt;
    TextView none;
    ImageView search_img;
    ProgressDialog dialog;
    ArrayList<SearchResultModel> model;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent i=getIntent();
        model=new ArrayList<>();


        search_img = findViewById(R.id.search_img);
        none=findViewById(R.id.search_none);
        searchtxt=findViewById(R.id.search_text);
        adapter=new SearchAdapter(this,model);
        rv=findViewById(R.id.search_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=new ProgressDialog(SearchActivity.this);
                dialog.setTitle("Searching");
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();

                model.clear();

                String search=searchtxt.getText().toString();

                new ServiceAsync(AllUrls.Search, new ServiceAsync.OnAsyncResult() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject object=new JSONObject(result);
                            JSONArray array=object.getJSONArray("search");
                            for (int i = 0; i <array.length() ; i++) {
                                JSONObject firstobj=array.getJSONObject(i);
                                SearchResultModel model1=new SearchResultModel(firstobj.getString("id"),firstobj.getString("productName"),firstobj.getString("originalPrice"),firstobj.getString("discountPrice"),firstobj.getString("categoryName"),firstobj.getString("image"));
                                model.add(model1);
                            }


                            rv.setVisibility(View.VISIBLE);
                            none.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Log.e("TAG",e+"");
                            e.printStackTrace();
                            rv.setVisibility(View.GONE);
                            none.setVisibility(View.VISIBLE);
                            Toast.makeText(SearchActivity.this, "No Items Found", Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();
                    }

                    @Override
                    public void onFailure(String result) {
                        dialog.cancel();
                        Toast.makeText(SearchActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }, new Uri.Builder().appendQueryParameter("searchChar", search)).execute();

            }
        });
    }
}