package com.example.anton.mushroomman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LevelSelect extends AppCompatActivity {

    private static final String LOG_TAG = GameScreen.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        SharedPreferences stats = getSharedPreferences("stats",0);
        //for (Button)
    }

    public void togame(View view){
        int level = Integer.parseInt(view.getTag().toString());
        Intent intent = new Intent(this, GameScreen.class);
        intent.putExtra("level",level);
        startActivity(intent);
    }
}
