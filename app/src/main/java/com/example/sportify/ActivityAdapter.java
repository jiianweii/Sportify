package com.example.sportify;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyViewHolder> {
    /**
     * This variable holds an ArrayList of Activity class, which is used for storing activities
     * to display in the dashboard
     */
    private final ArrayList<Activity> activityArrayList;

    /**
     * This constructor is used to copy an existing ArrayList to the current ArrayList
     * to be displayed in the view
     * @param activityArrayList ArrayList<Activity>
     */
    public ActivityAdapter(ArrayList<Activity> activityArrayList) {
        this.activityArrayList = activityArrayList;
    }

    /**
     * This method is used to create the ViewHolder via the View object
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return MyViewHolder
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_activity, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * This method is used to bind each item in the ArrayList into the dashboard as an item of the
     * adapter for displaying purposes
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Activity activityDetail = activityArrayList.get(position);

        holder.date.setText(activityDetail.getDate());
        holder.time.setText(activityDetail.getTime());
        holder.distance.setText(String.format("%.2f km", activityDetail.getKilometer()));

//        holder.itemView.setOnClickListener(view -> {
//            AppCompatActivity activity = (AppCompatActivity) view.getContext();
//            UpdateCattle updateCattle = new UpdateCattle();
//
//            TextView cattleTextView = view.findViewById(R.id.cattleNumText);
//            TextView typeTextView = view.findViewById(R.id.cattleTypeText);
//            TextView rfidTextView = view.findViewById(R.id.rfidIDText);
//
//            String cattleNum = cattleTextView.getText().toString();
//            String type = typeTextView.getText().toString();
//            String rfid = rfidTextView.getText().toString();
//
//            Bundle bundle = new Bundle();
//            bundle.putString("cattleNum", cattleNum);
//            bundle.putString("cattleType", type);
//            bundle.putString("rfidID", rfid);
//
//            updateCattle.setArguments(bundle);
//            activity.getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.userContainer, updateCattle)
//                    .addToBackStack(null).commit();
//        });
    }

    /**
     * This method returns an integer which will show the number of items in the ArrayList
     * @return
     */
    @Override
    public int getItemCount() {
        return activityArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, distance;

        /**
         * This is the constructor used by MyViewHolder to initialise the XML components
         * which will be used later for binding
         * @param itemView View
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateText);
            time = itemView.findViewById(R.id.timeText);
            distance = itemView.findViewById(R.id.distanceText);
        }
    }
}
