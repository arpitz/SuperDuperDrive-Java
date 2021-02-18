package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Integer fileId){
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(  file.getContenttype() ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
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
        // If file size is greater than 1000 KB i.e. 1 MB
        if(max_file_size > 1000) {
            return "fileSizeLimit";
        }

        File f = fileService.getFileByName(file.getOriginalFilename(), userid);
        if(f != null){
            return "duplicateFile";
        } else return null;
    }
}
