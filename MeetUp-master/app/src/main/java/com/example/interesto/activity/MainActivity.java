package com.example.interesto.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.interesto.R;
import com.example.interesto.adapter.UserAdapter;
import com.example.interesto.adapter.ViewPagerFragmentAdapter;
import com.example.interesto.databinding.ActivityMainBinding;
import com.example.interesto.supportive.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mainActivityObj;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ActivityMainBinding binding;
    ArrayList<User> users;
    public UserAdapter usersAdapter;
    public String selectedURL="";

    private String[] titles=new String[]{"Like Minded","All"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        mainActivityObj=this;
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        Intent prevIntent=getIntent();
        String selected=prevIntent.getStringExtra("selected");

        if(selected.equalsIgnoreCase("share"))
        {
            selectedURL=prevIntent.getStringExtra("url");
        }

        ViewPagerFragmentAdapter viewPagerFragmentAdapter=new ViewPagerFragmentAdapter(this);
        binding.viewPager2.setAdapter(viewPagerFragmentAdapter);

        new TabLayoutMediator(binding.tabLayout1,binding.viewPager2,((tab,position) -> tab.setText(titles[position]))).attach();

//        ProgressDialog dialog=new ProgressDialog(MainActivity.this);
//        dialog.setCancelable(false);
//        dialog.setMessage("Loading...");
//        dialog.show();
//
//        users=new ArrayList<User>();
//        usersAdapter=new UserAdapter(MainActivity.this,users);
//        binding.recyclerView.setAdapter(usersAdapter);
//
//        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                users.clear();
//                if(snapshot!=null)
//                {
//                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
//                    {
//                        User user=dataSnapshot1.getValue(User.class);
//                        if(!user.getUid().equalsIgnoreCase(auth.getUid())) {
//                            if(Information.userAsMe.getUinterest().equalsIgnoreCase(user.getUinterest())) {
//                                users.add(user);    //if user is not current one and user having same interest as current user
//                            }
//                        }
//                    }
//
//                    dialog.dismiss();
//                    usersAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();
            }
        });

    }

}