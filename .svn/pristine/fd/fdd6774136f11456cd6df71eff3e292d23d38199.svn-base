package com.iepf.iepfApp.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import com.iepf.iepfApp.Constant.CellCordinatesForIEPf;
import com.iepf.iepfApp.Constant.IepfConstant;
import com.iepf.iepfApp.excelDataClass.IEPF1ExcelData;
import com.iepf.iepfApp.excelDataClass.IEPF7ExcelData;
import com.iepf.iepfApp.formDatabeanClass.IEPF1FormData;
import com.iepf.iepfApp.formDatabeanClass.IEPF7FormData;

/**
 * This class is used to get Form data from Siebel and Excel data from DMS for
 * IEPF1 and IEPF7 it is also comaparing Form Data and Excel Data
 * 
 * @author Satish5 kumar, Saumya Pandey LTIM
 *
 */
public class IEPF1InvestorDataValidation {
	RestTemplate restTemplate = new RestTemplate();
	private static final Logger logger = LoggerFactory.getLogger(IEPF1InvestorDataValidation.class);
	public IEPF1FormData getIEPF1FormData(String srnDetails) throws JsonProcessingException {

		logger.info("Getting form Data from the Onload Response for the SRN:::--" + srnDetails);
		IEPF1FormData iepf1FormData = new IEPF1FormData();
		IEPF1ExcelData iepf1ExcelData = new IEPF1ExcelData();
		IEPF7FormData iepf7FormData = new IEPF7FormData();
		IEPF7ExcelData iepf7ExcelData = new IEPF7ExcelData();
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
			String jsonBody = objectMapper.writeValueAsString(dataelement); //
			HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); //
			ResponseEntity<String> response = restTemplate.exchange(IepfConstant.iepf1And7Url, HttpMethod.POST, request,
					String.class);
			String responseBody = response.getBody();
			jsonObject = new JSONObject(responseBody);
			String data = jsonObject.getString("data");
			jsonObjectdata = new JSONObject(data);
			String data1 = jsonObjectdata.getString("formData");
			jsonObjectform = new JSONObject(data1);
			if (jsonObjectform.getString("purposeOfFilling").equals("Statement of amounts credited to IEPF")) {
				jsonArray = jsonObjectform.getJSONArray("detailsOfUnclaimedAndUnpaid");
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						funddatajson = (JSONObject) jsonArray.get(i);
						if (funddatajson.getString("perticulars")
								.equals("Amount in the unpaid dividend accounts of companies/banks")) {
							iepf1FormData.setSumOfUnpaidAndUnclaimedDividend(funddatajson.getDouble("amountInRs"));

						} else if (funddatajson.getString("perticulars").equals(
								"The application money received by companies/banks for allotment of any securities and due for refund")) {
							iepf1FormData.setSumOfApplicationMoneyDueForRefund(funddatajson.getDouble("amountInRs"));
						} else if (funddatajson.getString("perticulars")
								.equals("Matured deposit with companies/banks")) {
							iepf1FormData.setSumOfMaturedDeposits(funddatajson.getDouble("amountInRs"));
						} else if (funddatajson.getString("perticulars")
								.equals("Matured debentures with companies/banks")) {
							iepf1FormData.setSumOfMaturedDebentures(funddatajson.getDouble("amountInRs"));
						} else if (funddatajson.getString("perticulars").equals("Application money due for refund")) {
							iepf1FormData.setSumOfInterestOnApplicationMoneyDueForRefund(
									funddatajson.getDouble("amountInRs"));
						} else if (funddatajson.getString("perticulars")
								.equals("(ii) Matured deposit with companies/banks")) {
							iepf1FormData.setSumOfInterestOnMaturedDeposits(funddatajson.getDouble("amountInRs"));
						} else if (funddatajson.getString("perticulars")
								.equals("(iii) Matured debentures with companies/banks")) {
							iepf1FormData.setSumOfInterestOnMaturedDebentures(funddatajson.getDouble("amountInRs"));
						} else if (funddatajson.getString("perticulars").equals(
								"Sale proceeds of fractional shares arising out of issuance of bonus shares, merger and amalgamation")) {
							iepf1FormData.setSalesProceedForFractionalShare(funddatajson.getDouble("amountInRs"));
						} else if (funddatajson.getString("perticulars")
								.equals("Redemption amount of preference shares")) {
							iepf1FormData.setRedemptionAmountOfPreferenceShares(funddatajson.getDouble("amountInRs"));
						} else if (funddatajson.getString("perticulars").equals(
								"Surplus amount after payment of dues for Section 8 company before its conversion into any other kind")) {
							iepf1FormData.setSurplusAmountAfterPaymentDues(funddatajson.getDouble("amountInRs"));
						} else if (funddatajson.getString("perticulars").equals(
								"Amount received through disposal of securities under section 38(4) of The Companies Act 2013")) {
							iepf1FormData
									.setAmountReceivedThroughDisposalOfsecurities(funddatajson.getDouble("amountInRs"));
						} else if (funddatajson.getString("perticulars").equals("Grants and donation")) {
							iepf1FormData.setGrantsAndDonations(funddatajson.getDouble("amountInRs"));
						} else if (funddatajson.getString("perticulars").equals("Others")) {
							iepf1FormData.setSumOfOtherInvestmentType(funddatajson.getDouble("amountInRs"));
						}

					}

