package com.halloworkd.zsfssdd.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class SNoteToolbarActivity extends AppCompatActivity {

    MyDatabaseHelper mdh;
    int id;
    TextView content_view;
    FloatingActionButton fab_note;
    ImageView imageView;
    String title;
    String content1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setTeme(this);

        setContentView(R.layout.activity_snote_toolbar);
        mdh = new MyDatabaseHelper(this, "NotepadList", null, 1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_SNOTE);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageView = (ImageView) findViewById(R.id.note_img_view);
        content_view = (TextView) findViewById(R.id.note_content_textview_2);
        fab_note = (FloatingActionButton) findViewById(R.id.FAB_note_edit);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content1 = intent.getStringExtra("content");
        id = intent.getIntExtra("id", 0);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_left_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(title);

        String bingPic = prefUtils.getString("bing_pic", null, this);
        if (bingPic != null){
            Glide.with(this).load(bingPic).into(imageView);
        }else {
            loadBingPic();
        }
        content_view.setText(content1);

        fab_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SNoteToolbarActivity.this, EditNote.class);
                intent1.putExtra("title", title);
                intent1.putExtra("content", content1);
                intent1.putExtra("id", id);
                startActivity(intent1);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarnotemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deletec();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }

    
    public void deletec() {
        AlertDialog.Builder ifdelete = new AlertDialog.Builder(this);
        ifdelete.setTitle(R.string.hint);
        ifdelete.setMessage(R.string.delete_hint);
        ifdelete.setCancelable(false);
        ifdelete.setPositiveButton(this.getString(R.string.agree), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = mdh.getWritableDatabase();
                db.delete("Notepad", "id = ?", new String[]{String.valueOf(id)});
                Toast.makeText(SNoteToolbarActivity.this, SNoteToolbarActivity.this.getString(R.string.delete), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        ifdelete.setNegativeButton(this.getString(R.string.disagree), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        ifdelete.show();
    }

    
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SNoteToolbarActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(SNoteToolbarActivity.this).load(bingPic).into(imageView);
                    }
                });
            }
        });

    }
}
