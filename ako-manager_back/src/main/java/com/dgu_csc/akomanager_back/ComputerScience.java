package com.dgu_csc.akomanager_back;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ComputerScience extends Major {
    ComputerScience() {
        majorName = "AI융합대학 AI소프트웨어융합학부 컴퓨터공학전공";
        majorCode = new ArrayList<>(List.of("CSE", "CSC"));
        commonElective = 17; // 공통 교양
        basicElective = 9; // 기본 소양
        BSMCredit = 21;
        majorCredit = 84;
        totalCredit = 130;
        creditAverage = 2.0;
        TOEIC = 700;
        englishLecture = 4; // 전공 2개 필수
    }

    ArrayList<String> essentialElective; // 교양 필수
    ArrayList<String> essentialBSM; // BSM 필수
    ArrayList<String> essentialMajor = new ArrayList<>(List.of("CSC2004", "CSC4018", "CSC4019", "CSC2007",
            "CSC2011", "CSC2005", "CSC4004", "CSC2025"));
    // 전공 필수
}
