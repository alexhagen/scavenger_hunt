package io.github.alexhagen.scavenger_hunt;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import io.github.alexhagen.scavenger_hunt.Clue;

/**
 * Created by ahagen on 9/12/16.
 */
public class Song {

    String song_name;
    String song_available;
    int resource;
    int clue_time;
    String artist_name;
    Clue clue;
    Activity parent;
    Song(Activity p, int res, java.lang.String sn, java.lang.String an, int ct, java.lang.String ad){
        init(p, res, sn, an, ct, ad);

    }

    Song(Activity p, int res, java.lang.String sn, java.lang.String an, int ct, java.lang.String ad, Clue c){
        init(p, res, sn, an, ct, ad);
        clue = c;
    }

    public void init(Activity p, int res, java.lang.String sn, java.lang.String an, int ct, java.lang.String ad){
        song_name = sn;
        artist_name = an;
        resource = res;
        clue_time = ct;
        song_available = ad;
        parent = p;

        Date available = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm MM/dd/yyyy");
        //formatter.setTimeZone(TimeZone.getTimeZone("Indianapolis"));
        try {
            available = formatter.parse(song_available);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        AlarmManager mgr = (AlarmManager) p.getSystemService(Context.ALARM_SERVICE);
        Intent notintent = new Intent(p, new_song_alarm.class);
        PendingIntent pi = PendingIntent.getService(p, 0, notintent, 0);
        mgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, available.getTime(), pi);
    }

    public boolean is_available(){
        Date available = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm MM/dd/yyyy");
        //formatter.setTimeZone(TimeZone.getTimeZone("Indianapolis"));
        try {
            available = formatter.parse(song_available);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date now = new Date();
        Log.d("DATE", formatter.format(now));
        Log.d("DATE", formatter.format(available));
        Log.d("DATE", String.format("available: %d", available.compareTo(now)));
        return available.compareTo(now) <= 0;
    }
}
