package com.example.alayb.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alayb.myapplication.ProductDetails;
import com.example.alayb.myapplication.R;

import java.util.ArrayList;

/**
 * Created by alayb on 08-Jul-19.
 */

public class ProductList_Adapter extends RecyclerView.Adapter<ProductList_Adapter.Holder> {
    Context context;
    ArrayList<String> product_id;
    ArrayList<String> product_name;
    ArrayList<String> product_image;
    ArrayList<String> product_amount;
    ArrayList<String> product_rating;

    public ProductList_Adapter(Context context, ArrayList<String> product_id, ArrayList<String> product_name, ArrayList<String> product_image, ArrayList<String> product_amount, ArrayList<String> product_rating) {
        this.context=context;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_amount = product_amount;
        this.product_rating = product_rating;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new ProductList_Adapter.Holder(layoutInflater.inflate(R.layout.product_list_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.pos=i;
        holder.txt.setText(product_name.get(i));
        holder.amount.setText("₹. "+product_amount.get(i));
        holder.rating.setRating(Float.parseFloat(product_rating.get(i)));
        Glide.with(context).load(product_image.get(i)).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return product_amount.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txt,amount;
        ImageView img;
        int pos;
        RatingBar rating;
        public Holder(@NonNull final View itemView) {
            super(itemView);

            txt = itemView.findViewById(R.id.product_list_name);
            img = itemView.findViewById(R.id.product_list_img);
            amount=itemView.findViewById(R.id.product_list_amount);
            rating=itemView.findViewById(R.id.product_list_rating);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i =new Intent(context, ProductDetails.class);
                    i.putExtra("productid",product_id.get(pos));
                    context.startActivity(i);

                }
            });
        }
    }
}
