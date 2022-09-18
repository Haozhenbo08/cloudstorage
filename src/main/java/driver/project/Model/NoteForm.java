package driver.project.Model;

public class NoteForm {

    private String noteTitle;
    private String noteDescription;
    private String noteId;

    public NoteForm(String noteTitle, String noteDescription, String noteId) {
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
}
