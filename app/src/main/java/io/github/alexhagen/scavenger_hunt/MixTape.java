package io.github.alexhagen.scavenger_hunt;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_clues);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Zero out the clues
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("CLUES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("CLUE_1", 1);
        editor.putInt("CLUE_2", 1);
        editor.putInt("CLUE_3", 1);
        editor.putInt("CLUE_4", 0);
        editor.putInt("CLUE_5", 0);
        editor.putInt("CLUE_6", 0);
        editor.putInt("CLUE_7", 0);
        editor.commit();

        //MenuItem clue_button = (MenuItem) findViewById(R.layout.mon13reset);

        playlist.add(new Song(this, R.raw.hozier_work_song, "Work Song", "Hozier", 46, "18:30 9/12/2016",
                     new Clue("CLUE_1", R.drawable.clue_1, "'Cause my baby's sweet as can be\n" +
                                                 "She give me toothaches just from kissin' me")));  // Working!
        playlist.add(new Song(this, R.raw.the_lumineers_cleopatra, "Cleopatra", "The Lumineers", -1, "18:30 9/13/2016")); // Working!
        playlist.add(new Song(this, R.raw.lord_huron_she_lit_a_fire, "She Lit a Fire", "Lord Huron", 26, "18:30 9/14/2016",
                     new Clue("CLUE_2", R.drawable.clue_2, "I've been walking through the mountains\n" +
                                                 "I've wandered through the trees\n" +
                                                 "For her")));  // Working!
        playlist.add(new Song(this, R.raw.amber_run_i_found, "I Found", "Amber Run", -1, "18:30 9/15/2016"));
        playlist.add(new Song(this, R.raw.the_head_and_the_heart_rivers_and_roads, "Rivers and Roads", "The Head ...", 185, "19:30 9/16/2016",
                     new Clue("CLUE_3", 40.4733713, -86.87091399999997, "Rivers and roads\n" +
                                                              "Rivers 'til I reach you"))); // Working
        playlist.add(new Song(this, R.raw.gregory_alan_isakov_the_stable_song, "Stable Song", "Gregory Alan ...", -1, "18:30 9/17/2016")); // Working
        playlist.add(new Song(this, R.raw.city_and_colour_the_girl, "The Girl", "City and Colour", -1, "18:30 9/18/2016")); // Working
        playlist.add(new Song(this, R.raw.benton_paul_i_only_see_you, "I Only See You", "Benton Paul", 61, "18:30 9/19/2016",
                     new Clue("CLUE_4", R.drawable.clue_4, "I only see you,\n" +
                                                           "In all that I do."))); // Working
        playlist.add(new Song(this, R.raw.peter_gabriel_the_book_of_love, "The Book of Love", "Peter Gabriel", -1, "18:30 9/20/2016")); // Working
        playlist.add(new Song(this, R.raw.death_cab_for_cutie_the_new_year, "The New Year", "Death Cab", 132, "18:30 9/21/2016",
                     new Clue("CLUE_7", R.drawable.clue_7, "So everybody put your best suit or dress on.")));
        playlist.add(new Song(this, R.raw.lord_huron_ends_of_the_earth, "Ends of the Earth", "Lord Huron", 66, "19:00 9/21/2016",
                     new Clue("CLUE_5", 40.50553333333333, -86.84603333333334, "To the ends of the earth, \nwould you follow me?"))); // Working
        playlist.add(new Song(this, R.raw.the_lumineers_sleep_on_the_floor, "Sleep on the Floor", "Lumineers", 68, "18:30 9/22/2016",
                     new Clue("CLUE_6", R.drawable.clue_3, "We'll be driving through the state."))); // Working
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

    public void refresh(){
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
            if (current_time_s == playlist.get(i).clue_time && playlist.get(i).is_unsolved()) {
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
                extras.putString("CLUE_NAME", playlist.get(i).clue.clue_name);
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
                    refresh();
                    playSong(j);
                } else {
                    refresh();
                    Context context = getApplicationContext();
                    CharSequence text = "Checking for new songs!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
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
            textView.setTextSize(18);
            textView.setTextColor(getResources().getColor(R.color.tan));
            ib.setImageResource(R.drawable.ic_music_video);
        } else {
            Date available = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm MM/dd/yyyy");
            //formatter.setTimeZone(TimeZone.getTimeZone("Indianapolis"));
            try {
                available = formatter.parse(playlist.get(j).song_available);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat printformatter = new SimpleDateFormat("E, MM/dd\nh:mm a");
            textView.setText(printformatter.format(available));
            textView.setTextSize(14);
            textView.setTextColor(getResources().getColor(R.color.greylight));
            ib.setImageResource(R.drawable.mixtape_plain_grey);
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
        set_button(R.id.tnydcfc, 9);
        set_button(R.id.eotelh, 10);
        set_button(R.id.sotfl, 11);

    }
}

