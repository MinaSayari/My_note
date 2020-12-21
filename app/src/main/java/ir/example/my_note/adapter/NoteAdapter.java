package ir.example.my_note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.example.my_note.Model.Note;
import ir.example.my_note.R;
import ir.example.my_note.utils.MyJalaliDate;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    private Context mContext;
    private List<Note> mNote;
    private MyOnItemClickListener<Note> clickListener;
    private FirebaseUser firebaseUser;

    public NoteAdapter(Context mContext, List<Note> mNote,MyOnItemClickListener<Note> clickListener){
        this.mContext = mContext;
        this.mNote = mNote;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_item , parent , false);
        return new NoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Note note = mNote.get(position);

        holder.titleTextView.setText(note.getTitle());
        holder.noteTextView.setText(note.getNote());

        Date miladiDate = new Date(Long.parseLong(note.getTime()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(miladiDate);
        MyJalaliDate myJalaliDate = new MyJalaliDate();
        String shamsiDate = myJalaliDate.shamsiDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) +1,calendar.get(Calendar.DAY_OF_MONTH));
        holder.dateTextView.setText(shamsiDate);


        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(note));


    }

    @Override
    public int getItemCount() {
        return mNote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView titleTextView;
        public TextView noteTextView;
        public TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.noteTitle);
            noteTextView = itemView.findViewById(R.id.noteContent);
            dateTextView = itemView.findViewById(R.id.noteDate);
        }
    }


}
