package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getAllFiles(Integer userid){
        return fileMapper.getAllFiles(userid);
    }

    public void insertFile(File file){
        fileMapper.insertFile(new File(null, file.getFilename(), file.getContenttype(),
                file.getFilesize(), file.getUserid(),file.getFiledata()));
    }

    public void deleteFile(long fileId, Integer userid){
        fileMapper.deleteFile(fileId, userid);
    }

    public File getFileById(long fileid){
        return fileMapper.getFileById(fileid);
    }

    public File getFileByName(String filename, Integer userid){
        return fileMapper.getFileByName(filename, userid);
    }
}
