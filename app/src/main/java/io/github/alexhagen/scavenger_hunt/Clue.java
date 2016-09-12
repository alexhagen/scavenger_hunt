package io.github.alexhagen.scavenger_hunt;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import io.github.alexhagen.scavenger_hunt.MixTape;

import io.github.alexhagen.scavenger_hunt.InsideClue;

/**
 * Created by ahagen on 9/12/16.
 */
public class Clue extends Activity{

    Clue(double lat, double lon) {

    }

    Clue(int res) {
        InsideClue ic = new InsideClue();
        Intent intent = new Intent(Clue.this, InsideClue.class);
        ImageView iv = (ImageView) ic.findViewById(R.id.clue_pic);
        iv.setImageResource(res);
        startActivity(intent);
    }
}
