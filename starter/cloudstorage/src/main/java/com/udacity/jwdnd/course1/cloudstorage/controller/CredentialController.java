package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;

@Controller
@RequestMapping()
public class CredentialController {

    private CredentialService credentialService;
    private EncryptionService encryptionService;
    private UserService userService;

    public CredentialController(CredentialService credentialService, EncryptionService encryptionService, UserService userService) {
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    @PostMapping("/credential")
    public String saveCredential(@ModelAttribute("credentialForm") Credential credentialForm, Model model,
                                 RedirectAttributes redirectAttributes, Principal principal) throws NoSuchAlgorithmException, InvalidKeySpecException {
        User user = userService.getUser(principal.getName());
        Integer userid = user.getUserid();

        // If a credential exists with that id, update it
        if(credentialForm.getCredentialid() != null){
            try{
                credentialService.updateCredential(credentialForm, userid);
                redirectAttributes.addFlashAttribute("credEditSuccess", "credEditSuccess");
            } catch (Exception e){
                redirectAttributes.addFlashAttribute("credEditFailure", "credEditFailure");
            }
        } else {
            credentialForm.setUserid(userid);
            try{
                credentialService.insertCredential(credentialForm);
                redirectAttributes.addFlashAttribute("credentialSuccess", "credentialSuccess");
            } catch (Exception e){
                redirectAttributes.addFlashAttribute("credentialFailure", "credentialFailure");
            }
        }
        model.addAttribute("credentials", credentialService.getAllCredentials(userid));
        redirectAttributes.addFlashAttribute("setTab", "CredentialTab");
        return "redirect:/home";
    }

    @GetMapping("/deleteCredential/{id}")
    public String deleteCredential(@PathVariable("id") long id, Model model,
                                   RedirectAttributes redirectAttributes, Principal principal){
        User user = userService.getUser(principal.getName());
        Integer userid = user.getUserid();

        credentialService.deleteCredential(id, userid);
        model.addAttribute("credentials", credentialService.getAllCredentials(userid));
        redirectAttributes.addFlashAttribute("setTab", "CredentialTab");
        return "redirect:/home";
    }
}
