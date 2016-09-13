package io.github.alexhagen.scavenger_hunt;

import android.content.Intent;
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
        Button fb = (Button) findViewById(R.id.finished_inside_clue);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsideClue.this.finish();
            }
        });
    }
}
