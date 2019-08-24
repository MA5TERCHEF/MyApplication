package com.example.alayb.myapplication.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alayb.myapplication.AllUrls;
import com.example.alayb.myapplication.CartActivity;
import com.example.alayb.myapplication.R;
import com.example.alayb.myapplication.asyncfiles.ServiceAsync;
import com.example.alayb.myapplication.models.CartModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by alayb on 17-Jul-19.
 */

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.Holder> {
Context context;
ArrayList<CartModel> model;

    public Cart_Adapter(Context context,ArrayList<CartModel> model){
        this.context=context;
        this.model=model;
    }

    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        return new Holder(layoutInflater.inflate(R.layout.cart_listing_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
    holder.name_cart.setText(model.get(i).getName());
    holder.qty_cart.setText(model.get(i).getQty());
    holder.price_cart.setText("₹. "+model.get(i).getPrice());
    holder.position=i;
    Glide.with(context).load(model.get(i).getImg()).into(holder.img_cart);

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        int position;
        TextView name_cart,price_cart,qty_cart,remove_cart;
        ImageView img_cart;


        public Holder(@NonNull View itemView) {
            super(itemView);
            name_cart=itemView.findViewById(R.id.cart_name);
            img_cart=itemView.findViewById(R.id.cart_img);
            price_cart=itemView.findViewById(R.id.cart_amt);
            qty_cart=itemView.findViewById(R.id.cart_qty);
            remove_cart=itemView.findViewById(R.id.cart_remove);

            remove_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog dialog=new ProgressDialog(context);
                    dialog.setTitle("Removing Item");
                    dialog.setMessage("Please wait");
                    dialog.setCancelable(false);
                    dialog.show();

                    Uri.Builder builder=new Uri.Builder();
                    builder.appendQueryParameter("action","remove-from-cart");
                    builder.appendQueryParameter("uId", context.getSharedPreferences("Login",MODE_PRIVATE).getString("userId",""));
                    builder.appendQueryParameter("cartId",model.get(position).getCartId());
                    Log.e("TAG","Message : "+builder.toString());
                    new ServiceAsync(AllUrls.Cart, new ServiceAsync.OnAsyncResult() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONArray array=new JSONArray(result);
                                JSONObject object=array.getJSONObject(0);
                                if (object.getString("status").equals("valid")&&object.getString("statusCode").equals("1")){
                                    Toast.makeText(context, "Item Removed From Cart", Toast.LENGTH_SHORT).show();
                                    model.remove(position);
                                    notifyDataSetChanged();
                                }
                                else if (model.size()==1&&object.getString("status").equals("valid")&&object.getString("statusCode").equals("2")){
                                    Toast.makeText(context, "Item Removed From Cart", Toast.LENGTH_SHORT).show();
                                    model.remove(position);
                                    ((CartActivity)context).btn.setEnabled(false);
                                    ((CartActivity)context).btn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                                    notifyDataSetChanged();

                                }
                                else
                                    Toast.makeText(context, "Could Not Delete Item", Toast.LENGTH_SHORT).show();
                                double dbl = 0;
                                for (int i = 0; i <model.size() ; i++) {
                                    dbl+=Double.parseDouble(model.get(i).getPrice())*Double.parseDouble(model.get(i).getQty());
                                }
                                 ((CartActivity)context).amt.setText("Total Amount ₹."+dbl);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.cancel();

                        }

                        @Override
                        public void onFailure(String result) {
                            dialog.cancel();
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                        }
                    }, builder).execute();

                }
            });
        }
    }
}
