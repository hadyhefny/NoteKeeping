package com.example.hodhod.notekeeping.repository;

import com.example.hodhod.notekeeping.models.Note;
import com.example.hodhod.notekeeping.persistence.NoteDao;
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
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Not;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.internal.operators.single.SingleToFlowable;

import static com.example.hodhod.notekeeping.repository.NoteRepository.DELETE_FAILURE;
import static com.example.hodhod.notekeeping.repository.NoteRepository.DELETE_SUCCESS;
import static com.example.hodhod.notekeeping.repository.NoteRepository.INSERT_FAILURE;
import static com.example.hodhod.notekeeping.repository.NoteRepository.INSERT_SUCCESS;
import static com.example.hodhod.notekeeping.repository.NoteRepository.INVALID_NOTE_ID;
import static com.example.hodhod.notekeeping.repository.NoteRepository.NOTE_TITLE_NULL;
import static com.example.hodhod.notekeeping.repository.NoteRepository.UPDATE_FAILURE;
import static com.example.hodhod.notekeeping.repository.NoteRepository.UPDATE_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(InstantExecutorExtension.class)
public class NoteRepositoryTest {

    Note NOTE1 = new Note(TestUtil.TEST_NOTE_1);
    // system under test
    private NoteRepository mNoteRepository;

    @Mock
    private NoteDao mNoteDao;

    @BeforeEach
    public void initEach() {
        MockitoAnnotations.initMocks(this);
//        mNoteDao = Mockito.mock(NoteDao.class); // we can use this instead of using annotations(@Mock) and MockAnnotations
        mNoteRepository = new NoteRepository(mNoteDao);
    }

    /*
        insert note
        verify the correct method is called
        confirm observer is triggered
        confirm new rows inserted
     */

    @Test
    void insertNote_returnRow() throws Exception {

        // Arrange
        final Long insertedRow = 1L;
        final Single<Long> returnedData = Single.just(insertedRow);
        when(mNoteDao.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final Resource<Integer> returnedValue = mNoteRepository.insertNote(NOTE1).blockingFirst();

        // Assert
        verify(mNoteDao).insertNote(any(Note.class));
        verifyNoMoreInteractions(mNoteDao);

        System.out.println("Returned value: " + returnedValue.data);
        assertEquals(Resource.success(1, INSERT_SUCCESS), returnedValue);

//        // or test using RxJava
//        mNoteRepository.insertNote(NOTE1)
//                .test()
//                .await()
//                .assertValue(Resource.success(1,INSERT_SUCCESS));
    }
    
    /*
        insert note
        Failure (return -1)
     */

    @Test
    void insertNote_returnFailure() throws Exception {

        // Arrange
        final Long failedInsert = -1L;
        final Single<Long> returnedData = Single.just(failedInsert);
        when(mNoteDao.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final Resource<Integer> returnedValue = mNoteRepository.insertNote(NOTE1).blockingFirst();

        // Assert
        verify(mNoteDao).insertNote(any(Note.class));
        verifyNoMoreInteractions(mNoteDao);

        assertEquals(Resource.error(null, INSERT_FAILURE), returnedValue);
    }
    
    /*
        insert note
        null title
        confirm throw exception
     */

    @Test
    void insertNote_nullTitle_throwException() throws Exception {

        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                mNoteRepository.insertNote(note);
            }
        });

        assertEquals(NOTE_TITLE_NULL, exception.getMessage());

    }

    /*
        update note
        verify correct method is called
        confirm observer is triggered
        confirm number of rows updated
     */

    @Test
    void updateNote_returnNumRowsUpdated() throws Exception {

        // Arrange
        final int updatedRow = 1;
        Single<Integer> returnedData = Single.just(updatedRow);
        when(mNoteDao.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final Resource<Integer> returnedValue = mNoteRepository.updateNote(NOTE1).blockingFirst();

        // Assert
        verify(mNoteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(mNoteDao);

        assertEquals(Resource.success(updatedRow, UPDATE_SUCCESS), returnedValue);
    }

    /*
        update note
        Failure (-1)
     */

    @Test
    void updateNote_returnFailure() throws Exception {

        // Arrange
        final int failedRow = -1;
        final Single<Integer> returnedData = Single.just(failedRow);
        when(mNoteDao.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final Resource<Integer> returnedValue = mNoteRepository.updateNote(NOTE1).blockingFirst();

        // Assert
        assertEquals(Resource.error(null, UPDATE_FAILURE), returnedValue);

    }

    /*
        update note
        null title
        throw an exception
     */

    @Test
    void updateNote_nullTitle_throwException() throws Exception {

        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                mNoteRepository.updateNote(note);
            }
        });

        assertEquals(NOTE_TITLE_NULL,exception.getMessage());
    }

    /*
        delete note
        null id
        throw exception
     */

    @Test
    void deleteNote_nullId_throwException() throws Exception {

        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setId(-1);
                mNoteRepository.deleteNote(note);
            }
        });

        assertEquals(INVALID_NOTE_ID, exception.getMessage());
    }
    
    /*
        delete note
        delete success
        return Resource.success with deleted row
     */

    @Test
    void deleteNote_deleteSuccess_returnResourceSuccess() throws Exception {

        // Arrange
        final int deletedRow = 1;
        Resource<Integer> successResponse = Resource.success(deletedRow,DELETE_SUCCESS);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        when(mNoteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        // Act
        Resource<Integer> observedResponse= liveDataTestUtil.getValue(mNoteRepository.deleteNote(NOTE1));

        // Assert
        assertEquals(successResponse, observedResponse);
    }

    /* delete note
        delete failure
        return resource.error
     */

    @Test
    void deleteNote_deleteFailure_returnResourceError() throws Exception {

        // Arrange
        final int deletedRow = -1;
        Resource<Integer> errorResponse = Resource.error(null,DELETE_FAILURE);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        when(mNoteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        // Act
        Resource<Integer> observedResponse= liveDataTestUtil.getValue(mNoteRepository.deleteNote(NOTE1));

        // Assert
        assertEquals(errorResponse, observedResponse);
    }

    /*
        retrieve notes
        return list of notes
     */

    @Test
    void getNotes_returnListWithNotes() throws Exception {

        // Arrange
        List<Note> notes = TestUtil.TEST_NOTES_LIST;
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(notes);
        when(mNoteDao.getNotes()).thenReturn(returnedData);

        // Act
        List<Note> observedData = liveDataTestUtil.getValue(mNoteRepository.getNotes());

        // Assert
        assertEquals(notes,observedData);

    }
    
    /*
        retrieve notes
        return empty list
     */

    @Test
    void getNotes_returnEmptyList() throws Exception {

        // Arrange
        List<Note> notes = new ArrayList<>();
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(notes);
        when(mNoteDao.getNotes()).thenReturn(returnedData);

        // Act
        List<Note> observedData = liveDataTestUtil.getValue(mNoteRepository.getNotes());

        // Assert
        assertEquals(notes,observedData);

    }
}
