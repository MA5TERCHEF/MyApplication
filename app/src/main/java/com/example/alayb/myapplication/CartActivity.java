package com.example.alayb.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.PaymentMethod;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.alayb.myapplication.adapters.Cart_Adapter;
import com.example.alayb.myapplication.asyncfiles.ServiceAsync;
import com.example.alayb.myapplication.models.CartModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alayb on 17-Jul-19.
 */

public class CartActivity extends AppCompatActivity{

    RecyclerView rv;
    public Button btn;
    public TextView amt;
    Context context;
    ArrayList<CartModel> model;
    Cart_Adapter adapter;
    ProgressDialog dialog;
    String token;
    double cart_amount;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);
        rv=findViewById(R.id.cart_rv);
        amt=findViewById(R.id.total_amt);
        btn=findViewById(R.id.btn_checkout);
        dialog=new ProgressDialog(this);
        dialog.setTitle("Getting Cart Items");
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
        context=this;
        model=new ArrayList<>();
        toolbar=findViewById(R.id.toolbarcart);
        setSupportActionBar(toolbar);
        adapter=new Cart_Adapter(context,model);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
        cart_amount=0;


        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("action","all-cart-item");
        builder.appendQueryParameter("uId",getSharedPreferences("Login",MODE_PRIVATE).getString("userId",""));
        new ServiceAsync(AllUrls.Cart, new ServiceAsync.OnAsyncResult() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array=new JSONArray(result);
                    if (array.getJSONObject(0).getString("status").equals("valid")&&array.getJSONObject(0).getString("statusCode").equals("1")){
                        for (int i = 1; i <array.length() ; i++) {
                            JSONObject object=array.getJSONObject(i);
                            CartModel models=new CartModel(object.getString("productName"),object.getString("amount"),object.getString("quantity"),object.getString("image"),object.getString("cartId"),object.getString("productId"));
                            model.add(models);
                            cart_amount+=Double.parseDouble(models.getPrice())*Double.parseDouble(models.getQty());
                        }
                        amt.setText("Total Amount: ₹."+cart_amount);
                        adapter.notifyDataSetChanged();


                    }
                    else{
                        Toast.makeText(context, "No Items in Cart", Toast.LENGTH_SHORT).show();
                        btn.setEnabled(false);
                        btn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                        amt.setText("Total Amount: ₹.0.0");
                    }

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
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.setTitle("Loading Payment Gateway");
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();

            new ServiceAsync(AllUrls.Token, new ServiceAsync.OnAsyncResult() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject object=new JSONObject(result);
                        if (object.getString("status").equals("true")&&!object.getString("token").equals("")) {
                            token = object.getString("token");
                            DropInRequest request=new DropInRequest().clientToken(token);
                            startActivityForResult(request.getIntent(context),123);

                        }
                        else
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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
            }, new Uri.Builder()).execute();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==123&&resultCode== Activity.RESULT_OK){
            DropInResult result=data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
            PaymentMethodNonce paymentMethodNonce=result.getPaymentMethodNonce();
            String nonce=paymentMethodNonce.getNonce();
            apicallForSendingNonce(nonce);


        }
    }

    private void apicallForSendingNonce(String nonce) {
        dialog.setTitle("Purchasing");
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("userId",getSharedPreferences("Login",MODE_PRIVATE).getString("userId",""));
        builder.appendQueryParameter("totalAmount",""+cart_amount+"");
        builder.appendQueryParameter("nonce",nonce);
        Log.e("TAG",builder.toString());
        ServiceAsync async=new ServiceAsync(AllUrls.Notify, new ServiceAsync.OnAsyncResult() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array=new JSONArray(result);
                    JSONObject object=array.getJSONObject(0);
                    if (object.getString("status").equals("valid")&&object.getString("statusCode").equals("1")){
                    Toast.makeText(context, "Transaction Successful", Toast.LENGTH_SHORT).show();Toast.makeText(context, cart_amount+"", Toast.LENGTH_SHORT).show();Intent i=new Intent(context,DashboardActivity.class);
                    startActivity(i);
                    finish();
                    }

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
        }, builder);
        async.execute();
    }
}
