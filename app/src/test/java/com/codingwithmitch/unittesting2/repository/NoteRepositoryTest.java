package com.codingwithmitch.unittesting2.repository;

import com.codingwithmitch.unittesting2.persistence.NoteDao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NoteRepositoryTest {

    // system under test
    private NoteRepository noteRepository;

    private NoteDao noteDao;

    @BeforeEach
    public void initEach(){
        noteDao = mock(NoteDao.class);
        noteRepository = new NoteRepository(noteDao);
    }


    @Test
    void dummTest() throws Exception {

        assertNotNull(noteDao);
        assertNotNull(noteRepository);

    }
}





















