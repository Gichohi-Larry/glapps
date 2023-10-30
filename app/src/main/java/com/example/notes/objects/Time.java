package com.example.notes.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Time {
    public static long getCurrentTime(){
        Calendar currentTime = Calendar.getInstance();
        return currentTime.getTimeInMillis();
    }
    public static String getDate(long timeStamp){

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeStamp);
        String formattedTime = new SimpleDateFormat("dd MMMM yyyy").format(cal.getTime());
        return formattedTime;
    }
}
