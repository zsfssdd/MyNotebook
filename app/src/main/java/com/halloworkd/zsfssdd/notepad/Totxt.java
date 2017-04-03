package com.halloworkd.zsfssdd.notepad;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by zsfss on 2017/3/27.
 */

public class Totxt {
    static private MyDatabaseHelper dbHelper;
    static Context ctx;

    static boolean txt()
    {
        ctx = MyApplication.getContext();
        dbHelper = new MyDatabaseHelper(ctx, "NotepadList", null, 1);
        dbHelper.getWritableDatabase();

        FileOutputStream out = null;
        BufferedWriter writer = null;
        File f = null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Notepad", null, null, null, null, null, null);

        int id;
        String title;
        String content;
        if (cursor.moveToLast()){
            do {
                title = cursor.getString(cursor.getColumnIndex("title"));
                content = cursor.getString(cursor.getColumnIndex("content"));
                try {
                    f = new File(title + ".txt");
                    if (!f.exists()){
                        f.createNewFile();
                    }
                    out = ctx.openFileOutput(title + ".txt", Context.MODE_APPEND);
                    writer = new BufferedWriter(new OutputStreamWriter(out));
                    writer.write(content);
                    writer.close();
                } catch (IOException e) {

                }
            }while (cursor.moveToPrevious());
        }else {
            return false;
        }
        cursor.close();
        return true;
    }

}
