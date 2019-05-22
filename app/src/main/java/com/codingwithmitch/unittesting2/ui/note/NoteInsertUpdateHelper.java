package com.codingwithmitch.unittesting2.ui.note;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.codingwithmitch.unittesting2.ui.Resource;


public abstract class NoteInsertUpdateHelper<T> {

    public static final String ACTION_INSERT = "ACTION_INSERT";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";

    private MediatorLiveData<Resource<T>> result = new MediatorLiveData<>();

    public NoteInsertUpdateHelper(){
        init();
    }

    private void init()  {

        result.setValue((Resource<T>) Resource.loading(null));
        try {
            result.addSource(getAction(), new Observer<Resource<T>>() {
                @Override
                public void onChanged(Resource<T> tResource) {
                    result.setValue(tResource);

                    setNewNoteId(tResource);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setNewNoteId(Resource<T> resource){
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

    public final LiveData<Resource<T>> getAsLiveData(){
        return result;
    }
}











