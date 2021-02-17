package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
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

        String errors = checkValidations(file, userid);
        if(errors != null){
            redirectAttributes.addFlashAttribute(errors, errors);
            redirectAttributes.addFlashAttribute("setTab", "FileTab");
            return "redirect:/home";
        }

        try{
            fileService.insertFile(newFile);
            redirectAttributes.addFlashAttribute("uploadSuccess", "uploadSuccess");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("uploadFailure", "uploadFailure");
        }
        model.addAttribute("files", fileService.getAllFiles(userid));
        redirectAttributes.addFlashAttribute("setTab", "FileTab");
        return "redirect:/home";
    }

    @GetMapping("/download/{id}")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response,
                               @PathVariable("id") long id) throws IOException {
        File file = fileService.getFileById(id);
        java.io.File ioFile = new java.io.File(file.getFilename());
//        java.io.File outputFile = new java.io.File(newFile.getFilename());
//        FileUtils.writeByteArrayToFile(outputFile, newFile.getFiledata());
//        redirectAttributes.addFlashAttribute("setTab", "FileTab");
//        return "redirect:/home";
        if (file != null) {
            //get the mimetype
            String mimeType = URLConnection.guessContentTypeFromName(file.getFilename());
            if (mimeType == null) {
                //unknown mimetype so set the mimetype to application/octet-stream
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);
            //response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getFilename() + "\""));
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getFilename() + "\""));
            response.setContentLength(Integer.parseInt(file.getFilesize()));

            InputStream inputStream = new BufferedInputStream(new FileInputStream(ioFile));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
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

    public String checkValidations(MultipartFile file, Integer userid){
        if(file.getOriginalFilename().isEmpty()){
            return "emptyFile";
        }
        Long max_file_size = Math.round(Math.ceil((float)file.getSize()/1000));
        if(max_file_size > 128){
            return "fileSizeLimit";
        }
        File f = fileService.getFileByName(file.getOriginalFilename(), userid);
        if(f != null){
            return "duplicateFile";
        } else return null;
    }
}
