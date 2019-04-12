package com.example.wirdakurnia.notesapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.wirdakurnia.notesapp.Constant;
import com.example.wirdakurnia.notesapp.Data;
import com.example.wirdakurnia.notesapp.R;
import com.example.wirdakurnia.notesapp.Settings;
import com.example.wirdakurnia.notesapp.adapters.NoteAdapter;

public class NoteFragment extends Fragment {
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private Settings settings;
    SharedPreferences preferences;

    public NoteFragment(){

    }

//    public NoteFragment(Context context) {
//        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
//    }

    public void setListener(OnNoteFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void displayAsList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter.setLayout(Constant.LAYOUT_MODE_LIST);
    }
    private void displayAsGrid() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setLayout(Constant.LAYOUT_MODE_GRID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_list:
                displayAsList();
                return true;
            case R.id.action_show_grid:
                displayAsGrid();
                return true;

            case R.id.action_logout:
                listener.onLogoutClick();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        recyclerView = view.findViewById(R.id.rv_notes);

        adapter = new NoteAdapter(getContext(), Data.getNotes());
        recyclerView.setAdapter(adapter);

        displayAsList();

        return view;
    }

    public interface OnNoteFragmentListener {
        void onLogoutClick();
    }

    private OnNoteFragmentListener listener;

    private void addFragment(){
        Fragment fragment = null;

        if (settings.getLayoutMode() == Constant.LAYOUT_MODE_LIST) {
            new NoteFragment().displayAsList();
        } else {
            new NoteFragment().displayAsGrid();
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.rv_notes, fragment)
                .addToBackStack(null)
                .commit();
    }
}
