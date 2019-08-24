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
import com.example.alayb.myapplication.ProductListActivity;
import com.example.alayb.myapplication.R;

import java.util.ArrayList;

/**
 * Created by alayb on 05-Jul-19.
 */

public class SubCat_Adapter extends RecyclerView.Adapter<SubCat_Adapter.Holder> {

    ArrayList<String> sub_id;
    ArrayList<String> sub_image;
    ArrayList<String> sub_name;
    Context context;

    public SubCat_Adapter(Context context,ArrayList<String> sub_id,ArrayList<String> sub_name,ArrayList<String> sub_image){
        this.sub_id=sub_id;
        this.sub_image=sub_image;
        this.sub_name=sub_name;
        this.context=context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new SubCat_Adapter.Holder(layoutInflater.inflate(R.layout.cat_rv_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.txt.setText(sub_name.get(i));
        holder.pos=i;
        Glide.with(context).load(sub_image.get(i)).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return sub_id.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txt;
        int pos;
        ImageView img;
        public Holder(@NonNull final View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.cat_name);
            img = itemView.findViewById(R.id.cat_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context, ProductListActivity.class);
                    context.startActivity(i);
                    context.getSharedPreferences("Login",Context.MODE_PRIVATE).edit().putString("subcatid",sub_id.get(pos)).commit();
                }
            });
        }
    }
}
