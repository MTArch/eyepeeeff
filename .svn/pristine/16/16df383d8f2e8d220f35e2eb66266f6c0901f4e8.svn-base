package com.iepf.iepfApp.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iepf.iepfApp.Constant.IepfConstant;
import com.iepf.iepfApp.excelDataClass.IEPF4ExcelData;
import com.iepf.iepfApp.formDatabeanClass.IEPF4FormData;

/**
 * This class is used to get Form data from Siebel and Excel data from DMS for
 * IEPF4 it is also comaparing Form Data and Excel Data
 * 
 * @author Satish5 kumar, Saumya Pandey LTIM
 *
 */
public class IEPF4InvestorDataValidation {
	RestTemplate restTemplate = new RestTemplate();
	private static final Logger logger = LoggerFactory.getLogger(IEPF4InvestorDataValidation.class);
	public IEPF4FormData iepf4FormData(String str) throws JsonProcessingException {
		logger.info("Getting form Data from the Onload Response for the SRN:::--" + str);
		IEPF4FormData iepf4FormData = new IEPF4FormData();
		IEPF4ExcelData iepf4ExcelData = new IEPF4ExcelData();

		try {

			JSONObject jsonObject = null;
			JSONObject jsonObjectdata = null;
			JSONObject jsonObjectform = null;
			JSONObject Categoryjson = null;
			JSONArray jsonArray = null;

			List<String> listOfDMSIDData = new ArrayList<String>();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(IepfConstant.authorization, IepfConstant.bearer + IepfServiceImpl.getAcessToken());
			Map<String, String> elements = new HashMap();
			elements.put("referenceNumber", "");
			elements.put("srNumber", str);
			Map<String, Map<String, String>> dataelement = new HashMap<String, Map<String, String>>();
			dataelement.put("requestBody", elements);
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonBody = objectMapper.writeValueAsString(dataelement);
			HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
			ResponseEntity<String> response = restTemplate.exchange(IepfConstant.iepf4Url, HttpMethod.POST, request,
					String.class);
			String responseBody = response.getBody();
			jsonObject = new JSONObject(responseBody);
			String data = jsonObject.getString("data");
			jsonObjectdata = new JSONObject(data);
			String formdata = jsonObjectdata.getString("formData");
			jsonObjectform = new JSONObject(formdata);
			iepf4FormData.setNominalValueOfShares(jsonObjectform.getDouble("totalNominalAmount"));
			double ss = iepf4FormData.getNominalValueOfShares();
			jsonArray = jsonObjectform.getJSONArray("formAttachment");

			// for DMSID
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.length(); i++) {
					Categoryjson = (JSONObject) jsonArray.get(i);
					if (Categoryjson.getString("attachmentCategory").equals("Investor Info")) {// Investor Info
						String dmsIdData = Categoryjson.getString("attachmentDMSId");
						listOfDMSIDData.add(dmsIdData);

					}
				}

				iepf4ExcelData = getExcelDataByDmsIdForIEPF4(listOfDMSIDData);
				compareListsformAndexcelIEPF4(iepf4FormData, iepf4ExcelData, str);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return iepf4FormData;
	}

//ExcelDataIEPF4
	public IEPF4ExcelData getExcelDataByDmsIdForIEPF4(List<String> listData) {

		logger.info("***************************************************");
		logger.info("Reading Value from  investor info Excel file");
		logger.info("***************************************************");
		Map<String, String> inputData = new HashMap<String, String>();
		IEPF4ExcelData iepf4ExcelData = new IEPF4ExcelData();
		try {
			if(listData.size()!=0) {
			for(String dmsID:listData)
			{
			
				byte[] excelData =IepfServiceImpl.getExcelByte(dmsID);
				InputStream input = new ByteArrayInputStream(excelData);
				HSSFWorkbook workBook = new HSSFWorkbook(input);
				Sheet firstSheet = workBook.getSheetAt(1);
				Iterator<Row> rowIterator = firstSheet.rowIterator();
				int rowNum = 3;
				Row desiredRow = null;
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if (row.getRowNum() == rowNum) {
						desiredRow = row;
						break;
					}
				}
				if (desiredRow != null) {
					
					Cell cell = desiredRow.getCell(4);
					if (cell != null && cell.getCellType()==CellType.NUMERIC) {
						logger.info("Cell Value::::---"+cell.getNumericCellValue());
					 
						iepf4ExcelData.setNominalValueOfShares(iepf4ExcelData.getNominalValueOfShares() + cell.getNumericCellValue());
					} else {
						logger.error("The cell is empty or does not exist in the row.");
					}
				} else {
					logger.error("The specified row does not exist in the sheet.");
				}

				workBook.close();
			}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return iepf4ExcelData;
	}

	//// For comprision with FormData and excel Data
	public String compareListsformAndexcelIEPF4(IEPF4FormData iepf4FormData, IEPF4ExcelData iepf4ExcelData,
			String srnNo) {

		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.info("Now Inside Validation Method");
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		logger.info("IEPFFORMDATA:::----" + iepf4FormData + " Excel Data:::---" + iepf4ExcelData);
		String validationSuccess = "N";
		if (iepf4FormData.getNominalValueOfShares() == iepf4ExcelData.getNominalValueOfShares()) {
			validationSuccess = "Y";
			logger.info("Pending for DSC Upload and Payment");
			IepfServiceImpl.changeStatusAfterValidationServiceCall(validationSuccess,srnNo);
		} else {
			logger.info("Investor data fail");
			 IepfServiceImpl.changeStatusAfterValidationServiceCall(validationSuccess, srnNo);
		}
		return validationSuccess;
	}
}
