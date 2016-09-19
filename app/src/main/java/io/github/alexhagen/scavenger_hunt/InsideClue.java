package io.github.alexhagen.scavenger_hunt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class InsideClue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_clue);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int res = extras.getInt("CLUE_RES");
        String clue_text = extras.getString("CLUE_TEXT");
        ImageView ci = (ImageView) findViewById(R.id.clue_pic);
        ci.setImageResource(res);
        TextView ct = (TextView) findViewById(R.id.clue_text);
        ct.setText(clue_text);
        final String clue_name = extras.getString("CLUE_NAME");
        Button fb = (Button) findViewById(R.id.finished_inside_clue);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("CLUES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(clue_name, 1);
                InsideClue.this.finish();
            }
        });
    }
}
