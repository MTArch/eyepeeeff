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
import org.json.JSONException;
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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iepf.iepfApp.IepfServiceCallApplication;
import com.iepf.iepfApp.Constant.CellCordinatesForIEPf;
import com.iepf.iepfApp.Constant.IepfConstant;
import com.iepf.iepfApp.excelDataClass.IEPF1AExcelData;
//import com.iepf.iepfApp.formDatabeanClass.FormAttachment;
import com.iepf.iepfApp.formDatabeanClass.IEPF1AFormData;

/**
 * This class is used to get Form data from Siebel and Excel data from DMS for
 * IEPF1A it is also comaparing Form Data and Excel Data
 * 
 * @author Satish5 kumar, Saumya Pandey LTIM
 *
 */
public class IEPF1AInvestorDataValidation {
	RestTemplate restTemplate = new RestTemplate();
	private static final Logger logger = LoggerFactory.getLogger(IEPF1AInvestorDataValidation.class);

	public IEPF1AFormData iepf1AFormData(String srnDetails) throws JsonProcessingException {

		logger.info("Getting form Data from the Onload Response for the SRN:::--" + srnDetails);
		IEPF1AFormData iepf1AFormData = new IEPF1AFormData();
		IEPF1AExcelData iepf1AExcelData = new IEPF1AExcelData();
		List<String> listOfDMSID = new ArrayList<String>();

		try {
			JSONObject jsonObject = null;
			JSONObject jsonObjectdata = null;
			JSONObject jsonObjectform = null;
			JSONArray jsonArray = null;
			JSONArray jsonArrayform = null;
			JSONObject funddatajson = null;
			JSONObject formattechmentjson = null;

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(IepfConstant.authorization, IepfConstant.bearer + IepfServiceImpl.getAcessToken());

			Map<String, String> elements = new HashMap<String, String>();
			elements.put("referenceNumber", "");
			elements.put("srn", srnDetails);

			Map<String, Map<String, String>> dataelement = new HashMap<String, Map<String, String>>();
			dataelement.put("requestBody", elements);

			ObjectMapper objectMapper = new ObjectMapper();
			String jsonBody = objectMapper.writeValueAsString(dataelement);
			HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
			ResponseEntity<String> response = restTemplate.exchange(IepfConstant.iepf1AUrl, HttpMethod.POST, request,
					String.class);
			String responseBody = response.getBody();
			jsonObject = new JSONObject(responseBody);
			String data = jsonObject.getString("data");
			jsonObjectdata = new JSONObject(data);
			String data1 = jsonObjectdata.getString("formData");
			jsonObjectform = new JSONObject(data1);
			jsonArray = jsonObjectform.getJSONArray("detailsOfTheAmountCreditedToTheFund");
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.length(); i++) {
					funddatajson = (JSONObject) jsonArray.get(i);
					if (funddatajson.getString("particulars")
							.equals("Amount in the unpaid dividend accounts of companies/banks")) {
						iepf1AFormData.setSumOfUnpaidAndUnclaimedDividend(funddatajson.getDouble("amountInRs"));
					} else if (funddatajson.getString("particulars").equals(
							"The application money received by companies/banks for allotment of any securities and due for refund")) {
						iepf1AFormData.setSumOfApplicationMoneyDueForRefund(funddatajson.getDouble("amountInRs"));
					} else if (funddatajson.getString("particulars").equals("Matured deposits with companies/banks")) {
						iepf1AFormData.setSumOfMaturedDeposits(funddatajson.getDouble("amountInRs"));
					} else if (funddatajson.getString("particulars")
							.equals("Matured debentures with companies/banks")) {
						iepf1AFormData.setSumOfMaturedDebentures(funddatajson.getDouble("amountInRs"));
					} else if (funddatajson.getString("particulars").equals("(i) Application money due for refund")) {
						iepf1AFormData.setSumOfInterestOnApplicationMoneyDueForRefund(funddatajson.getDouble("amountInRs"));
					} else if (funddatajson.getString("particulars")
							.equals("(ii) Matured deposits with companies/banks")) {
						iepf1AFormData.setSumOfInterestOnMaturedDeposits(funddatajson.getDouble("amountInRs"));
					} else if (funddatajson.getString("particulars")
							.equals("(iii) Matured debentures with companies/banks")) {
						iepf1AFormData.setSumOfInterestOnMaturedDebentures(funddatajson.getDouble("amountInRs"));
					} else if (funddatajson.getString("particulars").equals(
							"Sale proceeds of fractional shares arising out of issuance of bonus shares, merger and amalgamation")) {
						iepf1AFormData.setSalesProceedForFractionalShare(funddatajson.getDouble("amountInRs"));
					} else if (funddatajson.getString("particulars").equals("Redemption amount of preference shares")) {
						iepf1AFormData.setRedemptionAmountOfPreferenceShares(funddatajson.getDouble("amountInRs"));
					} else if (funddatajson.getString("particulars").equals("Grants and donation")) {
						iepf1AFormData.setGrantsAndDonations(funddatajson.getDouble("amountInRs"));

					} else if (funddatajson.getString("particulars").equals("Others")) {
						iepf1AFormData.setSumOfOtherInvestmentType(funddatajson.getDouble("amountInRs"));
					} else {
						System.out.println("Data not found");
					}

				}

				logger.info("***************************************************");
				logger.info("Displaying Aggeragate Values From IEPF Siebel form");
				logger.info("***************************************************");

				logger.info("+++++++++++++++++Amount in the unpaid dividend accounts of companies/banks:- "
						+ iepf1AFormData.getSumOfUnpaidAndUnclaimedDividend());
				logger.info("++++++++++++++++ The application money received by companies/banks:- "
						+ iepf1AFormData.getSumOfApplicationMoneyDueForRefund());
				logger.info("+++++++++++++++++Matured deposits with companies/banks:- "
						+ iepf1AFormData.getSumOfMaturedDeposits());
				logger.info("+++++++++++++++++Matured debentures with companies/banks:- "
						+ iepf1AFormData.getSumOfMaturedDebentures());
				logger.info("+++++++++++++++++Application money due for refund:- "
						+ iepf1AFormData.getSumOfInterestOnApplicationMoneyDueForRefund());
				logger.info("+++++++++++++++++Matured sum of Interest On Matured Deposits:- "
						+ iepf1AFormData.getSumOfInterestOnMaturedDeposits());
				logger.info("+++++++++++++++++Sum Of Interest On Matured Debentures:- "
						+ iepf1AFormData.getSumOfInterestOnMaturedDebentures());
				logger.info(
						"+++++++++++++++++Sale proceeds of fractional shares arising out of issuance of bonus shares:- "
								+ iepf1AFormData.getSalesProceedForFractionalShare());
				logger.info("+++++++++++++++++Redemption amount of preference shares:- "
						+ iepf1AFormData.getRedemptionAmountOfPreferenceShares());
				logger.info("+++++++++++++++++Grants and donation:- " + iepf1AFormData.getGrantsAndDonations());
				logger.info("+++++++++++++++++Others:- " + iepf1AFormData.getSumOfOtherInvestmentType());
			}

