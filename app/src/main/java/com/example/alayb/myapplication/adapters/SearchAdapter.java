package com.example.alayb.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alayb.myapplication.ProductDetails;
import com.example.alayb.myapplication.R;
import com.example.alayb.myapplication.models.SearchResultModel;

import java.util.ArrayList;

/**
 * Created by alayb on 19-Jul-19.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder>{
    Context context;
    ArrayList<SearchResultModel> model;

    public SearchAdapter(Context context, ArrayList<SearchResultModel> model) {
        this.context = context;
        this.model = model;
    }

    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new SearchAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.search_result_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.position=i;
        holder.name.setText(model.get(i).getName());
        holder.price.setText(model.get(i).getPrice());
        holder.category.setText(model.get(i).getCategory());
        Glide.with(context).load(model.get(i).getImage()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        int position;
        ImageView img;
        TextView name,price,category;

        public Holder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.search_result_image);
            name=itemView.findViewById(R.id.search_result_name);
            price=itemView.findViewById(R.id.search_result_price);
            category=itemView.findViewById(R.id.search_result_category);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i =new Intent(context, ProductDetails.class);
                    i.putExtra("productid",model.get(position).getId());
                    context.startActivity(i);
                }
            });
        }
    }
}
