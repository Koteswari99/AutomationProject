package com.capco.driver;


import org.testng.annotations.Test;
import com.capco.engine.KeyWordEngine;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//@Listeners({ITestListenersClass.class})
public class CapcoDriver {

	public KeyWordEngine keyWordEngine;

	@Test
	public void test() throws Exception {

		// Get the list of all Excel files in the specified path.
		keyWordEngine = new KeyWordEngine();
		File dir = new File("C:\\Users\\DKTW\\CAPCO-WebFasTest\\src\\main\\java\\com\\capco\\scenarios");
		List<File> excelFiles = new ArrayList<>();
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith("_Yes.xlsx")) {
				excelFiles.add(file);
			}
		}

		List<String> sheetNames = new ArrayList<>();
		for (int j = 0; j < excelFiles.size(); j++) {
			Workbook workbook = WorkbookFactory.create(excelFiles.get(j));
			XSSFWorkbook xssfWorkbook = (XSSFWorkbook) workbook;
			Sheet sheet = xssfWorkbook.getSheetAt(0);
			for (int i = 0; i < sheet.getLastRowNum(); i++) {
				Cell cell = sheet.getRow(i + 1).getCell(2);
				String executionFlag = cell.getStringCellValue();
				if (executionFlag.equals("Yes")) {
					String sheetName = sheet.getRow(i + 1).getCell(1).getStringCellValue();
					sheetNames.add(sheetName);
				}
			}
			for (String sheetName : sheetNames) {
				

				boolean sheetExists = xssfWorkbook.getSheet(sheetName) != null;

				if (!sheetExists) {
					System.out.println("The sheet " + sheetName + " does not exist.");
				} else {
					String Excelfilename = excelFiles.get(j).getName();
					String path = dir.getPath() + "\\" + Excelfilename;
					System.out.println(sheetName);
					System.out.println(path);
					keyWordEngine.startExecution(path, sheetName);
				}
			}

			
			/*  String sheetName = null; 
			  for (int i = 0; i <xssfWorkbook.getNumberOfSheets(); i++) { 
			  Sheet sheet =xssfWorkbook.getSheetAt(i); 
			  if (sheet.getSheetName().endsWith("_Y")) {
			  sheetName = sheet.getSheetName(); 
			  String Excelfilename =excelFiles.get(j).getName(); 
			  String path = dir.getPath() + "\\" +Excelfilename; 
			  sheetNames.add(sheetName);
			  System.out.println(path); 
			  System.out.println(sheetName);
			  keyWordEngine.startExecution(path, sheetName); 
			  }*/
			 

		}

	}

}

