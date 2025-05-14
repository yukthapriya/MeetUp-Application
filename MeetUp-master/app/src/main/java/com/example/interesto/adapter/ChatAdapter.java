package com.example.interesto.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interesto.R;
import com.example.interesto.databinding.RecieverItemBinding;
import com.example.interesto.databinding.SenderItemBinding;
import com.example.interesto.supportive.MessageModel;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<MessageModel> messages;
    Context context;
    final int SENDER_TYPE=1;
    final int RECIEVER_TYPE=2;

    public ChatAdapter() {
    }

    public ChatAdapter(ArrayList<MessageModel> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_item,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_item,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getUid().equalsIgnoreCase(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_TYPE;
        }
        else{
            return RECIEVER_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel msg=messages.get(position);

        if(holder.getClass()==SenderViewHolder.class)
        {
            if(msg.getMessage().startsWith("http"))
            {
                ((SenderViewHolder) holder).bindingSender.senderMsg.setText(Html.fromHtml(String.format("<a href=\"%s\">"+msg.getMessage()+"<a>",msg.getMessage())));
                ((SenderViewHolder) holder).bindingSender.senderMsg.setMovementMethod(LinkMovementMethod.getInstance());
                ((SenderViewHolder) holder).bindingSender.senderTime.setText(new SimpleDateFormat("hh:mm a").format(new Date(msg.getTimeStamp())));
            }
            else {
                ((SenderViewHolder) holder).bindingSender.senderMsg.setText(msg.getMessage());
                ((SenderViewHolder) holder).bindingSender.senderTime.setText(new SimpleDateFormat("hh:mm a").format(new Date(msg.getTimeStamp())));
            }

        }else{
            if(msg.getMessage().startsWith("http"))
            {
                ((RecieverViewHolder) holder).bindingReciever.recieverMsg.setText(Html.fromHtml(String.format("<a href=\"%s\">"+msg.getMessage()+"<a>",msg.getMessage())));
                ((RecieverViewHolder) holder).bindingReciever.recieverMsg.setMovementMethod(LinkMovementMethod.getInstance());
                ((RecieverViewHolder) holder).bindingReciever.recieverTime.setText(new SimpleDateFormat("hh:mm a").format(new Date(msg.getTimeStamp())));
            }
            else {
                ((RecieverViewHolder) holder).bindingReciever.recieverMsg.setText(msg.getMessage());
                ((RecieverViewHolder) holder).bindingReciever.recieverTime.setText(new SimpleDateFormat("hh:mm a").format(new Date(msg.getTimeStamp())));
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder{

        RecieverItemBinding bindingReciever;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingReciever=RecieverItemBinding.bind(itemView);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{

        SenderItemBinding bindingSender;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingSender=SenderItemBinding.bind(itemView);
        }
    }
}
