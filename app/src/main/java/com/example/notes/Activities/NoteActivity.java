package com.example.notes.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.notes.R;
import com.example.notes.objects.FileHelper;
import com.example.notes.objects.Note;
import com.example.notes.objects.Time;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {
    public static final int CREATE=1,VIEW=2;
    public static final String TYPE="type";
    private boolean isView, isEditButton;
    private Button backButton,editAndSaveButton;
    private EditText titleEditText, bodyEditText;
    private LinearLayout noteLayout;
    private ArrayList<Note>notes;
    private FileHelper fileHelper;
    private String originalTitle;
    TextView timeTextView;
    private Note currentNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        attach();
        isView=getIntent().getIntExtra(NoteActivity.TYPE,0)==VIEW;
        isEditButton =isView;
        fileHelper=new FileHelper(this);

        notes=fileHelper.read();

        if(isView){
            currentNote=Note.createFromIntent(getIntent());
            originalTitle=currentNote.getTitle();
            titleEditText.setText(currentNote.getTitle());
            bodyEditText.setText(currentNote.getBody());
            timeTextView.setText(currentNote.getTimeToString());
            disableEdit();
        }
        else{
            currentNote=new Note(titleEditText.getText().toString(),
                    bodyEditText.getText().toString(),
                    -1);
            enableEdit();
        }
    }
    void disableEdit(){
        isEditButton =true;
        editAndSaveButton.setBackgroundResource(R.drawable.edit_icon);
        noteLayout.setBackgroundResource(R.drawable.background1);
        titleEditText.setEnabled(false);
        bodyEditText.setEnabled(false);
        timeTextView.setVisibility(View.VISIBLE);
    }
    void enableEdit(){
        isEditButton =false;
        editAndSaveButton.setBackgroundResource(R.drawable.save_icon);
        noteLayout.setBackgroundResource(R.drawable.background3);
        titleEditText.setEnabled(true);
        bodyEditText.setEnabled(true);
        timeTextView.setVisibility(View.INVISIBLE);
    }
    void attach(){
        backButton=findViewById(R.id.BACK_BUTTON_NOTE_ACTIVITY);
        editAndSaveButton=findViewById(R.id.EDIT_AND_SAVE);
        titleEditText =findViewById(R.id.NOTE_TITLE_NOTE_ACTIVITY);
        bodyEditText =findViewById(R.id.NOTE_BODY_NOTE_ACTIVITY);
        timeTextView=findViewById(R.id.NOTE_TIME_NOTE_ACTIVITY);
        noteLayout=findViewById(R.id.NOTE_LAYOUT_NOTE_ACTIVITY);
        setAction();
    }
    void setAction(){
        backButton.setOnClickListener((v)->onBackPressed());
        editAndSaveButton.setOnClickListener((v)->{
            if(isEditButton){
                //editNote
                enableEdit();
            }
            else {
                currentNote.setTitle(titleEditText.getText().toString());
                currentNote.setBody(bodyEditText.getText().toString());
                currentNote.setTimeStamp(Time.getCurrentTime());
                //save note
                if(currentNote.getTitle().equals("")){
                    Snackbar.make(titleEditText,"add a title",Snackbar.LENGTH_SHORT).show();
                }
                else if(currentNote.getTitle().length()>50){
                    Snackbar.make(titleEditText,"title should have 50 characters or less",Snackbar.LENGTH_SHORT).show();
                }
                else if(currentNote.getBody().equals("")){
                    Snackbar.make(bodyEditText,"add a body",Snackbar.LENGTH_SHORT).show();
                }
                else{
                    disableEdit();
                    saveNote();
                }
            }
        });
    }
    void saveNote(){
        if(isView){
            int i=new Note().setTitle(originalTitle).indexIn(notes);
            notes.get(i).update(currentNote);
            originalTitle=currentNote.getTitle();
            fileHelper.write(notes);
            timeTextView.setText(currentNote.getTimeToString());
            Toast.makeText(this, "note updated!", Toast.LENGTH_SHORT).show();
        }
        else{
            isView=true;
            originalTitle=currentNote.getTitle();
            int i=new Note().setTitle(originalTitle).indexIn(notes);
            if(i==-1){
                notes.add(currentNote);
                fileHelper.write(notes);
                timeTextView.setText(currentNote.getTimeToString());
                Toast.makeText(this, "note created!", Toast.LENGTH_SHORT).show();
            }
            else{
                Snackbar.make(editAndSaveButton,"note with this title exists",Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}