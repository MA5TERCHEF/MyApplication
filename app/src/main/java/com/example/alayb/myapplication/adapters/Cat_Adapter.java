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
import com.example.alayb.myapplication.R;
import com.example.alayb.myapplication.SubCatActivity;

import java.util.ArrayList;

/**
 * Created by alayb on 04-Jul-19.
 */

public class Cat_Adapter extends RecyclerView.Adapter<Cat_Adapter.Holder> {

    ArrayList<String> category;
    ArrayList<String> cat_image;
    ArrayList<String> cat_id;
    Context context;

    public Cat_Adapter(Context context, ArrayList<String> category, ArrayList<String> cat_image, ArrayList<String> cat_id) {
        this.cat_image = cat_image;
        this.cat_id = cat_id;
        this.category = category;
        this.context = context;

    }

    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new Holder(layoutInflater.inflate(R.layout.cat_rv_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        holder.txt.setText(category.get(i));
        holder.pos=i;
        Glide.with(context).load(cat_image.get(i)).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return cat_image.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txt;
        int pos;
        ImageView img;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.cat_name);
            img = itemView.findViewById(R.id.cat_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context, SubCatActivity.class);
                    context.startActivity(i);
                    context.getSharedPreferences("Login",Context.MODE_PRIVATE).edit().putString("Cat_Id",cat_id.get(pos)).commit();

                }
            });
        }
    }
}