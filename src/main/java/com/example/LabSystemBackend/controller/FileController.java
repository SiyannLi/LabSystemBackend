package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.jwt.annotation.AdminLoginToken;
import com.example.LabSystemBackend.jwt.annotation.PassToken;
import com.example.LabSystemBackend.ui.KeyMessage;
import com.example.LabSystemBackend.ui.OutputMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @version 1.0
 * @author Cong Liu
 *
 * Files Controller
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/files")
public class FileController {
    private static String URL_C_AND_T;
    @Value("${web.upload-path}")
    private String uploadPath;

    private SimpleDateFormat sdf = new SimpleDateFormat(KeyMessage.DATE_PATH);

    @AdminLoginToken
    @PostMapping("/upload")
    public Response upload(@RequestHeader(KeyMessage.TOKEN) String token,
                           @RequestParam("File") MultipartFile uploadFile,
                           HttpServletRequest request) {
        String opEmail = JwtUtil.getUserInfo(token, KeyMessage.EMAIL);
        String format = sdf.format(new Date());
        File folder = new File(uploadPath + format);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }

        String oldName = uploadFile.getOriginalFilename();
        String newName = UUID.randomUUID().toString()
                + oldName.substring(oldName.lastIndexOf(OutputMessage.POINT), oldName.length());
        try {
            uploadFile.transferTo(new File(folder, newName));
            String filePath = request.getScheme() + OutputMessage.COLON + OutputMessage.DOUBLE_SLASH
                    + request.getServerName()
                    + OutputMessage.COLON + request.getServerPort()
                    + OutputMessage.SLASH + format + newName;
            URL_C_AND_T = filePath;
            return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail), OutputMessage.SUCCEED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseGenerator.genFailResult(UserController.emailTokens.get(opEmail), OutputMessage.ERROR);
    }

    @PassToken
    @GetMapping("/view")
    public Response view() {
        return ResponseGenerator.genSuccessResultStringData(URL_C_AND_T);
    }
}
