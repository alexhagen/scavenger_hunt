package io.github.alexhagen.scavenger_hunt;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.github.alexhagen.scavenger_hunt.Song;

public class MixTape extends AppCompatActivity {

    Timer timer = new Timer();
    MediaPlayer mixtapemedia = new MediaPlayer();
    ArrayList<Song> playlist = new ArrayList<>();
    int i=0;
    TextView songtimetv;
    TextView songtexttv;
    ImageButton playbutton;
    long current_time;
    boolean is_playing;
    long final_time;
    int available = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix_tape);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        playlist.add(new Song(R.raw.hozier_work_song, "Work Song", "Hozier", 10, "16:10 9/12/2016", new Clue(R.drawable.clue_1)));
        playlist.add(new Song(R.raw.the_lumineers_cleopatra, "Cleopatra", "The Lumineers", -1, "16:20 9/13/2016"));
        playlist.add(new Song(R.raw.lord_huron_she_lit_a_fire, "She Lit a Fire", "Lord Huron", 10, "16:20 9/14/2016"));
        playlist.add(new Song(R.raw.amber_run_i_found, "I Found", "Amber Run", -1, "16:20 9/15/2016"));
        playlist.add(new Song(R.raw.the_head_and_the_heart_rivers_and_roads, "Rivers and Roads", "The Head and the Heart", 10, "16:20 9/16/2016"));
        playlist.add(new Song(R.raw.gregory_alan_isakov_the_stable_song, "Stable Song", "Gregory Alan Isakov", -1, "16:20 9/17/2016"));
        playlist.add(new Song(R.raw.city_and_colour_the_girl, "The Girl", "City and Colour", -1, "16:20 9/18/2016"));
        playlist.add(new Song(R.raw.benton_paul_i_only_see_you, "I Only See You", "Benton Paul", -1, "16:20 9/19/2016"));
        playlist.add(new Song(R.raw.peter_gabriel_the_book_of_love, "The Book of Love", "Peter Gabriel", -1, "17:20 9/20/2016"));
        playlist.add(new Song(R.raw.lord_huron_ends_of_the_earth, "Ends of the Earth", "Lord Huron", 10, "17:20 9/21/2016"));
        playlist.add(new Song(R.raw.the_lumineers_sleep_on_the_floor, "Sleep on the Floor", "Lumineers", 10, "17:20 9/22/2016"));
        available = 0;
        for(int ii=0; ii<playlist.size(); ii++){
            if (playlist.get(ii).is_available()) {
                available++;
            }
        }
        mixtapemedia = MediaPlayer.create(this, playlist.get(0).resource);
        mixtapemedia.start();
        mixtapemedia.setScreenOnWhilePlaying(true);
        songtimetv = (TextView) findViewById(R.id.time_text);
        songtexttv = (TextView) findViewById(R.id.song_text);
        playbutton = (ImageButton) findViewById(R.id.play);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                is_playing = mixtapemedia.isPlaying();
                final_time = mixtapemedia.getDuration();
                current_time = mixtapemedia.getCurrentPosition();
                mHandler.obtainMessage(1).sendToTarget();
            }
        }, 0, 1000);
        ImageButton nextbutton = (ImageButton) findViewById(R.id.forward);
        ImageButton lastbutton = (ImageButton) findViewById(R.id.rewind);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        });
        lastbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playLast();
            }
        });
        add_buttons();
        Button refresh_button = (Button) findViewById(R.id.refresh);
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
    }

    public void refresh(){
        add_buttons();
    }

    public void playSong(int j) {
        mixtapemedia.stop();
        if (j >= 0 && j <= available) {
            i = j;
        } else if (j<0) {
            i = available;
        } else {
            i = 0;
        }
        mixtapemedia = MediaPlayer.create(MixTape.this, playlist.get(i).resource);
        mixtapemedia.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext();
            }
        });
        mixtapemedia.start();
        mixtapemedia.setScreenOnWhilePlaying(true);
    }

    public void playNext() {
        playSong(++i);
    }

    public void playLast() {
        playSong(--i);
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            songtexttv.setText(playlist.get(i).artist_name + ": " + playlist.get(i).song_name);
            long current_min = current_time / (60000);
            long current_s = (current_time / 1000) - 60 * current_min;
            long total_min = final_time / 60000;
            long total_s = (final_time / 1000) - 60 * total_min;
            songtimetv.setText(String.format("%d:%02d / %d:%02d", current_min, current_s, total_min, total_s)); //this is the textview
            int current_time_s = (int) current_time / 1000;
            Log.d("Clue", String.format("%d, %d", current_time_s, playlist.get(i).clue_time));
            if (current_time_s == playlist.get(i).clue_time) {
                Log.i("CLUE", "Clue triggered!");
                Log.i("CLUE", "Clue triggered!");
                Log.i("CLUE", "Clue triggered!");
                Log.i("CLUE", "Clue triggered!");
                Log.i("CLUE", "Clue triggered!");
                Log.i("CLUE", "Clue triggered!");
                Log.i("CLUE", "Clue triggered!");
                playlist.get(i).
            }
            if (is_playing) {
                playbutton.setImageResource(R.drawable.ic_pause);
                playbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mixtapemedia.pause();
                    }
                });
            } else {
                playbutton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                playbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mixtapemedia.start();
                    }
                });
            }
        }
    };

    public void set_button(int res, final int j){
        ImageButton ib = (ImageButton) findViewById(res);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playlist.get(j).is_available()) {
                    playSong(j);
                }
            }
        });
        TextView textView = null;
        ViewGroup row = (ViewGroup) ib.getParent();
        for (int itemPos = 0; itemPos < row.getChildCount(); itemPos++) {
            View view = row.getChildAt(itemPos);
            if (view instanceof TextView) {
                textView = (TextView) view; //Found it!
                break;
            }
        }
        if (playlist.get(j).is_available()) {
            textView.setText(String.format("%s\n%s", playlist.get(j).artist_name, playlist.get(j).song_name));
        } else {
            textView.setText("...");
        }
    }

    public void add_buttons(){
        set_button(R.id.wsh, 0);
        set_button(R.id.cl, 1);
        set_button(R.id.slaflh, 2);
        set_button(R.id.ifar, 3);
        set_button(R.id.rarhath, 4);
        set_button(R.id.ssgai, 5);
        set_button(R.id.tgcac, 6);
        set_button(R.id.iosybp, 7);
        set_button(R.id.bolpg, 8);
        set_button(R.id.eotelh, 9);
        set_button(R.id.sotfl, 10);

    }
}

