package com.halloworkd.zsfssdd.notepad;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditNote extends AppCompatActivity {

    String title;
    String content1;
    int id;
    EditText title_edit;
    EditText content_edit;
    FloatingActionButton fab_save_edit;
    MyDatabaseHelper dbHelper;
    boolean isSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setTeme(this);
        setContentView(R.layout.activity_edit_note);

        title_edit = (EditText) findViewById(R.id.note_title_edit);
        content_edit = (EditText) findViewById(R.id.note_content_edit);
        fab_save_edit = (FloatingActionButton) findViewById(R.id.fab_note_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_note_edit);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_left_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new MyDatabaseHelper(this, "NotepadList", null, 1);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content1 = intent.getStringExtra("content");
        id = intent.getIntExtra("id", 0);
        isSave = false;

        title_edit.setText(title);
        content_edit.setText(content1);

        fab_save_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seave_note();
                isSave = true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                is_saved();
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            is_saved();
        }
        return false;
    }

    private void is_saved(){
        boolean flag = true;
        if (content_edit.getText().toString().equals(content1) && title_edit.getText().toString().equals(title)){
            flag = false;
        }
        if (!isSave && flag){
            AlertDialog.Builder save = new AlertDialog.Builder(this);
            save.setTitle(R.string.hint);
            save.setMessage(R.string.no_saved);
            save.setPositiveButton(this.getString(R.string.agree), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    seave_note();
                    isSave = true;
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
        if (!flag){
            finish();
        }
    }

    private void seave_note(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title_edit.getText().toString());
        values.put("content", content_edit.getText().toString());
        db.update("Notepad", values, "id = ?", new String[]{ String.valueOf(id) });
        Toast.makeText(this, this.getString(R.string.saved), Toast.LENGTH_SHORT).show();
        finish();
    }
}
