package com.example.hodhod.notekeeping.ui.notesList;

import com.example.hodhod.notekeeping.models.Note;
import com.example.hodhod.notekeeping.repository.NoteRepository;
import com.example.hodhod.notekeeping.ui.Resource;
import com.example.hodhod.notekeeping.ui.noteslist.NotesListViewModel;
import com.example.hodhod.notekeeping.util.InstantExecutorExtension;
import com.example.hodhod.notekeeping.util.LiveDataTestUtil;
import com.example.hodhod.notekeeping.util.TestUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Not;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.example.hodhod.notekeeping.repository.NoteRepository.DELETE_FAILURE;
import static com.example.hodhod.notekeeping.repository.NoteRepository.DELETE_SUCCESS;

@ExtendWith(InstantExecutorExtension.class)
public class NoteListViewModelTest {

    // system under test
    private NotesListViewModel mNotesListViewModel;

    @Mock
    private NoteRepository mNoteRepository;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        mNotesListViewModel = new NotesListViewModel(mNoteRepository);
    }

    /*
        retrieve list of notes
        observe list
        return list
     */

    @Test
    void retrieveNotes_returnNotesList() throws Exception {

        // Arrange
        List<Note> returnedData = TestUtil.TEST_NOTES_LIST;
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        Mockito.when(mNoteRepository.getNotes()).thenReturn(returnedValue);

        // Act
        mNotesListViewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(mNotesListViewModel.observeNotes());

        // Assert
        Assertions.assertEquals(returnedData,observedData);
    }
    
    /*
        retrieve list of notes
        observe list
        return empty list
     */

    @Test
    void retrieveNotes_returnEmptyNotesList() throws Exception {

        // Arrange
        List<Note> returnedData = new ArrayList<>();
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        Mockito.when(mNoteRepository.getNotes()).thenReturn(returnedValue);

        // Act
        mNotesListViewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(mNotesListViewModel.observeNotes());

        // Assert
        Assertions.assertEquals(returnedData,observedData);
    }
    
    /*
        delete note
        observe Resource.success
        return Resource.success
     */

    @Test
    void deleteNote_observeResourceSuccess() throws Exception {

        // Arrange
        Note deletedNote = new Note(TestUtil.TEST_NOTE_1);
        Resource<Integer> returnedData = Resource.success(1,DELETE_SUCCESS);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<Resource<Integer>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        Mockito.when(mNoteRepository.deleteNote(Mockito.any(Note.class))).thenReturn(returnedValue);

        // Act
        Resource<Integer> observedValue = liveDataTestUtil.getValue(mNotesListViewModel.deleteNote(deletedNote));

        // Assert
        Assertions.assertEquals(returnedData,observedValue);

    }

    /*
        delete note
        observe Resource.error
        return Resource.error
     */

    @Test
    void deleteNote_observeResourceError() throws Exception {

        // Arrange
        Note deletedNote = new Note(TestUtil.TEST_NOTE_1);
        Resource<Integer> returnedData = Resource.error(null ,DELETE_FAILURE);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<Resource<Integer>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        Mockito.when(mNoteRepository.deleteNote(Mockito.any(Note.class))).thenReturn(returnedValue);

        // Act
        Resource<Integer> observedValue = liveDataTestUtil.getValue(mNotesListViewModel.deleteNote(deletedNote));

        // Assert
        Assertions.assertEquals(returnedData,observedValue);

    }


}
