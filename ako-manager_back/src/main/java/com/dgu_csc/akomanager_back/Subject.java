package com.dgu_csc.akomanager_back;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Subject {
    private String courseNumber; // 학수 번호
    private ArrayList<String> yearOfOffering; // 개설 학년
    private String semesterOfOffering; // 개설 학기
    private String curriculum; // 교과 과정
    private String lectureType; // 이수 구분
    private String lectureName; // 교과목명
    private String credit; // 학점
}
