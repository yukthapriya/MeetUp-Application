package com.example.interesto.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.interesto.R;

import com.example.interesto.activity.Information;
import com.example.interesto.activity.MainActivity;
import com.example.interesto.activity.WebViewClass;
import com.example.interesto.supportive.ModelClass;

import java.util.ArrayList;

public class NewsCardAdapter extends RecyclerView.Adapter<NewsCardAdapter.ViewHolder> {

    Context context;
    ArrayList<ModelClass> modelClassArrayList;

    public NewsCardAdapter(Context context, ArrayList<ModelClass> modelClassArrayList) {
        this.context = context;
        this.modelClassArrayList = modelClassArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_item,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.ttime.setText("Published At : "+modelClassArrayList.get(position).getPublishedAt());
        holder.theading.setText(modelClassArrayList.get(position).getTitle());
        holder.tauthor.setText(modelClassArrayList.get(position).getAuthor());
        holder.tcontent.setText(modelClassArrayList.get(position).getDescription());
        Glide.with(context).load(modelClassArrayList.get(position).getUrlToImage()).into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, WebViewClass.class);
                intent.putExtra("url",modelClassArrayList.get(position).getUrl());
                context.startActivity(intent);
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Information.userAsMe!=null) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("selected", "share");
                    intent.putExtra("url", modelClassArrayList.get(position).getUrl());
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(view.getContext(),"We have detected a Slow connection, please wait for a while",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelClassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView theading,tcontent,tauthor,ttime;
        ImageView imageView,share;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            theading=itemView.findViewById(R.id.mainHeading);
            tcontent=itemView.findViewById(R.id.content);
            tauthor=itemView.findViewById(R.id.author);
            ttime=itemView.findViewById(R.id.time);
            cardView=itemView.findViewById(R.id.cardView);
            imageView=itemView.findViewById(R.id.newsimg);
            share=itemView.findViewById(R.id.shareBut);
        }
    }
}
