package ir.example.my_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ir.example.my_note.Adabter.NoteAdabter;
import ir.example.my_note.Model.Note;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNote;
    private NoteAdabter noteAdabter;
    private List<Note> NoteList;
    ArrayList<String> noteList = new ArrayList<>();

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , AddEditActivity.class));
            }
        });

        recyclerViewNote = findViewById(R.id.recycler_view_note);

        NoteList = new ArrayList<>();
        noteAdabter = new NoteAdabter(getBaseContext(), NoteList);
        recyclerViewNote.setAdapter(noteAdabter);

        showNote();

    }

    private void showNote() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Note");
        reference.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NoteList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Note note = snapshot.getValue(Note.class);
                    //NoteList.add(note);

                    //noteList.add(snapshot.child("Note").child("Note").getValue(String.class));
                }
                noteAdabter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}