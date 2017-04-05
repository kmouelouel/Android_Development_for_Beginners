package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {
 // global varaibles and listeners:
 private MediaPlayer mMediaPlayer;
    // global variable to handle audio focus when playing a sound file
    private AudioManager maudioManager;
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener(){
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    }else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                        // resume: we do not have resume so we restart the audio.
                        mMediaPlayer.start();
                    }else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                        // the AudioFocus_loss case mean we've lost audio focus and
                        // stop playback and clean the resource
                        releaseMediaPlayer();
                    }

                }
            };
    private MediaPlayer.OnCompletionListener mCompletionListener=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            maudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    public ColorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.word_list,container,false);
        // create and setup the {@link AudioManager} to request audio focus.
        maudioManager= (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("red", "Azeggar",R.drawable.color_red,R.raw.color_red));
        words.add(new Word("yellow", "Awrar",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));
        words.add(new Word("green", "Ahchiychi",R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown", "Aqahwi",R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray", "Topoppi",R.drawable.color_gray,R.raw.color_gray));
        words.add(new Word("black", "Aberkan",R.drawable.color_black,R.raw.color_black));
        words.add(new Word("white", "Amallal",R.drawable.color_white,R.raw.color_white));
        //  words.add(new Word("orange", "alimuni"));
        // Create an {@link WordAdapterimlloul}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(getActivity(), words,R.color.category_colors);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);
        // Set a click listener to play the audio when the list item is clicked on listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();
                Word word =words.get(position);
                // request audio focus  for playback
                //maudioManager.OnAudioFocusChangeListener maudiofocuschangeListener;
                int result= maudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // use the music stream
                        AudioManager.STREAM_MUSIC,
                        //request permanent focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result== AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    //we have a audio focus now.
                    mMediaPlayer=MediaPlayer.create(getActivity(),word.getAudioResourceId());
                    // start the audio file
                    mMediaPlayer.start();
                    //swtup a listener on the medio, so that media player, so that we can stop and release
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }

        });
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
