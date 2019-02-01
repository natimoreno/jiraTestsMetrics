/**
 * Copyright (c) 2018. Natalia Moreno
 */

package com.github.testing.jiraTestsMetrics;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author nmoreno
 *
 */
public class SpreadsheetReader {
	
	
	public static final String REPORT_TEMPLATE_PATH = "/src/main/resources/files/Template.xls";
	
	public static final String REPORT_TEMPLATE_PATH_TEMP = "./target/Test_Report.xls";
	
	private WritableWorkbook reportCopy;
	
	private WritableSheet sheet;
	
	private static final String REPORT_ENCODING = "Cp1252";
	
	private static final int DEFAULT_SHEET_ID = 0;

	public static Map<ReportData, String> values = new HashMap<ReportData, String>();

	public static enum ReportData {
		NAME(0), TYPE(1), LEVEL(2), AUTOMATION_LEVEL(17), PRIORITY(8), NARRATIVE(3), PRE_CONDITIONS(4), 
		STEPS(5), EXPECTED_RESULT(6), STORY(7), AUTHOR(15), COMPONENT(21), REFACTORING(-1), JIRA_LINK(18), 
		COMMENTS(20);
	
		private int designReport = 0;
		
		ReportData(int designReport) {
			this.designReport = designReport;
		}
		
		public int getCellIdDesign() {
			return designReport;
		}

	}

	
	
	/**
	 * Constructor
	 */
	public SpreadsheetReader(){}
	
	
	/**
	 * Get data from XLS File
	 * @param i
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public Map<ReportData, String> readXls(int i, String file) throws BiffException, IOException{
		
		WorkbookSettings setting = new WorkbookSettings();
		setting.setEncoding("Cp1252");
		Workbook workbook =  Workbook.getWorkbook(new File(file), setting);
		Sheet sheet = workbook.getSheet(0);

		values.put(ReportData.NAME, sheet.getCell(ReportData.NAME.getCellIdDesign(), i).getContents());
		values.put(ReportData.TYPE, sheet.getCell(ReportData.TYPE.getCellIdDesign(), i).getContents());
		values.put(ReportData.LEVEL, sheet.getCell(ReportData.LEVEL.getCellIdDesign(), i).getContents());
		values.put(ReportData.AUTOMATION_LEVEL, sheet.getCell(ReportData.AUTOMATION_LEVEL.getCellIdDesign(), i).getContents());
		values.put(ReportData.NARRATIVE, sheet.getCell(ReportData.NARRATIVE.getCellIdDesign(), i).getContents());
		values.put(ReportData.PRE_CONDITIONS, sheet.getCell(ReportData.PRE_CONDITIONS.getCellIdDesign(), i).getContents());
		values.put(ReportData.STEPS, sheet.getCell(ReportData.STEPS.getCellIdDesign(), i).getContents());
		values.put(ReportData.EXPECTED_RESULT, sheet.getCell(ReportData.EXPECTED_RESULT.getCellIdDesign(), i).getContents());
		values.put(ReportData.STORY, sheet.getCell(ReportData.STORY.getCellIdDesign(), i).getContents());
		values.put(ReportData.PRIORITY, sheet.getCell(ReportData.PRIORITY.getCellIdDesign(), i).getContents());
		values.put(ReportData.AUTHOR, sheet.getCell(ReportData.AUTHOR.getCellIdDesign(), i).getContents());
		values.put(ReportData.JIRA_LINK, sheet.getCell(ReportData.JIRA_LINK.getCellIdDesign(), i).getContents());
		values.put(ReportData.COMMENTS, sheet.getCell(ReportData.COMMENTS.getCellIdDesign(), i).getContents());
		values.put(ReportData.COMPONENT, sheet.getCell(ReportData.COMPONENT.getCellIdDesign(), i).getContents());
		return values;	
	}

	
	public void writeDataInRow(int col, int row, String value) throws RowsExceededException, WriteException{
		
		WritableCellFormat wcf = new WritableCellFormat();
		wcf.setAlignment(Alignment.RIGHT);
		
		Label label = new Label(col, row, value, wcf);
        sheet.addCell(label);
	}
	
	public void writeNumInRow(int col, int row, String value) throws RowsExceededException, WriteException{
		
		WritableFont numberFont = new WritableFont(WritableFont.ARIAL);
		WritableCellFormat numberFormat = new WritableCellFormat(numberFont, new NumberFormat("#0"));
		
		Label label = new Label(col, row, value, numberFormat);
        sheet.addCell(label);

	}
	
	public void writeAutomationLevel(List<String> data) throws Exception{
		
		int row = sheet.getRows();
	
		for (int i = 0; i < data.size(); i++) {
			
			if (StringUtils.isNumeric(data.get(i)))
				writeNumInRow(i+1,  row, data.get(i));
			else{
				writeDataInRow(i+1,  row, data.get(i));
			}
		}
	}
	
	public void createReport() throws Exception{
		
		WorkbookSettings settings = new WorkbookSettings();
		settings.setEncoding(REPORT_ENCODING);
		
		File template = new File(System.getProperty("user.dir") + REPORT_TEMPLATE_PATH);
		
		reportCopy = Workbook.createWorkbook(new File(REPORT_TEMPLATE_PATH_TEMP), Workbook.getWorkbook(template, settings));
		sheet = reportCopy.getSheet(DEFAULT_SHEET_ID);
		writeAutomationHeader();
	}
	
	public void closeReport() throws IOException, WriteException{
		
		reportCopy.write();
		reportCopy.close();
	}
	
	public void writeTestTypes(List<String> data) throws Exception{
		
		int row = sheet.getRows();
	
		for (int i = 0; i < data.size(); i++) {
			
			if (StringUtils.isNumeric(data.get(i)))
				writeNumInRow(i+1,  row, data.get(i));
			else{
				writeDataInRow(i+1,  row, data.get(i));
			}
		}
	}
	
	public void writeTypeHeader() throws Exception{
		
		writeDataInRow(1, 15, "Unit");
		writeDataInRow(2, 15, "System");
		writeDataInRow(3, 15, "Integration");
		writeDataInRow(4, 15, "Manuals");
	}
	
	public void writeAutomationHeader() throws Exception{
		
		writeDataInRow(1, 1, "Unit");
		writeDataInRow(2, 1, "Automatics");
		writeDataInRow(3, 1, "Manuals");
	}
	
}
