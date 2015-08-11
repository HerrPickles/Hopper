package com.example.misklahr.hopper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
    private Button play, reset;
    private SharedPreferences spref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spref = PreferenceManager.getDefaultSharedPreferences(this);

        play = (Button) findViewById(R.id.play_button);
        play.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent startGame = new Intent(MainActivity.this, DrawGame.class);
                startActivity(startGame);
            }
        });


        reset = (Button) findViewById(R.id.reset_button);
        reset.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                SharedPreferences.Editor editor = spref.edit();
                editor.putInt("points", 0);
                editor.putInt("CurrentLevel", 1);
                editor.commit();
            }
        });

    }




}