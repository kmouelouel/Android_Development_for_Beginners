package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by kmoue on 3/29/2017.
 */

public class PetProvider extends ContentProvider {
    // setup the code change in your app and fill in the blanks here.
    private  static final int PETS = 100;
    private static final int PET_ID= 101;
    private static final UriMatcher sUriMatch = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatch.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS,PETS);
        sUriMatch.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS + "/#",PET_ID);
    }
    // Tag for the log messages
    public static final String LOG_TAG = PetProvider.class.getName();
    // create a global PetDbHelper object
    // initialize the provider and the database helper object.
    PetDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper= new PetDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
                 // get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        // this cursor will hold the result of the query
        Cursor cursor;
        // figure out if the uri matcher can match the URI to a specific code.
        int match =sUriMatch.match(uri);
        switch (match){
            case PETS:
                // act on the pets table
                cursor=database.query(PetContract.PetEntry.TABLE_NAME,
                        projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case PET_ID:
                // act on a single pet in the pets table
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs= new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknow URI "+ uri);
        }
        // set notification URI on the cursor
        //so we know what content URI the cursor was created for
        //if the data at this URI change .then we know we need to update the cursor.
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        //return the cursor
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatch.match(uri);
        switch (match) {
            case PETS:
                return PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
   final int match=sUriMatch.match(uri);
        switch (match){
            case PETS:
                return insertPet(uri,values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " +uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Track the number of rows that were deleted
        int rowsDeleted;
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase(); int match =sUriMatch.match(uri);
        switch (match){
            case PETS:
                // act on the pets table
                // Delete all rows that match the selection and selection args
// For  case PETS:
                rowsDeleted = database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
                // If 1 or more rows were deleted, then notify all listeners that the data at the
                // given URI has changed
                if (rowsDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);
                //  Lastly, updatePet() will return the number of rows that were delete:

                // Return the number of rows deleted
                return rowsDeleted;
            case PET_ID:
                // act on a single pet in the pets table
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs= new String[] {String.valueOf(ContentUris.parseId(uri))};
                // For case PET_ID:
                // Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
                // If 1 or more rows were deleted, then notify all listeners that the data at the
                // given URI has changed
                if (rowsDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);
                //  Lastly, updatePet() will return the number of rows that were delete:

                // Return the number of rows deleted
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }


    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatch.match(uri);
        switch (match) {
            case PETS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case PET_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updatePet(Uri uri, ContentValues values,
                          String selection, String[] selectionArgs) {
        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(PetEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }
        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !PetEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }
        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(PetEntry.COLUMN_PET_WEIGHT)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }
        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
 // Returns the number of database rows affected by the update statement
        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(PetEntry.TABLE_NAME, values, selection, selectionArgs);
       // If it was more than zero, I call notifyChange:

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;
    }

    private Uri insertPet(Uri uri, ContentValues values){
        // Check that the name is not null
        String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        // Check that the gender is valid
        Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if (gender == null || !PetEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Pet requires valid gender");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }
        // Gets the data repository in write mode
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Insert the new row, returning the primary key value of the new row
        long id = database.insert(PetContract.PetEntry.TABLE_NAME, null, values);
        if(id ==-1){
        Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        //notify all listeners tha the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri,null);
        // return the new URI waith the ID appended at the end.
        return ContentUris.withAppendedId(uri,id);
    }

}
