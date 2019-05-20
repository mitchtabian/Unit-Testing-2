package com.codingwithmitch.unittesting2.repository;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.codingwithmitch.unittesting2.models.Note;
import com.codingwithmitch.unittesting2.persistence.NoteDao;
import com.codingwithmitch.unittesting2.ui.Resource;
import com.codingwithmitch.unittesting2.util.InstantExecutorExtension;
import com.codingwithmitch.unittesting2.util.LiveDataTestUtil;
import com.codingwithmitch.unittesting2.util.TestUtil;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static com.codingwithmitch.unittesting2.repository.NoteRepository.DELETE_FAILURE;
import static com.codingwithmitch.unittesting2.repository.NoteRepository.DELETE_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.*;
import static org.mockito.Mockito.*;

@ExtendWith(InstantExecutorExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class NoteRepositoryTest {

    @Mock
    private NoteDao noteDao;

    // system under test
    private NoteRepository noteRepository;

    @BeforeAll
    public void init(){
        MockitoAnnotations.initMocks(this);
        noteRepository = new NoteRepository(noteDao);
    }


    // ------------------------------------------------------------------------------------
    // Insert and Update
    //-------------------------------------------------------------------------------------

    /*
        insert notes
        verify correct method is called
        confirm Observer is triggered
        confirm new rows inserted
     */
    @Test
    void insertNote_returnRows() throws Exception {

        // Arrange
        final long insertedRow = 1L;
        when(noteDao.insertNote(any(Note.class))).thenReturn(Single.just(insertedRow));

        // Act
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Integer> liveDataTestUtil = new LiveDataTestUtil<>();
        final Integer data = liveDataTestUtil.getValue(noteRepository.insertNote(note));

        // Assert
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                verify(noteDao).insertNote(any(Note.class));
                verifyNoMoreInteractions(noteDao);

                assertEquals(1, data.intValue());
            }
        });

    }


    /*
        insert note
        null title
        confirm throw exception
     */
    @Test
    void insertNotes_nullTitle_throwException() throws Exception {

        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                noteRepository.insertNote(note);
            }
        });
    }


    /*
        update note
        verify correct method is called
        confirm Observer is triggered
        confirm number of rows is updated
     */

    @Test
    void updateNote_returnNumRowsUpdated() throws Exception {
        // Arrange
        final int updatedRow = 1;
        when(noteDao.updateNote(any(Note.class))).thenReturn(Single.just(updatedRow));

        // Act
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Integer> liveDataTestUtil = new LiveDataTestUtil<>();
        final Integer data = liveDataTestUtil.getValue(noteRepository.updateNote(note));

        // Assert
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {

                verify(noteDao).updateNote(any(Note.class));
                verifyNoMoreInteractions(noteDao);

                assertEquals(updatedRow, data.intValue());

            }
        });
    }

    /*
        update note
        null title
        confirm throw exception
    */
    @Test
    void updateNote_nullTitle_throwException() throws Exception {

        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                noteRepository.updateNote(note);
            }
        });
    }


    // ------------------------------------------------------------------------------------
    // Retrieve and delete
    //-------------------------------------------------------------------------------------


    /*
        delete note
        null id
        throw exception
     */
    @Test
    void deleteNote_nullId_throwException() throws Exception {
        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setId(-1);
                noteRepository.deleteNote(note);
            }
        });
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
        Resource<Integer> successResponse = Resource.success(deletedRow, DELETE_SUCCESS);
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        when(noteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        // Act
        Resource<Integer> observedResponse = liveDataTestUtil.getValue(noteRepository.deleteNote(note));

        // Assert
        assertEquals(successResponse, observedResponse);
    }

    /*
        delete note
        delete failure
        return Resource.error
     */
    @Test
    void deleteNote_deleteFailure_returnResourceError() throws Exception {
        // Arrange
        final int row = -1;
        Resource<Integer> errorResponse = Resource.error(DELETE_FAILURE, null);
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        when(noteDao.deleteNote(any(Note.class))).thenReturn(Single.just(row));

        // Act
        Resource<Integer> observedResponse = liveDataTestUtil.getValue(noteRepository.deleteNote(note));

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
        when(noteDao.getNotes()).thenReturn(returnedData);

        // Act
        List<Note> observedData = liveDataTestUtil.getValue(noteRepository.getNotes());


        // Assert
        assertEquals(notes, observedData);
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
        when(noteDao.getNotes()).thenReturn(returnedData);

        // Act
        List<Note> observedData = liveDataTestUtil.getValue(noteRepository.getNotes());


        // Assert
        assertEquals(notes, observedData);
    }
}





















