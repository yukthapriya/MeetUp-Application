package com.example.interesto.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.interesto.R;
import com.example.interesto.adapter.PagerAdapter;
import com.example.interesto.networkStatus.NetworkChangeListener;
import com.example.interesto.supportive.MyAPIKey;
import com.example.interesto.supportive.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class Information extends AppCompatActivity {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    public static User userAsMe;
    TabLayout tabLayout;
    TabItem ihome,iscience,ientertainment,itechnology,isports,ihealth,ibussiness;
    PagerAdapter pagerAdapter;
    ViewPager viewPager;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ImageView userImgView,messengerView;
    ArrayList<String> tabToOpen=new ArrayList<>(Arrays.asList("General","Sports","Science","Health","Entertainment","Technology","Bussiness"));

    //private final String API_KEY="838f118f96444870bd7e8acd1e885f13";
    private String API_KEY= MyAPIKey.getMyKey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getSupportActionBar().hide();

        userImgView=(ImageView)findViewById(R.id.userImg);
        messengerView=(ImageView)findViewById(R.id.messenger);

        tabLayout=(TabLayout) findViewById(R.id.include);

        ihome=(TabItem) findViewById(R.id.home);
        isports=(TabItem) findViewById(R.id.sports);
        iscience=(TabItem) findViewById(R.id.science);
        ihealth=(TabItem) findViewById(R.id.health);
        ientertainment=(TabItem) findViewById(R.id.entertainment);
        itechnology=(TabItem) findViewById(R.id.technology);
        ibussiness=(TabItem) findViewById(R.id.bussiness);

        viewPager=(ViewPager)findViewById(R.id.fragmentcontainer);

        pagerAdapter=new PagerAdapter(getSupportFragmentManager(),7);
        viewPager.setAdapter(pagerAdapter);

        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        getUserInfo();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0 || tab.getPosition()==1 || tab.getPosition()==2 || tab.getPosition()==3 ||tab.getPosition()==4 ||tab.getPosition()==5 ||tab.getPosition()==6)
                {
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        userImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Information.this,Profile.class);
                Information.this.startActivity(intent);
            }
        });

        messengerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userAsMe!=null) {
                    Intent intent = new Intent(Information.this, MainActivity.class);
                    intent.putExtra("selected", "messenger");
                    Information.this.startActivity(intent);
                }
                else{
                    getUserInfo();
                    Toast.makeText(Information.this,"We have detected a Slow connection, please wait for a while",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void getUserInfo()
    {
        database.getReference().child("users").child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    User user=task.getResult().getValue(User.class);
                    userAsMe=task.getResult().getValue(User.class);
                    Glide.with(Information.this).load(user.getProfileImage())    //using Glide library for image processing
                            .placeholder(R.drawable.user)
                            .into(userImgView);
                    viewPager.setCurrentItem(tabToOpen.indexOf(user.getUinterest()));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}