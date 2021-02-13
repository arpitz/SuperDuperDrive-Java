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
        // If a note exists with that id, update it
        if(noteForm.getNoteid() != null){
            noteService.updateNote(noteForm, userid);
        } else {
            noteForm.setUserid(userid);
            noteService.insertNote(noteForm);
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
