package com.codingwithmitch.unittesting2.ui;

import androidx.lifecycle.MutableLiveData;

import com.codingwithmitch.unittesting2.models.Note;
import com.codingwithmitch.unittesting2.repository.NoteRepository;
import com.codingwithmitch.unittesting2.ui.noteslist.NotesListViewModel;
import com.codingwithmitch.unittesting2.util.InstantExecutorExtension;
import com.codingwithmitch.unittesting2.util.LiveDataTestUtil;
import com.codingwithmitch.unittesting2.util.TestUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.codingwithmitch.unittesting2.repository.NoteRepository.DELETE_FAILURE;
import static com.codingwithmitch.unittesting2.repository.NoteRepository.DELETE_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(InstantExecutorExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NotesListViewModelTest {

    @Mock
    private NoteRepository noteRepository;

    // system under test
    private NotesListViewModel viewModel;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        viewModel = new NotesListViewModel(noteRepository);
    }

    /*
        retrieve list of notes
        observe list
        return list of list
     */

    @Test
    void retrieveNotes_returnNotesList() throws Exception {
        // Arrange
        List<Note> returnedData = TestUtil.TEST_NOTES_LIST;
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.getNotes()).thenReturn(returnedValue);

        // Act
        viewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(viewModel.observeNotes());

        // Assert
        assertEquals(returnedData, observedData);
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
        when(noteRepository.getNotes()).thenReturn(returnedValue);

        // Act
        viewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(viewModel.observeNotes());

        // Assert
        assertEquals(returnedData, observedData);
    }


    /*
        delete note
        observe Resource.success of deleted note
        return Resource.success when comparing
     */

    @Test
    void deleteNote_observeResourceSuccess() throws Exception {
        // Arrange
        Note deletedNote = TestUtil.TEST_NOTE_1;
        Resource<Integer> returnedData = Resource.success(1, DELETE_SUCCESS);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<Resource<Integer>> returnedValued = new MutableLiveData<>();
        returnedValued.setValue(returnedData);
        when(noteRepository.deleteNote(any(Note.class))).thenReturn(returnedValued);

        // Act
        Resource<Integer> observedValue = liveDataTestUtil.getValue(viewModel.deleteNote(deletedNote));

        // Assert
        assertEquals(returnedData, observedValue);
    }

    /*
        delete note
        observe Resource.error of deleted note
        return Resource.error when comparing
     */
    @Test
    void deleteNote_observeResourceError() throws Exception {
        // Arrange
        Note deletedNote = TestUtil.TEST_NOTE_1;
        Resource<Integer> returnedData = Resource.error( 1, DELETE_FAILURE);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<Resource<Integer>> returnedValued = new MutableLiveData<>();
        returnedValued.setValue(returnedData);
        when(noteRepository.deleteNote(any(Note.class))).thenReturn(returnedValued);

        // Act
        Resource<Integer> observedValue = liveDataTestUtil.getValue(viewModel.deleteNote(deletedNote));

        // Assert
        assertEquals(returnedData, observedValue);
    }

}





























