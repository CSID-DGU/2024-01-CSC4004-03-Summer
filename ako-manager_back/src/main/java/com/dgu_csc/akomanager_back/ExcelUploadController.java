package com.dgu_csc.akomanager_back;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/excel")
public class ExcelUploadController {
    private List<String[][]> excels = new ArrayList<String[][]>(); // 각 액셀 파일을 2차원 스트링 형태로 저장
    private ArrayList<Subject> basicSubjectList;

    public static void readExcelLocal(ArrayList<Subject> list) {
        String path = "C:\\Users\\jwn63\\Downloads\\"; // 파일 경로 설정
        String filename = "교육과정목록.xlsx"; // 파일명 설정
        try {
            FileInputStream file = new FileInputStream(path + filename);
            Workbook workbook = WorkbookFactory.create(file);

            NumberFormat f = NumberFormat.getInstance();
            f.setGroupingUsed(false); // 지수로 안나오게 설정

            int sheetNum = workbook.getNumberOfSheets(); // 시트 갯수

            for (int s = 0; s < sheetNum; s++) {
                Sheet sheet = workbook.getSheetAt(s);
                int rows = sheet.getPhysicalNumberOfRows();

                for (int r = 1; r < rows; r++) {
                    Row row = sheet.getRow(r);

                    int cells = row.getPhysicalNumberOfCells();

                    Subject newSubject = new Subject();

                    for (int c = 0; c < cells - 3; c++) {
                        Cell cell = row.getCell(c);

                        String value = "";
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case STRING:
                                    value = cell.getStringCellValue();
                                    break;
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        value = cell.getDateCellValue().toString();
                                    } else {
                                        value = f.format(cell.getNumericCellValue());
                                    }
                                    break;
                                case BOOLEAN:
                                    value = cell.getBooleanCellValue() + "";
                                    break;
                                case BLANK:
                                    value = "";
                                    break;
                                case ERROR:
                                    value = cell.getErrorCellValue() + "";
                                    break;
                            }
                        }

                        switch (c) {
                            case 1: { // 개설 학년
                                String[] numbers = value.split(",");
                                ArrayList<String> schoolYears = new ArrayList<>();
                                Collections.addAll(schoolYears, numbers);
                                newSubject.setYearOfOffering(schoolYears);
                                break;
                            }
                            case 2: { // 개설 학기
                                newSubject.setSemesterOfOffering(value);
                                break;
                            }
                            case 3: { // 교과과정
                                newSubject.setCurriculum(value);
                                break;
                            }
                            case 5: { // 이수구분
                                newSubject.setLectureType(value);
                                break;
                            }
                            case 6: { // 학수번호
                                newSubject.setCourseNumber(value);
                                break;
                            }
                            case 7: { // 교과목국문명
                                newSubject.setLectureName(value);
                                break;
                            }
                            case 9: { // 학점
                                newSubject.setCredit(value);
                                break;
                            }
                            default:
                                break;
                        }
                    }
                    System.out.println();
                    list.add(newSubject);
                }
            }
            file.close(); // 파일 닫기
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[][] readExcel(MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        int cols = sheet.getRow(0).getPhysicalNumberOfCells();
        String[][] data = new String[rows][cols];

        for (int r = 0; r < rows; r++) {
            Row row = sheet.getRow(r);
            for (int c = 0; c < cols; c++) {
                Cell cell = row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                data[r][c] = (cell == null) ? "" : cell.toString();
            }
        }
        workbook.close();
        return data;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded.");
        }
        try {
            String[][] data = readExcel(file);
            excels.add(data);
            return ResponseEntity.ok("File processed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing file.");
        }
    }
}