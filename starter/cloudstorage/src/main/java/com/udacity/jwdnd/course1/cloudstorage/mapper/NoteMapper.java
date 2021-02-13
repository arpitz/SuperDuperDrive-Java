package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * from notes where userid=#{userid}")
    List<Note> getAllNotes(Integer userid);

    @Insert("INSERT into notes (noteid, notetitle, notedescription, userid) " +
            "values (#{noteid}, #{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insertNote(Note note);

    @Delete("DELETE from notes where noteid=#{noteid} and userid=#{userid}")
    void deleteNote(long noteid, Integer userid);

    @Select("SELECT * from notes where noteid=#{noteid}")
    Note getNoteById(long noteid);

    @Update("UPDATE notes set notetitle=#{note.notetitle}, notedescription=#{note.notedescription} " +
            "where noteid=#{note.noteid} and userid=#{userid}")
    int updateNote(Note note, Integer userid);
}
