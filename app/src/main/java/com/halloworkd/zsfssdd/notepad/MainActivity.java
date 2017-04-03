
package com.halloworkd.zsfssdd.notepad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private List<NotePreview> noteList = new ArrayList<>();
    private NoteAdapter adapter;
    private DrawerLayout mDrawerLayout;
    public static MainActivity Main_instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTeme(this);
        setContentView(R.layout.activity_main);

        Main_instance = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        
        dbHelper = new MyDatabaseHelper(this, "NotepadList", null, 1);
        dbHelper.getWritableDatabase();

        
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }



        
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_new_note:
                        openNoteActivity();
                        break;
                    case R.id.nav_settings:
                        openString();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNoteActivity();
            }
        });

        
        initNotes();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(adapter);
    }

    public void initNotes(){
        noteList.clear();
        int id;
        String title;
        String content;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Notepad", null, null, null, null, null, null);
        if (cursor.moveToLast()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"));
                title = cursor.getString(cursor.getColumnIndex("title"));
                content = cursor.getString(cursor.getColumnIndex("content"));
                noteList.add(new NotePreview(title, content, id));
            }while (cursor.moveToPrevious());
        }
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initNotes();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(adapter);
        isLocked();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyApplication.setisLock(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.settings:
                openString();
                break;
            case R.id.theme:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.theme))
                        .setPositiveButton(getString(R.string.disagree), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).create();
                dialog.setView(getColorPickerView(dialog));
                dialog.show();
                break;
            default:
        }
        return true;
    }

    
    private View getColorPickerView(final AlertDialog dialog){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.color_picker, null);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor spe = getSharedPreferences("item", MODE_PRIVATE).edit();
                switch (v.getId()) {
                    case R.id.magenta:
                        spe.putInt("theme",R.style.AppTheme);
                        break;
                    case R.id.red:
                        spe.putInt("theme",R.style.AppTheme_Red);
                        break;
                    case R.id.pink:
                        spe.putInt("theme",R.style.AppTheme_Pink);
                        break;
                    case R.id.yellow:
                        spe.putInt("theme",R.style.AppTheme_Yellow);
                        break;
                    default:
                        break;
                }
                spe.apply();
                dialog.cancel();
                restart();
            }
        };

        rootView.findViewById(R.id.magenta).setOnClickListener(clickListener);
        rootView.findViewById(R.id.red).setOnClickListener(clickListener);
        rootView.findViewById(R.id.pink).setOnClickListener(clickListener);
        rootView.findViewById(R.id.yellow).setOnClickListener(clickListener);

        return rootView;
    }

    
    public static void setTeme(Context context){
        SharedPreferences preferences = context.getSharedPreferences("item", MODE_PRIVATE);
        int themeId = preferences.getInt("theme", 0);

        switch (themeId) {
            case R.style.AppTheme_Red:
                context.setTheme(themeId);
                break;
            case R.style.AppTheme_Pink:
                context.setTheme(themeId);
                break;
            case R.style.AppTheme_Yellow:
                context.setTheme(themeId);
                break;
            default:
                context.setTheme(R.style.AppTheme);
                break;
        }
    }

    private void restart() {
        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void openNoteActivity(){
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    public void  openString(){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    
    public void isLocked(){
        Log.d("pc", "isLocked: " + String.valueOf(MyApplication.isLock) + "," + String.valueOf(prefUtils.getBoolean(MyApplication.getContext().getString(R.string.settings_password), false, this)));
        if (MyApplication.isLock && prefUtils.getBoolean(MyApplication.getContext().getString(R.string.settings_password), false, this)){
            startActivity(new Intent(this, Passwoed.class));
        }
    }
}
