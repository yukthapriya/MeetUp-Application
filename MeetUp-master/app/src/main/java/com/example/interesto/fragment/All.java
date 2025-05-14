package com.example.interesto.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.interesto.R;
import com.example.interesto.activity.Grid_User_View;
import com.example.interesto.adapter.OtherUserAdapter;
import com.example.interesto.supportive.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class All extends Fragment {

    RecyclerView recyclerView;
    OtherUserAdapter otherUserAdapter;
    ArrayList<User> otherUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_all,null);

        recyclerView=(RecyclerView) view.findViewById(R.id.rcyview);
        otherUser=new ArrayList<>();
        otherUserAdapter=new OtherUserAdapter(getContext(),otherUser);
        recyclerView.setAdapter(otherUserAdapter);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
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


        return view;
    }
}