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
import com.example.alayb.myapplication.ReviewActivity;

import java.util.ArrayList;

/**
 * Created by alayb on 20-Jul-19.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder> {
    ArrayList<String> history_name,history_cat,history_img,history_id,product_id;
    Context context;

    public HistoryAdapter(Context context, ArrayList<String> history_name, ArrayList<String> history_cat, ArrayList<String> history_img, ArrayList<String> history_id,ArrayList<String> product_id) {
    this.context=context;
    this.history_name=history_name;
    this.history_cat=history_cat;
    this.history_id=history_id;
    this.history_img=history_img;
    this.product_id=product_id;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HistoryAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.product_history_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        Glide.with(context).load(history_img.get(i)).into(holder.img);
        holder.name.setText(history_name.get(i));
        holder.cat.setText(history_cat.get(i));
        holder.position=i;
    }

    @Override
    public int getItemCount() {
        return history_name.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView name,cat,product,review;
        ImageView img;
        int position;

        public Holder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.history_img);
            name=itemView.findViewById(R.id.history_name);
            product=itemView.findViewById(R.id.history_product);
            review=itemView.findViewById(R.id.history_review);
            cat=itemView.findViewById(R.id.history_cat);
            product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context, ProductDetails.class);
                    i.putExtra("productid",product_id.get(position));
                    context.startActivity(i);
                }
            });
            review.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Intent i=new Intent(context,ReviewActivity.class);
                                              i.putExtra("id",product_id.get(position));
                                              i.putExtra("name",history_name.get(position));
                                              i.putExtra("img",history_img.get(position));
                                              i.putExtra("cat",history_cat.get(position));
                                              context.startActivity(i);

                                          }
                                      }
            );
        }
    }
}