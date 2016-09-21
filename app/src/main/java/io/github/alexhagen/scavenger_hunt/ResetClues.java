package io.github.alexhagen.scavenger_hunt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class ResetClues extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_clues);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        refresh_buttons();
    }

    public void refresh_buttons(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("CLUES", Context.MODE_PRIVATE);
        MenuItem clue = (MenuItem) findViewById(R.id.clue_1);
        if (sharedPref.getInt("CLUE_1", 0) == 1){
            clue.setIcon(R.drawable.ic_check_box_black_24dp);
        } else {
            clue.setIcon(R.drawable.ic_check_box_outline_blank_black_24dp);
        }
        clue = (MenuItem) findViewById(R.id.clue_2);
        if (sharedPref.getInt("CLUE_2", 0) == 1){
            clue.setIcon(R.drawable.ic_check_box_black_24dp);
        } else {
            clue.setIcon(R.drawable.ic_check_box_outline_blank_black_24dp);
        }
        clue = (MenuItem) findViewById(R.id.clue_3);
        if (sharedPref.getInt("CLUE_3", 0) == 1){
            clue.setIcon(R.drawable.ic_check_box_black_24dp);
        } else {
            clue.setIcon(R.drawable.ic_check_box_outline_blank_black_24dp);
        }
        clue = (MenuItem) findViewById(R.id.clue_4);
        if (sharedPref.getInt("CLUE_4", 0) == 1){
            clue.setIcon(R.drawable.ic_check_box_black_24dp);
        } else {
            clue.setIcon(R.drawable.ic_check_box_outline_blank_black_24dp);
        }
        clue = (MenuItem) findViewById(R.id.clue_5);
        if (sharedPref.getInt("CLUE_5", 0) == 1){
            clue.setIcon(R.drawable.ic_check_box_black_24dp);
        } else {
            clue.setIcon(R.drawable.ic_check_box_outline_blank_black_24dp);
        }
        clue = (MenuItem) findViewById(R.id.clue_6);
        if (sharedPref.getInt("CLUE_6", 0) == 1){
            clue.setIcon(R.drawable.ic_check_box_black_24dp);
        } else {
            clue.setIcon(R.drawable.ic_check_box_outline_blank_black_24dp);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reset_clues, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("CLUES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();


        if (id == R.id.clue_1) {
            editor.putInt("CLUE_1", 0);
        } else if (id == R.id.clue_2) {
            editor.putInt("CLUE_2", 0);
        } else if (id == R.id.clue_3) {
            editor.putInt("CLUE_3", 0);
        } else if (id == R.id.clue_4) {
            editor.putInt("CLUE_4", 0);
        } else if (id == R.id.clue_5) {
            editor.putInt("CLUE_5", 0);
        } else if (id == R.id.clue_6) {
            editor.putInt("CLUE_6", 0);
        }
        editor.commit();
        refresh_buttons();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
