package com.example.hodhod.notekeeping.ui.note;

import com.example.hodhod.notekeeping.models.Note;
import com.example.hodhod.notekeeping.repository.NoteRepository;
import com.example.hodhod.notekeeping.ui.Resource;
import com.example.hodhod.notekeeping.util.InstantExecutorExtension;
import com.example.hodhod.notekeeping.util.LiveDataTestUtil;
import com.example.hodhod.notekeeping.util.TestUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Not;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;
import io.reactivex.internal.operators.single.SingleToFlowable;

import static com.example.hodhod.notekeeping.repository.NoteRepository.INSERT_SUCCESS;
import static com.example.hodhod.notekeeping.repository.NoteRepository.NOTE_TITLE_NULL;
import static com.example.hodhod.notekeeping.repository.NoteRepository.UPDATE_SUCCESS;
import static com.example.hodhod.notekeeping.ui.note.NoteViewModel.NO_CONTENT_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(InstantExecutorExtension.class)
public class NoteViewModelTest {

    // system under test
    private NoteViewModel mNoteViewModel;

    @Mock
    private NoteRepository mNoteRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mNoteViewModel = new NoteViewModel(mNoteRepository);
    }

    /*
        can't observe a note that hasn't been set
     */

    @Test
    void observeEmptyNoteWhenNoteSet() throws Exception {

        // Arrange
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();

        // Act
        Note note = liveDataTestUtil.getValue(mNoteViewModel.observeNote());

        // Assert
        assertNull(note);
    }
    
    /*
        observe a note has been set and onChanged wil trigger in activity
     */

    @Test
    void observeNote_whenSet() throws Exception {

        // Arrange
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();

        // Act
        Note note = new Note(TestUtil.TEST_NOTE_1);
        mNoteViewModel.setNote(note);
        Note observedNote = liveDataTestUtil.getValue(mNoteViewModel.observeNote());

        // Assert
        assertEquals(note,observedNote);
    }

    /*
        insert a new note and observe row returned
     */

    @Test
    void insertNote_returnRow() throws Exception {

        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Resource<Integer>>  liveDataTestUtil = new LiveDataTestUtil<>();
        final int insertedRow = 1;
        Flowable<Resource<Integer>> returnedData = SingleToFlowable.just(Resource.success(insertedRow,INSERT_SUCCESS));
        when(mNoteRepository.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        mNoteViewModel.setNote(note);
        mNoteViewModel.setIsNewNote(true);
        Resource<Integer> returnedValue = liveDataTestUtil.getValue(mNoteViewModel.saveNote());

        // Assert
        assertEquals(Resource.success(insertedRow,INSERT_SUCCESS),returnedValue);
    }

    /*
        insert: don't return a new row without observer
     */

    @Test
    void dontReturnInsertRowWithoutObserver() throws Exception {

        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);

        // Act
        mNoteViewModel.setNote(note);

        // Assert
        verify(mNoteRepository,never()).insertNote(any(Note.class));
    }

    /*
        set note, null title, throw exception
     */

    @Test
    void setNote_nullTitle_throwException() throws Exception {

        // Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setTitle(null);

        // Assert
        assertThrows(Throwable.class, new Executable() {
            @Override
            public void execute() throws Throwable {

                // Act
                mNoteViewModel.setNote(note);
            }
        });

    }

     /*
        update a note and observe row returned
     */

    @Test
    void updateNote_returnRow() throws Exception {

        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Resource<Integer>>  liveDataTestUtil = new LiveDataTestUtil<>();
        final int updatedRow = 1;
        Flowable<Resource<Integer>> returnedData = SingleToFlowable.just(Resource.success(updatedRow,UPDATE_SUCCESS));
        when(mNoteRepository.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        mNoteViewModel.setNote(note);
        mNoteViewModel.setIsNewNote(false);
        Resource<Integer> returnedValue = liveDataTestUtil.getValue(mNoteViewModel.saveNote());

        // Assert
        assertEquals(Resource.success(updatedRow,UPDATE_SUCCESS),returnedValue);
    }

    /*
        update: don't return a new row without observer
     */

    @Test
    void dontReturnUpdateRowWithoutObserver() throws Exception {

        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);

        // Act
        mNoteViewModel.setNote(note);

        // Assert
        verify(mNoteRepository,never()).updateNote(any(Note.class));
    }

    @Test
    void saveNote_shouldAllowSave_returnFalse() throws Exception {

        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setContent(null);

        // Act
        mNoteViewModel.setNote(note);
        mNoteViewModel.setIsNewNote(true);

        // Assert
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                mNoteViewModel.saveNote();
            }
        });

        assertEquals(NO_CONTENT_ERROR,exception.getMessage());
    }

}
