package com.example.interesto.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.interesto.R;
import com.example.interesto.activity.ChattingActivity;
import com.example.interesto.activity.MainActivity;
import com.example.interesto.databinding.UserLayoutBinding;
import com.example.interesto.fragment.Like_Minded;
import com.example.interesto.supportive.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    Context context;
    ArrayList<User> users;

    public UserAdapter(Context context, ArrayList<User> users)
    {
        this.context=context;
        this.users=users;
    }

    @NonNull
    @NotNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserViewHolder holder, int position) {
        User user=users.get(position);
        holder.binding.name.setText(user.getName());        //setting name of user
        Glide.with(context).load(user.getProfileImage())    //using Glide library for image processing
                .placeholder(R.drawable.user)
                .into(holder.binding.imageView);

        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid()+user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            try {   //In case if unseen node is not created yet then catch the exception
                                Long l = Long.parseLong(snapshot.child("unseen").getValue().toString());
                                if (l == 0) {
                                    holder.binding.unseenMsg.setVisibility(View.GONE);
                                } else {
                                    holder.binding.unseenMsg.setVisibility(View.VISIBLE);
                                    if (l > 9) {
                                        holder.binding.unseenMsg.setText("9+");
                                    } else {
                                        holder.binding.unseenMsg.setText(l + "");
                                    }
                                }
                            }catch (Exception e)
                            {
                                Log.d("krish","No conversation yet");
                            }
                            holder.binding.recentMsg.setText(snapshot.child("lastMessage").getValue().toString());
                            holder.binding.timeview.setText(new SimpleDateFormat("hh:mm a").format(new Date(snapshot.child("lastMessageTime").getValue(Long.class))));
                        }
                        else{
                            holder.binding.unseenMsg.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });     //code for setting up last message and time

        //setting onclick listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChattingActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("uid",user.getUid());
                intent.putExtra("imageuri",user.getProfileImage());
                intent.putExtra("selectedURL",MainActivity.mainActivityObj.selectedURL);
                context.startActivity(intent);
            }
        });

        holder.binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(user);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        UserLayoutBinding binding;

        public UserViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=UserLayoutBinding.bind(itemView);
        }
    }

    private void showAlertDialog(User myUser) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customLayout = li.inflate(R.layout.custom_imgview, null);
        alertDialog.setView(customLayout);
        ImageView userImg=customLayout.findViewById(R.id.userImgView);
        Glide.with(context).load(myUser.getProfileImage())    //using Glide library for image processing
                .placeholder(R.drawable.user)
                .into(userImg);

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();

        ImageView cancel=(ImageView)customLayout.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
    }

}
