package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.jwt.comment.PassToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/files")
public class FileController {
    private static String URL_C_AND_T;
    @Value("${web.upload-path}")
    private String uploadPath;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

    @PassToken
    @PostMapping("/upload")
    public Response upload(@RequestParam("File") MultipartFile uploadFile,
                           HttpServletRequest request) {
        String format = sdf.format(new Date());
        File folder = new File(uploadPath + format);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }

        String oldName = uploadFile.getOriginalFilename();
        String newName = UUID.randomUUID().toString()
                + oldName.substring(oldName.lastIndexOf("."), oldName.length());
        try {
            uploadFile.transferTo(new File(folder, newName));
            String filePath = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + "/" + format + newName;
            URL_C_AND_T = filePath;
            return ResponseGenerator.genSuccessResult(URL_C_AND_T);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseGenerator.genFailResult("error");
    }

    @PassToken
    @GetMapping("/view")
    public Response view() {
        return ResponseGenerator.genSuccessResultStringData(URL_C_AND_T);
    }
}
