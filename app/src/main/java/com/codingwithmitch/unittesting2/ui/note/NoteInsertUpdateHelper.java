package com.codingwithmitch.unittesting2.ui.note;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.codingwithmitch.unittesting2.ui.Resource;

public abstract class NoteInsertUpdateHelper<T> {

    public static final String ACTION_INSERT = "ACTION_INSERT";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String GENERIC_ERROR = "Something went wrong";

    private MediatorLiveData<Resource<T>> result = new MediatorLiveData<>();

    public NoteInsertUpdateHelper(){
        init();
    }

    private void init()  {

        result.setValue((Resource<T>) Resource.loading(null));
        try {
            final LiveData<Resource<T>> source = getAction();
            result.addSource(source, new Observer<Resource<T>>() {
                @Override
                public void onChanged(Resource<T> tResource) {
                    result.removeSource(source);
                    result.setValue(tResource);
                    setNewNoteIdIfIsNewNote(tResource);
                    onTransactionComplete();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            result.setValue(Resource.<T>error(null, GENERIC_ERROR));
        }
    }

    private void setNewNoteIdIfIsNewNote(Resource<T> resource){
        if(resource.data != null) {
            if (resource.data.getClass() == Integer.class) {
                int i = (Integer) resource.data;
                if (defineAction().equals(ACTION_INSERT)) {
                    if (i >= 0) {
                        setNoteId(i);
                    }
                }
            }
        }
    }

    public abstract void setNoteId(int noteId);

    public abstract LiveData<Resource<T>> getAction() throws Exception;

    public abstract String defineAction();

    public abstract void onTransactionComplete();

    public final LiveData<Resource<T>> getAsLiveData(){
        return result;
    }
}














