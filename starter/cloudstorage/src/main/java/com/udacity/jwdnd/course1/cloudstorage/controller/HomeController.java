package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserService userService;
    private NoteService noteService;
    private CredentialService credentialService;
    private FileService fileService;
    private EncryptionService encryptionService;

    public HomeController(UserService userService, NoteService noteService, CredentialService credentialService,
                          FileService fileService, EncryptionService encryptionService) {
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String homeView(@ModelAttribute("noteForm") Note noteForm,
                           @ModelAttribute("credentialForm") Credential credentialForm,
                           @ModelAttribute("fileForm") Note fileForm,
                           @ModelAttribute("setTab") String setTab,
                           @ModelAttribute("descLimitError") String descLimitError,
                           @ModelAttribute("emptyFile") String emptyFile,
                           @ModelAttribute("fileSizeLimit") String fileSizeLimit,
                           @ModelAttribute("duplicateFile") String duplicateFile,
                           @ModelAttribute("uploadSuccess") String uploadSuccess,
                           @ModelAttribute("uploadFailure") String uploadFailure,
                           @ModelAttribute("noteSuccess") String noteSuccess,
                           @ModelAttribute("noteFailure") String noteFailure,
                           @ModelAttribute("noteEditSuccess") String noteEditSuccess,
                           @ModelAttribute("noteEditFailure") String noteEditFailure,
                           @ModelAttribute("credentialSuccess") String credentialSuccess,
                           @ModelAttribute("credentialFailure") String credentialFailure,
                           @ModelAttribute("credEditSuccess") String credEditSuccess,
                           @ModelAttribute("credEditFailure") String credEditFailure,
                           Model model,
                           Principal principal){
        User user = userService.getUser(principal.getName());
        Integer userid = user.getUserid();

        model.addAttribute("notes", noteService.getAllNotes(userid));
        model.addAttribute("credentials", credentialService.getAllCredentials(userid));
        model.addAttribute("files", fileService.getAllFiles(userid));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("setTab", setTab);
        model.addAttribute("descLimitError", descLimitError);
        model.addAttribute("emptyFile", emptyFile);
        model.addAttribute("fileSizeLimit", fileSizeLimit);
        model.addAttribute("duplicateFile", duplicateFile);
        model.addAttribute("uploadSuccess", uploadSuccess);
        model.addAttribute("uploadFailure", uploadFailure);
        model.addAttribute("noteSuccess", noteSuccess);
        model.addAttribute("noteFailure", noteFailure);
        model.addAttribute("noteEditSuccess", noteEditSuccess);
        model.addAttribute("noteEditFailure", noteEditFailure);
        model.addAttribute("credentialSuccess", credentialSuccess);
        model.addAttribute("credentialFailure", credentialFailure);
        model.addAttribute("credEditSuccess", credEditSuccess);
        model.addAttribute("credEditFailure", credEditFailure);
        return "home";
    }
}
