package com.example.notepad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseRecyclerAdapter<NewNote, RecycleAdapter> adapter;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<NewNote> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        if(isNetworkAvail()== false){
            Toast.makeText(this, "Internet Not Available", Toast.LENGTH_SHORT).show();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("notes");
        databaseReference.keepSynced(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.note_list);

        options = new FirebaseRecyclerOptions.Builder<NewNote>().setQuery(databaseReference, NewNote.class).build();

        adapter = new FirebaseRecyclerAdapter<NewNote, RecycleAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RecycleAdapter holder, final int position, @NonNull final NewNote model) {
                String editTitle;
                if ((model.getTitle()).length() > 35) {
                    editTitle = model.getTitle().substring(0, 35).concat("...");
                } else {
                    editTitle = model.getTitle();
                }
                holder.title.setText(editTitle);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, AddNote.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("desc", model.getDesc());
                        intent.putExtra("key", model.getId());
                        startActivity(intent);
                        //Toast.makeText(MainActivity.this, "Recycler Click: "+position+ model.getDesc(), Toast.LENGTH_LONG).show();
                    }
                });

                holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //Toast.makeText(MainActivity.this, "Long Clicked", Toast.LENGTH_SHORT).show();
                        openDialog(model.getId());
                        return true;
                    }
                });
            }

            @NonNull
            @Override
            public RecycleAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_note, viewGroup, false);
                return new RecycleAdapter(view);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        if (adapter != null) {
            adapter.stopListening();
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_note:
                Intent intent = new Intent(this, AddNote.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openDialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete!");
        builder.setMessage("Are you sure you want to Delete this Note?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(id).removeValue();
                Toast.makeText(MainActivity.this, "Note Rmoved", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private boolean isNetworkAvail(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if ( networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }else{
            return false;
        }
    }
}
