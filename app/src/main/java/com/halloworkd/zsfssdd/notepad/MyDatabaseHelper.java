package com.halloworkd.zsfssdd.notepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by zsfss on 2017/3/12.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String NOTEPAD_LIST = "create table Notepad ("
            + "id integer primary key autoincrement, "
            + "title text, "
            + "content text, "
            + "time integer)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTEPAD_LIST);
        Toast.makeText(mContext, "已建立SQL", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
