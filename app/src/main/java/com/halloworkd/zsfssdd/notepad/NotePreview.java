package com.halloworkd.zsfssdd.notepad;

/**
 * Created by zsfss on 2017/3/11.
 * 获取笔记预览内容
 */

public class NotePreview {

    private String title;
    private String content;
    private int id;


    public NotePreview(String title, String content, int id){
        this.title = title;
        this.content = content;
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public int getId(){
        return id;
    }
}
