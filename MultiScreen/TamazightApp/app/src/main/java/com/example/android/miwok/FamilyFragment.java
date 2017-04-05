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
public class FamilyFragment extends Fragment {
// add global varaibles
private MediaPlayer mMediaPlayer;
    // global variable to handle audio focus when playing a sound file
    private AudioManager mAudioManager;
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
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.word_list,container,false);
// create and setup the {@link AudioManager} to request audio focus.
        mAudioManager= (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("father", "Baba",R.drawable.family_father,R.raw.family_father));
        words.add(new Word("mother", "Yemma",R.drawable.family_mother,R.raw.family_mother));
        words.add(new Word("son", "Ami",R.drawable.family_son,R.raw.family_son));
        words.add(new Word("daughter", "Yelli",R.drawable.family_daughter,R.raw.family_daughter));
        words.add(new Word("older brother", "Zizi",R.drawable.family_older_brother,R.raw.family_older_brother));
        words.add(new Word("younger brother", "Amazouz",R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "Nana",R.drawable.family_older_sister,R.raw.family_older_sister));
        words.add(new Word("younger sister", "Tamazouzth",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        words.add(new Word("grandmother ", "Setti",R.drawable.family_grandmother,R.raw.family_grandmother));
        words.add(new Word("grandfather", "Jeddi",R.drawable.family_grandfather,R.raw.family_grandfather));
//        words.add(new Word("uncle, brother of my mother ", "khali"));
//        words.add(new Word("aunt,sister of my mother", "khalti"));
//        words.add(new Word("uncle,brother of my father ", "ami"));
//        words.add(new Word("aunt,sister of my father", "amti"));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(getActivity(), words,R.color.category_family);

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
                int result= mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
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
