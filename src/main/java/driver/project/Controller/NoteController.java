package driver.project.Controller;

import driver.project.Model.Note;
import driver.project.Model.NoteForm;
import driver.project.Model.User;
import driver.project.Service.NoteService;
import driver.project.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class NoteController {

    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/home/note/add")
    public String addNote(@ModelAttribute("noteForm")NoteForm noteForm, RedirectAttributes redirectAttributes, Authentication auth, Model model) {
        // Create the current user instance
        String username = auth.getName();
        User user = userService.getUser(username);

        // Check if note already exists
        if (noteForm.getNoteId().isEmpty()) {
            // Add new note.
            int rowInserted = noteService.addNote(new Note(null, noteForm.getNoteTitle(), noteForm.getNoteDescription(), user.getUserId()));
            // If success, show the message and return to home.
            if (rowInserted > 0) {
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "Note added successfully.");

            }
        } else {
            noteService.updateNote(Integer.parseInt(noteForm.getNoteId()), noteForm.getNoteTitle(), noteForm.getNoteDescription());
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Note edited successfully.");
        }

            return "redirect:/home";
    }

    @GetMapping(value = "/home/note/delete/{noteId}")
    public String deleteFile(@PathVariable Integer noteId, RedirectAttributes redirectAttributes, Authentication auth){
        Integer userId = userService.getUser(auth.getName()).getUserId();
        Integer noteUserId = noteService.getNote(noteId).getUserId();
        if (!userId.equals(noteUserId)) {
            redirectAttributes.addFlashAttribute("danger", true);
            redirectAttributes.addFlashAttribute("message", "Note does not exist.");
            return "redirect:/home";
        }
        noteService.deleteNote(noteId);
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Note deleted successfully.");
        return "redirect:/home";
    }
}
