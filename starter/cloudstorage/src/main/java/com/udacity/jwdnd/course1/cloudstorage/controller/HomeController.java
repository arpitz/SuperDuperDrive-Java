package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;
    private CredentialService credentialService;
    private FileService fileService;
    private EncryptionService encryptionService;

    public HomeController(NoteService noteService, CredentialService credentialService,
                          FileService fileService, EncryptionService encryptionService) {
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
        model.addAttribute("notes", noteService.getAllNotes());
        model.addAttribute("credentials", credentialService.getAllCredentials());
        model.addAttribute("files", fileService.getAllFiles());
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("setTab", setTab);
        System.out.println(principal.getName());
        return "home";
    }
}
