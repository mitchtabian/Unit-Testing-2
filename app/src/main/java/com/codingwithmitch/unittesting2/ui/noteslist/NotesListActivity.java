package com.codingwithmitch.unittesting2.ui.noteslist;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingwithmitch.unittesting2.R;
import com.codingwithmitch.unittesting2.models.Note;
import com.codingwithmitch.unittesting2.ui.Resource;
import com.codingwithmitch.unittesting2.ui.note.NoteActivity;
import com.codingwithmitch.unittesting2.util.VerticalSpacingItemDecorator;
import com.codingwithmitch.unittesting2.viewmodels.ViewModelProviderFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;


public class NotesListActivity extends DaggerAppCompatActivity implements
        NotesRecyclerAdapter.OnNoteListener,
        View.OnClickListener
{

    private static final String TAG = "NotesListActivity";


    // ui components
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private CoordinatorLayout parent;


    // vars
    private NotesListViewModel viewModel;
    private NotesRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        parent = findViewById(R.id.parent);

        fab.setOnClickListener(this);

        viewModel = ViewModelProviders.of(this, providerFactory).get(NotesListViewModel.class);

        initRecyclerView();
    }

    private void subscribeObservers(){

        Log.d(TAG, "subscribeObservers: called.");

        viewModel.observeNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(notes != null){
                    adapter.setNotes(notes);
                }
            }
        });

        viewModel.getNotes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscribeObservers();
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new VerticalSpacingItemDecorator(10));
        adapter = new NotesRecyclerAdapter(this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(getString(R.string.intent_note), note);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:{
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void showSnackBar(String message){
        if(!TextUtils.isEmpty(message)) {

            Snackbar.make(parent, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    Note note = adapter.getNote(viewHolder.getAdapterPosition());
                    adapter.removeNote(note);

                    try {
                        final LiveData<Resource<Integer>> deleteAction = viewModel.deleteNote(note);
                        deleteAction.observe(NotesListActivity.this, new Observer<Resource<Integer>>() {
                            @Override
                            public void onChanged(Resource<Integer> integerResource) {
                                if(integerResource != null){
                                    showSnackBar(integerResource.message);
                                }
                                deleteAction.removeObserver(this);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        showSnackBar(e.getMessage());
                    }
                }
            };
}











