package com.example.interesto.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.interesto.fragment.BussinessFragment;
import com.example.interesto.fragment.EntertainmentFragment;
import com.example.interesto.fragment.HealthFragment;
import com.example.interesto.fragment.HomeFragment;
import com.example.interesto.fragment.ScienceFragment;
import com.example.interesto.fragment.SportsFragment;
import com.example.interesto.fragment.TechnologyFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    int tabcount;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new HomeFragment();

            case 1:
                return new SportsFragment();

            case 2:
                return new ScienceFragment();

            case 3:
                return new HealthFragment();

            case 4:
                return new EntertainmentFragment();

            case 5:
                return new TechnologyFragment();

            case 6:
                return new BussinessFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
