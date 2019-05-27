package com.codingwithmitch.unittesting2.ui.noteslist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.codingwithmitch.unittesting2.R;
import com.codingwithmitch.unittesting2.repository.NoteRepository;
import com.codingwithmitch.unittesting2.ui.note.NoteActivity;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class NotesListActivity extends DaggerAppCompatActivity {

    private static final String TAG = "NotesListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);


        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }
}
