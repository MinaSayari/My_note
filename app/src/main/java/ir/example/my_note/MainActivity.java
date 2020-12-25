package ir.example.my_note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ir.example.my_note.Model.Note;
import ir.example.my_note.adapter.MyOnItemClickListener;
import ir.example.my_note.adapter.NoteAdapter;
import ir.example.my_note.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private NoteAdapter noteAdapter;
    private List<Note> NoteList;

    public MainActivity() {
    }

ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            intent.putExtra("EXTRA_Mode_Edit", false);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.mainToolbar.toolbarSimpleMenuImageButton.setVisibility(View.VISIBLE);
        binding.mainToolbar.toolbarSimpleMenuImageButton.setOnClickListener((View.OnClickListener) v -> {

            PopupMenu popup = new PopupMenu(MainActivity.this, v);
            popup.getMenuInflater().inflate(R.menu.pop_up, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
               if (item.getItemId()==R.id.aboutItemMenu){
                startActivity(new Intent(MainActivity.this , AboutActivity.class));
               }
               else if (item.getItemId() == R.id.gridViewItemMenu){
                  binding.recyclerViewNote.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
               }
               else if (item.getItemId() == R.id.listViewItemMenu){
                   binding.recyclerViewNote.setLayoutManager(new LinearLayoutManager(MainActivity.this));
               }
               else if (item.getItemId() == R.id.logoutItemMenu){
                   FirebaseAuth.getInstance().signOut();
                   startActivity(new Intent(this , LoginActivity.class)
                           .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                   finish();
               }

                return true;
            });
            popup.show();//showing popup menu
        });

        binding.mainToolbar.toolbarSimpleBackImageButton.setVisibility(View.GONE);
        binding.mainToolbar.toolbarSimpleTitleTextView.setText(getString(R.string.app_name));

        NoteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(getBaseContext(), NoteList, model -> {
            Intent intent = new Intent(MainActivity.this , ViewNoteActivity.class);
            intent.putExtra("EXTRA_Note_Title", model.getTitle());
            intent.putExtra("EXTRA_Note_Note", model.getNote());
            intent.putExtra("EXTRA_Note_Time", model.getTime());
            intent.putExtra("EXTRA_Note_Id", model.getNoteId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        binding.recyclerViewNote.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewNote.setAdapter(noteAdapter);

        showNote();

        //chane Status Toolbar Background to gradient
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Drawable background = getDrawable(R.drawable.blue_gradient); //bg_gradient is your gradient.
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    //Exit dialog
    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        dialog.setIcon(R.drawable.ic_exit);
        dialog.setPositiveButton("yes",(dialog1, which) -> finish());
        dialog.setNegativeButton("No",(dialog1, which) -> dialog1.cancel());
        dialog.setTitle("Close MyNote");
        dialog.setMessage("Are you sure?");
        dialog.show();
    }



    private void showNote() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Note");
        reference.getRef().addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NoteList.clear();

                reference.child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String publisher = String.valueOf(snapshot.child("publisher").getValue());
                        if (publisher.equals( FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            String note = String.valueOf(snapshot.child("note").getValue());
                            String time = String.valueOf(snapshot.child("time").getValue());
                            String title = String.valueOf(snapshot.child("title").getValue());
                            String noteId = String.valueOf(snapshot.child("noteId").getValue());
                            Note myNote = new Note(title, note, noteId, time, publisher);
//                          Note myNote = snapshot.getValue(Note.class);
                            NoteList.add(myNote);
//                          noteList.add(snapshot.child("Note").child("Note").getValue(String.class));
                            noteAdapter.notifyDataSetChanged();
                        }
                }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}