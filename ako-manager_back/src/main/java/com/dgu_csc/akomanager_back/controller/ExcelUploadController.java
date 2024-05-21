package com.dgu_csc.akomanager_back.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dgu_csc.akomanager_back.model.Subject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ExcelUploadController {

    private final List<String[][]> excels = new ArrayList<>();

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded.");
        }

        try {
            String[][] data = readExcel(file);
            excels.add(data);
            return ResponseEntity.ok("File processed successfully.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File not found: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }

    public static String[][] readExcel(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
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
            return data;
        }
    }

    public static void readExcelLocal(String path, String filename, ArrayList<Subject> list) {
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
                                        value = f.format(cell.getNumericCellValue()) + "";
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
                                newSubject.setEstablishedGrade(value);
                                break;
                            }
                            case 2: { // 개설 학기
                                newSubject.setOpenSemester(value);
                                break;
                            }
                            case 3: { // 교과과정
                                newSubject.setCourseOfStudy(value);
                                break;
                            }
                            case 5: { // 이수구분
                                newSubject.setClassificationOfCompletion(value);
                                break;
                            }
                            case 6: { // 학수번호
                                newSubject.setSubjectNum(value);
                                break;
                            }
                            case 7: { // 교과목국문명
                                newSubject.setSubjectName(value);
                                break;
                            }
                            case 9: { // 학점
                                newSubject.setGrade(Integer.parseInt(value));
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
}
