package com.codingwithmitch.unittesting2.ui.note;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingwithmitch.unittesting2.R;
import com.codingwithmitch.unittesting2.models.Note;
import com.codingwithmitch.unittesting2.ui.Resource;
import com.codingwithmitch.unittesting2.ui.note.NoteViewModel;
import com.codingwithmitch.unittesting2.util.DateUtil;
import com.codingwithmitch.unittesting2.util.LinedEditText;
import com.codingwithmitch.unittesting2.viewmodels.ViewModelProviderFactory;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;


public class NoteActivity extends DaggerAppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher

{

    private static final String TAG = "NoteActivity";

    // UI components
    private LinedEditText linedEditText;
    private EditText editText;
    private TextView viewTitle;
    private RelativeLayout checkContainer, backArrowContrainer;
    private ImageButton check, backArrow;
    private ConstraintLayout parent;

    @Inject
    ViewModelProviderFactory providerFactory;

    private NoteViewModel viewModel;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        linedEditText = findViewById(R.id.note_text);
        editText = findViewById(R.id.note_edit_title);
        viewTitle = findViewById(R.id.note_text_title);
        check = findViewById(R.id.toolbar_check);
        backArrow = findViewById(R.id.toolbar_back_arrow);
        checkContainer = findViewById(R.id.check_container);
        backArrowContrainer = findViewById(R.id.back_arrow_container);
        parent = findViewById(R.id.parent);

        viewModel = ViewModelProviders.of(this, providerFactory).get(NoteViewModel.class);

        subscribeObservers();
        setListeners();

        if(savedInstanceState == null){
            getIncomingIntent();
            enableEditMode();
        }

    }


    private void getIncomingIntent() {
        try {
            Note note;
            if(getIntent().hasExtra(getString(R.string.intent_note))){
                note = new Note((Note)getIntent().getParcelableExtra(getString(R.string.intent_note)));
                viewModel.setIsNewNote(false);
            }
            else{
                note = new Note("Title", "", DateUtil.getCurrentTimeStamp());
                viewModel.setIsNewNote(true);
            }
            viewModel.setNote(note);
        } catch (Exception e) {
            e.printStackTrace();
            showSnackBar(getString(R.string.error_intent_note));
        }
    }


    private void setListeners(){
        mGestureDetector = new GestureDetector(this, this);
        linedEditText.setOnTouchListener(this);
        check.setOnClickListener(this);
        viewTitle.setOnClickListener(this);
        backArrow.setOnClickListener(this);
        editText.addTextChangedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("has_started", true);
    }


    private void subscribeObservers(){

        viewModel.observeNote().observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                setNoteProperties(note);
            }
        });

        viewModel.observeViewState().observe(this, new Observer<NoteViewModel.ViewState>() {
            @Override
            public void onChanged(NoteViewModel.ViewState viewState) {
                switch (viewState){
                    case EDIT:{
                        enableContentInteraction();
                        break;
                    }

                    case VIEW:{
                        disableContentInteraction();
                        break;
                    }
                }
            }
        });
    }

    private void saveNote(){
        Log.d(TAG, "saveNote: called.");
        try {
            viewModel.saveNote().observe(this, new Observer<Resource<Integer>>() {
                @Override
                public void onChanged(Resource<Integer> integerResource) {
                    try {
                        if(integerResource != null){
                            switch (integerResource.status){

                                case SUCCESS:{
                                    Log.e(TAG, "onChanged: save note: success..." );
                                    showSnackBar(integerResource.message);
                                    break;
                                }

                                case ERROR:{
                                    Log.e(TAG, "onChanged: save note: error..." );
                                    showSnackBar(integerResource.message);
                                    break;
                                }

                                case LOADING:{
                                    Log.e(TAG, "onChanged: save note: loading..." );
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showSnackBar(e.getMessage());
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showSnackBar(String message){
        if(!TextUtils.isEmpty(message)) {

            Snackbar.make(parent, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void setNoteProperties(Note note) {
        try{
            viewTitle.setText(note.getTitle());
            editText.setText(note.getTitle());
            linedEditText.setText(note.getContent());
        }catch (NullPointerException e){
            e.printStackTrace();
            showSnackBar("Error displaying note properties");
        }

    }

    private void enableEditMode(){
        Log.d(TAG, "enableEditMode: called.");
        viewModel.setViewState(NoteViewModel.ViewState.EDIT);
    }

    private void disableEditMode(){
        Log.d(TAG, "disableEditMode: called.");
        viewModel.setViewState(NoteViewModel.ViewState.VIEW);

        if(!TextUtils.isEmpty(linedEditText.getText())){
            try {
                viewModel.updateNote(editText.getText().toString(), linedEditText.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
                showSnackBar("Error setting note properties");
            }
        }

        saveNote();
    }

    private void disableContentInteraction(){
        hideKeyboard(this);

        backArrowContrainer.setVisibility(View.VISIBLE);
        checkContainer.setVisibility(View.GONE);

        viewTitle.setVisibility(View.VISIBLE);
        editText.setVisibility(View.GONE);

        linedEditText.setKeyListener(null);
        linedEditText.setFocusable(false);
        linedEditText.setFocusableInTouchMode(false);
        linedEditText.setCursorVisible(false);
        linedEditText.clearFocus();
    }

    private void enableContentInteraction(){
        backArrowContrainer.setVisibility(View.GONE);
        checkContainer.setVisibility(View.VISIBLE);

        viewTitle.setVisibility(View.GONE);
        editText.setVisibility(View.VISIBLE);

        linedEditText.setKeyListener(new EditText(this).getKeyListener());
        linedEditText.setFocusable(true);
        linedEditText.setFocusableInTouchMode(true);
        linedEditText.setCursorVisible(true);
        linedEditText.requestFocus();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        viewTitle.setText(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbar_back_arrow:{
                finish();
                break;
            }
            case R.id.toolbar_check:{
                disableEditMode();
                break;
            }
            case R.id.note_text_title:{
                enableEditMode();
                editText.requestFocus();
                editText.setSelection(editText.length());
                break;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if(viewModel.shouldNavigateBack()){
            super.onBackPressed();
        }
        else{
            onClick(check);
        }
    }
}



