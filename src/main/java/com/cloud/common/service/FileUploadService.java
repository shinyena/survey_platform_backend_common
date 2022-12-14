package com.cloud.common.service;

import com.cloud.common.dto.FileDTO;
import com.cloud.common.openfeign.KakaoAccessClient;
import com.cloud.common.openfeign.KakaoStorageClient;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final KakaoStorageClient kakaoStorageClient;
    private final KakaoAccessClient kakaoAccessClient;

    @Value("${kakao.access.id}")
    String accessId;

    @Value("${kakao.access.secret}")
    String accessSecret;

    @Value("${kakao.storage.account}")
    String account;

    @Value("${kakao.storage.bucketName}")
    String bucketName;

    public String uploadFile(FileDTO fileDTO, String folder)
            throws IllegalStateException, IOException {

        // 파일 이름 생성하기 (uuid.확장자)
        String originalName = fileDTO.getOriginalName();
        String fileUuid = UUID.randomUUID().toString();
        String fileExt = originalName.substring(originalName.lastIndexOf("."));
        String fileName = fileUuid + fileExt;

        // 파일 업로드
        Response response = kakaoStorageClient.fileUpload(getAuthToken(), account, bucketName, folder, fileName, fileDTO.getFileBytes());

        if (response.status() == 201 ) {
            return "https://objectstorage.kr-central-1.kakaoi.io/v1/" + account + "/" + bucketName + "/" + folder + File.separator + fileName;
        }
        return null;
    }

    public String getAuthToken() {
        JSONObject application_credential = new JSONObject();
        application_credential.put("id", accessId);
        application_credential.put("secret", accessSecret);
        JSONArray methods = new JSONArray();
        methods.add("application_credential");
        JSONObject identity = new JSONObject();
        identity.put("methods", methods);
        identity.put("application_credential", application_credential);
        JSONObject auth = new JSONObject();
        auth.put("identity", identity);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("auth", auth);
        Response accessToken = kakaoAccessClient.getAccessToken(jsonObject);
        return accessToken.headers().get("x-subject-token").toString();
    }


}
