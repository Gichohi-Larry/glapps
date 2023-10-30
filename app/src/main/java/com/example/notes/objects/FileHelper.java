package com.example.notes.objects;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileHelper {
    private final Context context;
    private static final String FILENAME="notes.dat";
    public FileHelper(Context context){
        this.context=context;
    }
    public void write(ArrayList<Note>notes){
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream;
        try{
            fileOutputStream=context.openFileOutput(FILENAME,Context.MODE_PRIVATE);
            objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(notes);
            objectOutputStream.close();
            fileOutputStream.close();
            System.out.println("saved");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public ArrayList<Note>read(){
        ArrayList<Note>notes=null;
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        try{
            fileInputStream=context.openFileInput(FILENAME);
            objectInputStream=new ObjectInputStream(fileInputStream);
            notes=(ArrayList<Note>) objectInputStream.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return notes==null?new ArrayList<>():notes;
    }
}
