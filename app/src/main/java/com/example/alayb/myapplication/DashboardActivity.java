package com.example.alayb.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alayb.myapplication.fragments.FragmentCat;
import com.example.alayb.myapplication.fragments.FragmentHistory;
import com.example.alayb.myapplication.fragments.FragmentProfile;

import java.util.ArrayList;

/**
 * Created by alayb on 27-Jun-19.
 */

public class DashboardActivity extends AppCompatActivity {

    TextView nav_username;
    TextView nav_email;
    TextView title;
    ImageView nav_image, search_img;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DrawerLayout draw;
    static DashboardActivity dash;
    NavigationView nav;
    FrameLayout frame;
    android.support.v7.widget.Toolbar toolbar;
    ActionBarDrawerToggle bar;
    ArrayList<Fragment> frag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dash = this;
        frame = findViewById(R.id.frame);
        draw = findViewById(R.id.draw);
        search_img = findViewById(R.id.search_image);
        nav = findViewById(R.id.nav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        title=findViewById(R.id.toolbar_title);
        frag = new ArrayList<>();
        frag.add(new FragmentCat());
        frag.add(new FragmentProfile());
        frag.add(new FragmentHistory());
        bar = new ActionBarDrawerToggle(this, draw, toolbar, R.string.open, R.string.close);
        bar.getDrawerArrowDrawable().setColor(Color.parseColor("White"));
        draw.addDrawerListener(bar);
        bar.syncState();
        nav_username = nav.getHeaderView(0).findViewById(R.id.nav_username);
        nav_email = nav.getHeaderView(0).findViewById(R.id.nav_email);
        nav_image = nav.getHeaderView(0).findViewById(R.id.nav_img);
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        nav_username.setText(sharedPreferences.getString("userName", "User Name"));
        nav_email.setText(sharedPreferences.getString("email", "Email"));

        String imagepath = sharedPreferences.getString("profile", "");
        if (!imagepath.equals("")) {
            Glide.with(this).load(imagepath).into(nav_image);
        }

        Intent i=getIntent();
        boolean frags=i.getBooleanExtra("isfromEdit",false);
        if (frags){
            Log.e("TAG","isfromEdit");
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, frag.get(1)).commit();
            title.setText("My Profile");
        }
        else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, frag.get(0)).commit();
            title.setText("Categories");
        }

        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, SearchActivity.class);
                startActivity(i);
            }
        });

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu1) {
                    title.setText("Categories");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame, frag.get(0)).commit();


                    /*setTitle(menuItem.getTitle());*/

                    draw.closeDrawers();
                }
                if (menuItem.getItemId() == R.id.menu2) {
                    title.setText("My Profile");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame, frag.get(1)).commit();

                    /*setTitle(menuItem.getTitle());*/

                    draw.closeDrawers();
                }if (menuItem.getItemId() == R.id.menu4) {
                    title.setText("Purchase History");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame, frag.get(2)).commit();

                    /*setTitle(menuItem.getTitle());*/

                    draw.closeDrawers();
                }

                if (menuItem.getItemId() == R.id.menu3) {
                    editor.putBoolean("staylogin", false);
                    editor.commit();
                    Intent i = new Intent(DashboardActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }
                menuItem.setChecked(true);
                
                return true;
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("TAG","Activity on Activity result");
        if (requestCode==123&&resultCode== Activity.RESULT_OK){
            Log.e("TAG","Activity on Activity result in IF");
            int a=data.getIntExtra("frag",0);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,new FragmentProfile());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i=new Intent(this,CartActivity.class);
        startActivity(i);

        return super.onOptionsItemSelected(item);
    }
}