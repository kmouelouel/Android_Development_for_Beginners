package com.example.android.miwok;

/**
 * Created by kmoue on 2/16/2017.
 */

public class Word {
     // constant value that represents no image was provided for this word
   private static final int NO_IMAGE_PROVIDED=-1;
    // audio resource ID for the word
    private int mAudioResourceId;
    // Default translation for the word

    private String mDefaultTranslation;

    //Miwok translation for the word

    private String mMiwokTranslation;

    // Drawable int mImageResourceId
    private int mImageResourceId=NO_IMAGE_PROVIDED;
    //
    /**
     * Create a new Word object.
     *
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation   is the word in the Miwok language
     */
    public Word(String defaultTranslation, String miwokTranslation, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
    }
    /**
     * Create a new Word object.
     *
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation   is the word in the Miwok language
     *                           (such as English)
     * @param ImageResourceId   is the drawable resource ID for the image associate
     *
     */
    public Word(String defaultTranslation, String miwokTranslation, int ImageResourceId, int audioResourceId ) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId = ImageResourceId;
        mAudioResourceId = audioResourceId;
    }
    /**
     Return whether or not there os an image for this word
     */
    public boolean hasImage(){

        return mImageResourceId !=NO_IMAGE_PROVIDED;
    }

    //Get the default translation of the word.

    public int getAudioResourceId() {
        return mAudioResourceId;
    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    // Get the Miwok translation of the word.

    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }
    public int getImageResourceId() {
        return mImageResourceId;
    }

    @Override
    public String toString() {
        return "Word{" +
                "mAudioResourceId=" + mAudioResourceId +
                ", mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mImageResourceId=" + mImageResourceId +
                '}';
    }
}