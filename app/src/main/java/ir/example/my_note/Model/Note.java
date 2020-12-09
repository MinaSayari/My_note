package ir.example.my_note.Model;

public class Note {

    private String Title;
    private String Note;
    private String noteId;
    private String time;
    private String publisher;

    public Note(){

    }

   // Constructor
    public Note(String title, String note, String noteId, String time, String publisher) {
        Title = title;
        Note = note;
        this.noteId = noteId;
        this.time = time;
        this.publisher = publisher;
    }

   // Getter & Setter
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

}
