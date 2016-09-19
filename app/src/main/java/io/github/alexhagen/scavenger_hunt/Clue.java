package io.github.alexhagen.scavenger_hunt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.widget.ImageView;

/**
 * Created by ahagen on 9/12/16.
 */
public class Clue {
    int res;
    java.lang.String clue_text;
    java.lang.String clue_name;
    int type;
    double latitude, longitude;

    Clue(java.lang.String cn, double lat, double lon, java.lang.String ct) {
        type = 1;
        latitude = lat;
        longitude = lon;
        clue_text = ct;
        clue_name = cn;
    }

    Clue(java.lang.String cn, int r, java.lang.String ct) {
        type = 2;
        res = r;
        clue_text = ct;
        clue_name = cn;
    }

}
