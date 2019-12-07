package com.example.hodhod.notekeeping.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {

    public static final String TIMESTAMP_1 = "05-2019";
    public static final String TIMESTAMP_2 = "06-2019";

    /*
        compare two equal notes
     */

    @Test
    void isNotesEqual_identicalProperties_returnTrue() throws Exception {

        // Arrange
        Note note1 = new Note("Note #1", "This is note #1", TIMESTAMP_1);
        note1.setId(1);
        Note note2 = new Note("Note #1", "This is note #1", TIMESTAMP_1);
        note2.setId(1);
        // Act

        // Assert
        assertEquals(note1, note2);
        System.out.println("The notes are equal");
    }


    /*
        compare notes with 2 different ids
     */

    @Test
    void isNotesEqual_differentIds_returnFalse() throws Exception {
        // Arrange
        Note note1 = new Note("Note #1", "This is note #1", TIMESTAMP_1);
        note1.setId(1);
        Note note2 = new Note("Note #1", "This is note #1", TIMESTAMP_1);
        note2.setId(2);
        // Act

        // Assert
        assertNotEquals(note1, note2);
        System.out.println("The notes are not equal");
    }

    /*
        compare two notes with different timestamps
     */

    @Test
    void isNotesEqual_differentTimestamps_returnTrue() throws Exception {
        // Arrange
        Note note1 = new Note(1,"Note #1","This is note #1",TIMESTAMP_1);
        Note note2 = new Note(1,"Note #1","This is note #1",TIMESTAMP_2);
        // Act

        // Assert
        assertEquals(note1,note2);
        System.out.println("The notes are equal");
    }

    /*
        compare two notes with different titles
     */

    @Test
    void isNotesEqual_differentTitles_returnFalse() throws Exception {
        // Arrange
        Note note1 = new Note(1,"Note #1","This is note #1",TIMESTAMP_1);
        Note note2 = new Note(1,"Note #2","This is note #1",TIMESTAMP_2);
        // Act

        // Assert
        assertNotEquals(note1,note2);
        System.out.println("The notes are not equal. They have different titles");
    }

    /*
        compare two notes with different contents
     */

    @Test
    void isNotesEqual_differentContents_returnFalse() throws Exception {
        // Arrange
        Note note1 = new Note(1,"Note #1","This is note #1",TIMESTAMP_1);
        Note note2 = new Note(1,"Note #1","This is note #2",TIMESTAMP_2);
        // Act

        // Assert
        assertNotEquals(note1,note2);
        System.out.println("The notes are not equal. They have different contents");
    }
}
