package ir.example.my_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import ir.example.my_note.Model.Note;
import ir.example.my_note.databinding.ActivityAddEditBinding;

public class AddEditActivity extends AppCompatActivity {
    private ActivityAddEditBinding binding;
    private DatabaseReference mRootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        String noteId = null,noteTitle = null,noteNote= null,noteTime = null;
        boolean editMode = getIntent().getBooleanExtra("EXTRA_Mode_Edit", false);
        if (! editMode){
            binding.addNoteToolbar.toolbarSimpleTitleTextView.setText(getString(R.string.add_note));
        }
        else{
            binding.addNoteToolbar.toolbarSimpleTitleTextView.setText(getString(R.string.edit_note));
            noteTitle = getIntent().getStringExtra("EXTRA_Note_Title");
            noteNote = getIntent().getStringExtra("EXTRA_Note_Note");
            noteTime = getIntent().getStringExtra("EXTRA_Note_Time");
            noteId = getIntent().getStringExtra("EXTRA_Note_Id");
            binding.etTitle.setText(noteTitle);
            binding.etNote.setText(noteNote);
        }
        String finalNoteId = noteId;
        binding.saveButton.setOnClickListener(v -> {
            if (binding.etTitle.getText()==null || String.valueOf(binding.etTitle.getText()).equals("")){
                Toast.makeText(AddEditActivity.this,"The title box cannot be Empty!",Toast.LENGTH_LONG).show();
            }
            else {
                String txt_Title = binding.etTitle.getText().toString();
                String txt_Note = String.valueOf(binding.etNote.getText());
                if (editMode)
                     updateNote(txt_Title,txt_Note, finalNoteId);
                else
                    upload(txt_Title,txt_Note);
            }
        });

        String finalNoteTitle = noteTitle;
        String finalNoteNote = noteNote;
        String finalNoteTime = noteTime;
        String finalNoteId1 = noteId;
        binding.addNoteToolbar.toolbarSimpleBackImageButton.setOnClickListener(v -> {
            if (editMode){
                Intent intent = new Intent(AddEditActivity.this , ViewNoteActivity.class);
                intent.putExtra("EXTRA_Note_Title", finalNoteTitle);
                intent.putExtra("EXTRA_Note_Note", finalNoteNote);
                intent.putExtra("EXTRA_Note_Time", finalNoteTime);
                intent.putExtra("EXTRA_Note_Id", finalNoteId1);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            else {
                if (binding.etTitle.getText() == null || String.valueOf(binding.etTitle.getText()).equals("")) {
                    startActivity(new Intent(AddEditActivity.this , MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                } else {
                    String txt_Title = binding.etTitle.getText().toString();
                    String txt_Note = String.valueOf(binding.etNote.getText());
                    upload(txt_Title, txt_Note);
                }
            }
        });


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


    private void upload(final String title, final String note) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Saving ...");
        pd.show();
        String noteId = mRootRef.push().getKey();
        HashMap<String, Object> map = new HashMap<>();
        map.put("noteId", noteId);
        map.put("Title", title);
        map.put("Note", note);
        map.put("time", System.currentTimeMillis() + "." + (title));
        map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
        Note myNote = new Note(title,note,noteId,String.valueOf(System.currentTimeMillis()),FirebaseAuth.getInstance().getCurrentUser().getUid());

        mRootRef.child("Note").child(noteId).setValue(myNote).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                pd.dismiss();
                Toast.makeText(AddEditActivity.this,"Saving note is Successful!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddEditActivity.this , MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(AddEditActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        });


/*
        mRootRef.child("Note").setValue(Note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Note");
                String noteId = ref.push().getKey();
                HashMap<String, Object> map = new HashMap<>();
                map.put("noteId", noteId);
                map.put("Title", Title);
                map.put("Note", Note);
                map.put("time", System.currentTimeMillis() + "." + (Title));
                map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                mRootRef.child("Note").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(AddEditActivity.this,"Saving note is Successful!",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AddEditActivity.this , MainActivity.class));
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(AddEditActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });
*/


    }

    private void updateNote(final String title, final String note, final String noteId) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Saving ...");
        pd.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("noteId", noteId);
        map.put("Title", title);
        map.put("Note", note);
        map.put("time", System.currentTimeMillis() + "." + (title));
        map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
        Note myNote = new Note(title,note,noteId,String.valueOf(System.currentTimeMillis()),FirebaseAuth.getInstance().getCurrentUser().getUid());
        Query update = mRootRef.child("Note").child(noteId);
        update.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().setValue(myNote);
                Intent intent = new Intent(AddEditActivity.this , ViewNoteActivity.class);
                intent.putExtra("EXTRA_Note_Title", myNote.getTitle());
                intent.putExtra("EXTRA_Note_Note", myNote.getNote());
                intent.putExtra("EXTRA_Note_Time", myNote.getTime());
                intent.putExtra("EXTRA_Note_Id", myNote.getNoteId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        binding.addNoteToolbar.toolbarSimpleBackImageButton.callOnClick();
    }
}