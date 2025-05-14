package com.example.interesto.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.interesto.R;
import com.example.interesto.activity.Information;
import com.example.interesto.activity.MainActivity;
import com.example.interesto.adapter.UserAdapter;
import com.example.interesto.supportive.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Like_Minded extends Fragment {

    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<User> users;
    public static UserAdapter usersAdapter;
    RecyclerView recyclerView;
    TextView empty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_like__minded,null);

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        empty=(TextView) view.findViewById(R.id.empty);

        ProgressDialog dialog=new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");
        dialog.show();

        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView1);
        users=new ArrayList<User>();
        usersAdapter=new UserAdapter(getContext(),users);
        recyclerView.setAdapter(usersAdapter);

        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                if(snapshot!=null)
                {
                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                    {
                        User user=dataSnapshot1.getValue(User.class);
                        try {
                            if (!user.getUid().equalsIgnoreCase(auth.getUid())) {
                                if (Information.userAsMe.getUinterest().equalsIgnoreCase(user.getUinterest())) {
                                    users.add(user);    //if user is not current one and user having same interest as current user
                                }
                            }
                        }catch (Exception e)
                        {
                            System.out.println(e.toString());
                            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(users.size()==0) {
                        empty.setVisibility(View.VISIBLE);
                    }
                    else {
                        empty.setVisibility(View.GONE);
                    }
                    dialog.dismiss();
                    usersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

}