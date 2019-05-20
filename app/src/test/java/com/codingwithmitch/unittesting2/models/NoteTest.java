package com.codingwithmitch.unittesting2.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {

    public static final String TIMESTAMP_1 = "05-2019";
    public static final String TIMESTAMP_2 = "04-2019";


    /*
        Compare two equal notes
     */
    @Test
    void isNotesEqual_identicalProperties_returnTrue() {
        // Arrange
        Note note1 = new Note("note #1", "this is note #1", TIMESTAMP_1);
        note1.setId(1);
        Note note2 = new Note("note #1", "this is note #1", TIMESTAMP_1);
        note2.setId(1);

        // Act

        // Assert
        assertEquals(note1, note2);
        System.out.println("Same timestamp");
    }

    /*
        Compare notes with 2 different ids
     */
    @Test
    void isNotesEqual_differentIds_returnTrue() {
        // Arrange
        Note note1 = new Note("note #1", "this is note #1", TIMESTAMP_1);
        note1.setId(1);
        Note note2 = new Note("note #1", "this is note #1", TIMESTAMP_1);
        note2.setId(2);

        // Act

        // Assert
        assertNotEquals(note1, note2);
        System.out.println("Different ids");
    }

    /*
        Compare notes with two different timestamps
     */
    @Test
    void isNotesEqual_differentTimestamp_returnTrue() {
        // Arrange
        Note note1 = new Note("note #1", "this is note #1", TIMESTAMP_1);
        note1.setId(1);
        Note note2 = new Note("note #1", "this is note #1", TIMESTAMP_2);
        note2.setId(1);

        // Act

        // Assert
        assertEquals(note1, note2); // timestamp is not considered in the .equal() method
        System.out.println("Different timestamp");
    }

    /*
        Compare notes with two different titles
     */
    @Test
    void isNotesEqual_differentTitle_returnTrue() {
        // Arrange
        Note note1 = new Note("note #1", "this is note #1", TIMESTAMP_1);
        note1.setId(1);
        Note note2 = new Note("note #2", "this is note #1", TIMESTAMP_2);
        note2.setId(1);

        // Act

        // Assert
        assertNotEquals(note1, note2);
        System.out.println("Different title");
    }

    /*
        Compare notes with different content
     */
    @Test
    void isNotesEqual_differentContent_returnTrue() {
        // Arrange
        Note note1 = new Note("note #1", "this is note #1", TIMESTAMP_1);
        note1.setId(1);
        Note note2 = new Note("note #1", "this is note #2", TIMESTAMP_2);
        note2.setId(1);

        // Act

        // Assert
        assertNotEquals(note1, note2);
        System.out.println("Different content");
    }
}
