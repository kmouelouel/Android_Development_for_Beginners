package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;

/**
 * Created by kmoue on 3/31/2017.
 */

public class PetCursorAdapter extends CursorAdapter {
    public PetCursorAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // find fields name and summary
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView breedTextView = (TextView) view.findViewById(R.id.summary);
        // find the columns of pet attributes that we are interested in
        int nameColumnIndex =cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex =cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
        //read the pet attributs from the cursor for the current pet.
        String petNameString = cursor.getString(nameColumnIndex);
        String petBreedString = cursor.getString(breedColumnIndex);
        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.
        if (TextUtils.isEmpty(petBreedString)) {
            petBreedString = context.getString(R.string.unknown_breed);
        }
        nameTextView.setText(petNameString);
        breedTextView.setText(petBreedString);
    }
}
