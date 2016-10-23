package com.example.anton.mushroomman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }
    public void togame(View view){
        SharedPreferences stats = getSharedPreferences("stats",0);
        Intent intent = new Intent(this, GameScreen.class);
        intent.putExtra("level",stats.getInt("toplvl",1));
        startActivity(intent);
    }
    public void toselect(View view){
        Intent intent = new Intent(this, LevelSelect.class);
        startActivity(intent);
    }
    public void tohelp(View view){
        Intent intent = new Intent(this, HowToPlay.class);
        startActivity(intent);
    }
    public void toabout(View view){
        Intent intent = new Intent(this, AboutScreen.class);
        startActivity(intent);
    }
}
