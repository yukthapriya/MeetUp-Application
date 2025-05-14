package com.example.interesto.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.interesto.fragment.All;
import com.example.interesto.fragment.Like_Minded;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private String[] titles=new String[]{"Like Minded","All"};

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new Like_Minded();
            case 1:
                return new All();
        }
        return new Like_Minded();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
