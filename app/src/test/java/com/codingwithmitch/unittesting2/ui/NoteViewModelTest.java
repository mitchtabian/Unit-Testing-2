package com.codingwithmitch.unittesting2.ui;

import androidx.lifecycle.MutableLiveData;

import com.codingwithmitch.unittesting2.models.Note;
import com.codingwithmitch.unittesting2.repository.NoteRepository;
import com.codingwithmitch.unittesting2.ui.note.NoteViewModel;
import com.codingwithmitch.unittesting2.util.InstantExecutorExtension;
import com.codingwithmitch.unittesting2.util.LiveDataTestUtil;
import com.codingwithmitch.unittesting2.util.TestUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(InstantExecutorExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteViewModelTest {

    @Mock
    private NoteRepository noteRepository;

    // system under test
    private NoteViewModel noteViewModel;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        noteViewModel = new NoteViewModel(noteRepository);
    }

    /*
        can't observe a note that hasn't been set
     */
    @Test
    void observeEmptyNoteWhenNoteSet() throws Exception {
        // Arrange
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();

        // Act
        Note note = liveDataTestUtil.getValue(noteViewModel.observeNote());

        // Assert
        assertNull(note);
    }

    /*
        Observe a note has been set and onChanged will trigger in activity
     */
    @Test
    void observeNote_whenSet() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();

        // Act
        noteViewModel.setNote(note);
        Note observedNote = liveDataTestUtil.getValue(noteViewModel.observeNote());

        // Assert
        assertEquals(observedNote, note);
    }

    /*
        Observe a note has been set and onChanged will trigger in activity
     */
    @Test
    void dontObserveNote_whenNotSet() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();

        // Act
        Note data = liveDataTestUtil.getValue(noteViewModel.observeNote());

        // Assert
        assertNotEquals(note, data);
    }

    /*
        Insert a new note and observe row returned
     */
    @Test
    void insertNote_returnRow() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Integer> liveDataTestUtil = new LiveDataTestUtil<>();
        final int insertedRow = 1;
        MutableLiveData<Integer> returnData = new MutableLiveData<>();
        returnData.setValue(insertedRow);
        when(noteRepository.insertNote(any(Note.class))).thenReturn(returnData);

        // Act
        noteViewModel.setNote(note);
        Integer data = liveDataTestUtil.getValue(noteViewModel.insertNote());

        // Assert
        assertEquals(insertedRow, data.intValue());
    }

    /*
        insert: dont return a new row without observer
     */
    @Test
    void dontReturnInsertRowWithoutObserver() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);

        // Act
        noteViewModel.setNote(note);

        // Assert
        verify(noteRepository, never()).insertNote(any(Note.class));
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
        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {

                // Act
                noteViewModel.setNote(note);
            }
        });
    }


    /*
            Update a note and observe row number returned
         */
    @Test
    void updateNote_returnRowNum() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Integer> liveDataTestUtil = new LiveDataTestUtil<>();
        int updatedRow = 1;
        MutableLiveData<Integer> returnData = new MutableLiveData<>();
        returnData.setValue(updatedRow);
        when(noteRepository.updateNote(any(Note.class))).thenReturn(returnData);

        // Act
        noteViewModel.setNote(note);
        Integer observedReturnValue = liveDataTestUtil.getValue(noteViewModel.updateNote());

        // Assert
        assertEquals(updatedRow, observedReturnValue.intValue());
    }

    /*
       update: dont return a new row without observer
    */
    @Test
    void dontReturnUpdateRowNumWithoutObserver() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);

        // Act
        noteViewModel.setNote(note);

        // Assert
        verify(noteRepository, never()).updateNote(any(Note.class));
    }

}




























