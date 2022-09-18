package driver.project.Controller;

import driver.project.Model.*;
import driver.project.Service.CredentialService;
import driver.project.Service.EncryptionService;
import driver.project.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping
public class CredentialController {

    private CredentialService credentialService;
    private UserService userService;
    private EncryptionService encryptionService;

    public CredentialController(CredentialService credentialService, UserService userService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/home/credential/add")
    public String addCredential(@ModelAttribute("credentialForm") CredentialForm credentialForm, Authentication auth, RedirectAttributes redirectAttributes, Model model) {
        // Create the current user instance
        String username = auth.getName();
        User user = userService.getUser(username);

        String password = credentialForm.getPassword();
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        // Check if note already exists
        if (credentialForm.getCredentialId().isEmpty()) {
            // Add new note.
            int rowInserted = credentialService.addCredential(new Credential(null, credentialForm.getUrl(), credentialForm.getUsername(), encodedKey, encryptedPassword, user.getUserId()));
            // If success, show the message and return to home.
            if (rowInserted > 0) {
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "Credential uploaded successfully.");

            }
        } else {
            credentialService.updateCredential(Integer.parseInt(credentialForm.getCredentialId()), credentialForm.getUrl(), credentialForm.getUsername(), encodedKey, encryptedPassword);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Credential updated successfully.");

        }

        return "redirect:/home";
    }
    @GetMapping(value = "/home/credential/delete/{credentialId}")
    public String deleteFile(@PathVariable Integer credentialId, RedirectAttributes redirectAttributes, Authentication auth){
        Integer userId = userService.getUser(auth.getName()).getUserId();
        Integer credUserId = credentialService.getCredential(credentialId).getUserId();
        if (!userId.equals(credUserId)) {
            redirectAttributes.addFlashAttribute("danger", true);
            redirectAttributes.addFlashAttribute("message", "Credential does not exist.");

            return "redirect:/home";
        }
        credentialService.deleteCred(credentialId);
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Credential deleted successfully.");

        return "redirect:/home";
    }
}