			jsonArrayform = jsonObjectform.getJSONArray("formAttachment");
			if (jsonArrayform != null) {
				for (int i = 0; i < jsonArrayform.length(); i++) {
					formattechmentjson = (JSONObject) jsonArrayform.get(i);
					if (formattechmentjson.getString("attachmentCategory").equals("Investor Info")) {
						String formAttachmentdata = formattechmentjson.getString("attachmentDMSId");
						listOfDMSID.add(formAttachmentdata);
					}
				}

				logger.info("***************************************************");
				logger.info("Found this DMSIDS for Investor info:::------" + listOfDMSID);
				logger.info("***************************************************");
				if (listOfDMSID != null && !listOfDMSID.isEmpty()) {
					iepf1AExcelData = getDocumentByDmsIdForIEPF1A(srnDetails, listOfDMSID);
					compareListsformAndexcelIEPF1A(srnDetails, iepf1AExcelData, iepf1AFormData);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return iepf1AFormData;
	}

	public IEPF1AExcelData getDocumentByDmsIdForIEPF1A(String srnNo, List<String> listdmsId)
			throws JsonMappingException, JsonProcessingException {
		logger.info("***************************************************");
		logger.info("Reading Value from  investor info Excel file");
		logger.info("***************************************************");
		IEPF1AExcelData iEPF1AExcelData = new IEPF1AExcelData();
		Map<String, String> inputData = new HashMap<String, String>();
		String targetSheetName = "Investor Details";
		List<CellCordinatesForIEPf> cellCoordinates = new ArrayList<>();
		
		cellCoordinates.add(new CellCordinatesForIEPf(3, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(5, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(7, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(9, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(11, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(13, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(3, 11));
		cellCoordinates.add(new CellCordinatesForIEPf(5, 11));
		cellCoordinates.add(new CellCordinatesForIEPf(7, 11));
		cellCoordinates.add(new CellCordinatesForIEPf(9, 11));
		cellCoordinates.add(new CellCordinatesForIEPf(11, 11));

		try {
		
			if (listdmsId != null) {
				for (String formAttachmentlist : listdmsId) {
				byte[] excelData =IepfServiceImpl.getExcelByte(formAttachmentlist);
				InputStream is = new ByteArrayInputStream(excelData);
				HSSFWorkbook workBook = new HSSFWorkbook(is);
				String sheetName = workBook.getSheetName(1);
				Sheet sheet = workBook.getSheet(targetSheetName);
				int rowCount = 0;
				if (sheet != null) {
					Iterator<Row> rowIterator = sheet.rowIterator();
					while (rowIterator.hasNext() && rowCount < 15) {

						Row row = rowIterator.next();

						int rowIdx = row.getRowNum();
						for (int i = 0; i < cellCoordinates.size(); i++) {
							int colIdx = cellCoordinates.get(i).getColumn();
							if (rowIdx == cellCoordinates.get(i).getRow()) {
								Cell cell = row.getCell(colIdx);
								if (cell != null && cell.getCellType() == CellType.NUMERIC) {
									if (rowIdx == 3 && colIdx == 4) {

										iEPF1AExcelData.setSumOfUnpaidAndUnclaimedDividend(
												iEPF1AExcelData.getSumOfUnpaidAndUnclaimedDividend()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 3 && colIdx == 11) {

										iEPF1AExcelData.setSumOfInterestOnMaturedDebentures(
												iEPF1AExcelData.getSumOfInterestOnMaturedDebentures()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 5 && colIdx == 4) {
										iEPF1AExcelData.setSumOfMaturedDeposits(
												iEPF1AExcelData.getSumOfMaturedDeposits() + cell.getNumericCellValue());
									}
									if (rowIdx == 5 && colIdx == 11) {
										iEPF1AExcelData.setSumOfInterestOnMaturedDeposits(
												iEPF1AExcelData.getSumOfInterestOnMaturedDeposits()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 7 && colIdx == 4) {
										iEPF1AExcelData
												.setSumOfMaturedDebentures(iEPF1AExcelData.getSumOfMaturedDebentures()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 7 && colIdx == 11) {

										iEPF1AExcelData.setSumOfInterestOnApplicationMoneyDueForRefund(
												iEPF1AExcelData.getSumOfInterestOnApplicationMoneyDueForRefund()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 9 && colIdx == 4) {
										iEPF1AExcelData.setSumOfApplicationMoneyDueForRefund(
												iEPF1AExcelData.getSumOfApplicationMoneyDueForRefund()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 9 && colIdx == 11) {

										iEPF1AExcelData.setRedemptionAmountOfPreferenceShares(
												iEPF1AExcelData.getRedemptionAmountOfPreferenceShares()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 11 && colIdx == 4) {

										iEPF1AExcelData.setSalesProceedForFractionalShare(
												iEPF1AExcelData.getSalesProceedForFractionalShare()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 11 && colIdx == 11) {

										iEPF1AExcelData.setSumOfOtherInvestmentType(
												iEPF1AExcelData.getSumOfOtherInvestmentType()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 13 && colIdx == 4) {
										iEPF1AExcelData.setGrantsAndDonations(
												iEPF1AExcelData.getGrantsAndDonations() + cell.getNumericCellValue());
									}
								} else {

									logger.error("Cell not found at [" + rowIdx + "," + colIdx + "]");

								}

							}

						}
						rowCount++;
					}

				} else {

					logger.error("Sheet not found.");
				}
			}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return iEPF1AExcelData;
	}

	public String compareListsformAndexcelIEPF1A(String srnNo, IEPF1AExcelData iepf1AExcelData,
			IEPF1AFormData iPF1AFormData) {

		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.info("Now Inside Validation Method");
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.info("FormData::::::--" + iPF1AFormData);
		logger.info("ExcelData::::---" + iepf1AExcelData);
		String validationSuccess = "N";
		if ((iepf1AExcelData.getSumOfUnpaidAndUnclaimedDividend() == iPF1AFormData.getSumOfUnpaidAndUnclaimedDividend())
				&& (iepf1AExcelData.getGrantsAndDonations() == iPF1AFormData.getGrantsAndDonations())
				&& (iepf1AExcelData.getRedemptionAmountOfPreferenceShares() == iPF1AFormData
						.getRedemptionAmountOfPreferenceShares())
				&& (iepf1AExcelData.getSalesProceedForFractionalShare() == iPF1AFormData
						.getSalesProceedForFractionalShare())
				&& (iepf1AExcelData.getSumOfApplicationMoneyDueForRefund() == iPF1AFormData
						.getSumOfApplicationMoneyDueForRefund())
				&& (iepf1AExcelData.getSumOfInterestOnApplicationMoneyDueForRefund() == iPF1AFormData
						.getSumOfInterestOnApplicationMoneyDueForRefund())
				&& (iepf1AExcelData.getSumOfInterestOnMaturedDebentures() == iPF1AFormData
						.getSumOfInterestOnMaturedDebentures())
				&& (iepf1AExcelData.getSumOfInterestOnMaturedDeposits() == iPF1AFormData
						.getSumOfInterestOnMaturedDeposits())
				&& (iepf1AExcelData.getSumOfMaturedDebentures() == iPF1AFormData.getSumOfMaturedDebentures())
				&& (iepf1AExcelData.getSumOfMaturedDeposits() == iPF1AFormData.getSumOfMaturedDeposits())
				&& (iepf1AExcelData.getSumOfOtherInvestmentType() == iPF1AFormData.getSumOfOtherInvestmentType())) {

			validationSuccess = "Y";
			logger.info("Pending for DSC Upload and Payment for IEBF1A");
			IepfServiceImpl.changeStatusAfterValidationServiceCall(validationSuccess,srnNo);

		} else {
			 IepfServiceImpl.changeStatusAfterValidationServiceCall(validationSuccess,srnNo);
			logger.info("Investor data fail");
		}
		return null;
	}
}
