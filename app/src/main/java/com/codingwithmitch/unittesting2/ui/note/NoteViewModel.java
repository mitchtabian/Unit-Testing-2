package com.codingwithmitch.unittesting2.ui.note;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codingwithmitch.unittesting2.models.Note;
import com.codingwithmitch.unittesting2.repository.NoteRepository;
import com.codingwithmitch.unittesting2.ui.Resource;
import com.codingwithmitch.unittesting2.util.DateUtil;

import javax.inject.Inject;

public class NoteViewModel extends ViewModel {

    private static final String TAG = "NoteViewModel";
    public static final String NO_CONTENT_ERROR = "Can't save note with no content";

    public enum ViewState {VIEW, EDIT}

    // inject
    private final NoteRepository noteRepository;

    // vars
    private MutableLiveData<Note> note = new MutableLiveData<>();
    private MutableLiveData<ViewState> viewState = new MutableLiveData<>();
    private MutableLiveData<Boolean> hasActivityCreated = new MutableLiveData<>();
    private boolean isNewNote;

    @Inject
    public NoteViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
        hasActivityCreated.setValue(false);
    }

    public LiveData<Integer> insertNote() throws Exception {
        return noteRepository.insertNote(note.getValue());
    }

    public LiveData<Integer> updateNote() throws Exception {
        return noteRepository.updateNote(note.getValue());
    }

    public LiveData<Boolean> hasActivityCreated(){
        return hasActivityCreated;
    }

    public LiveData<Note> observeNote(){
        return note;
    }

    public LiveData<ViewState> observeViewState(){
        return viewState;
    }

    public void setViewState(ViewState viewState){
        this.viewState.setValue(viewState);
    }

    public void setActivityCreated(){
        hasActivityCreated.setValue(true);
    }

    public void setNote(Note note) throws Exception {
        if(note.getTitle() == null || note.getTitle().equals("")){
            throw new NullPointerException("Title can't be null");
        }
        this.note.setValue(note);
    }

    public void setIsNewNote(boolean isNewNote){
        this.isNewNote = isNewNote;
    }

    public LiveData<Resource<Integer>> saveNote() throws Exception{
        Log.d(TAG, "saveNote: attempting to save note...");

        if(!shouldAllowSave()){
            throw new Exception(NO_CONTENT_ERROR);
        }

        return new NoteInsertUpdateHelper<Integer>(){

            @Override
            public LiveData<Integer> getAction() throws Exception {
                if(isNewNote){
                    return insertNote();
                }
                else{
                    return updateNote();
                }
            }

            @Override
            public String defineAction() {
                if(isNewNote){
                    return ACTION_INSERT;
                }
                else{
                    return ACTION_UPDATE;
                }
            }

            @Override
            public void setNoteId(int noteId) {
                isNewNote = false;
                Note currentNote = note.getValue();
                currentNote.setId(noteId);
                note.setValue(currentNote);
            }
        }.getAsLiveData();
    }

    public void updateNote(String title, String content) throws Exception{
        if(title == null || title.equals("")){
            throw new NullPointerException("Title can't be null");
        }
        content = removeWhiteSpace(content);
        if(content.length() > 0){
            Note updatedNote = new Note(note.getValue());
            updatedNote.setTitle(title);
            updatedNote.setContent(content);
            updatedNote.setTimestamp(DateUtil.getCurrentTimeStamp());

            note.setValue(updatedNote);
        }
    }

    private boolean shouldAllowSave(){
        return removeWhiteSpace(note.getValue().getContent()).length() > 0;
    }

    private String removeWhiteSpace(String string){
        string = string.replace("\n", "");
        string = string.replace(" ", "");
        return string;
    }

    public boolean shouldNavigateBack(){
        if(viewState.getValue() == ViewState.VIEW){
            return true;
        }
        else {
            return false;
        }
    }
}


















