package driver.project.Service;

import driver.project.Mapper.NoteMapper;
import driver.project.Model.Note;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public Note getNote(Integer noteId) {
        return noteMapper.getNote(noteId);
    }

    public Note[] getAllNotes(Integer userId) {
        return noteMapper.getAllNotes(userId);
    }

    public int addNote(Note note){
        return noteMapper.addNote(note);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }

    public void updateNote(Integer noteId, String noteTitle, String noteDescription){
        noteMapper.updateNote(noteId, noteTitle, noteDescription);
    }
}
