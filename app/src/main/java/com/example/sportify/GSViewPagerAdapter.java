package com.example.sportify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class GSViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Integer> cards;

    /**
     * This constructor is used to get the context of the activity and copy an existing arraylist
     * into this class
     * @param context Context
     * @param cards ArrayList<Integer>
     */
    public GSViewPagerAdapter(Context context, ArrayList<Integer> cards)
    {
        this.context = context;
        this.cards = cards;
    }

    /**
     * This method is used to get the total size of the arraylist
     * @return
     */
    @Override
    public int getCount() {
        return cards.size();
    }

    /**
     * This method is used to check if the object is in the view
     * @param view Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return boolean
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object.equals(view);
    }

    /**
     * This method is used to destroy the item when the user go to another page
     * @param container The containing View from which the page will be removed.
     * @param position The page position to be removed.
     * @param object The same object that was returned by
     * {@link #instantiateItem(View, int)}.
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    /**
     * This method is used to instantiate the item, every item in the Adapter will be set with an image
     * based on the current position of the adapter and from the arraylist
     * @param container The containing View in which the page will be shown.
     * @param position The page position to be instantiated.
     * @return Object
     */

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.view_pager_item, container, false);

        ImageView imageView = item.findViewById(R.id.imageItem);
        imageView.setImageResource(cards.get(position));

        container.addView(item);

        return item;

    }

}
