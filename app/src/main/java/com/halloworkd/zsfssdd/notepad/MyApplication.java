package com.halloworkd.zsfssdd.notepad;

import android.app.Application;
import android.content.Context;

/**
 * Created by zsfss on 2017/3/12.
 */

public class MyApplication extends Application {

    private static Context context;
    static boolean isLock;
    static Context settingctx;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        isLock = true;
    }


    public static Context getContext(){
        return context;
    }

    public static void setisLock(boolean b){
        isLock = b;
    }

    public static boolean getisLock(){
        return isLock;
    }

    public static void setSettingctx(Context ctx){
        settingctx = ctx;
    }

    public static Context getSettingctx(){
        return settingctx;
    }
}
