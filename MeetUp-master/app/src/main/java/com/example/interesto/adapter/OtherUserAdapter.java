package com.example.interesto.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.interesto.R;
import com.example.interesto.activity.ChattingActivity;
import com.example.interesto.activity.MainActivity;
import com.example.interesto.supportive.User;

import java.util.ArrayList;

public class OtherUserAdapter extends RecyclerView.Adapter<OtherUserAdapter.OtherViewHolder> {

    Context context;
    ArrayList<User> otherUser;

    public OtherUserAdapter(Context context, ArrayList<User> otherUser) {
        this.context = context;
        this.otherUser = otherUser;
    }

    @NonNull
    @Override
    public OtherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.other_user,parent,false);
        return new OtherUserAdapter.OtherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherViewHolder holder, int position) {
        User user=otherUser.get(position);
        holder.uname.setText(user.getName());        //setting name of user
        Glide.with(context).load(user.getProfileImage())    //using Glide library for image processing
                .placeholder(R.drawable.user)
                .into(holder.uimg);
        holder.uabout.setText(user.getUabout());
        holder.ucategory.setText(user.getUinterest());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChattingActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("uid",user.getUid());
                intent.putExtra("imageuri",user.getProfileImage());
                intent.putExtra("selectedURL", MainActivity.mainActivityObj.selectedURL);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return otherUser.size();
    }

    public class OtherViewHolder extends RecyclerView.ViewHolder {

        ImageView uimg;
        TextView uname,ucategory,uabout;

        public OtherViewHolder(@NonNull View itemView) {
            super(itemView);
            uimg=(ImageView) itemView.findViewById(R.id.uimage);
            uname=(TextView) itemView.findViewById(R.id.uname);
            ucategory=(TextView)itemView.findViewById(R.id.category);
            uabout=(TextView) itemView.findViewById(R.id.about);
        }
    }
}
