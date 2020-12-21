package ir.example.my_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import ir.example.my_note.Model.Note;
import ir.example.my_note.databinding.ActivityViewNoteBinding;
import ir.example.my_note.utils.MyJalaliDate;

public class ViewNoteActivity extends AppCompatActivity {

    ActivityViewNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_view_note);

        String noteTitle = getIntent().getStringExtra("EXTRA_Note_Title");
        String noteNote = getIntent().getStringExtra("EXTRA_Note_Note");
        String noteTime = getIntent().getStringExtra("EXTRA_Note_Time");
        String noteId = getIntent().getStringExtra("EXTRA_Note_Id");

        binding.viewNoteActivityTitleTextView.setText(noteTitle);
        binding.viewNoteActivityNoteTextView.setText(noteNote);

        Date miladiDate = new Date(Long.parseLong(noteTime));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(miladiDate);
        MyJalaliDate myJalaliDate = new MyJalaliDate();
        String shamsiDate = myJalaliDate.shamsiDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) +1,calendar.get(Calendar.DAY_OF_MONTH));
        binding.viewNoteActivityDateTextView.setText(shamsiDate);

        binding.viewNoteActivityDeleteImageButton.setOnClickListener(v -> deleteNote(noteId));
        binding.viewNoteActivityEditImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewNoteActivity.this,AddEditActivity.class);
            intent.putExtra("EXTRA_Mode_Edit", false);
            startActivity(intent);
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

    private void deleteNote(String noteID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query delete = ref.child("Note").child(noteID);
        delete.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
                //back to home fragment
                startActivity(new Intent(ViewNoteActivity.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewNoteActivity.this, "Error Deleting Note: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}