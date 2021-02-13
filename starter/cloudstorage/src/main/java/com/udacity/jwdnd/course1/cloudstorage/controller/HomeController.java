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
                           Model model,
                           Principal principal){
        User user = userService.getUser(principal.getName());
        Integer userid = user.getUserid();

        model.addAttribute("notes", noteService.getAllNotes(userid));
        model.addAttribute("credentials", credentialService.getAllCredentials(userid));
        model.addAttribute("files", fileService.getAllFiles(userid));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("setTab", setTab);
        return "home";
    }
}
