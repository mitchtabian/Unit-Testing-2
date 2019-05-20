package com.codingwithmitch.unittesting2.di;

import android.app.Application;

import androidx.room.Room;


import com.codingwithmitch.unittesting2.persistence.NoteDao;
import com.codingwithmitch.unittesting2.persistence.NoteDatabase;
import com.codingwithmitch.unittesting2.repository.NoteRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.codingwithmitch.unittesting2.persistence.NoteDatabase.DATABASE_NAME;

@Module
public class AppModule {

    @Singleton
    @Provides
    static NoteDatabase provideNoteDatabase(Application application){
        return Room.databaseBuilder(
                application,
                NoteDatabase.class,
                DATABASE_NAME
        ).build();
    }

    @Singleton
    @Provides
    static NoteDao provideNoteDao(NoteDatabase database){
        return database.getNoteDao();
    }

    @Singleton
    @Provides
    static NoteRepository provideNoteRepository(NoteDao noteDao){
        return new NoteRepository(noteDao);
    }

}
