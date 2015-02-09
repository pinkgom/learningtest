package poi;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class ExcelTest {

	@Test
	public void testReadExcel() {

		// TODO Auto-generated method stub

		// String filePath = "c:/품질관리_cdma_0111.xlsx";
		String filePath = "savedxls.xlxs";

		try {

			
			/*
			// xls 파일 읽기
			 * POIFSFileSystem excel = new POIFSFileSystem(new
			 * FileInputStream(filePath));
			 * 
			 * HSSFWorkbook workBook = new HSSFWorkbook(new FileInputStream(new
			 * File(filePath)));
			 * 
			 * HSSFSheet sheet = null;
			 * HSSFRow row = null;
			 * HSSFCell cell = null;			 
			*/

			XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream(new File(filePath)));

			XSSFSheet sheet = null;
			XSSFRow row = null;
			XSSFCell cell = null;



			int sheetNum = workBook.getNumberOfSheets();

			for (int k = 0; k < sheetNum; k++) {

				sheet = workBook.getSheetAt(k);
				int rows = sheet.getPhysicalNumberOfRows();

				for (int r = 0; r < rows; r++) {
					row = sheet.getRow(r);
					int cells = row.getPhysicalNumberOfCells();

					for (short c = 0; c < cells; c++) {

						cell = row.getCell(c);
						System.out.println("row:" + r + "==cells:" + c);

						if (cell == null) {
							continue;
						}

						switch (cell.getCellType()) {

						case 0:
							System.out.println(cell.getNumericCellValue());

							break;

						case 1:
							System.out.println(cell.getStringCellValue());
							break;

						case Cell.CELL_TYPE_FORMULA:
							System.out.println(cell.getCellFormula());
							break;

						default:
							System.out.println("");

						}

					}// cell
				}// row

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
