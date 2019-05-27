package com.codingwithmitch.unittesting2.ui.note;

import com.codingwithmitch.unittesting2.repository.NoteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NoteViewModelTest {

    // system under test
    private NoteViewModel noteViewModel;

    @Mock
    private NoteRepository noteRepository;

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

        // Act

        // Assert
    }

    /*
        Observe a note has been set and onChanged will trigger in activity
     */

    @Test
    void observeNote_whenSet() throws Exception {
        // Arrange

        // Act

        // Assert
    }

    /*
        Insert a new note and observe row returned
     */

    @Test
    void insertNote_returnRow() throws Exception {
        // Arrange

        // Act

        // Assert
    }

    /*
        insert: dont return a new row without observer
     */

    @Test
    void dontReturnInsertRowWithoutObserver() throws Exception {
        // Arrange

        // Act

        // Assert
    }

    /*
        set note, null title, throw exception
     */

    @Test
    void setNote_nullTitle_throwException() throws Exception {
        // Arrange

        // Act

        // Assert
    }
}





















