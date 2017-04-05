package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by kmoue on 3/28/2017.
 */
// Database Helper for the Pets app. Manager database creation and version
public  class PetDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PetDbHelper.class.getName();
    //If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    //Name of the database file
    protected static final String DATABASE_NAME = "Shelter.db";



    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE=
                "CREATE TABLE " +  PetEntry.TABLE_NAME + " (" +
                        PetEntry._ID + " INTEGER PRIMARY KEY  AUTOINCREMENT," +
                        PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL," +
                        PetEntry.COLUMN_PET_BREED + " TEXT, " +
                        PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, " +
                        PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0" +
                        ")";
        db.execSQL(SQL_CREATE_PETS_TABLE);
        Log.i(LOG_TAG,SQL_CREATE_PETS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over

        // The database is still at version 1, so there's nothing to do be done here.
         String SQL_DELETE_PETS_TABLE =
                "DROP TABLE IF EXISTS " + PetEntry.TABLE_NAME;
        //db.execSQL(SQL_DELETE_ENTRIES);
       // onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
