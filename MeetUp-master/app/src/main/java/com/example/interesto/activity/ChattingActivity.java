package com.example.interesto.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.interesto.R;
import com.example.interesto.adapter.ChatAdapter;
import com.example.interesto.databinding.ActivityChattingBinding;
import com.example.interesto.networkStatus.NetworkChangeListener;
import com.example.interesto.supportive.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChattingActivity extends AppCompatActivity {

    ActivityChattingBinding binding;
    String friendName,friendUid,friendImg,userUid,selectedURL;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<MessageModel> messageModelArrayList;
    ChatAdapter chatAdapter;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChattingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        friendName=getIntent().getStringExtra("name");
        friendUid=getIntent().getStringExtra("uid");
        friendImg=getIntent().getStringExtra("imageuri");
        selectedURL=getIntent().getStringExtra("selectedURL");
        userUid=auth.getUid();

        binding.txtMsg.setText(selectedURL);

        binding.friendName.setText(friendName);
        Glide.with(this).load(friendImg)
                .placeholder(R.drawable.user)
                .into(binding.userImg);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChattingActivity.this.finish();
                database=null;
            }
        });

        messageModelArrayList=new ArrayList<MessageModel>();
        chatAdapter=new ChatAdapter(messageModelArrayList,ChattingActivity.this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(chatAdapter);

        final String senderRoom=userUid+friendUid;
        final String recieverRoom=friendUid+userUid;

        try {
            database.getReference().child("chats")
                    .child(senderRoom)
                    .child("myMessages")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                messageModelArrayList.clear();
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    MessageModel model = snapshot1.getValue(MessageModel.class);
                                    messageModelArrayList.add(model);
                                }

                                chatAdapter.notifyDataSetChanged();
                                binding.recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());

                                resetUnseenMessage(senderRoom);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }catch (Exception e)
        {
            Toast.makeText(ChattingActivity.this,"Null Pointer Exception Outer",Toast.LENGTH_SHORT).show();
        }

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.backButton.setEnabled(false);
                String msg=binding.txtMsg.getText().toString();
                if(!msg.trim().isEmpty())
                {
                    Date date=new Date();
                    MessageModel messageModel=new MessageModel(userUid,msg);
                    messageModel.setTimeStamp(date.getTime());
                    binding.txtMsg.setText("");

                    HashMap<String, Object> lastMsgObj=new HashMap<>();
                    lastMsgObj.put("lastMessage",msg);
                    lastMsgObj.put("lastMessageTime",date.getTime());

                    setUnseenMessage(recieverRoom);

                    database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                    database.getReference().child("chats").child(recieverRoom).updateChildren(lastMsgObj);

                    database.getReference().child("chats")
                            .child(senderRoom)
                            .child("myMessages")
                            .push()
                            .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            database.getReference().child("chats")
                                    .child(recieverRoom)
                                    .child("myMessages")
                                    .push()
                                    .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    binding.backButton.setEnabled(true);
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    public void setUnseenMessage(String recieverRoom)
    {
        database.getReference().child("chats")
                .child(recieverRoom)
                .child("unseen")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            long i=Long.parseLong(snapshot.getValue().toString());
                            i++;
                            HashMap<String,Object> hashMap1=new HashMap<>();
                            hashMap1.put("unseen",i);
                            database.getReference().child("chats")
                                    .child(recieverRoom)
                                    .updateChildren(hashMap1);
                        }
                        else{
                            HashMap<String,Object> hashMap1=new HashMap<>();
                            hashMap1.put("unseen",1);
                            database.getReference().child("chats")
                                    .child(recieverRoom)
                                    .updateChildren(hashMap1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void resetUnseenMessage(String senderRoom)
    {
        try {
            if(database!=null) {
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("unseen")
                        .setValue(0);
            }
        }catch (Exception e)
        {
            //Toast.makeText(ChattingActivity.this,"Null Pointer Exception Inner",Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onBackPressed() {
        if(binding.backButton.isEnabled()) {
            ChattingActivity.this.finish();
            database = null;
        }
    }
}