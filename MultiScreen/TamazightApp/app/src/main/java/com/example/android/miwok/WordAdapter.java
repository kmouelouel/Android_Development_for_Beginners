package com.example.android.miwok;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kmoue on 2/16/2017.
 */

public class WordAdapter extends ArrayAdapter<Word>{
    /*
    resources ID for the background color for this list of words
    */
    private int mColorResourceId;

    public WordAdapter(Context context, ArrayList<Word> words,int  colorResourceId)
    {
        super(context, 0,words);
        mColorResourceId = colorResourceId;
    }




    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Word currentWord = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView tamazightTextView = (TextView) listItemView.findViewById(R.id.tamazight_text_view);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        tamazightTextView.setText(currentWord.getMiwokTranslation());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView defaultTextView  = (TextView) listItemView.findViewById(R.id.default_text_view);
        // Get the version number from the current AndroidFlavor object and
        // set thisdefaultTextView text on the number TextView
        defaultTextView.setText(currentWord.getDefaultTranslation());
        // Find the ImageView in the list_item.xml layout with the ID list_item_icon
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);
        // Get the image resource ID from the current AndroidFlavor object and
   // check if an image is provided for this word or not
        if(currentWord.hasImage()){
            imageView.setImageResource(currentWord.getImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        }else{
                imageView.setVisibility(View.GONE);
        }
        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        View textContainer=listItemView.findViewById(R.id.text_container);
        int color= ContextCompat.getColor(getContext(),mColorResourceId);
        textContainer.setBackgroundColor(color);
        // so that it can be shown in the ListView
         return listItemView;
    }
}
