package ir.example.my_note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class AddEditActivity extends AppCompatActivity {

    private TextView save;
    private EditText Title;
    private EditText Note;
    private DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        save = findViewById(R.id.tvSave);
        Title = findViewById(R.id.etTitle);
        Note = findViewById(R.id.etNote);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_Title = Title.getText().toString();
                String txt_Note = Note.getText().toString();
                upload(txt_Title,txt_Note);
            }
        });

    }


    private void upload(final String Title, final String Note) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Saving ...");
        pd.show();

        String noteId = mRootRef.push().getKey();
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