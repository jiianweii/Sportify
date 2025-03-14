package com.example.sportify;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class Activity {
    /**
     * This variable is used to record the date of the activity
     */
    private String date;
    /**
     * This variable is used to record the timing/duration of the activity
     */
    private String time;
    /**
     * This variable is used for recording the distance spent on the activity
     */
    private double kilometer;
    /**
     * This ArrayList of PolyLineOptions is used to store all the polylines used
     * while the sensors is active during running
     */
    private ArrayList<PolylineOptions> points;

    /**
     * This is the constructor of the Activity class, which is used for initialising the class
     * @param date String
     * @param time String
     * @param kilometer double
     * @param points ArrayList<PolylineOptions>
     */
    public Activity(String date, String time, double kilometer, ArrayList<PolylineOptions> points) {
        this.date = date;
        this.time = time;
        this.kilometer = kilometer;
        this.points = points;
    }

    /**
     * This is the default constructor for initialising an empty class
     */
    public Activity() {
        this.date = "01/01/1999";
        this.time = "00:00";
        this.kilometer = 0.0;
        this.points = new ArrayList<>();
    }

    /**
     * This method is used to store all the PolylineOptions inside the ArrayList
     * @return ArrayList<PolylineOptions>
     */
    public ArrayList<PolylineOptions> getPoints() {
        return points;
    }

    /**
     * This method is used to copy an ArrayList of PolylineOptions in the current ArrayList
     * @param points ArrayList<PolylineOptions>
     */
    public void setPoints(ArrayList<PolylineOptions> points) {
        this.points = points;
    }

    /**
     * This method is used to add PolylineOptions inside the ArrayList
     * @param point PolylineOptions
     */
    public void addPoints(PolylineOptions point) {
        this.points.add(point);
    }

    /**
     * This method is used to return the current date of the activity
     * @return String
     */
    public String getDate() {
        return date;
    }

    /**
     * This method is used to set the date of the activity
     * @param date String
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * This method is used to return the time of the activity
     * @return String
     */
    public String getTime() {
        return time;
    }

    /**
     * This method is used to set the time of the activity
     * @param time String
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * This method is used to return the distance of the activity
     * @return double
     */
    public double getKilometer() {
        return kilometer;
    }

    /**
     * This method is used to set the kilometer of the activity
     * @param kilometer double
     */
    public void setKilometer(double kilometer) {
        this.kilometer = kilometer;
    }
}
