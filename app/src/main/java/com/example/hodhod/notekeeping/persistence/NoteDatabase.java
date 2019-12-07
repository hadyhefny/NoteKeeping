package com.example.hodhod.notekeeping.persistence;

import com.example.hodhod.notekeeping.models.Note;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = Note.class, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "notes_db";

    public abstract NoteDao getNoteDao();

}
