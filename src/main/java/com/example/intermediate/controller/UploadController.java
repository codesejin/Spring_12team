package com.example.intermediate.controller;

import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class UploadController {

    private final UploadService uploadService;

    @RequestMapping(value = "/api/auth/upload", method = RequestMethod.POST)
    public ResponseDto<?> upload(@RequestPart("file")MultipartFile file,
                                 HttpServletRequest request) throws IOException {
        return uploadService.upload(file, request);
    }
}
