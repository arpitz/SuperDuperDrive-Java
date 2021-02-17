package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping()
public class NoteController {

    private static final int DESCRIPTION_LIMIT = 150;
    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/note")
    public String saveNote(@ModelAttribute("noteForm") Note noteForm, Model model,
                           RedirectAttributes redirectAttributes, Principal principal){
        User user = userService.getUser(principal.getName());
        Integer userid = user.getUserid();

        // Check the description size
        if(noteForm.getNotedescription().length() > DESCRIPTION_LIMIT){
            redirectAttributes.addFlashAttribute("descLimitError", "descLimitError");
            redirectAttributes.addFlashAttribute("setTab", "NoteTab");
            return "redirect:/home";
        }
        // If a note exists with that id, update it
        if(noteForm.getNoteid() != null){
            try{
                noteService.updateNote(noteForm, userid);
                redirectAttributes.addFlashAttribute("noteEditSuccess", "noteEditSuccess");
            } catch (Exception e){
                redirectAttributes.addFlashAttribute("noteEditFailure", "noteEditFailure");
            }
        } else {
            noteForm.setUserid(userid);
            try{
                noteService.insertNote(noteForm);
                redirectAttributes.addFlashAttribute("noteSuccess", "noteSuccess");
            } catch (Exception e){
                redirectAttributes.addFlashAttribute("noteFailure", "noteFailure");
            }
        }

        model.addAttribute("notes", noteService.getAllNotes(userid));
        redirectAttributes.addFlashAttribute("setTab", "NoteTab");
        return "redirect:/home";
    }

    @GetMapping("/deleteNote/{id}")
    public String deleteNote(@PathVariable("id") long id, Model model,
                             RedirectAttributes redirectAttributes, Principal principal){
        User user = userService.getUser(principal.getName());
        Integer userid = user.getUserid();

        noteService.deleteNote(id, userid);
        model.addAttribute("notes", noteService.getAllNotes(userid));
        redirectAttributes.addFlashAttribute("setTab", "NoteTab");
        return "redirect:/home";
    }
}
