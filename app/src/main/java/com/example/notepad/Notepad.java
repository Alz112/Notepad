package com.example.notepad;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Notepad extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
