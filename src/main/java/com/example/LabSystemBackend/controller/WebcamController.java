package com.example.LabSystemBackend.controller;

import com.example.LabSystemBackend.common.Response;
import com.example.LabSystemBackend.common.ResponseGenerator;
import com.example.LabSystemBackend.jwt.JwtUtil;
import com.example.LabSystemBackend.jwt.annotation.AdminLoginToken;
import com.example.LabSystemBackend.jwt.annotation.UserLoginToken;
import com.example.LabSystemBackend.ui.OutputMessage;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;


/**
 * @version 1.0
 * @author Cong Liu
 *
 * Webcam Controller
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/webcam")
public class WebcamController {
    @AdminLoginToken
    @ApiOperation("Intercept access to webcam from non-administrators")
    @GetMapping("adminWebcamAccess")
    public Response adminWebcamAccess(@RequestHeader("Authorization") String token) {
        String opEmail = JwtUtil.getUserInfo(token, "email");
       return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(opEmail), OutputMessage.SUCCEED);
    }

    @UserLoginToken
    @ApiOperation("Intercept access to webcam from non-user")
    @GetMapping("userWebcamAccess")
    public Response userWebcamAccess(@RequestHeader("Authorization") String token) {
        String email = JwtUtil.getUserInfo(token, "email");
        return ResponseGenerator.genSuccessResult(UserController.emailTokens.get(email), OutputMessage.SUCCEED);
    }
}
