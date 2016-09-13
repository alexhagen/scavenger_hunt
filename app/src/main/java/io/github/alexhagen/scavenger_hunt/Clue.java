package io.github.alexhagen.scavenger_hunt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.widget.ImageView;

/**
 * Created by ahagen on 9/12/16.
 */
public class Clue {
    int res;
    java.lang.String clue_text;
    int type;
    double latitude, longitude;

    Clue(double lat, double lon, java.lang.String ct) {
        type = 1;
        latitude = lat;
        longitude = lon;
        clue_text = ct;
    }

    Clue(int r, java.lang.String ct) {
        type = 2;
        res = r;
        clue_text = ct;
    }

}
