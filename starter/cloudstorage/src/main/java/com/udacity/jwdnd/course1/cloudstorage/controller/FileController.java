package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping()
public class FileController {
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/fileUpload")
    public String saveFile(@RequestParam("fileUpload") MultipartFile file, Model model) throws IOException {
        File newFile = new File(
                null,
                file.getOriginalFilename(),
                file.getContentType(),
                String.valueOf(file.getSize()),
                null,
                file.getBytes()
        );

        fileService.insertFile(newFile);
        model.addAttribute("files", fileService.getAllFiles());
        model.addAttribute("setTab", "FileTab");
        return "home";
    }

    @GetMapping("/download/{id}")
    public String downloadFile(@PathVariable("id") long id, Model model) throws IOException {
        File newFile = fileService.getFileById(id);
        java.io.File outputFile = new java.io.File(newFile.getFilename());
        FileUtils.writeByteArrayToFile(outputFile, newFile.getFiledata());
        model.addAttribute("setTab", "FileTab");
        return "home";
    }

    @GetMapping("/deleteFile/{id}")
    public String deleteFile(@PathVariable("id") long id, Model model){
        fileService.deleteFile(id);
        model.addAttribute("files", fileService.getAllFiles());
        model.addAttribute("setTab", "FileTab");
        return "home";
    }
}
