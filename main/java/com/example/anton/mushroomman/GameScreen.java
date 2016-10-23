package com.example.anton.mushroomman;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

import java.util.ArrayList;

public class GameScreen extends AppCompatActivity{

    int level;
    TableLayout table;
    String[][] board;
    TextView text1, text2, text3, text4;
    int[] items;
    int[] mmloc = new int[2];
    private static final String LOG_TAG = GameScreen.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        Intent intent = getIntent();
        level = intent.getIntExtra("level",1);
        table = (TableLayout) findViewById(R.id.table);
        board = initialize(level);
        text1 = (TextView) findViewById(R.id.keynum);
        text2 = (TextView) findViewById(R.id.dollnum);
        text3 = (TextView) findViewById(R.id.oxynum);
        text4 = (TextView) findViewById(R.id.cemnum);

        findViewById(android.R.id.content).setOnTouchListener(new OnSwipeTouchListener(GameScreen.this) {
            public void onSwipeTop() {
                move("up");
            }
            public void onSwipeRight() {
                move("right");
            }
            public void onSwipeLeft() {
                move("left");
            }
            public void onSwipeBottom() {
                move("down");
            }

        });

    }

    public String[][] initialize(int level){
        items = new int[]{0,0,0,0};
        String temp = "level_"+String.valueOf(level);
        int temp2 = getStringIdentifier(this,temp);
        String code = getResources().getString(temp2);
        String[] codevals = code.split("-");
        String[][] board = new String[Integer.parseInt(codevals[0])][Integer.parseInt(codevals[1])];
        String item = null;
        for (int i=2;i<codevals.length;i++){
            try {
                int int1 = Integer.parseInt(codevals[i])-1;
                int int2 = Integer.parseInt(codevals[i+1])-1;
                board[int1][int2] = item;
                if (item.equals("mm")){
                    mmloc[0]=int1;
                    mmloc[1]=int2;
                }
                i++;
            } catch(NumberFormatException e){
                item = codevals[i];
            }
        }
        for (int i = 0; i < Integer.parseInt(codevals[0]); i++) {
            TableRow row = new TableRow(this);
            int size = Math.min(275/Integer.parseInt(codevals[0]),450/Integer.parseInt(codevals[1]));
            int sizeact = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    sizeact));
            // inner for loop
            for (int j = 0; j < Integer.parseInt(codevals[1]); j++) {
                ImageView iv = new ImageView(this);
                iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                        sizeact));
                String tempval = board[i][j];
                if (tempval!=null){
                    iv.setImageResource(getResources().getIdentifier(tempval, "drawable", getPackageName()));
                }
                row.addView(iv);
            }
            table.addView(row);
        }

        return board;
    }

    public void move(String dir){
        int[] moveto = mmloc.clone();
        int[] moveto2 = mmloc.clone();
        switch(dir){
            case "up":
                moveto[0]--;
                moveto2[0]-=2;
                break;
            case "down":
                moveto[0]++;
                moveto2[0]+=2;
                break;
            case "left":
                moveto[1]--;
                moveto2[1]-=2;
                break;
            case "right":
                moveto[1]++;
                moveto2[1]+=2;
                break;
        }
        String dest = null;
        ArrayList<siarr> updater = new ArrayList<siarr>();
        try{
            if (board[moveto[0]][moveto[1]]!=null){
                dest = board[moveto[0]][moveto[1]];
            } else {
                dest = "";
            }
        } catch(NullPointerException e){
            return;
        }
        if (dest.contains("port")){
            String color = dest.substring(0,1);
            int[] loc = new int[2];
            String targ = color.concat("port");
            int rows = board.length;
            int cols = board[0].length;
            for(int i=0;i<rows;i++){
                for (int j=0;j<cols;j++){
                    if (board[i][j].contains(targ) && !(i==moveto[0] && j==moveto[1])){
                        char point = board[i][j].charAt(5);
                        switch(point){
                            case 'u':
                                moveto[0]=i-1; moveto[1]=j;
                                moveto2[0]=i-2; moveto2[1]=j;
                                break;
                            case 'd':
                                moveto[0]=i+1; moveto[1]=j;
                                moveto2[0]=i+2; moveto2[1]=j;
                                break;
                            case 'l':
                                moveto[0]=i; moveto[1]=j-1;
                                moveto2[0]=i; moveto2[1]=j-2;
                                break;
                            case 'r':
                                moveto[0]=i; moveto[1]=j+1;
                                moveto2[0]=i; moveto2[1]=j+2;
                                break;
                        }
                    }
                }
            }
            dest = "";
        }
        String gameover = "false";
        switch(dest){
            case "bwall":
            case "gwall":
            case "dyn":
                return;
            case "key":
                board[mmloc[0]][mmloc[1]]=null;
                board[moveto[0]][moveto[1]]="mm";
                updater.add(new siarr(null,mmloc[0],mmloc[1]));
                updater.add(new siarr("mm",moveto[0],moveto[1]));
                mmloc = moveto;
                items[0]++;
                TextView text = (TextView) findViewById(R.id.keynum);
                text.setText(String.valueOf(Integer.parseInt(text.getText().toString())+1));
                break;
            case "lock":
                if (items[0]==0) {
                    return;
                }
                board[mmloc[0]][mmloc[1]]=null;
                board[moveto[0]][moveto[1]]="mm";
                updater.add(new siarr(null,mmloc[0],mmloc[1]));
                updater.add(new siarr("mm",moveto[0],moveto[1]));
                mmloc = moveto;
                items[0]--;
                TextView text11 = (TextView) findViewById(R.id.keynum);
                text11.setText(String.valueOf(Integer.parseInt(text11.getText().toString())-1));
                break;
            case "doll":
                board[mmloc[0]][mmloc[1]]=null;
                board[moveto[0]][moveto[1]]="mm";
                updater.add(new siarr(null,mmloc[0],mmloc[1]));
                updater.add(new siarr("mm",moveto[0],moveto[1]));
                mmloc = moveto;
                items[1]++;
                TextView text2 = (TextView) findViewById(R.id.dollnum);
                text2.setText(String.valueOf(Integer.parseInt(text2.getText().toString())+1));
                break;
            case "guard":
                if (items[1]==0) {
                    return;
                }
                board[mmloc[0]][mmloc[1]]=null;
                board[moveto[0]][moveto[1]]="mm";
                updater.add(new siarr(null,mmloc[0],mmloc[1]));
                updater.add(new siarr("mm",moveto[0],moveto[1]));
                mmloc = moveto;
                items[1]--;
                TextView text12 = (TextView) findViewById(R.id.dollnum);
                text12.setText(String.valueOf(Integer.parseInt(text12.getText().toString())-1));
                break;
            case "oxy":
                board[mmloc[0]][mmloc[1]]=null;
                board[moveto[0]][moveto[1]]="mm";
                updater.add(new siarr(null,mmloc[0],mmloc[1]));
                updater.add(new siarr("mm",moveto[0],moveto[1]));
                mmloc = moveto;
                items[2]+=3;
                TextView text3 = (TextView) findViewById(R.id.oxynum);
                text3.setText(String.valueOf(Integer.parseInt(text3.getText().toString())+3));
                break;
            case "wat":
                if (items[2]==0) {
                    ArrayList<siarr> newlist = new ArrayList<siarr>();
                    newlist.add(new siarr(null,mmloc[0],mmloc[1]));
                    update(newlist);
                    gameover("wat");
                    return;
                }
                board[mmloc[0]][mmloc[1]]=null;
                board[moveto[0]][moveto[1]]="mm";
                updater.add(new siarr(null,mmloc[0],mmloc[1]));
                updater.add(new siarr("mm",moveto[0],moveto[1]));
                mmloc = moveto;
                items[2]--;
                TextView text13 = (TextView) findViewById(R.id.oxynum);
                text13.setText(String.valueOf(Integer.parseInt(text13.getText().toString())-1));
                break;
            case "cem":
                board[mmloc[0]][mmloc[1]]=null;
                board[moveto[0]][moveto[1]]="mm";
                updater.add(new siarr(null,mmloc[0],mmloc[1]));
                updater.add(new siarr("mm",moveto[0],moveto[1]));
                mmloc = moveto;
                items[3]++;
                TextView text4 = (TextView) findViewById(R.id.cemnum);
                text4.setText(String.valueOf(Integer.parseInt(text4.getText().toString())+1));
                break;
            case "hole":
                if (items[3]==0) {
                    ArrayList<siarr> newlist = new ArrayList<siarr>();
                    newlist.add(new siarr(null,mmloc[0],mmloc[1]));
                    update(newlist);
                    gameover("hole");
                    return;
                }
                board[mmloc[0]][mmloc[1]]=null;
                board[moveto[0]][moveto[1]]="mm";
                updater.add(new siarr(null,mmloc[0],mmloc[1]));
                updater.add(new siarr("mm",moveto[0],moveto[1]));
                mmloc = moveto;
                items[3]--;
                TextView text14 = (TextView) findViewById(R.id.cemnum);
                text14.setText(String.valueOf(Integer.parseInt(text14.getText().toString())-1));
                break;
            case "jelly":
                if (board[moveto2[0]][moveto2[1]]!=null) {
                    return;
                }
                board[moveto2[0]][moveto2[1]]="jelly";
                board[mmloc[0]][mmloc[1]]=null;
                board[moveto[0]][moveto[1]]="mm";
                updater.add(new siarr("jelly",moveto2[0],moveto2[1]));
                updater.add(new siarr(null,mmloc[0],mmloc[1]));
                updater.add(new siarr("mm",moveto[0],moveto[1]));
                mmloc = moveto;
                break;
            case "gun":
                try {
                    if (board[moveto2[0]][moveto2[1]]=="exit") {
                        gameover = "exit";
                    }
                    if (board[moveto2[0]][moveto2[1]]=="dyn") {
                        ArrayList<siarr> newlist = new ArrayList<siarr>();
                        newlist.add(new siarr(null,mmloc[0],mmloc[1]));
                        update(newlist);
                        gameover = "dyn";
                    }
                    if(board[moveto2[0]][moveto2[1]]!="gwall"){
                        board[moveto2[0]][moveto2[1]]=null;
                        updater.add(new siarr(null,moveto2[0],moveto2[1]));
                    }
                    board[mmloc[0]][mmloc[1]]=null;
                    board[moveto[0]][moveto[1]]="mm";
                    updater.add(new siarr(null,mmloc[0],mmloc[1]));
                    updater.add(new siarr("mm",moveto[0],moveto[1]));
                    mmloc = moveto;
                } catch (NullPointerException e){

                }
                break;
            case "bomb":
                for (int i=moveto[0]-1;i<=moveto[0]+1;i++){
                    for (int j=moveto[1]-1;j<=moveto[1]+1;j++){
                        try {
                            if(board[i][j]=="exit") {
                                gameover = "exit";
                            }
                            if(board[i][j]=="dyn") {
                                ArrayList<siarr> newlist = new ArrayList<siarr>();
                                newlist.add(new siarr(null,mmloc[0],mmloc[1]));
                                update(newlist);
                                gameover = "dyn";
                            }
                            if (board[i][j]!="gwall"){
                                board[i][j]=null;
                                updater.add(new siarr(null,i,j));
                            }
                        } catch (NumberFormatException e){

                        }
                    }
                }
                board[mmloc[0]][mmloc[1]]=null;
                board[moveto[0]][moveto[1]]="mm";
                updater.add(new siarr(null,mmloc[0],mmloc[1]));
                updater.add(new siarr("mm",moveto[0],moveto[1]));
                mmloc = moveto;
                break;
            case "exit":
                complete();
                break;
            default:
                board[mmloc[0]][mmloc[1]]=null;
                board[moveto[0]][moveto[1]]="mm";
                updater.add(new siarr(null,mmloc[0],mmloc[1]));
                updater.add(new siarr("mm",moveto[0],moveto[1]));
                mmloc = moveto;
                break;
        }
        if (!gameover.equals("false")){
            gameover(gameover);
        }
        update(updater);
    }

    public void update(ArrayList<siarr> updater){
        for (int i=0;i<updater.size();i++){
            String string = updater.get(i).getstring();
            int int1 = updater.get(i).getint1();
            int int2 = updater.get(i).getint2();
            TableRow row = (TableRow)table.getChildAt(int1);
            ImageView imview = (ImageView)row.getChildAt(int2);
            if (string!=null){
                imview.setImageResource(getResources().getIdentifier(string, "drawable", getPackageName()));
            } else {
                imview.setImageDrawable(null);
            }
        }
    }

    public void complete(){
        Toast.makeText(this, "You made it!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, GameScreen.class);
        intent.putExtra("level",level+1);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void gameover(String cond){
        String message = "";
        switch(cond){
            case "wat":
                message = "You drowned!";
                break;
            case "hole":
                message = "You fell into a hole!";
                break;
            case "exit":
                message = "You destroyed the exit!";
                break;
            case "dyn":
                message = "You blew yourself up!";
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, GameScreen.class);
        intent.putExtra("level",level);
        startActivity(intent);
    }

    public static int getStringIdentifier(Context context, String name) {
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }
}

class siarr {
    private String string;
    private int int1;
    private int int2;
    public siarr(String string, int int1, int int2){
        this.string = string;
        this.int1 = int1;
        this.int2 = int2;
    }
    public String getstring(){
        return this.string;
    }
    public int getint1(){
        return this.int1;
    }
    public int getint2(){
        return this.int2;
    }
}