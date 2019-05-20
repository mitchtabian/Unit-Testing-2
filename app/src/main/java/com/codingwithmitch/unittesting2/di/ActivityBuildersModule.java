package com.codingwithmitch.unittesting2.di;

import com.codingwithmitch.unittesting2.ui.note.NoteActivity;
import com.codingwithmitch.unittesting2.ui.noteslist.NotesListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract NotesListActivity contributeNoteListActivity();

    @ContributesAndroidInjector
    abstract NoteActivity contributeNoteActivity();

}
