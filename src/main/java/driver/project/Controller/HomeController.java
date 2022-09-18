package driver.project.Controller;

import driver.project.Model.*;
import driver.project.Service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserService userService;
    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public HomeController(UserService userService, FileService fileService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String getHomePage(
            Authentication auth,
            @ModelAttribute("fileForm")FileForm fileForm,
            @ModelAttribute("noteForm")NoteForm noteForm,
            @ModelAttribute("credentialForm") CredentialForm credentialForm,
            Model model) {
        int userId = userService.getUser(auth.getName()).getUserId();
        String[] fileList = fileService.getAllFiles(userId);
        Note[] noteList = noteService.getAllNotes(userId);
        Credential[] credList = credentialService.getAllCredentials(userId);
        model.addAttribute("fileList", fileList);
        model.addAttribute("noteList", noteList);
        model.addAttribute("credList", credList);
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }
}
