package com.example.alayb.myapplication.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.alayb.myapplication.AllUrls;
import com.example.alayb.myapplication.R;
import com.example.alayb.myapplication.adapters.HistoryAdapter;
import com.example.alayb.myapplication.asyncfiles.ServiceAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentHistory extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_product_history, container, false);
    }

    ArrayList<String> history_name, history_cat, history_img, history_id, product_id;
    RecyclerView rv;
    HistoryAdapter adapter;
    TextView none;
    ProgressDialog dialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialog=new ProgressDialog(getActivity());
        dialog.setTitle("Loading History");
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        rv = getActivity().findViewById(R.id.history_rv);
        none=getActivity().findViewById(R.id.history_none);
        history_name = new ArrayList<>();
        history_img = new ArrayList<>();
        history_id = new ArrayList<>();
        product_id = new ArrayList<>();
        history_cat = new ArrayList<>();
        adapter = new HistoryAdapter(getActivity(), history_name, history_cat, history_img, history_id, product_id);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);

        new ServiceAsync(AllUrls.Order, new ServiceAsync.OnAsyncResult() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    JSONObject object = array.getJSONObject(0);
                    if (object.getString("status").equals("valid") && object.getString("statusCode").equals("1")) {
                        rv.setVisibility(View.VISIBLE);
                        none.setVisibility(View.GONE);

                        for (int i = 1; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            history_name.add(jsonObject.getString("productName"));
                            history_img.add(jsonObject.getString("productImage"));
                            product_id.add(jsonObject.getString("productId"));
                            history_id.add(jsonObject.getString("purchaseId"));
                            history_cat.add(jsonObject.getString("categoryName"));
                        }

                        adapter.notifyDataSetChanged();
                    }
                    if (history_cat.size()==0){
                        rv.setVisibility(View.GONE);
                        none.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.cancel();
            }

            @Override
            public void onFailure(String result) {
                dialog.cancel();
                Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }, new Uri.Builder().appendQueryParameter("uId", getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE).getString("userId", ""))).execute();

    }
}