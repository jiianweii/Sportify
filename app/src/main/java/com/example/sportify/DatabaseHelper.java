package com.example.sportify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * This variable is used to store the name of the current class
     */
    private static final String TAG = "DatabaseHelper";
    /**
     * This variable is used to store the name of the Database
     */
    private static final String DATABASE_NAME = "Sportify.db";
    /**
     * This variable is used to store the version of the Database
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * This variable is used to store the path of the Database
     */
    private static String DB_PATH = "";
    /**
     * This variable is used to reference the User table in the database
     */
    public static final String USERS_TABLE = "User";
    /**
     * This variable is used to reference the Activity table in the database
     */
    public static final String ACTIVITY_TABLE = "Activity";

    /**
     * This constructor is used for initialising the class
     * @param context Context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This method is used to create database, which will get the current database in the assets folder
     * and copy them into the emulator
     * @throws IOException Exception
     */
//    public void createDataBase() throws IOException {
//        boolean dbExist = checkDataBase();
//        if (!dbExist) {
//            this.getReadableDatabase();
//            try {
//                copyDataBase();
//                Log.d(TAG, "Database created successfully");
//            } catch (IOException e) {
//                throw new IOException("Error copying database");
//            }
//        }
//    }

    /**
     * This method is used to check if the database exists in the emulator
     * @return boolean
     */
//    private boolean checkDataBase() {
//        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
//        return dbFile.exists();
//    }

    /**
     * This method is used to copy the existing database from the assets folder into the emulator
     * data folder which will be used for reading and writing later
     * @throws IOException Exception
     */
//    private void copyDataBase() throws IOException {
//        InputStream inputStream = mContext.getAssets().open(DATABASE_NAME);
//        String outFileName = DB_PATH + DATABASE_NAME;
//        OutputStream outputStream = new FileOutputStream(outFileName);
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = inputStream.read(buffer)) > 0) {
//            outputStream.write(buffer, 0, length);
//        }
//        outputStream.flush();
//        outputStream.close();
//        inputStream.close();
//    }

    /**
     * This method is used to insert data values into the Activity table
     * @param Date String
     * @param Timing String
     * @param Distance String
     * @param Email String
     * @return result long - The result is used to check if the data has been inserted correctly
     */
    public long insertActivity(String Date, String Timing, String Distance, String Email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Date", Date);
        values.put("Time", Timing);
        values.put("Distance", Distance);
        // values.put("UserEmail", account.getEmail());
        values.put("UserEmail", Email);
        // Check if rows inserted is more than 0
        // If less than 0, means the data is not inserted
        long result = db.insert("Activity", null, values);
        db.close();
        return result;
    }

    /**
     * This method is used to get an ArrayList of Activity that belongs to a certain user
     * based on the email as the foreign key in the Activity table
     * @param email String
     * @return activities ArrayList<Activity>
     */
    public ArrayList<Activity> getActivityData(String email) {
        ArrayList<Activity> activities = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String query = "SELECT * FROM Activity WHERE UserEmail = '" + email + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String date = cursor.getString(1);
            String time = cursor.getString(2);
            String distance = cursor.getString(3);

            Activity activity = new Activity();
            activity.setDate(date);
            activity.setTime(time);
            activity.setKilometer(Double.parseDouble(distance));

            activities.add(activity);
        }

        cursor.close();
        return activities;
    }

    /**
     * This method is used to get the user information based on the email given
     * and stores them inside the Account class which will be returned for other purposes
     * @param Email String
     * @return user Account
     */
    public Account getUser(String Email) {
        Account user = new Account();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM User WHERE Email ='"+ Email + "'", null, null);

        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("Email")));
            user.setAge(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("Age"))));
            user.setGender(cursor.getString(cursor.getColumnIndexOrThrow("Gender")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("Password")));
        }


        return user;
    }

    /**
     * This method is used to insert data of the user inside the User table
     * @param Name String
     * @param email String
     * @param password String
     * @param age String
     * @param gender String
     * @return result boolean - This is to check if the insertion is done correctly
     */
    public Boolean insertUserData(String Name, String email, String password, String age, String gender) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", Name);
        contentValues.put("Email", email);
        contentValues.put("Password", password);
        contentValues.put("Age", age);
        contentValues.put("Gender", gender);

        long result = myDB.insert("User", null, contentValues);
        return result != -1; // It will return -1 if insertion fails
    }

    /**
     * This method is used to update data from the User table
     * @param name String
     * @param email String
     * @param password String
     * @param age String
     * @param gender String
     */
    public void updateUserData(String name, String email, String password, String age, String gender) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // Ensure that the fields are not empty when updating
        if (!name.equals(""))
            contentValues.put("Name", name);
        if (!email.equals(""))
            contentValues.put("Email", email);
        if (!password.equals(""))
            contentValues.put("Password", password);
        if (!age.equals(""))
            contentValues.put("Age", age);
        if (!gender.equals(""))
            contentValues.put("Gender", gender);

        myDB.update("User", contentValues, "email=?", new String[]{email});
        myDB.close();
    }

    /**
     * This method is used to check the username (email) of the user from User table if it exists
     * @param email String
     * @return boolean
     */
    public Boolean checkUsername(String email) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM User WHERE Email = ?", new String[]{email});
        return cursor.getCount() > 0;
    }

    /**
     * This method is used to validate the user's email and password if they are correct
     * @param email String
     * @param password String
     * @return boolean
     */
    public Boolean checkUsernamePassword(String email, String password){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM User WHERE Email = ? AND Password = ?", new String[]{email, password});
        return cursor.getCount() > 0;
    }

    /**
     * This method is used when the database is initialised. It will create two tables and insert them with sample data
     * @param db SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table " + USERS_TABLE + "(Email TEXT PRIMARY KEY, Password TEXT, Name TEXT, Age INTEGER, Gender TEXT)");
        db.execSQL("INSERT INTO " + USERS_TABLE + " (Email, Password, Name, Age, Gender) VALUES (\"johndoe@gmail.com\", \"12345\", \"John Doe\", \"18\", \"Male\")");
        db.execSQL("create Table " + ACTIVITY_TABLE + "(ActivityID INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT, " +
                "Time TEXT, Distance TEXT, UserEmail TEXT, FOREIGN KEY (UserEmail) REFERENCES User (Email))");
        db.execSQL("INSERT INTO " + ACTIVITY_TABLE + " (Date, Time, Distance, UserEmail) VALUES (\"13/03/2024\", \"00:03:42\", \"0.71\", \"johndoe@gmail.com\")");
        db.execSQL("INSERT INTO " + ACTIVITY_TABLE + " (Date, Time, Distance, UserEmail) VALUES (\"10/03/2024\", \"00:05:18\", \"0.35\", \"johndoe@gmail.com\")");
    }

    /**
     * This method will be called when the version of the database has been updated
     * @param db SQLiteDatabase
     * @param oldVersion int
     * @param newVersion int
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists " + USERS_TABLE);
        db.execSQL("drop Table if exists " + ACTIVITY_TABLE);
        onCreate(db);
    }
}
