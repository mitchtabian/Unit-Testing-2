package com.codingwithmitch.unittesting2.ui.note;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.codingwithmitch.unittesting2.ui.Resource;


public abstract class NoteInsertUpdateHelper<T> {

    public static final String INSERT_SUCCESS = "Insert success";
    public static final String INSERT_FAILED = "Insert failed";
    public static final String UPDATE_SUCCESS = "Update success";
    public static final String UPDATE_FAILED = "Update failed";
    public static final String GENERIC_FAILURE = "Something went wrong";
    public static final String ACTION_INSERT = "ACTION_INSERT";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";

    private MediatorLiveData<Resource<T>> result = new MediatorLiveData<>();

    public NoteInsertUpdateHelper(){
        init();
    }

    private void init()  {

        result.setValue((Resource<T>) Resource.loading(null));
        try {
            result.addSource(getAction(), new Observer<T>() {
                @Override
                public void onChanged(T t) {

                    int i = (Integer)t;
                    try {
                        switch (defineAction()){
                            case ACTION_INSERT:{
                                if(i >= 0){
                                    setNoteId(i);
                                    result.setValue(Resource.success(t, INSERT_SUCCESS));
                                }
                                else{
                                    result.setValue((Resource<T>) Resource.error(INSERT_FAILED, null));
                                }
                                break;
                            }

                            case ACTION_UPDATE:{
                                if(i > 0){
                                    result.setValue(Resource.success(t, UPDATE_SUCCESS));
                                }
                                else{
                                    result.setValue((Resource<T>) Resource.error(UPDATE_FAILED, null));
                                }
                                break;
                            }
                        }
                        result.removeSource(getAction());
                    } catch (Exception e) {
                        e.printStackTrace();
                        result.setValue((Resource<T>) Resource.error(GENERIC_FAILURE, null));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void setNoteId(int noteId);

    public abstract LiveData<T> getAction() throws Exception;

    public abstract String defineAction();

    public final LiveData<Resource<T>> getAsLiveData(){
        return result;
    }
}











