package com.halloworkd.zsfssdd.notepad;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by zsfss on 2017/3/13.
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
