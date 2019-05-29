package com.example.notepad;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNote extends AppCompatActivity {
    private EditText title1;
    private EditText desc1;
    Button addNew;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        final Bundle extras = getIntent().getExtras();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("notes");
        addNew = findViewById(R.id.add_new);
        title1 = (EditText) findViewById(R.id.add_title);
        desc1 = (EditText) findViewById(R.id.add_desc);
        if (extras == null) {
            addNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNew();
                }
            });
        } else {
            title1.setText(extras.getString("title"));
            desc1.setText(extras.getString("desc"));
            addNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(AddNote.this, extras.getString("key"), Toast.LENGTH_SHORT).show();
                    String title = title1.getText().toString().trim();
                    String desc = desc1.getText().toString().trim();
                    NewNote newNote = new NewNote(extras.getString("key"), title, desc);
                    databaseReference.child(extras.getString("key")).setValue(newNote);
                    Toast.makeText(AddNote.this, "Note Edited", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    public void addNew() {
        String title = title1.getText().toString().trim();
        String desc = desc1.getText().toString().trim();
        if (!TextUtils.isEmpty(title)) {
            String id = databaseReference.push().getKey();
            NewNote newNote = new NewNote(id, title, desc);
            databaseReference.child(id).setValue(newNote);
            Toast.makeText(this, "New note added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please enter the title", Toast.LENGTH_SHORT).show();
        }
    }


}
