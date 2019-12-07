package com.example.hodhod.notekeeping;

import com.example.hodhod.notekeeping.persistence.NoteDao;
import com.example.hodhod.notekeeping.persistence.NoteDatabase;

import org.junit.After;
import org.junit.Before;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

public abstract class NoteDatabaseTest {

    //system under test
    private NoteDatabase mNoteDatabase;

    public NoteDao getNoteDao() {
        return mNoteDatabase.getNoteDao();
    }

    @Before
    public void init() {
        mNoteDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                NoteDatabase.class
        ).build();
    }

    @After
    public void finish() {
        mNoteDatabase.close();
    }

}
