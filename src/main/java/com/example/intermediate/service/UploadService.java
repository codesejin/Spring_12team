package com.example.intermediate.service;

//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Member;
import com.example.intermediate.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.dir}")
    private String dir;

    private final AmazonS3Client s3Client;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> upload(MultipartFile file, HttpServletRequest request) throws IOException {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        String s3FileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(file.getSize());

        s3Client.putObject(bucket, s3FileName, file.getInputStream(), objMeta);

        String httpUrl = s3Client.getUrl(bucket, dir + s3FileName).toString();
        //https://myspartabucket12.s3.ap-northeast-2.amazonaws.com/52af4048-2405-4531-b13d-c6a515896759-beach.jpg",
        String temp = httpUrl.substring(httpUrl.lastIndexOf("/")+1);
        String answer = "https://s3."+region+".amazonaws.com/"+bucket+"/"+temp;
        return ResponseDto.success(answer);

    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
