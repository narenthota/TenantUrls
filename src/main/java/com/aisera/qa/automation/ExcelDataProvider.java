package com.aisera.qa.automation;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExcelDataProvider {

    @DataProvider(name = "urlProvider")
    public Object[][] readUrlsFromExcel() throws IOException {
        // Specify the path to your Excel file
        String excelFilePath = "test-data/urls.xlsx";
        InputStream fis = getClass().getClassLoader().getResourceAsStream(excelFilePath);

        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int rowCount = sheet.getPhysicalNumberOfRows();
        Object[][] data = new Object[rowCount - 1][2]; // Assuming the first row is the header

        for (int i = 1; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            data[i - 1][0] = row.getCell(0).getStringCellValue(); // Website Name in the first column
            data[i - 1][1] = row.getCell(1).getStringCellValue(); // URL in the second column
        }

        workbook.close();
        fis.close();

        return data;
    }
}
