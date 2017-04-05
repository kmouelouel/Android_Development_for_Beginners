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
public class NumbersFragment extends Fragment {
// global variables and listener
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

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View rootView=inflater.inflate(R.layout.word_list,container,false);
        // create and setup the {@link AudioManager} to request audio focus.
         maudioManager= (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // request the
        //create a list of number in tamazight

        final ArrayList<Word> words= new ArrayList<Word>();
        words.add(new Word("one","Yan",R.drawable.number_one,R.raw.number_one));
        words.add(new Word("two","Sin",R.drawable.number_two,R.raw.number_two));
        words.add(new Word("three","Krad",R.drawable.number_three,R.raw.number_three));
        words.add(new Word("four","Kkuz",R.drawable.number_four,R.raw.number_four));
        words.add(new Word("five","Smmus",R.drawable.number_five,R.raw.number_five));
        words.add(new Word("six","Sdis",R.drawable.number_six,R.raw.number_six));
        words.add(new Word("seven","Sa",R.drawable.number_seven,R.raw.number_seven));
        words.add(new Word("eight","Tam",R.drawable.number_eight,R.raw.number_eight));
        words.add(new Word("nine","Tza",R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten","Mraw",R.drawable.number_ten,R.raw.number_ten));

        WordAdapter adapter =
                new WordAdapter(getActivity(),words,R.color.category_numbers);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
//        // Verify the contents of the arrayList by printing out each arrayList element to the logs
//        LinearLayout rootview = (LinearLayout) findViewById(R.id.rootview);
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
