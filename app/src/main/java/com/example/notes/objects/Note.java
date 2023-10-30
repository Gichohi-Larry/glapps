package com.example.notes.objects;

import android.content.Intent;
import java.io.Serializable;
import java.util.ArrayList;

public class Note implements Serializable{
    public static final String TITLE="title",BODY="body",TIMESTAMP="timestamp";
    private String title,body;
    private long timeStamp;
    public Note(String title,String body,long timeStamp) {
        this.title = title;
        this.body = body;
        this.timeStamp = timeStamp;
    }
    public Note(){

    }
    public static Note createFromIntent(Intent intent){
        return new Note(intent.getStringExtra(TITLE),
                intent.getStringExtra(BODY),
                intent.getLongExtra(TIMESTAMP,0));
    }
    public Note setTitle(String title){
        this.title=title;
        return this;
    }
    public Note setBody(String body){
        this.body=body;
        return this;
    }
    public Note setTimeStamp(long timeStamp){
        this.timeStamp=timeStamp;
        return this;
    }
    public String getTitle(){
        return title;
    }
    public String getBody(){
        return body;
    }
    public long getTimestamp(){
        return timeStamp;
    }
    public String getTimeToString(){
        return Time.getDate(timeStamp);
    }
    public void update(Note note){
        this.title=note.getTitle();
        this.body= note.getBody();
        this.timeStamp= note.getTimestamp();
    }
    public int indexIn(ArrayList<Note>notes){
        int i=0;
        while(i<notes.size() && !notes.get(i).getTitle().equals(title))
            i++;
        return i==notes.size()?-1:i;
    }
}
