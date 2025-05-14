package com.example.interesto.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.interesto.R;
import com.example.interesto.adapter.OtherUserAdapter;
import com.example.interesto.supportive.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Grid_User_View extends AppCompatActivity {

    RecyclerView recyclerView;
    OtherUserAdapter otherUserAdapter;
    ArrayList<User> otherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_user_view);

        recyclerView=(RecyclerView) findViewById(R.id.rcyview);
        otherUser=new ArrayList<>();
        otherUserAdapter=new OtherUserAdapter(Grid_User_View.this,otherUser);
        recyclerView.setAdapter(otherUserAdapter);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                otherUser.clear();
                if(snapshot!=null)
                {
                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                    {
                        User user=dataSnapshot1.getValue(User.class);
                        if(!user.getUid().equalsIgnoreCase(FirebaseAuth.getInstance().getUid())) {
                            otherUser.add(user);    //if user is not current one
                        }
                    }
                    otherUserAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}