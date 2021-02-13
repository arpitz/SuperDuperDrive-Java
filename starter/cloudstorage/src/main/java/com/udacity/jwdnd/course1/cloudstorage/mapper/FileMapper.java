package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * from files where userid=#{userid}")
    List<File> getAllFiles(Integer userid);

    @Insert("INSERT into files (fileid, filename, contenttype, filesize, userid, filedata)" +
            "values (#{fileid}, #{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileid")
    int insertFile(File file);

    @Delete("DELETE from files where fileid=#{fileid} and userid=#{userid}")
    void deleteFile(long fileid, Integer userid);

    @Select("SELECT * from files where fileid=#{fileid}")
    File getFileById(long fileid);
}
