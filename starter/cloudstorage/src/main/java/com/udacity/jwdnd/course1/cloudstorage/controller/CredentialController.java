package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Controller
@RequestMapping()
public class CredentialController {

    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public CredentialController(CredentialService credentialService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/credential")
    public String saveCredential(@ModelAttribute("credentialForm") Credential credentialForm, Model model,
                                 RedirectAttributes redirectAttributes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // If a credential exists with that id, update it
        if(credentialForm.getCredentialid() != null){
            credentialService.updateCredential(credentialForm);
        } else {
            credentialService.insertCredential(credentialForm);
        }
        model.addAttribute("credentials", credentialService.getAllCredentials());
        redirectAttributes.addFlashAttribute("setTab", "CredentialTab");
        return "redirect:/home";
    }

    @GetMapping("/deleteCredential/{id}")
    public String deleteCredential(@PathVariable("id") long id, Model model,
                                   RedirectAttributes redirectAttributes){
        credentialService.deleteCredential(id);
        model.addAttribute("credentials", credentialService.getAllCredentials());
        redirectAttributes.addFlashAttribute("setTab", "CredentialTab");
        return "redirect:/home";
    }
}