					logger.info("***************************************************");
					logger.info("Displaying Aggeragate Values From IEPF Siebel form");
					logger.info("***************************************************");

					logger.info(
							"####################################Amount in the unpaid dividend accounts of companies/banks:- "
									+ iepf1FormData.getSumOfUnpaidAndUnclaimedDividend());
					logger.info(
							"####################################The application money received by companies/banks:- "
									+ iepf1FormData.getSumOfApplicationMoneyDueForRefund());
					logger.info("####################################Matured deposits with companies/banks:- "
							+ iepf1FormData.getSumOfMaturedDeposits());
					logger.info("####################################Matured debentures with companies/banks:- "
							+ iepf1FormData.getSumOfMaturedDebentures());
					logger.info("####################################Application money due for refund:- "
							+ iepf1FormData.getSumOfApplicationMoneyDueForRefund());
					logger.info(
							"####################################Matured sum of Interest On Matured Deposits:- "
									+ iepf1FormData.getSumOfInterestOnMaturedDeposits());
					logger.info("####################################Sum Of Interest On Matured Debentures:- "
							+ iepf1FormData.getSumOfInterestOnMaturedDebentures());
					logger.info(
							"####################################Sale proceeds of fractional shares arising out of issuance of bonus shares:- "
									+ iepf1FormData.getSalesProceedForFractionalShare());
					logger.info("####################################Redemption amount of preference shares:- "
							+ iepf1FormData.getRedemptionAmountOfPreferenceShares());
					logger.info(
							"####################################Surplus amount after payment of dues for Section 8 company before its conversion into any other kind:"
									+ iepf1FormData.getSurplusAmountAfterPaymentDues());
					logger.info(
							"####################################Amount received through disposal of securities under section 38(4) of The Companies Act 2013:- "
									+ iepf1FormData.getAmountReceivedThroughDisposalOfsecurities());
					logger.info("####################################Grants and donation:- "
							+ iepf1FormData.getGrantsAndDonations());
					logger.info("####################################Others:- "
							+ iepf1FormData.getSumOfOtherInvestmentType());

				}
				jsonArrayform = jsonObjectform.getJSONArray("formAttachment");
				if (jsonArrayform != null) {
					for (int i = 0; i < jsonArrayform.length(); i++) {
						formattechmentjson = (JSONObject) jsonArrayform.get(i);
						if (formattechmentjson.getString("attachmentCategory").equals("Investor Info")) {

							listOfDMSID.add(formattechmentjson.getString("attachmentDMSId"));

						}
					}

					logger.info("listOFDMSIDformData:::::::::::::::::::" + listOfDMSID);
					if (listOfDMSID != null && !listOfDMSID.isEmpty()) {
						iepf1ExcelData = getExcelDataForIEPF1(srnDetails, listOfDMSID);
						validateFormAndExcelIEPF1(srnDetails, iepf1ExcelData, iepf1FormData);
					}
				}
			} else if (jsonObjectform.getString("purposeOfFilling")
					.equals("Statement of transfer of amounts on account of shares transferred to the fund")) {

				jsonArray = jsonObjectform.getJSONArray("detailsOfUnclaimedAndUnpaid");
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						funddatajson = (JSONObject) jsonArray.get(i);
						if (funddatajson.getString("perticulars").equals("Dividend on shares transferred to IEPF")) {
							iepf7FormData
									.setSumOfDividendOnSharesTransferredToIEPF(funddatajson.getDouble("amountInRs"));

						} else if (funddatajson.getString("perticulars").equals(
								"Proceeds realized on delisting of companies/banks with respect to shares transferred to IEPF")) {
							iepf7FormData
									.setProceedsRealizedOnDelistingOfCompanies(funddatajson.getDouble("amountInRs"));

						} else if (funddatajson.getString("perticulars").equals(
								"Proceeds realized on winding up of companies/banks with respect to shares transferred to IEPF")) {
							iepf7FormData
									.setProceedsRealizedOnWindingUpOfCompanies(funddatajson.getDouble("amountInRs"));

						} else if (funddatajson.getString("perticulars")
								.equals("Surrender of shares under Section 236 of Companies, Act 2013")) {
							iepf7FormData.setSurrenderOfSharesUnderSection236(funddatajson.getDouble("amountInRs"));

						} else if (funddatajson.getString("perticulars").equals("Others")) {
							iepf7FormData.setSumOfOtherInvestmentTypes(funddatajson.getDouble("amountInRs"));

						}

					}

					logger.info("***************************************************");
					logger.info("Displaying Aggeragate Values From IEPF Siebel form");
					logger.info("***************************************************");
					logger.info("**********************************Dividend on shares transferred to IEPF:- "
							+ iepf7FormData.getSumOfDividendOnSharesTransferredToIEPF());
					logger.info(
							"**********************************Proceeds realized on delisting of companies/banks with respect to shares transferred to IEPF:- "
									+ iepf7FormData.getProceedsRealizedOnDelistingOfCompanies());
					logger.info(
							"**********************************Proceeds realized on winding up of companies/banks with respect to shares transferred to IEPF:- "
									+ iepf7FormData.getProceedsRealizedOnWindingUpOfCompanies());
					logger.info(
							"**********************************Surrender of shares under Section 236 of Companies, Act 2013:- "
									+ iepf7FormData.getSurrenderOfSharesUnderSection236());
					logger.info("**********************************Others- "
							+ iepf7FormData.getSumOfOtherInvestmentTypes());

				}
				jsonArrayform = jsonObjectform.getJSONArray("formAttachment");
				if (jsonArrayform != null) {

					for (int i = 0; i < jsonArrayform.length(); i++) {
						formattechmentjson = (JSONObject) jsonArrayform.get(i);
						if (formattechmentjson.getString("attachmentCategory").equals("Investor Info")) { // Investor
																											// Info

							listOfDMSID.add(formattechmentjson.getString("attachmentDMSId"));

						}
					}

					logger.info("***************************************************");
					logger.info("Found this DMSIDS for Investor info:::------" + listOfDMSID);
					logger.info("***************************************************");
					if (listOfDMSID != null && !listOfDMSID.isEmpty()) {
						iepf7ExcelData = getExcelDataForIEPF7(srnDetails, listOfDMSID);
						validateFormAndExcelIEPF7(srnDetails, iepf7ExcelData, iepf7FormData);
					}
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	// Read IEPF1 Excel Data Response
	public IEPF1ExcelData getExcelDataForIEPF1(String srn, List<String> dmsId)
			throws JsonMappingException, JsonProcessingException {

		logger.info("***************************************************");
		logger.info("Reading Value from  investor info Excel file");
		logger.info("***************************************************");
		String targetSheetName = "Investor Details";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		IEPF1ExcelData iEPF1ExcelData = new IEPF1ExcelData();
		List<CellCordinatesForIEPf> cellCoordinates = new ArrayList<>();
		cellCoordinates.add(new CellCordinatesForIEPf(3, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(3, 11));
		cellCoordinates.add(new CellCordinatesForIEPf(5, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(5, 11));
		cellCoordinates.add(new CellCordinatesForIEPf(7, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(7, 11));
		cellCoordinates.add(new CellCordinatesForIEPf(9, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(9, 11));
		cellCoordinates.add(new CellCordinatesForIEPf(11, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(11, 11));
		cellCoordinates.add(new CellCordinatesForIEPf(13, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(13, 11));
		cellCoordinates.add(new CellCordinatesForIEPf(15, 4));

		if(dmsId!=null)
		{
		for (String id:dmsId) {

			byte[] excelData = IepfServiceImpl.getExcelByte(id);

			InputStream is = new ByteArrayInputStream(excelData);

			try {
				HSSFWorkbook workBook = new HSSFWorkbook(is);

				Sheet sheet = workBook.getSheet(targetSheetName);
				int rowCount = 0;
				if (sheet != null) {
					Iterator<Row> rowIterator = sheet.rowIterator();
					while (rowIterator.hasNext() && rowCount < 17) {
						Row row = rowIterator.next();

						int rowIdx = row.getRowNum();
						for (int i = 0; i < cellCoordinates.size(); i++) {
							int colIdx = cellCoordinates.get(i).getColumn();
							if (rowIdx == cellCoordinates.get(i).getRow()) {
								Cell cell = row.getCell(colIdx);
								if (cell != null && cell.getCellType() == CellType.NUMERIC) {
									if (rowIdx == 3 && colIdx == 4) {

										iEPF1ExcelData.setSumOfUnpaidAndUnclaimedDividend(
												iEPF1ExcelData.getSumOfUnpaidAndUnclaimedDividend()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 3 && colIdx == 11) {
										iEPF1ExcelData.setSumOfInterestOnMaturedDebentures(
												iEPF1ExcelData.getSumOfInterestOnMaturedDebentures()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 5 && colIdx == 4) {
										iEPF1ExcelData.setSumOfMaturedDeposits(
												iEPF1ExcelData.getSumOfMaturedDeposits() + cell.getNumericCellValue());
									}
									if (rowIdx == 5 && colIdx == 11) {

										iEPF1ExcelData.setSumOfInterestOnMaturedDeposits(
												iEPF1ExcelData.getSumOfInterestOnMaturedDeposits()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 7 && colIdx == 4) {

										iEPF1ExcelData
												.setSumOfMaturedDebentures(iEPF1ExcelData.getSumOfMaturedDebentures()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 7 && colIdx == 11) {

										iEPF1ExcelData.setSumOfInterestOnApplicationMoneyDueForRefund(
												iEPF1ExcelData.getSumOfInterestOnApplicationMoneyDueForRefund()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 9 && colIdx == 4) {

										iEPF1ExcelData.setSumOfApplicationMoneyDueForRefund(
												iEPF1ExcelData.getSumOfApplicationMoneyDueForRefund()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 9 && colIdx == 11) {

										iEPF1ExcelData.setRedemptionAmountOfPreferenceShares(
												iEPF1ExcelData.getRedemptionAmountOfPreferenceShares()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 11 && colIdx == 4) {

										iEPF1ExcelData.setSalesProceedForFractionalShare(
												iEPF1ExcelData.getSalesProceedForFractionalShare()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 11 && colIdx == 11) {

										iEPF1ExcelData.setAmountReceivedThroughDisposalOfsecurities(
												iEPF1ExcelData.getAmountReceivedThroughDisposalOfsecurities()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 13 && colIdx == 4) {

										iEPF1ExcelData.setSurplusAmountAfterPaymentDues(
												iEPF1ExcelData.getSurplusAmountAfterPaymentDues()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 13 && colIdx == 11) {

										iEPF1ExcelData.setSumOfOtherInvestmentType(
												iEPF1ExcelData.getSumOfOtherInvestmentType()
														+ cell.getNumericCellValue());
									}
									if (rowIdx == 15 && colIdx == 4) {
										logger.info("Cell Value::--" + cell.getNumericCellValue());
										iEPF1ExcelData.setGrantsAndDonations(
												iEPF1ExcelData.getGrantsAndDonations() + cell.getNumericCellValue());
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

				workBook.close();
			} catch (NumberFormatException e1) {
				return null;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		}
		logger.info("Total Sum of Excel Data --::" + iEPF1ExcelData);

		return iEPF1ExcelData;
	}

	// For comprision with FormData and excel Data
	public void validateFormAndExcelIEPF1(String srnNo, IEPF1ExcelData iEPF1ExcelData, IEPF1FormData IEPF1FormData) {

		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.info("Now Inside Validation Method");
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.info("FormData::::::--" + IEPF1FormData);
		logger.info("ExcelData:::::---" + iEPF1ExcelData);

		String validationSuccess = "N";
		if (iEPF1ExcelData.getSumOfUnpaidAndUnclaimedDividend() == IEPF1FormData.getSumOfUnpaidAndUnclaimedDividend()
				&& iEPF1ExcelData.getGrantsAndDonations() == IEPF1FormData.getGrantsAndDonations()
				&& iEPF1ExcelData.getRedemptionAmountOfPreferenceShares() == IEPF1FormData
						.getRedemptionAmountOfPreferenceShares()
				&& iEPF1ExcelData.getSumOfApplicationMoneyDueForRefund() == IEPF1FormData
						.getSumOfApplicationMoneyDueForRefund()
				&& iEPF1ExcelData.getSalesProceedForFractionalShare() == IEPF1FormData
						.getSalesProceedForFractionalShare()
				&& iEPF1ExcelData.getSumOfInterestOnApplicationMoneyDueForRefund() == IEPF1FormData
						.getSumOfInterestOnApplicationMoneyDueForRefund()
				&& iEPF1ExcelData.getSumOfInterestOnMaturedDebentures() == IEPF1FormData
						.getSumOfInterestOnMaturedDebentures()
				&& iEPF1ExcelData.getSumOfInterestOnMaturedDeposits() == IEPF1FormData
						.getSumOfInterestOnMaturedDeposits()
				&& iEPF1ExcelData.getSumOfMaturedDebentures() == IEPF1FormData.getSumOfMaturedDebentures()
				&& iEPF1ExcelData.getSumOfMaturedDeposits() == IEPF1FormData.getSumOfMaturedDeposits()
				&& iEPF1ExcelData.getSurplusAmountAfterPaymentDues() == IEPF1FormData.getSurplusAmountAfterPaymentDues()
				&& iEPF1ExcelData.getSumOfOtherInvestmentType() == IEPF1FormData.getSumOfOtherInvestmentType()
				&& iEPF1ExcelData.getAmountReceivedThroughDisposalOfsecurities() == IEPF1FormData
						.getAmountReceivedThroughDisposalOfsecurities()) {

			validationSuccess = "Y";
			IepfServiceImpl.changeStatusAfterValidationServiceCall(validationSuccess,srnNo);
			logger.info("Pending for DSC Upload");

		} else {
		IepfServiceImpl.changeStatusAfterValidationServiceCall(validationSuccess,srnNo);
			logger.info("Investor data failed.");
		}

	}

	// IEPF7 for read Excel form data
	public IEPF7ExcelData getExcelDataForIEPF7(String srn, List<String> dmsId)
			throws JsonMappingException, JsonProcessingException {

		logger.info("--------------------------------Reading IEPF7 Excel----------------------------------");
		String targetSheetName = "Investor Details";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		IEPF7ExcelData iepf7ExcelData = new IEPF7ExcelData();
		List<CellCordinatesForIEPf> cellCoordinates = new ArrayList<>();
		cellCoordinates.add(new CellCordinatesForIEPf(3, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(5, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(7, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(9, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(11, 4));

		if (dmsId != null) {
			for (String id:dmsId) {
				byte[] excelData = IepfServiceImpl.getExcelByte(id);

				InputStream is = new ByteArrayInputStream(excelData);
				try {
					HSSFWorkbook workBook = new HSSFWorkbook(is);
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
											iepf7ExcelData.setSumOfDividendOnSharesTransferredToIEPF(
													iepf7ExcelData.getSumOfDividendOnSharesTransferredToIEPF()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 5 && colIdx == 4) {

											iepf7ExcelData.setProceedsRealizedOnDelistingOfCompanies(
													iepf7ExcelData.getProceedsRealizedOnDelistingOfCompanies()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 7 && colIdx == 4) {

											iepf7ExcelData.setProceedsRealizedOnWindingUpOfCompanies(
													iepf7ExcelData.getProceedsRealizedOnWindingUpOfCompanies()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 9 && colIdx == 4) {
											iepf7ExcelData.setSurrenderOfSharesUnderSection236(
													iepf7ExcelData.getSurrenderOfSharesUnderSection236()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 11 && colIdx == 4) {

											iepf7ExcelData.setSumOfOtherInvestmentTypes(
													iepf7ExcelData.getSumOfOtherInvestmentTypes()
															+ cell.getNumericCellValue());
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

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		logger.info("Total Sum of Excel Data--::" + iepf7ExcelData);

		return iepf7ExcelData;
	}

	// Compare Form and Excel file For IEPF7
	public String validateFormAndExcelIEPF7(String srnNo, IEPF7ExcelData iepf7ExcelData, IEPF7FormData iepf7FormData) {

		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.info("Now Inside Validation Method");
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		logger.info("FormData::::::---" + iepf7FormData);
		logger.info("ExcelData:::::---" + iepf7ExcelData);

		String validationSuccess = "N";
		if (iepf7FormData.getProceedsRealizedOnDelistingOfCompanies() == iepf7ExcelData
				.getProceedsRealizedOnDelistingOfCompanies()
				&& iepf7FormData.getProceedsRealizedOnWindingUpOfCompanies() == iepf7ExcelData
						.getProceedsRealizedOnWindingUpOfCompanies()
				&& iepf7FormData.getSumOfDividendOnSharesTransferredToIEPF() == iepf7ExcelData
						.getSumOfDividendOnSharesTransferredToIEPF()

				&& iepf7FormData.getSumOfOtherInvestmentTypes() == iepf7ExcelData.getSumOfOtherInvestmentTypes()

				&& iepf7FormData.getSurrenderOfSharesUnderSection236() == iepf7ExcelData
						.getSurrenderOfSharesUnderSection236()) {

			validationSuccess = "Y";
			logger.info("Pending for DSC Upload an Payment");
		 IepfServiceImpl.changeStatusAfterValidationServiceCall(validationSuccess,srnNo);

		} else {
			logger.info("Investor data failed.");
		 IepfServiceImpl.changeStatusAfterValidationServiceCall(validationSuccess,srnNo);
		}
		return null;
	}
}
