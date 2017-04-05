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
public class PhrasesFragment extends Fragment {
// global varaibles:
private MediaPlayer mMediaPlayer;
    // global variable to handle audio focus when playing a sound file
    private AudioManager maudioManager;
    // create a listener for audio focus
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

    public PhrasesFragment() {
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
        words.add(new Word("Where are you going?", "Anda ka at rouhad",R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "Amek i keqaren",R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "Ismiw...",R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "Amek telid?",R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "Akliya labas",R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "Adrouhath?",R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "Anaam, anrouhah",R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "Akliyan",R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "Iyan anruh",R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "Iya ar dda",R.raw.phrase_come_here));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

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

                Word word =words.get(position);
                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();
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
