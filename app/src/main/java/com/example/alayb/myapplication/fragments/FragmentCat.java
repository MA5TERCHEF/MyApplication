package com.example.alayb.myapplication.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alayb.myapplication.AllUrls;
import com.example.alayb.myapplication.R;
import com.example.alayb.myapplication.adapters.Cat_Adapter;
import com.example.alayb.myapplication.asyncfiles.ServiceAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alayb on 24-Jul-19.
 */

public class FragmentCat extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_category, container, false);
        return view;
    }
    ArrayList<String> category;
    ArrayList<String> cat_image;
    ArrayList<String> cat_id;
    RecyclerView rv;
    Cat_Adapter adapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cat_image = new ArrayList<>();
        category = new ArrayList<>();
        cat_id= new ArrayList<>();
        rv = view.findViewById(R.id.cat_rv);
        adapter = new Cat_Adapter(getContext(), category, cat_image,cat_id);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);


        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("action", "category");
        ServiceAsync async = new ServiceAsync(AllUrls.Category, new ServiceAsync.OnAsyncResult() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i = 1; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        cat_id.add(object.getString("categoryId"));
                        category.add(object.getString("categoryName"));
                        cat_image.add(object.getString("categoryImage"));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String result) {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
        }, builder);
        async.execute();



    }
}