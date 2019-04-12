package com.example.wirdakurnia.notesapp;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.wirdakurnia.notesapp.fragments.LoginFragment;
import com.example.wirdakurnia.notesapp.fragments.NoteFragment;
import com.example.wirdakurnia.notesapp.fragments.SettingsFragment;
import com.example.wirdakurnia.notesapp.models.User;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentListener, NoteFragment.OnNoteFragmentListener{
    private Settings settings;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings  = new Settings(this);
        session = new Session(settings);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addFragment();
    }

    private void addFragment() {
        Fragment fragment = null;
        if (session.isLogin()) {
            fragment = new NoteFragment();
            ((NoteFragment) fragment).setListener(this);
        } else {
            fragment = new LoginFragment();
            ((LoginFragment) fragment).setListener(this);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onLoginClicked(View view, String username, String password) {
        User user = session.doLogin(username, password);
        String message = "Authentication failed";
        if (user != null) {
            message = "Welcome " + username;
            session.setUser(username);
        }
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        addFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            createSettingFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLogoutClick() {
        session.doLogout();
        addFragment();
    }

    private void createSettingFragment() {
        Fragment settingsFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, settingsFragment)
                .addToBackStack(null)
                .commit();
    }
}
