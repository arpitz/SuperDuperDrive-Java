package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping()
public class NoteController {

    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/note")
    public String saveNote(@ModelAttribute("noteForm") Note noteForm, Model model){
        // If a note exists with that id, update it
        if(noteForm.getNoteid() != null){
            noteService.updateNote(noteForm);
        } else {
            noteService.insertNote(noteForm);
        }
        model.addAttribute("notes", noteService.getAllNotes());
        model.addAttribute("setTab", "NoteTab");
        return "home";
    }

    @GetMapping("/deleteNote/{id}")
    public String deleteNote(@PathVariable("id") long id, Model model){
        noteService.deleteNote(id);
        model.addAttribute("notes", noteService.getAllNotes());
        model.addAttribute("setTab", "NoteTab");
        return "home";
    }
}
