package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService{

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void postConstruct(){
        System.out.println("constructing note service bean");
    }

    public List<Note> getAllNotes(){
        return noteMapper.getAllNotes();
    }

    public void insertNote(Note note){
        noteMapper.insertNote(new Note(null, note.getNotetitle(), note.getNotedescription(), note.getUserid()));
    }

    public void deleteNote(long noteid){
        noteMapper.deleteNote(noteid);
    }

    public Note getNoteById(long noteid){
        return noteMapper.getNoteById(noteid);
    }

    public void updateNote(Note note){
        noteMapper.updateNote(note);
    }
}
