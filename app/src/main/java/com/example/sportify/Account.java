package com.example.sportify;

import java.util.ArrayList;

public class Account {

    /**
     * This variable is used to store the name of the user
     */
    private String name;
    /**
     * This variable is used to store the email of the user
     */
    private String email;
    /**
     * This variable is used to store the password of the user
     */
    private String password;
    /**
     * This variable is used to store the age of the user
     */
    private int age;
    /**
     * This variable is used to store the gender of the user
     */
    private String gender;
    /**
     * This ArrayList is used to store all the activities of the user
     */
    private ArrayList<Activity> activities;

    /**
     * This method is used to copy another ArrayList of Activity to the activities
     * @return ArrayList<Activity> Object
     */
    public ArrayList<Activity> getActivities() {
        return activities;
    }

    /**
     * This method is used to set activities of another ArrayList to current activities
     * @param activities ArrayList<Activity> Object
     */
    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    /**
     * This method is used to get the name of the user
     * @return name String
     */
    public String getName() {
        return name;
    }

    /**
     * This method is used to set the name of the user
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method is used to get the email of the user
     * @return email String
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method is used to set the email of the user
     * @param email String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method is used to get the age of the user
     * @return age int
     */
    public int getAge() {
        return age;
    }

    /**
     * This method is used to set the age of the user
     * @param age int
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * This method is used to get the gender of the user
     * @return gender String
     */
    public String getGender() {
        return gender;
    }

    /**
     * This method is used to set the gender of the user
     * @param gender String
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * This method is used to get the password of the user
     * @return password String
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method is used to set the password of the user
     * @param password String
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This is the default constructor for initialising the class
     */
    public Account() {
        this.name = "";
        this.email = "";
        this.password = "";
        this.age = 18;
        this.gender = "undefined";
    }

    /**
     * This is the constructor for storing user information
     * @param name String
     * @param email String
     * @param password String
     * @param age int
     * @param gender String
     */
    public Account(String name, String email, String password, int age, String gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }
}
