package io.github.alexhagen.scavenger_hunt;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    Intent intent;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix_tape);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /* Zero out the clues
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("CLUES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("CLUE_1", 0);
        editor.putInt("CLUE_2", 0);
        editor.putInt("CLUE_3", 0);
        editor.putInt("CLUE_4", 0);
        editor.putInt("CLUE_5", 0);
        editor.putInt("CLUE_6", 0);
        editor.commit(); */

        playlist.add(new Song(this, R.raw.hozier_work_song, "Work Song", "Hozier", 46, "18:30 9/12/2016",
                     new Clue(R.drawable.clue_1, "'Cause my baby's sweet as can be\n" +
                                                 "She give me toothaches just from kissin' me")));  // Working!
        playlist.add(new Song(this, R.raw.the_lumineers_cleopatra, "Cleopatra", "The Lumineers", -1, "18:30 9/13/2016")); // Working!
        playlist.add(new Song(this, R.raw.lord_huron_she_lit_a_fire, "She Lit a Fire", "Lord Huron", 26, "18:30 9/14/2016",
                     new Clue(R.drawable.clue_2, "I've been walking through the mountains\n" +
                                                 "I've wandered through the trees\n" +
                                                 "For her")));  // Working!
        playlist.add(new Song(this, R.raw.amber_run_i_found, "I Found", "Amber Run", -1, "18:30 9/15/2016"));
        playlist.add(new Song(this, R.raw.the_head_and_the_heart_rivers_and_roads, "Rivers and Roads", "The Head ...", 185, "19:30 9/16/2016",
                     new Clue(40.4733713, -86.87091399999997, "Rivers and roads\n" +
                                                              "Rivers 'til I reach you"))); // Working
        playlist.add(new Song(this, R.raw.gregory_alan_isakov_the_stable_song, "Stable Song", "Gregory Alan ...", -1, "18:30 9/17/2016")); // Working
        playlist.add(new Song(this, R.raw.city_and_colour_the_girl, "The Girl", "City and Colour", -1, "18:30 9/18/2016")); // Working
        playlist.add(new Song(this, R.raw.benton_paul_i_only_see_you, "I Only See You", "Benton Paul", -1, "18:30 9/19/2016")); // Working
        playlist.add(new Song(this, R.raw.peter_gabriel_the_book_of_love, "The Book of Love", "Peter Gabriel", -1, "18:30 9/20/2016")); // Working
        playlist.add(new Song(this, R.raw.lord_huron_ends_of_the_earth, "Ends of the Earth", "Lord Huron", 66, "18:30 9/21/2016",
                     new Clue(40.5068614, -86.84382160000001, "To the ends of the earth, would you follow me?"))); // Working
        playlist.add(new Song(this, R.raw.the_lumineers_sleep_on_the_floor, "Sleep on the Floor", "Lumineers", 68, "15:20 9/23/2016",
                     new Clue(R.drawable.clue_3, "We'll be driving through the state."))); // Working
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

    }


    public void playSong(int j) {
        mixtapemedia.stop();
        if (j >= 0 && j < available) {
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
        playSong(i+1);
    }

    public void playLast() {
        playSong(i-1);
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
                Bundle extras = new Bundle();
                if (playlist.get(i).clue.type == 2) {
                    intent = new Intent(MixTape.this, InsideClue.class);
                    extras.putInt("CLUE_RES", playlist.get(i).clue.res);
                } else {
                    intent = new Intent(MixTape.this, external_clue.class);
                    extras.putDouble("CLUE_LAT", playlist.get(i).clue.latitude);
                    extras.putDouble("CLUE_LON", playlist.get(i).clue.longitude);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                extras.putString("CLUE_TEXT", playlist.get(i).clue.clue_text);
                intent.putExtras(extras);
                startActivity(intent);
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
