package com.example.sportify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    /**
     * This variable is used to store an list of fragment
     */
    private final List<Fragment> fragments = new ArrayList<>();
    /**
     * This variable is used to store a list of titles
     */
    private final List<String> fragmentTitle = new ArrayList<>();

    /**
     * This constructor is used for initialisation
     * @param fm FragmentManager
     */
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    /**
     * This method is used to add item into both lists
     * @param fragment Fragment
     * @param title String
     */
    public void add(Fragment fragment, String title) {
        fragments.add(fragment);
        fragmentTitle.add(title);
    }

    /**
     * This method is used to get position of the item in the fragment list
     * @param position
     * @return Fragment
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /**
     * This method is used to get the total size of the fragment list
     * @return int
     */
    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     * This method is used to get the title of the page from the title list
     * @param position The position of the title requested
     * @return CharSequence
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
