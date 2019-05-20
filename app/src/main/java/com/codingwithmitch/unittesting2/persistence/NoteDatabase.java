package com.codingwithmitch.unittesting2.persistence;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.codingwithmitch.unittesting2.models.Note;


@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "notes_db";

    public abstract NoteDao getNoteDao();
}