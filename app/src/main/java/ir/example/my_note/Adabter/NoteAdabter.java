package ir.example.my_note.Adabter;

import android.content.Context;
import android.icu.text.CaseMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import ir.example.my_note.Model.Note;
import ir.example.my_note.R;

public class NoteAdabter extends RecyclerView.Adapter<NoteAdabter.ViewHolder> {

    private Context mContext;
    private List<Note> mNote;

    private FirebaseUser firebaseUser;

    public NoteAdabter(Context mContext, List<Note> mNote){
        this.mContext = mContext;
        this.mNote = mNote;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_item , parent , false);
        return new NoteAdabter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Note note = mNote.get(position);

        // ***************************************************************************************** important
        if(note.getPublisher().equals(firebaseUser.getUid())){
            holder.Title.setText(note.getTitle());
            holder.Note.setText(note.getNote());
        }

    }

    @Override
    public int getItemCount() {
        return mNote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Title;
        public TextView Note;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.noteTitle);
            Note = itemView.findViewById(R.id.noteContent);

        }
    }


}
