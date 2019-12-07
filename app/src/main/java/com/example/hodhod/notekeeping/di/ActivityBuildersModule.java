package com.example.hodhod.notekeeping.di;

import com.example.hodhod.notekeeping.ui.note.NoteActivity;
import com.example.hodhod.notekeeping.ui.noteslist.NotesListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract NotesListActivity contributeNotesListActivity();

    @ContributesAndroidInjector
    abstract NoteActivity contributeNoteActivity();

}
