package com.halloworkd.zsfssdd.notepad;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Time;

public class NoteActivity extends AppCompatActivity {

    EditText tilt;
    EditText content;
    FloatingActionButton saveBTN;
    boolean isSeave;
    MyDatabaseHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setTeme(this);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_note);
        setSupportActionBar(toolbar);

        
        isSeave = false;

        
        dbHelper = new MyDatabaseHelper(this, "NotepadList", null, 1);
        database = dbHelper.getWritableDatabase();

        tilt = (EditText) findViewById(R.id.note_title);
        content = (EditText) findViewById(R.id.note_content);
        saveBTN = (FloatingActionButton) findViewById(R.id.fab_note);

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (content.getText().toString().length() == 0){
                finish();
            }
            if (!isSeave && content.getText().toString().length() != 0){
                AlertDialog.Builder save = new AlertDialog.Builder(this);
                save.setTitle(R.string.hint);
                save.setMessage(R.string.no_saved);
                save.setPositiveButton(this.getString(R.string.agree), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveNote();
                        isSeave = true;
                        finish();
                    }
                });
                save.setNegativeButton(this.getString(R.string.disagree), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                save.show();
            }
        }
        return false;
    }

    
    private void saveNote(){
        String contentstr = content.getText().toString();
        String titlestr = tilt.getText().toString();

        if (contentstr.length() == 0){
            Toast.makeText(this, this.getString(R.string.content_null), Toast.LENGTH_SHORT).show();
        }else {
            if (titlestr.length() == 0){
                if (contentstr.length() >= 5){
                    titlestr = contentstr.substring(0, 5);
                }else {
                    titlestr = contentstr.substring(0, contentstr.length());
                }
            }
            ContentValues values = new ContentValues();
            long time = getTime();
            
            values.put("title", titlestr);
            values.put("content", contentstr);
            values.put("time", time);
            database.insert("Notepad", null, values);

            Toast.makeText(this, this.getString(R.string.saved), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    
    private long getTime(){
        long time = 0;
        android.text.format.Time t = new android.text.format.Time();
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour;
        int minute = t.minute;
        int second = t.second;
        String timestr = String.valueOf(year) + String.valueOf(month) + String.valueOf(date) + String.valueOf(hour) + String.valueOf(minute) + String.valueOf(second);
        time = Integer.valueOf(timestr);
        return time;
    }
}
