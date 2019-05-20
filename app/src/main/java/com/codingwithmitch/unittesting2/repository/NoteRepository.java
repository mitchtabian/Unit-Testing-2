package com.codingwithmitch.unittesting2.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.codingwithmitch.unittesting2.models.Note;
import com.codingwithmitch.unittesting2.persistence.NoteDao;
import com.codingwithmitch.unittesting2.ui.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class NoteRepository {

    private static final String TAG = "NoteRepository";
    public static final String NOTE_TITLE_NULL = "Note title cannot be null";
    public static final String INVALID_NOTE_ID = "Invalid id. Can't delete note";
    public static final String DELETE_SUCCESS = "Delete success";
    public static final String DELETE_FAILURE = "Delete failure";

    // inject
    @NonNull
    private final NoteDao noteDao;

    @Inject
    public NoteRepository(@NonNull NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    public LiveData<Integer> insertNote(final Note note) throws Exception{

        checkTitle(note);

        return LiveDataReactiveStreams.fromPublisher(
                noteDao.insertNote(note)
                        .map(new Function<Long, Integer>() {
                            @Override
                            public Integer apply(Long aLong) throws Exception {
                                long l = aLong;
                                return (int)l;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .toFlowable()
        );
    }

    public LiveData<Integer> updateNote(final Note note) throws Exception{

        checkTitle(note);

        return LiveDataReactiveStreams.fromPublisher(

                noteDao.updateNote(note)
                        .subscribeOn(Schedulers.io())
                        .toFlowable()
        );
    }

    public LiveData<Resource<Integer>> deleteNote(final Note note) throws Exception{

        checkId(note);

        return LiveDataReactiveStreams.fromPublisher(
                noteDao.deleteNote(note)
                        .map(new Function<Integer, Resource<Integer>>() {
                            @Override
                            public Resource<Integer> apply(Integer integer) throws Exception {
                                if(integer > 0){
                                    return Resource.success(integer, DELETE_SUCCESS);
                                }
                                return Resource.error(DELETE_FAILURE, null);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .toFlowable()
        );
    }

    public LiveData<List<Note>> getNotes(){
        return noteDao.getNotes();
    }

    private void checkTitle(Note note) throws Exception{
        if(note.getTitle() == null){
            throw new Exception(NOTE_TITLE_NULL);
        }
    }

    private void checkId(Note note)throws Exception{
        if(note.getId() < 0){
            throw new Exception(INVALID_NOTE_ID);
        }
    }

}


























