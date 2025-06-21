package com.selenium.test.data;

import java.io.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    private String fileName;
    private String filePath;
    private FileInputStream fis;
    private FileOutputStream fos;
    private Workbook wb;
    private Sheet sheet;
    private CellStyle style;

    public ExcelUtils(String excelName) {
        this.fileName = excelName;
        this.filePath = System.getProperty("user.dir") + "\\src\\test\\java\\com\\selenium\\test\\data\\" + fileName;
        try {
            fis = new FileInputStream(new File(filePath));
            wb = new XSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getRowCount(String sheetName) {
        sheet = wb.getSheet(sheetName);
        return sheet.getPhysicalNumberOfRows();
    }

    public int getColumnCount(String sheetName) {
        sheet = wb.getSheet(sheetName);
        Row row = sheet.getRow(0);
        return row.getLastCellNum();
    }

    public String getCellData(String sheetName, int rownum, int columnum) {
        sheet = wb.getSheet(sheetName);
        Cell cell = sheet.getRow(rownum).getCell(columnum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }

    public void setCellData(String sheetName, int rowIndex, String columnName, String data) throws IOException {
        sheet = wb.getSheet(sheetName);
        Row headerRow = sheet.getRow(0);
        int columnIndex = -1;

        for (Cell cell : headerRow) {
            if (cell.getStringCellValue().trim().equalsIgnoreCase(columnName.trim())) {
                columnIndex = cell.getColumnIndex();
                break;
            }
        }

        if (columnIndex == -1) {
            System.out.println("Column not found: " + columnName);
            return;
        }

        Row row = sheet.getRow(rowIndex);
        if (row == null) row = sheet.createRow(rowIndex);

        Cell cell = row.getCell(columnIndex);
        if (cell == null) cell = row.createCell(columnIndex);

        cell.setCellValue(data);

        fis.close(); // Close input stream before writing
        fos = new FileOutputStream(filePath);
        wb.write(fos);
        fos.close();
    }

    public void fillGreenColor(String sheetName, int rownum, int colnum) throws IOException {
        applyCellColor(sheetName, rownum, colnum, IndexedColors.GREEN);
    }

    public void fillRedColor(String sheetName, int rownum, int colnum) throws IOException {
        applyCellColor(sheetName, rownum, colnum, IndexedColors.RED);
    }

    private void applyCellColor(String sheetName, int rownum, int colnum, IndexedColors color) throws IOException {
        sheet = wb.getSheet(sheetName);
        if (sheet == null) {
            System.out.println("Sheet not found: " + sheetName);
            return;
        }

        Row row = sheet.getRow(rownum);
        if (row == null) {
            row = sheet.createRow(rownum);
        }

        Cell cell = row.getCell(colnum);
        if (cell == null) {
            cell = row.createCell(colnum);
            cell.setCellValue(""); // Set a default value to make the cell visible
        }

        // Create a new style and apply color
        style = wb.createCellStyle();
        style.setFillForegroundColor(color.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Apply the style
        cell.setCellStyle(style);

        // Save the workbook
        fis.close(); // Close input stream before writing
        fos = new FileOutputStream(filePath);
        wb.write(fos);
        fos.close();
    }


    public void closeFile() {
        try {
            if (fis != null) fis.close();
            if (fos != null) fos.close();
            if (wb != null) wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    // Test main method
    public static void main(String[] args) throws IOException {
        ExcelUtils es = new ExcelUtils("TrainSearchData.xlsx");
        System.out.println("Row Count: " + es.getRowCount("Testing"));
        System.out.println("Column Cont: " + es.getColumnCount("SearchData"));
        System.out.println("Cell Data: " + es.getCellData("SearchData", 0, 1));

        es.setCellData("Testing", 1, "Actual", "Doee");
        es.fillRedColor("Testing", 1, 2);
        es.closeFile();
    }
}
