package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * from files")
    List<File> getAllFiles();

    @Insert("INSERT into files (fileid, filename, contenttype, filesize, userid, filedata)" +
            "values (#{fileid}, #{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileid")
    int insertFile(File file);

    @Delete("DELETE from files where fileid=#{fileid}")
    void deleteFile(long fileid);

    @Select("SELECT * from files where fileid=#{fileid}")
    File getFileById(long fileid);
}
