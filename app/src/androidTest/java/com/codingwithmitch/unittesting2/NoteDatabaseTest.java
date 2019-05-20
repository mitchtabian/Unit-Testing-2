package com.codingwithmitch.unittesting2;

import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.codingwithmitch.unittesting2.persistence.NoteDao;
import com.codingwithmitch.unittesting2.persistence.NoteDatabase;

import org.junit.After;
import org.junit.Before;


import java.io.IOException;


public abstract class NoteDatabaseTest {

    private static final String TAG = "DatabaseTest";

    private NoteDatabase noteDatabase;

    public NoteDao getNoteDao(){
        return noteDatabase.getNoteDao();
    }

    @Before
    public void init(){
        noteDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                NoteDatabase.class
        ).build();
    }

    @After
    public void finish() throws IOException {
        noteDatabase.close();
    }


}






















