package driver.project.Controller;

import driver.project.Model.FileForm;
import driver.project.Model.User;
import driver.project.Service.FileService;
import driver.project.Service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@RequestMapping
@Controller
public class FileController {

    private UserService userService;
    private FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/home/file/upload")
    public String fileUpload(@ModelAttribute("fileForm") FileForm fileForm, RedirectAttributes redirectAttributes, Authentication auth, Model model) throws IOException {
        // Get the uploaded file name
        MultipartFile multipartFile = fileForm.getFile();
        String filename = multipartFile.getOriginalFilename();

        // Check if upload nothing
        if (multipartFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("danger", true);
            redirectAttributes.addFlashAttribute("message", "Cannot upload empty file.");
            return "redirect:/home";
        }

        // Create the current user instance
        String username = auth.getName();
        User user = userService.getUser(username);

        // Retrieve the file list of the current user
        String[] fileList = fileService.getAllFiles(user.getUserId());

        // Check if the filename already exists
        String err_msg = null;
        for (String file : fileList) {
            if (file.equals(filename)) {
                err_msg = "Filename already exists. Please rename and upload again.";
                break;
            }
        }

        // If filename already exists, show error message and return to home.
        if (err_msg != null) {
            redirectAttributes.addFlashAttribute("danger", true);
            redirectAttributes.addFlashAttribute("message", err_msg);
            return "redirect:/home";
        }

        // Otherwise, add the new file
        System.out.println(multipartFile.getSize());
        int rowInserted = fileService.addFile(multipartFile, username);

        // If success, show the message and return to home.
        if (rowInserted > 0) {
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully");
        }

        return "redirect:/home";
    }

    @GetMapping(
            value = "/home/file/get/{filename}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody byte[] getFile(@PathVariable String filename, Authentication auth, RedirectAttributes redirectAttributes) {
        Integer userId = userService.getUser(auth.getName()).getUserId();
        Integer fileUserId = fileService.getFile(filename).getUserId();
        if (!userId.equals(fileUserId)) {
            redirectAttributes.addFlashAttribute("danger", true);
            redirectAttributes.addFlashAttribute("message", "File does not exist.");
            return new byte[0];
        }
        return fileService.getFile(filename).getFileData();
    }

    @GetMapping(value = "/home/file/delete/{filename}")
    public String deleteFile(@PathVariable String filename, Authentication auth, RedirectAttributes redirectAttributes) {
        Integer userId = userService.getUser(auth.getName()).getUserId();
        Integer fileUserId = fileService.getFile(filename).getUserId();
        if (!userId.equals(fileUserId)) {
            redirectAttributes.addFlashAttribute("danger", true);
            redirectAttributes.addFlashAttribute("message", "File does not exist.");
            return "redirect:/home";
        }
        fileService.deleteFile(filename);
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "File deleted successfully.");
        return "redirect:/home";
    }
}
