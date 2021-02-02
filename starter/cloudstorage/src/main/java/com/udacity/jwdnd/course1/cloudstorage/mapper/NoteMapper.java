package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * from notes")
    List<Note> getAllNotes();

    @Insert("INSERT into notes (noteid, notetitle, notedescription, userid) " +
            "values (#{noteid}, #{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insertNote(Note note);

    @Delete("DELETE from notes where noteid=#{noteid}")
    void deleteNote(long noteid);

    @Select("SELECT * from notes where noteid=#{noteid}")
    Note getNoteById(long noteid);

    @Update("UPDATE notes set notetitle=#{notetitle}, notedescription=#{notedescription} where noteid=#{noteid}")
    int updateNote(Note note);
}
