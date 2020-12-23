package ir.example.my_note;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        String noteId = null;
        boolean editMode = getIntent().getBooleanExtra("EXTRA_Mode_Edit", false);
        if (! editMode){
            binding.addNoteToolbar.toolbarSimpleTitleTextView.setText(getString(R.string.add_note));
        }
        else{
            binding.addNoteToolbar.toolbarSimpleTitleTextView.setText(getString(R.string.edit_note));
            String noteTitle = getIntent().getStringExtra("EXTRA_Note_Title");
            String noteNote = getIntent().getStringExtra("EXTRA_Note_Note");
            String noteTime = getIntent().getStringExtra("EXTRA_Note_Time");
            noteId = getIntent().getStringExtra("EXTRA_Note_Id");
            binding.etTitle.setText(noteTitle);
            binding.etNote.setText(noteNote);
        }
        String finalNoteId1 = noteId;
        binding.saveButton.setOnClickListener(v -> {
            if (binding.etTitle.getText()==null || String.valueOf(binding.etTitle.getText()).equals("")){
                Toast.makeText(AddEditActivity.this,"The title box cannot be Empty!",Toast.LENGTH_LONG).show();
            }
            else {
                String txt_Title = binding.etTitle.getText().toString();
                String txt_Note = String.valueOf(binding.etNote.getText());
                upload(txt_Title,txt_Note, finalNoteId1);
            }
        });

        String finalNoteId = noteId;
        binding.addNoteToolbar.toolbarSimpleBackImageButton.setOnClickListener(v -> {

                if (binding.etTitle.getText()==null || String.valueOf(binding.etTitle.getText()).equals("")){
                    finish();
                }
                else {
                    String txt_Title = binding.etTitle.getText().toString();
                    String txt_Note = String.valueOf(binding.etNote.getText());
                    upload(txt_Title,txt_Note, finalNoteId);
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


    private void upload(final String title, final String note,final String id) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Saving ...");
        pd.show();
        String noteId = id;
        if (id == null || id.equals(""))
          noteId = mRootRef.push().getKey();
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
                startActivity(new Intent(AddEditActivity.this , MainActivity.class));
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


}