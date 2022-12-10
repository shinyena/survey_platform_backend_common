package com.cloud.common.controller;

import com.cloud.common.dto.MenuDTO;
import com.cloud.common.entity.QuestionType;
import com.cloud.common.entity.SurveyState;
import com.cloud.common.entity.UserAge;
import com.cloud.common.entity.UserJob;
import com.cloud.common.repository.UserAgeRepository;
import com.cloud.common.service.CommonService;
import com.cloud.common.service.FileUploadService;
import com.cloud.common.service.kafka.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="v1/common")
@RequiredArgsConstructor
public class CommonServiceController {
    private final CommonService commonService;
    private final KafkaProducer kafkaProducer;
    private final FileUploadService fileUploadService;

    @GetMapping("/test")
    public String test() {
        return "Common서버로부터 응답.";
    }


    @GetMapping("/menu")
    public List<MenuDTO> getUserMenuDTO(Principal principal) {
        System.out.println("principal = " + principal);
        if (principal != null) {
            JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
            Map<String,Object> resource_access = (Map<String,Object>) token.getTokenAttributes().get("resource_access");
            Map<String, Object> team_cloud_client = (Map<String, Object>) resource_access.get("team_cloud_client");
            ArrayList<String> roles = (ArrayList) team_cloud_client.get("roles");
            if (roles.get(0).equals("USER")) {
                return commonService.getUserMenuDTOList();
            } else if (roles.get(0).equals("ADMIN")) {
                return commonService.getAdminMenuDTOList();
            }
        }
        return commonService.getAllMenuDTOList();
    }

    @PostMapping("/kafkaConnTest")
    public String sendMessage(@RequestParam("message") String message) {
        this.kafkaProducer.sendMessage(message);

        return "success";
    }

    @PostMapping("/upload/{folder}")
    public String uploadFile(
            @RequestParam MultipartFile multipartFile,
            @PathVariable String folder) throws IllegalStateException, IOException {
        return fileUploadService.uploadFile(multipartFile, folder);
    }



    @GetMapping("/questionType")
    public List<QuestionType> getQuestionTypeList() {
        return commonService.getQuestionTypeList();
    }

    @GetMapping("/surveyState")
    public List<SurveyState> getSurveyStateList() {
        return commonService.getSurveyStateList();
    }

    @GetMapping("/userAge")
    public List<UserAge> getUserAgeList() {
        return commonService.getUserAgeList();
    }

    @GetMapping("/userJob")
    public List<UserJob> getUserJobList() {
        return commonService.getUserJobList();
    }
}

