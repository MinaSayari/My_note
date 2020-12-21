package ir.example.my_note.Model;

public class Note {

    private String title;
    private String note;
    private String noteId;
    private String time;
    private String publisher;

    public Note(){

    }

   // Constructor
    public Note(String title, String note, String noteId, String time, String publisher) {
        this.title = title;
        this.note = note;
        this.noteId = noteId;
        this.time = time;
        this.publisher = publisher;
    }

   // Getter & Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
