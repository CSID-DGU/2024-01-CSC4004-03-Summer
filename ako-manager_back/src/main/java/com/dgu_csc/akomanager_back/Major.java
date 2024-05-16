package com.dgu_csc.akomanager_back;

import java.util.ArrayList;
import lombok.Data;

@Data
public class Major {
    String majorName; // 전공 이름
    ArrayList<String> majorCode; // 전공 코드
    int commonElective; // 공통 교양
    int basicElective; // 기초 교양
    int BSMCredit; // BSM 학점
    int majorCredit; // 전공 학점
    int totalCredit; // 전체 학점
    double creditAverage; // 학점 평균
    int TOEIC; // 토익 점수
    int englishLecture; // 영어 강의
}
