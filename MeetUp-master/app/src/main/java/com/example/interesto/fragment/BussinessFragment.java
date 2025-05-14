package com.example.interesto.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interesto.R;
import com.example.interesto.adapter.NewsCardAdapter;
import com.example.interesto.supportive.ApiUtilities;
import com.example.interesto.supportive.MainNews;
import com.example.interesto.supportive.ModelClass;
import com.example.interesto.supportive.MyAPIKey;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BussinessFragment extends Fragment {

    //private String API_KEY="838f118f96444870bd7e8acd1e885f13";
    private String API_KEY= MyAPIKey.getMyKey();
    ArrayList<ModelClass> modelClassArrayList;
    NewsCardAdapter newsCardAdapter;
    String country="in";
    private RecyclerView recyclerViewBussiness;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bussinessfragment,null);

        recyclerViewBussiness=view.findViewById(R.id.recyclerviewofbussiness);
        modelClassArrayList=new ArrayList<>();
        recyclerViewBussiness.setLayoutManager(new LinearLayoutManager(getContext()));
        newsCardAdapter=new NewsCardAdapter(getContext(),modelClassArrayList);
        recyclerViewBussiness.setAdapter(newsCardAdapter);

        findNews();

        return view;
    }

    public void findNews()
    {
        ApiUtilities.getApiInterface().getNewsByCategory(country,"business",100,API_KEY).enqueue(new Callback<MainNews>() {
            @Override
            public void onResponse(Call<MainNews> call, Response<MainNews> response) {
                try {
                    modelClassArrayList.addAll(response.body().getArticles());
                    newsCardAdapter.notifyDataSetChanged();
                }catch (Exception e)
                {
                    Toast.makeText(getContext(),"Sorry, Unable to fetch details at this time !!!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MainNews> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
