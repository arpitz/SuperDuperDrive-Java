package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping()
public class FileController {
    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/fileUpload")
    public String saveFile(@RequestParam("fileUpload") MultipartFile file, Model model,
                           RedirectAttributes redirectAttributes, Principal principal) throws IOException {
        User user = userService.getUser(principal.getName());
        Integer userid = user.getUserid();

        File newFile = new File(
                null,
                file.getOriginalFilename(),
                file.getContentType(),
                String.valueOf(file.getSize()),
                userid,
                file.getBytes()
        );

        fileService.insertFile(newFile);
        model.addAttribute("files", fileService.getAllFiles(userid));
        redirectAttributes.addFlashAttribute("setTab", "FileTab");
        return "redirect:/home";
    }

    @GetMapping("/download/{id}")
    public String downloadFile(@PathVariable("id") long id, Model model,
                               RedirectAttributes redirectAttributes) throws IOException {
        File newFile = fileService.getFileById(id);
        java.io.File outputFile = new java.io.File(newFile.getFilename());
        FileUtils.writeByteArrayToFile(outputFile, newFile.getFiledata());
        redirectAttributes.addFlashAttribute("setTab", "FileTab");
        return "redirect:/home";
    }

    @GetMapping("/deleteFile/{id}")
    public String deleteFile(@PathVariable("id") long id, Model model,
                             RedirectAttributes redirectAttributes, Principal principal){
        User user = userService.getUser(principal.getName());
        Integer userid = user.getUserid();

        fileService.deleteFile(id, userid);
        model.addAttribute("files", fileService.getAllFiles(userid));
        redirectAttributes.addFlashAttribute("setTab", "FileTab");
        return "redirect:/home";
    }
}
