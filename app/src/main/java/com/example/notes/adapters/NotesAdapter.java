package com.example.notes.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notes.Activities.HomeActivity;
import com.example.notes.Activities.NoteActivity;
import com.example.notes.R;
import com.example.notes.objects.FileHelper;
import com.example.notes.objects.Note;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {
    private final Context context;
    private final ArrayList<Note>notes,originalNotes;
    private final HomeActivity root;

    public NotesAdapter(Context context, HomeActivity root, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
        this.root=root;
        this.originalNotes = new ArrayList<>(notes);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View noteView= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_design,parent,false);
        return new NoteHolder(noteView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note=notes.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.bodyTextView.setText(note.getBody());

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent noteIntent=new Intent(context, NoteActivity.class);
                noteIntent.putExtra(Note.TITLE,note.getTitle());
                noteIntent.putExtra(Note.BODY,note.getBody());
                noteIntent.putExtra(Note.TIMESTAMP,note.getTimestamp());
                noteIntent.putExtra(NoteActivity.TYPE,NoteActivity.VIEW);
                context.startActivity(noteIntent);
            }
        };
        holder.noteLayout.setOnClickListener(onClickListener);
        holder.titleTextView.setOnClickListener(onClickListener);
        holder.bodyTextView.setOnClickListener(onClickListener);

        holder.removeButton.setOnClickListener((v)->{
            new AlertDialog.Builder(context)
                    .setMessage("are you sure you want to delete '"+note.getTitle()+"'?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            notes.remove(note);
                            originalNotes.remove(note);
                            updateRoot();
                            notifyItemRemoved(holder.getAdapterPosition());
                            new FileHelper(context).write(originalNotes);
                        }
                    })
                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        });
    }
    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void filter(CharSequence txt) {
        notes.clear();
        for(Note note:originalNotes)
            if(note.getTitle().toLowerCase().contains(txt))
                notes.add(note);
        notifyDataSetChanged();
    }
    public void updateRoot(){
        if(notes.size()>0){
            root.notesTextView.setText("Notes("+notes.size()+")");
            root.zeroNotesTextView.setText("");
        }
        else{
            root.notesTextView.setText("Notes");
            root.zeroNotesTextView.setText("click plus to create a note");
        }

    }
    public static class NoteHolder extends RecyclerView.ViewHolder {
        TextView titleTextView,bodyTextView;
        LinearLayout noteLayout;
        Button removeButton;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            noteLayout=itemView.findViewById(R.id.NOTE_LAYOUT);
            titleTextView=itemView.findViewById(R.id.TITLE_TEXTVIEW);
            bodyTextView=itemView.findViewById(R.id.BODY_TEXT_VIEW);
            removeButton=itemView.findViewById(R.id.REMOVE_BUTTON_NOTE_DESIGN);
        }
    }
}
