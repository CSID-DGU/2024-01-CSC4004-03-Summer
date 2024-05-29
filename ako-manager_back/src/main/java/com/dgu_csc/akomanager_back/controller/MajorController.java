package com.dgu_csc.akomanager_back.controller;


import com.dgu_csc.akomanager_back.dto.MajorDto;
import com.dgu_csc.akomanager_back.jwt.JWTUtil;
import com.dgu_csc.akomanager_back.model.Major;
import com.dgu_csc.akomanager_back.model.User;
import com.dgu_csc.akomanager_back.service.MajorService;
import com.dgu_csc.akomanager_back.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Major")
public class MajorController {

    @Autowired
    private final MajorService majorService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;


    // POST : [/Major/add] 과목 추가
    @PostMapping("/add")
    public ResponseEntity<String> addMajor(@RequestBody Major major, HttpServletRequest request1) {
        try {
            String authorization= request1.getHeader("Authorization");
            String token = authorization.split(" ")[1];
            String studentId = jwtUtil.getUsername(token);
            Optional<User> masteruser = userService.findByStudentId(studentId);

            // 권한 확인 후 충족시만 저장
            if(masteruser.get().getRole().equals("11")) {
                majorService.saveMajor(major);
                return ResponseEntity.ok("Major added  successfully");
            }

            // 권한 없을 시 오류
            return ResponseEntity.status(409).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // POST : [/Major/getTotalScore]
    @PostMapping("/getTotalScore")
    public Integer getTotalScore(@RequestBody MajorDto majorDto, HttpServletRequest request) {
        return majorService.getTotalScore(majorDto);
    }

    // POST : [/Major/getTotalMajorScore]
    @PostMapping("/getTotalMajorScore")
    public Integer getTotalMajorScore(@RequestBody MajorDto majorDto, HttpServletRequest request) {
        return majorService.getTotalMajorScore(majorDto);
    }

    // POST : [/Major/getTotalMajorScore]
    @PostMapping("/getTotalCommonScore")
    public Integer getTotalCommonScore(@RequestBody MajorDto majorDto, HttpServletRequest request) {
        return majorService.getTotalCommonScore(majorDto);
    }

    // POST : [/Major/getTotalMajorScore]
    @PostMapping("/getTotalDesignatedScore")
    public Integer getTotalDesignatedScore(@RequestBody MajorDto majorDto, HttpServletRequest request) {
        return majorService.getTotalDesignatedScore(majorDto);
    }

}