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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import com.iepf.iepfApp.excelDataClass.IEPF2ExcelData;
import com.iepf.iepfApp.formDatabeanClass.IEPF2FormData;

public class IEPF2InvestorDataValidation {

	RestTemplate restTemplate = new RestTemplate();
	IepfServiceImpl iepfServiceImpl = new IepfServiceImpl();

	public Map<String, IEPF2FormData> iepf2FormData(String srnDetails) throws JsonProcessingException {

		System.out.println("IEPF2:::" + srnDetails);

		Map<String, IEPF2FormData> iepf2formlistData = new HashMap<String, IEPF2FormData>();

		List<String> listOfDMSID = new ArrayList<String>();
		Map<String, IEPF2ExcelData> mapData = new HashMap<String, IEPF2ExcelData>();

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
			elements.put("srNumber", srnDetails);

			Map<String, Map<String, String>> dataelement = new HashMap<String, Map<String, String>>();
			dataelement.put("requestBody", elements);

			ObjectMapper objectMapper = new ObjectMapper();
			String jsonBody = objectMapper.writeValueAsString(dataelement);
			System.out.println("json-----" + jsonBody);

			HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
			System.out.println("request::" + request);

			ResponseEntity<String> response = restTemplate.exchange(IepfConstant.iepf2Url, HttpMethod.POST, request,
					String.class);
			String responseBody = response.getBody();
			System.out.println(responseBody);

			jsonObject = new JSONObject(responseBody);
			System.out.println("jsonObject:::" + jsonObject);

			String data = jsonObject.getString("data");
			jsonObjectdata = new JSONObject(data);
			System.out.println("jsonObjectdata:::" + jsonObjectdata);
			String data1 = jsonObjectdata.getString("formData");
			jsonObjectform = new JSONObject(data1);
			jsonArray = jsonObjectform.getJSONArray("detailsOfUnclaimedAndUnpaid");
			System.out.println("jsonArray::" + jsonArray);
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.length(); i++) {
					funddatajson = (JSONObject) jsonArray.get(i);
					if (i == 0) {
						IEPF2FormData iEPF2FormDataFY1 = new IEPF2FormData();
						iEPF2FormDataFY1.setSumOfUnpaidAndUnclaimedDividend(
								funddatajson.getDouble("unclaimedAndUnpaidDividend"));
						iEPF2FormDataFY1
								.setNumberOfUnderlyingShares(funddatajson.getDouble("numberOfUnderlyingShares"));
						iEPF2FormDataFY1.setSumOfAmountRefundedfromUnpaidDividendAccount(
								funddatajson.getDouble("amountRefundedByCompany"));
						iEPF2FormDataFY1.setSumOfAmountReceivedUnderSubSection(
								funddatajson.getDouble("amountRecievedUnderSec38"));
						iEPF2FormDataFY1.setSumOfApplicationMoneyDueRefund(
								funddatajson.getDouble("appMoneyRecievedAndDueForRefund"));
						iEPF2FormDataFY1.setSumOfMaturedDeposits(funddatajson.getDouble("amountOfMaturedDeposit"));
						iEPF2FormDataFY1.setSumOfAmountRefundedFromMaturedDeposits(
								funddatajson.getDouble("refundByCompFromMetureDeposit"));
						iEPF2FormDataFY1.setSumOfMaturedDebentures(funddatajson.getDouble("amountOfMaturedDebentures"));
						iEPF2FormDataFY1.setSumOfAmountRefundedFromMaturedDebentures(
								funddatajson.getDouble("refundByCompFromMetureDebentures"));
						iEPF2FormDataFY1.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
								funddatajson.getDouble("appMoneyDueForRefund"));
						iEPF2FormDataFY1.setSumOfInterestAccruedOnMaturedDeposits(
								funddatajson.getDouble("maturedDepositWithCompaney"));
						iEPF2FormDataFY1.setSumOfInterestAccruedOnMaturedDebentures(
								funddatajson.getDouble("maturedDebenturesWithCompaney"));
						iEPF2FormDataFY1.setSalesProceedForFractionalShare(funddatajson.getDouble("fractionalShares"));
						iEPF2FormDataFY1
								.setRedemptionAmountOfPreferenceShare(funddatajson.getDouble("redemptionAmount"));
						iEPF2FormDataFY1.setSumOfOtherInvestmentType(funddatajson.getDouble("others"));
						iepf2formlistData.put("FY-1", iEPF2FormDataFY1);

					} else if (i == 1) {
						IEPF2FormData iEPF2FormDataFY2 = new IEPF2FormData();
						iEPF2FormDataFY2.setSumOfUnpaidAndUnclaimedDividend(
								funddatajson.getDouble("unclaimedAndUnpaidDividend"));
						iEPF2FormDataFY2
								.setNumberOfUnderlyingShares(funddatajson.getDouble("numberOfUnderlyingShares"));
						iEPF2FormDataFY2.setSumOfAmountRefundedfromUnpaidDividendAccount(
								funddatajson.getDouble("amountRefundedByCompany"));
						iEPF2FormDataFY2.setSumOfAmountReceivedUnderSubSection(
								funddatajson.getDouble("amountRecievedUnderSec38"));
						iEPF2FormDataFY2.setSumOfApplicationMoneyDueRefund(
								funddatajson.getDouble("appMoneyRecievedAndDueForRefund"));
						iEPF2FormDataFY2.setSumOfMaturedDeposits(funddatajson.getDouble("amountOfMaturedDeposit"));
						iEPF2FormDataFY2.setSumOfAmountRefundedFromMaturedDeposits(
								funddatajson.getDouble("refundByCompFromMetureDeposit"));
						iEPF2FormDataFY2.setSumOfMaturedDebentures(funddatajson.getDouble("amountOfMaturedDebentures"));
						iEPF2FormDataFY2.setSumOfAmountRefundedFromMaturedDebentures(
								funddatajson.getDouble("refundByCompFromMetureDebentures"));
						iEPF2FormDataFY2.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
								funddatajson.getDouble("appMoneyDueForRefund"));
						iEPF2FormDataFY2.setSumOfInterestAccruedOnMaturedDeposits(
								funddatajson.getDouble("maturedDepositWithCompaney"));
						iEPF2FormDataFY2.setSumOfInterestAccruedOnMaturedDebentures(
								funddatajson.getDouble("maturedDebenturesWithCompaney"));
						iEPF2FormDataFY2.setSalesProceedForFractionalShare(funddatajson.getDouble("fractionalShares"));
						iEPF2FormDataFY2
								.setRedemptionAmountOfPreferenceShare(funddatajson.getDouble("redemptionAmount"));
						iEPF2FormDataFY2.setSumOfOtherInvestmentType(funddatajson.getDouble("others"));

						iepf2formlistData.put("FY-2", iEPF2FormDataFY2);

					} else if (i == 2) {
						IEPF2FormData iEPF2FormDataFY3 = new IEPF2FormData();
						iEPF2FormDataFY3.setSumOfUnpaidAndUnclaimedDividend(
								funddatajson.getDouble("unclaimedAndUnpaidDividend"));
						iEPF2FormDataFY3
								.setNumberOfUnderlyingShares(funddatajson.getDouble("numberOfUnderlyingShares"));
						iEPF2FormDataFY3.setSumOfAmountRefundedfromUnpaidDividendAccount(
								funddatajson.getDouble("amountRefundedByCompany"));
						iEPF2FormDataFY3.setSumOfAmountReceivedUnderSubSection(
								funddatajson.getDouble("amountRecievedUnderSec38"));
						iEPF2FormDataFY3.setSumOfApplicationMoneyDueRefund(
								funddatajson.getDouble("appMoneyRecievedAndDueForRefund"));
						iEPF2FormDataFY3.setSumOfMaturedDeposits(funddatajson.getDouble("amountOfMaturedDeposit"));
						iEPF2FormDataFY3.setSumOfAmountRefundedFromMaturedDeposits(
								funddatajson.getDouble("refundByCompFromMetureDeposit"));
						iEPF2FormDataFY3.setSumOfMaturedDebentures(funddatajson.getDouble("amountOfMaturedDebentures"));
						iEPF2FormDataFY3.setSumOfAmountRefundedFromMaturedDebentures(
								funddatajson.getDouble("refundByCompFromMetureDebentures"));
						iEPF2FormDataFY3.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
								funddatajson.getDouble("appMoneyDueForRefund"));
						iEPF2FormDataFY3.setSumOfInterestAccruedOnMaturedDeposits(
								funddatajson.getDouble("maturedDepositWithCompaney"));
						iEPF2FormDataFY3.setSumOfInterestAccruedOnMaturedDebentures(
								funddatajson.getDouble("maturedDebenturesWithCompaney"));
						iEPF2FormDataFY3.setSalesProceedForFractionalShare(funddatajson.getDouble("fractionalShares"));
						iEPF2FormDataFY3
								.setRedemptionAmountOfPreferenceShare(funddatajson.getDouble("redemptionAmount"));
						iEPF2FormDataFY3.setSumOfOtherInvestmentType(funddatajson.getDouble("others"));
						iepf2formlistData.put("FY-3", iEPF2FormDataFY3);

					} else if (i == 3) {
						IEPF2FormData iEPF2FormDataFY4 = new IEPF2FormData();
						iEPF2FormDataFY4.setSumOfUnpaidAndUnclaimedDividend(
								funddatajson.getDouble("unclaimedAndUnpaidDividend"));
						iEPF2FormDataFY4
								.setNumberOfUnderlyingShares(funddatajson.getDouble("numberOfUnderlyingShares"));
						iEPF2FormDataFY4.setSumOfAmountRefundedfromUnpaidDividendAccount(
								funddatajson.getDouble("amountRefundedByCompany"));
						iEPF2FormDataFY4.setSumOfAmountReceivedUnderSubSection(
								funddatajson.getDouble("amountRecievedUnderSec38"));
						iEPF2FormDataFY4.setSumOfApplicationMoneyDueRefund(
								funddatajson.getDouble("appMoneyRecievedAndDueForRefund"));
						iEPF2FormDataFY4.setSumOfMaturedDeposits(funddatajson.getDouble("amountOfMaturedDeposit"));
						iEPF2FormDataFY4.setSumOfAmountRefundedFromMaturedDeposits(
								funddatajson.getDouble("refundByCompFromMetureDeposit"));
						iEPF2FormDataFY4.setSumOfMaturedDebentures(funddatajson.getDouble("amountOfMaturedDebentures"));
						iEPF2FormDataFY4.setSumOfAmountRefundedFromMaturedDebentures(
								funddatajson.getDouble("refundByCompFromMetureDebentures"));
						iEPF2FormDataFY4.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
								funddatajson.getDouble("appMoneyDueForRefund"));
						iEPF2FormDataFY4.setSumOfInterestAccruedOnMaturedDeposits(
								funddatajson.getDouble("maturedDepositWithCompaney"));
						iEPF2FormDataFY4.setSumOfInterestAccruedOnMaturedDebentures(
								funddatajson.getDouble("maturedDebenturesWithCompaney"));
						iEPF2FormDataFY4.setSalesProceedForFractionalShare(funddatajson.getDouble("fractionalShares"));
						iEPF2FormDataFY4
								.setRedemptionAmountOfPreferenceShare(funddatajson.getDouble("redemptionAmount"));
						iEPF2FormDataFY4.setSumOfOtherInvestmentType(funddatajson.getDouble("others"));

						iepf2formlistData.put("FY-4", iEPF2FormDataFY4);

					} else if (i == 4) {
						IEPF2FormData iEPF2FormDataFY5 = new IEPF2FormData();
						iEPF2FormDataFY5.setSumOfUnpaidAndUnclaimedDividend(
								funddatajson.getDouble("unclaimedAndUnpaidDividend"));
						iEPF2FormDataFY5
								.setNumberOfUnderlyingShares(funddatajson.getDouble("numberOfUnderlyingShares"));
						iEPF2FormDataFY5.setSumOfAmountRefundedfromUnpaidDividendAccount(
								funddatajson.getDouble("amountRefundedByCompany"));
						iEPF2FormDataFY5.setSumOfAmountReceivedUnderSubSection(
								funddatajson.getDouble("amountRecievedUnderSec38"));
						iEPF2FormDataFY5.setSumOfApplicationMoneyDueRefund(
								funddatajson.getDouble("appMoneyRecievedAndDueForRefund"));
						iEPF2FormDataFY5.setSumOfMaturedDeposits(funddatajson.getDouble("amountOfMaturedDeposit"));
						iEPF2FormDataFY5.setSumOfAmountRefundedFromMaturedDeposits(
								funddatajson.getDouble("refundByCompFromMetureDeposit"));
						iEPF2FormDataFY5.setSumOfMaturedDebentures(funddatajson.getDouble("amountOfMaturedDebentures"));
						iEPF2FormDataFY5.setSumOfAmountRefundedFromMaturedDebentures(
								funddatajson.getDouble("refundByCompFromMetureDebentures"));
						iEPF2FormDataFY5.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
								funddatajson.getDouble("appMoneyDueForRefund"));
						iEPF2FormDataFY5.setSumOfInterestAccruedOnMaturedDeposits(
								funddatajson.getDouble("maturedDepositWithCompaney"));
						iEPF2FormDataFY5.setSumOfInterestAccruedOnMaturedDebentures(
								funddatajson.getDouble("maturedDebenturesWithCompaney"));
						iEPF2FormDataFY5.setSalesProceedForFractionalShare(funddatajson.getDouble("fractionalShares"));
						iEPF2FormDataFY5
								.setRedemptionAmountOfPreferenceShare(funddatajson.getDouble("redemptionAmount"));
						iEPF2FormDataFY5.setSumOfOtherInvestmentType(funddatajson.getDouble("others"));

						iepf2formlistData.put("FY-5", iEPF2FormDataFY5);

					} else if (i == 5) {
						IEPF2FormData iEPF2FormDataFY6 = new IEPF2FormData();
						iEPF2FormDataFY6.setSumOfUnpaidAndUnclaimedDividend(
								funddatajson.getDouble("unclaimedAndUnpaidDividend"));
						iEPF2FormDataFY6
								.setNumberOfUnderlyingShares(funddatajson.getDouble("numberOfUnderlyingShares"));
						iEPF2FormDataFY6.setSumOfAmountRefundedfromUnpaidDividendAccount(
								funddatajson.getDouble("amountRefundedByCompany"));
						iEPF2FormDataFY6.setSumOfAmountReceivedUnderSubSection(
								funddatajson.getDouble("amountRecievedUnderSec38"));
						iEPF2FormDataFY6.setSumOfApplicationMoneyDueRefund(
								funddatajson.getDouble("appMoneyRecievedAndDueForRefund"));
						iEPF2FormDataFY6.setSumOfMaturedDeposits(funddatajson.getDouble("amountOfMaturedDeposit"));
						iEPF2FormDataFY6.setSumOfAmountRefundedFromMaturedDeposits(
								funddatajson.getDouble("refundByCompFromMetureDeposit"));
						iEPF2FormDataFY6.setSumOfMaturedDebentures(funddatajson.getDouble("amountOfMaturedDebentures"));
						iEPF2FormDataFY6.setSumOfAmountRefundedFromMaturedDebentures(
								funddatajson.getDouble("refundByCompFromMetureDebentures"));
						iEPF2FormDataFY6.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
								funddatajson.getDouble("appMoneyDueForRefund"));
						iEPF2FormDataFY6.setSumOfInterestAccruedOnMaturedDeposits(
								funddatajson.getDouble("maturedDepositWithCompaney"));
						iEPF2FormDataFY6.setSumOfInterestAccruedOnMaturedDebentures(
								funddatajson.getDouble("maturedDebenturesWithCompaney"));
						iEPF2FormDataFY6.setSalesProceedForFractionalShare(funddatajson.getDouble("fractionalShares"));
						iEPF2FormDataFY6
								.setRedemptionAmountOfPreferenceShare(funddatajson.getDouble("redemptionAmount"));
						iEPF2FormDataFY6.setSumOfOtherInvestmentType(funddatajson.getDouble("others"));
						iepf2formlistData.put("FY-6", iEPF2FormDataFY6);

					} else if (i == 6) {
						IEPF2FormData iEPF2FormDataFY7 = new IEPF2FormData();
						iEPF2FormDataFY7.setSumOfUnpaidAndUnclaimedDividend(
								funddatajson.getDouble("unclaimedAndUnpaidDividend"));
						iEPF2FormDataFY7
								.setNumberOfUnderlyingShares(funddatajson.getDouble("numberOfUnderlyingShares"));
						iEPF2FormDataFY7.setSumOfAmountRefundedfromUnpaidDividendAccount(
								funddatajson.getDouble("amountRefundedByCompany"));
						iEPF2FormDataFY7.setSumOfAmountReceivedUnderSubSection(
								funddatajson.getDouble("amountRecievedUnderSec38"));
						iEPF2FormDataFY7.setSumOfApplicationMoneyDueRefund(
								funddatajson.getDouble("appMoneyRecievedAndDueForRefund"));
						iEPF2FormDataFY7.setSumOfMaturedDeposits(funddatajson.getDouble("amountOfMaturedDeposit"));
						iEPF2FormDataFY7.setSumOfAmountRefundedFromMaturedDeposits(
								funddatajson.getDouble("refundByCompFromMetureDeposit"));
						iEPF2FormDataFY7.setSumOfMaturedDebentures(funddatajson.getDouble("amountOfMaturedDebentures"));
						iEPF2FormDataFY7.setSumOfAmountRefundedFromMaturedDebentures(
								funddatajson.getDouble("refundByCompFromMetureDebentures"));
						iEPF2FormDataFY7.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
								funddatajson.getDouble("appMoneyDueForRefund"));
						iEPF2FormDataFY7.setSumOfInterestAccruedOnMaturedDeposits(
								funddatajson.getDouble("maturedDepositWithCompaney"));
						iEPF2FormDataFY7.setSumOfInterestAccruedOnMaturedDebentures(
								funddatajson.getDouble("maturedDebenturesWithCompaney"));
						iEPF2FormDataFY7.setSalesProceedForFractionalShare(funddatajson.getDouble("fractionalShares"));
						iEPF2FormDataFY7
								.setRedemptionAmountOfPreferenceShare(funddatajson.getDouble("redemptionAmount"));
						iEPF2FormDataFY7.setSumOfOtherInvestmentType(funddatajson.getDouble("others"));

						iepf2formlistData.put("FY-7", iEPF2FormDataFY7);
					}

					else {
						System.out.println("Data not found");
					}

					// System.out.println("+++++++++iepf2formlistData--------------------"+iepf2formlistData);

				}
				System.out.println("+++++++++listOfiepf2formData--------------------" + iepf2formlistData);

			}
			System.out.println("********************************FOR DMSID************************************");
			jsonArrayform = jsonObjectform.getJSONArray("formAttachment");
			System.out.println("jsonArrayform::" + jsonArrayform);
			if (jsonArrayform != null) {

				for (int i = 0; i < jsonArrayform.length(); i++) {
					formattechmentjson = (JSONObject) jsonArrayform.get(i);
					if (formattechmentjson.getString("attachmentCategory").equals("Investor Info")) {
						listOfDMSID.add(formattechmentjson.getString("attachmentDMSId"));
					}

				}

				System.out.println("*********listOFDMSIDformData:::::::::::::::::::" + listOfDMSID);

				if (listOfDMSID != null && !listOfDMSID.isEmpty()) {
					mapData = getExcelDataForIEPF2(listOfDMSID);
					 validateIEPF2(srnDetails, iepf2formlistData,mapData);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Map<String, IEPF2ExcelData> getExcelDataForIEPF2(List<String> dmsId)
			throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub

		String targetSheetName = "Investor Details";
		byte[] excelData = null;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, IEPF2ExcelData> listofIEPF2ExcelData = new HashMap<String, IEPF2ExcelData>();
		List<CellCordinatesForIEPf> cellCoordinates = new ArrayList<>();

		// Reading row wise Data from Excel Sheet
		cellCoordinates.add(new CellCordinatesForIEPf(4, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(4, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(4, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(4, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(4, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(4, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(4, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(6, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(6, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(6, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(6, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(6, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(6, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(6, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(8, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(8, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(8, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(8, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(8, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(8, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(8, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(10, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(10, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(10, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(10, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(10, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(10, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(10, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(12, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(12, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(12, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(12, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(12, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(12, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(12, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(14, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(14, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(14, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(14, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(14, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(14, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(14, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(16, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(16, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(16, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(16, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(16, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(16, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(16, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(18, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(18, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(18, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(18, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(18, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(18, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(18, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(20, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(20, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(20, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(20, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(20, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(20, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(20, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(22, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(22, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(22, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(22, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(22, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(22, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(22, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(24, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(24, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(24, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(24, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(24, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(24, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(24, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(26, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(26, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(26, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(26, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(26, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(26, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(26, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(28, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(28, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(28, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(28, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(28, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(28, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(28, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(30, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(30, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(30, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(30, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(30, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(30, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(30, 10));

		cellCoordinates.add(new CellCordinatesForIEPf(32, 4));
		cellCoordinates.add(new CellCordinatesForIEPf(32, 5));
		cellCoordinates.add(new CellCordinatesForIEPf(32, 6));
		cellCoordinates.add(new CellCordinatesForIEPf(32, 7));
		cellCoordinates.add(new CellCordinatesForIEPf(32, 8));
		cellCoordinates.add(new CellCordinatesForIEPf(32, 9));
		cellCoordinates.add(new CellCordinatesForIEPf(32, 10));

		IEPF2ExcelData iEPF2ExcelDataFY1 = new IEPF2ExcelData();
		IEPF2ExcelData iEPF2ExcelDataFY2 = new IEPF2ExcelData();
		IEPF2ExcelData iEPF2ExcelDataFY3 = new IEPF2ExcelData();
		IEPF2ExcelData iEPF2ExcelDataFY4 = new IEPF2ExcelData();
		IEPF2ExcelData iEPF2ExcelDataFY5 = new IEPF2ExcelData();
		IEPF2ExcelData iEPF2ExcelDataFY6 = new IEPF2ExcelData();
		IEPF2ExcelData iEPF2ExcelDataFY7 = new IEPF2ExcelData();

		// excelData=iepfServiceImpl.readExcelFormData(dmsId);

		// System.out.println("**********************excelData::::::::"+excelData);
		if (dmsId.size() != 0 && dmsId != null) {
			for (String dms : dmsId) {

				excelData = iepfServiceImpl.getExcelByte(dms);

				InputStream is = new ByteArrayInputStream(excelData);

				try {
					HSSFWorkbook workBook = new HSSFWorkbook(is);
					Sheet sheet = workBook.getSheet(targetSheetName);
					int rowCount = 0;
					if (sheet != null) {
						Iterator<Row> rowIterator = sheet.rowIterator();
						while (rowIterator.hasNext() && rowCount < 35) {

							Row row = rowIterator.next();

							int rowIdx = row.getRowNum();
							for (int i = 0; i < cellCoordinates.size(); i++) {
								int colIdx = cellCoordinates.get(i).getColumn();
								if (rowIdx == cellCoordinates.get(i).getRow()) {
									Cell cell = row.getCell(colIdx);
									if (cell != null) {
										// System.out.println("Cell Value [" + rowIdx + "," + colIdx + "]: "

										// + cell.getNumericCellValue());

										// Reading value from 4th row and setting SumOfUnpaidAndUnclaimedDividend For
										// Each financial Year For Respective object;
										if (rowIdx == 4 && colIdx == 4) {

											iEPF2ExcelDataFY1.setSumOfUnpaidAndUnclaimedDividend(
													iEPF2ExcelDataFY1.getSumOfUnpaidAndUnclaimedDividend()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 4 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSumOfUnpaidAndUnclaimedDividend(
													iEPF2ExcelDataFY2.getSumOfUnpaidAndUnclaimedDividend()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 4 && colIdx == 6) {

											iEPF2ExcelDataFY3.setSumOfUnpaidAndUnclaimedDividend(
													iEPF2ExcelDataFY3.getSumOfUnpaidAndUnclaimedDividend()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 4 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSumOfUnpaidAndUnclaimedDividend(
													iEPF2ExcelDataFY4.getSumOfUnpaidAndUnclaimedDividend()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 4 && colIdx == 8) {

											iEPF2ExcelDataFY5.setSumOfUnpaidAndUnclaimedDividend(
													iEPF2ExcelDataFY5.getSumOfUnpaidAndUnclaimedDividend()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 4 && colIdx == 9) {

											iEPF2ExcelDataFY6.setSumOfUnpaidAndUnclaimedDividend(
													iEPF2ExcelDataFY6.getSumOfUnpaidAndUnclaimedDividend()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 4 && colIdx == 10) {

											iEPF2ExcelDataFY7.setSumOfUnpaidAndUnclaimedDividend(
													iEPF2ExcelDataFY7.getSumOfUnpaidAndUnclaimedDividend()
															+ cell.getNumericCellValue());
										}

										// Reading value from 6th row and setting NumberOfUnderlyingShares For Each
										// financial Year For Respective object;

										if (rowIdx == 6 && colIdx == 4) {
											iEPF2ExcelDataFY1.setNumberOfUnderlyingShares(
													iEPF2ExcelDataFY1.getNumberOfUnderlyingShares()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 6 && colIdx == 5) {

											iEPF2ExcelDataFY2.setNumberOfUnderlyingShares(
													iEPF2ExcelDataFY2.getNumberOfUnderlyingShares()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 6 && colIdx == 6) {

											iEPF2ExcelDataFY3.setNumberOfUnderlyingShares(
													iEPF2ExcelDataFY3.getNumberOfUnderlyingShares()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 6 && colIdx == 7) {

											iEPF2ExcelDataFY4.setNumberOfUnderlyingShares(
													iEPF2ExcelDataFY4.getNumberOfUnderlyingShares()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 6 && colIdx == 8) {

											iEPF2ExcelDataFY1.setNumberOfUnderlyingShares(
													iEPF2ExcelDataFY5.getNumberOfUnderlyingShares()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 6 && colIdx == 9) {

											iEPF2ExcelDataFY6.setNumberOfUnderlyingShares(
													iEPF2ExcelDataFY6.getNumberOfUnderlyingShares()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 6 && colIdx == 10) {

											iEPF2ExcelDataFY7.setNumberOfUnderlyingShares(
													iEPF2ExcelDataFY7.getNumberOfUnderlyingShares()
															+ cell.getNumericCellValue());
										}

										// Reading value from 8th row and setting
										// SumOfAmountRefundedfromUnpaidDividendAccount For Each financial Year For
										// Respective object;
										if (rowIdx == 8 && colIdx == 4) {
											iEPF2ExcelDataFY1.setSumOfAmountRefundedfromUnpaidDividendAccount(
													iEPF2ExcelDataFY1.getSumOfAmountRefundedfromUnpaidDividendAccount()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 8 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSumOfAmountRefundedfromUnpaidDividendAccount(
													iEPF2ExcelDataFY2.getSumOfAmountRefundedfromUnpaidDividendAccount()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 8 && colIdx == 6) {
											iEPF2ExcelDataFY3.setSumOfAmountRefundedfromUnpaidDividendAccount(
													iEPF2ExcelDataFY3.getSumOfAmountRefundedfromUnpaidDividendAccount()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 8 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSumOfAmountRefundedfromUnpaidDividendAccount(
													iEPF2ExcelDataFY4.getSumOfAmountRefundedfromUnpaidDividendAccount()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 8 && colIdx == 8) {

											iEPF2ExcelDataFY5.setSumOfAmountRefundedfromUnpaidDividendAccount(
													iEPF2ExcelDataFY5.getSumOfAmountRefundedfromUnpaidDividendAccount()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 8 && colIdx == 9) {
											iEPF2ExcelDataFY6.setSumOfAmountRefundedfromUnpaidDividendAccount(
													iEPF2ExcelDataFY6.getSumOfAmountRefundedfromUnpaidDividendAccount()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 8 && colIdx == 10) {
											iEPF2ExcelDataFY7.setSumOfAmountRefundedfromUnpaidDividendAccount(
													iEPF2ExcelDataFY7.getSumOfAmountRefundedfromUnpaidDividendAccount()
															+ cell.getNumericCellValue());
										}

										// Reading value from 10th row and setting
										// SumOfAmountRefundedfromUnpaidDividendAccount For Each financial Year For
										// Respective object;
										if (rowIdx == 10 && colIdx == 4) {

											iEPF2ExcelDataFY1.setSumOfAmountReceivedUnderSubSection(
													iEPF2ExcelDataFY1.getSumOfAmountReceivedUnderSubSection()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 10 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSumOfAmountReceivedUnderSubSection(
													iEPF2ExcelDataFY2.getSumOfAmountReceivedUnderSubSection()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 10 && colIdx == 6) {
											iEPF2ExcelDataFY3.setSumOfAmountReceivedUnderSubSection(
													iEPF2ExcelDataFY3.getSumOfAmountReceivedUnderSubSection()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 10 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSumOfAmountReceivedUnderSubSection(
													iEPF2ExcelDataFY4.getSumOfAmountReceivedUnderSubSection()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 10 && colIdx == 8) {
											iEPF2ExcelDataFY5.setSumOfAmountReceivedUnderSubSection(
													iEPF2ExcelDataFY5.getSumOfAmountReceivedUnderSubSection()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 10 && colIdx == 9) {

											iEPF2ExcelDataFY6.setSumOfAmountReceivedUnderSubSection(
													iEPF2ExcelDataFY6.getSumOfAmountReceivedUnderSubSection()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 10 && colIdx == 10) {

											iEPF2ExcelDataFY7.setSumOfAmountReceivedUnderSubSection(
													iEPF2ExcelDataFY7.getSumOfAmountReceivedUnderSubSection()
															+ cell.getNumericCellValue());
										}

										// Reading value from 12th row and setting SumOfApplicationMoneyDueRefund For
										// Each financial Year For Respective object;
										if (rowIdx == 12 && colIdx == 4) {

											iEPF2ExcelDataFY1.setSumOfApplicationMoneyDueRefund(
													iEPF2ExcelDataFY1.getSumOfApplicationMoneyDueRefund()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 12 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSumOfApplicationMoneyDueRefund(
													iEPF2ExcelDataFY2.getSumOfApplicationMoneyDueRefund()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 12 && colIdx == 6) {

											iEPF2ExcelDataFY3.setSumOfApplicationMoneyDueRefund(
													iEPF2ExcelDataFY3.getSumOfApplicationMoneyDueRefund()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 12 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSumOfApplicationMoneyDueRefund(
													iEPF2ExcelDataFY4.getSumOfApplicationMoneyDueRefund()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 12 && colIdx == 8) {

											iEPF2ExcelDataFY5.setSumOfApplicationMoneyDueRefund(
													iEPF2ExcelDataFY5.getSumOfApplicationMoneyDueRefund()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 12 && colIdx == 9) {

											iEPF2ExcelDataFY6.setSumOfApplicationMoneyDueRefund(
													iEPF2ExcelDataFY6.getSumOfApplicationMoneyDueRefund()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 12 && colIdx == 10) {

											iEPF2ExcelDataFY7.setSumOfApplicationMoneyDueRefund(
													iEPF2ExcelDataFY7.getSumOfApplicationMoneyDueRefund()
															+ cell.getNumericCellValue());
										}

										// Reading value from 14th row and setting SumOfMaturedDeposits For Each
										// financial Year For Respective object;
										if (rowIdx == 14 && colIdx == 4) {

											iEPF2ExcelDataFY1
													.setSumOfMaturedDeposits(iEPF2ExcelDataFY1.getSumOfMaturedDeposits()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 14 && colIdx == 5) {

											iEPF2ExcelDataFY2
													.setSumOfMaturedDeposits(iEPF2ExcelDataFY2.getSumOfMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 14 && colIdx == 6) {

											iEPF2ExcelDataFY3
													.setSumOfMaturedDeposits(iEPF2ExcelDataFY3.getSumOfMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 14 && colIdx == 7) {

											iEPF2ExcelDataFY4
													.setSumOfMaturedDeposits(iEPF2ExcelDataFY4.getSumOfMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 14 && colIdx == 8) {

											iEPF2ExcelDataFY5
													.setSumOfMaturedDeposits(iEPF2ExcelDataFY5.getSumOfMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 14 && colIdx == 9) {

											iEPF2ExcelDataFY6
													.setSumOfMaturedDeposits(iEPF2ExcelDataFY6.getSumOfMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 14 && colIdx == 10) {

											iEPF2ExcelDataFY7
													.setSumOfMaturedDeposits(iEPF2ExcelDataFY7.getSumOfMaturedDeposits()
															+ cell.getNumericCellValue());
										}

										// Reading value from 16th row and setting
										// SumOfAmountRefundedFromMaturedDeposits For Each financial Year For Respective
										// object;
										if (rowIdx == 16 && colIdx == 4) {

											iEPF2ExcelDataFY1.setSumOfAmountRefundedFromMaturedDeposits(
													iEPF2ExcelDataFY1.getSumOfAmountRefundedFromMaturedDeposits()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 16 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSumOfAmountRefundedFromMaturedDeposits(
													iEPF2ExcelDataFY2.getSumOfAmountRefundedFromMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 16 && colIdx == 6) {

											iEPF2ExcelDataFY3.setSumOfAmountRefundedFromMaturedDeposits(
													iEPF2ExcelDataFY3.getSumOfAmountRefundedFromMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 16 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSumOfAmountRefundedFromMaturedDeposits(
													iEPF2ExcelDataFY4.getSumOfAmountRefundedFromMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 16 && colIdx == 8) {

											iEPF2ExcelDataFY5.setSumOfAmountRefundedFromMaturedDeposits(
													iEPF2ExcelDataFY5.getSumOfAmountRefundedFromMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 16 && colIdx == 9) {

											iEPF2ExcelDataFY6.setSumOfAmountRefundedFromMaturedDeposits(
													iEPF2ExcelDataFY6.getSumOfAmountRefundedFromMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 16 && colIdx == 10) {

											iEPF2ExcelDataFY7.setSumOfAmountRefundedFromMaturedDeposits(
													iEPF2ExcelDataFY7.getSumOfAmountRefundedFromMaturedDeposits()
															+ cell.getNumericCellValue());
										}

										// Reading value from 18th row and setting SumOfMaturedDebentures For Each
										// financial Year For Respective object;
										if (rowIdx == 18 && colIdx == 4) {

											iEPF2ExcelDataFY1.setSumOfMaturedDebentures(
													iEPF2ExcelDataFY1.getSumOfMaturedDebentures()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 18 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSumOfMaturedDebentures(
													iEPF2ExcelDataFY2.getSumOfMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 18 && colIdx == 6) {

											iEPF2ExcelDataFY3.setSumOfMaturedDebentures(
													iEPF2ExcelDataFY3.getSumOfMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 18 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSumOfMaturedDebentures(
													iEPF2ExcelDataFY4.getSumOfMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 18 && colIdx == 8) {

											iEPF2ExcelDataFY5.setSumOfMaturedDebentures(
													iEPF2ExcelDataFY5.getSumOfMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 18 && colIdx == 9) {

											iEPF2ExcelDataFY6.setSumOfMaturedDebentures(
													iEPF2ExcelDataFY6.getSumOfMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 18 && colIdx == 10) {

											iEPF2ExcelDataFY7.setSumOfMaturedDebentures(
													iEPF2ExcelDataFY7.getSumOfMaturedDebentures()
															+ cell.getNumericCellValue());
										}

										// Reading value from 20th row and setting
										// SumOfAmountRefundedFromMaturedDebentures For Each financial Year For
										// Respective object;
										if (rowIdx == 20 && colIdx == 4) {

											iEPF2ExcelDataFY1.setSumOfAmountRefundedFromMaturedDebentures(
													iEPF2ExcelDataFY1.getSumOfAmountRefundedFromMaturedDebentures()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 20 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSumOfAmountRefundedFromMaturedDebentures(
													iEPF2ExcelDataFY2.getSumOfAmountRefundedFromMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 20 && colIdx == 6) {

											iEPF2ExcelDataFY3.setSumOfAmountRefundedFromMaturedDebentures(
													iEPF2ExcelDataFY3.getSumOfAmountRefundedFromMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 20 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSumOfAmountRefundedFromMaturedDebentures(
													iEPF2ExcelDataFY4.getSumOfAmountRefundedFromMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 20 && colIdx == 8) {

											iEPF2ExcelDataFY5.setSumOfAmountRefundedFromMaturedDebentures(
													iEPF2ExcelDataFY5.getSumOfAmountRefundedFromMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 20 && colIdx == 9) {

											iEPF2ExcelDataFY6.setSumOfAmountRefundedFromMaturedDebentures(
													iEPF2ExcelDataFY6.getSumOfAmountRefundedFromMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 20 && colIdx == 10) {

											iEPF2ExcelDataFY7.setSumOfAmountRefundedFromMaturedDebentures(
													iEPF2ExcelDataFY7.getSumOfAmountRefundedFromMaturedDebentures()
															+ cell.getNumericCellValue());
										}

										// Reading value from 22th row and setting
										// SumOfInterestAccruedOnApplicationMoneyDueForRefund For Each financial Year
										// For Respective object;
										if (rowIdx == 22 && colIdx == 4) {

											iEPF2ExcelDataFY1.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
													iEPF2ExcelDataFY1
															.getSumOfInterestAccruedOnApplicationMoneyDueForRefund()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 22 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
													iEPF2ExcelDataFY2
															.getSumOfInterestAccruedOnApplicationMoneyDueForRefund()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 22 && colIdx == 6) {

											iEPF2ExcelDataFY3.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
													iEPF2ExcelDataFY3
															.getSumOfInterestAccruedOnApplicationMoneyDueForRefund()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 22 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
													iEPF2ExcelDataFY4
															.getSumOfInterestAccruedOnApplicationMoneyDueForRefund()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 22 && colIdx == 8) {

											iEPF2ExcelDataFY5.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
													iEPF2ExcelDataFY5
															.getSumOfInterestAccruedOnApplicationMoneyDueForRefund()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 22 && colIdx == 9) {

											iEPF2ExcelDataFY6.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
													iEPF2ExcelDataFY6
															.getSumOfInterestAccruedOnApplicationMoneyDueForRefund()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 22 && colIdx == 10) {

											iEPF2ExcelDataFY7.setSumOfInterestAccruedOnApplicationMoneyDueForRefund(
													iEPF2ExcelDataFY7
															.getSumOfInterestAccruedOnApplicationMoneyDueForRefund()
															+ cell.getNumericCellValue());
										}
										// Reading value from 24th row and setting SumOfInterestAccruedOnMaturedDeposits
										// For Each financial Year For Respective object;
										if (rowIdx == 24 && colIdx == 4) {

											iEPF2ExcelDataFY1.setSumOfInterestAccruedOnMaturedDeposits(
													iEPF2ExcelDataFY1.getSumOfInterestAccruedOnMaturedDeposits()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 24 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSumOfInterestAccruedOnMaturedDeposits(
													iEPF2ExcelDataFY2.getSumOfInterestAccruedOnMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 24 && colIdx == 6) {

											iEPF2ExcelDataFY3.setSumOfInterestAccruedOnMaturedDeposits(
													iEPF2ExcelDataFY3.getSumOfInterestAccruedOnMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 24 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSumOfInterestAccruedOnMaturedDeposits(
													iEPF2ExcelDataFY4.getSumOfInterestAccruedOnMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 24 && colIdx == 8) {

											iEPF2ExcelDataFY5.setSumOfInterestAccruedOnMaturedDeposits(
													iEPF2ExcelDataFY5.getSumOfInterestAccruedOnMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 24 && colIdx == 9) {

											iEPF2ExcelDataFY6.setSumOfInterestAccruedOnMaturedDeposits(
													iEPF2ExcelDataFY6.getSumOfInterestAccruedOnMaturedDeposits()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 24 && colIdx == 10) {

											iEPF2ExcelDataFY7.setSumOfInterestAccruedOnMaturedDeposits(
													iEPF2ExcelDataFY7.getSumOfInterestAccruedOnMaturedDeposits()
															+ cell.getNumericCellValue());
										}

										// Reading value from 26th row and setting
										// SumOfInterestAccruedOnMaturedDebentures For Each financial Year For
										// Respective object;
										if (rowIdx == 26 && colIdx == 4) {

											iEPF2ExcelDataFY1.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY1.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 26 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY2.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 26 && colIdx == 6) {

											iEPF2ExcelDataFY3.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY3.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 26 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY4.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 26 && colIdx == 8) {

											iEPF2ExcelDataFY5.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY5.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 26 && colIdx == 9) {

											iEPF2ExcelDataFY6.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY6.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 26 && colIdx == 10) {

											iEPF2ExcelDataFY7.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY7.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}

										// Reading value from 28th row and setting
										// SumOfInterestAccruedOnMaturedDebentures For Each financial Year For
										// Respective object;
										if (rowIdx == 28 && colIdx == 4) {

											iEPF2ExcelDataFY1.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY1.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 28 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY2.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 28 && colIdx == 6) {

											iEPF2ExcelDataFY3.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY3.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 28 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY4.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 28 && colIdx == 8) {

											iEPF2ExcelDataFY5.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY5.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 28 && colIdx == 9) {

											iEPF2ExcelDataFY6.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY6.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 28 && colIdx == 10) {

											iEPF2ExcelDataFY7.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY7.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}

										// Reading value from 26th row and setting
										// SumOfInterestAccruedOnMaturedDebentures For Each financial Year For
										// Respective object;
										if (rowIdx == 26 && colIdx == 4) {

											iEPF2ExcelDataFY1.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY1.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 26 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY2.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 26 && colIdx == 6) {

											iEPF2ExcelDataFY3.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY3.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 26 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY4.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 26 && colIdx == 8) {

											iEPF2ExcelDataFY5.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY5.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 26 && colIdx == 9) {

											iEPF2ExcelDataFY6.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY6.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 26 && colIdx == 10) {

											iEPF2ExcelDataFY7.setSumOfInterestAccruedOnMaturedDebentures(
													iEPF2ExcelDataFY7.getSumOfInterestAccruedOnMaturedDebentures()
															+ cell.getNumericCellValue());
										}

										// Reading value from 28th row and setting SalesProceedForFractionalShare For
										// Each financial Year For Respective object;
										if (rowIdx == 28 && colIdx == 4) {

											iEPF2ExcelDataFY1.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY1.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 28 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY2.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 28 && colIdx == 6) {

											iEPF2ExcelDataFY3.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY3.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 28 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY4.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 28 && colIdx == 8) {

											iEPF2ExcelDataFY5.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY5.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 28 && colIdx == 9) {

											iEPF2ExcelDataFY6.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY6.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 28 && colIdx == 10) {

											iEPF2ExcelDataFY7.setSalesProceedForFractionalShare(
													iEPF2ExcelDataFY7.getSalesProceedForFractionalShare()
															+ cell.getNumericCellValue());
										}

										// Reading value from 30th row and setting RedemptionAmountOfPreferenceShare For
										// Each financial Year For Respective object;
										if (rowIdx == 30 && colIdx == 4) {

											iEPF2ExcelDataFY1.setRedemptionAmountOfPreferenceShare(
													iEPF2ExcelDataFY1.getRedemptionAmountOfPreferenceShare()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 30 && colIdx == 5) {

											iEPF2ExcelDataFY2.setRedemptionAmountOfPreferenceShare(
													iEPF2ExcelDataFY2.getRedemptionAmountOfPreferenceShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 30 && colIdx == 6) {

											iEPF2ExcelDataFY3.setRedemptionAmountOfPreferenceShare(
													iEPF2ExcelDataFY3.getRedemptionAmountOfPreferenceShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 30 && colIdx == 7) {

											iEPF2ExcelDataFY4.setRedemptionAmountOfPreferenceShare(
													iEPF2ExcelDataFY4.getRedemptionAmountOfPreferenceShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 30 && colIdx == 8) {

											iEPF2ExcelDataFY5.setRedemptionAmountOfPreferenceShare(
													iEPF2ExcelDataFY5.getRedemptionAmountOfPreferenceShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 30 && colIdx == 9) {

											iEPF2ExcelDataFY6.setRedemptionAmountOfPreferenceShare(
													iEPF2ExcelDataFY6.getRedemptionAmountOfPreferenceShare()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 30 && colIdx == 10) {

											iEPF2ExcelDataFY7.setRedemptionAmountOfPreferenceShare(
													iEPF2ExcelDataFY7.getRedemptionAmountOfPreferenceShare()
															+ cell.getNumericCellValue());
										}

										// Reading value from 32th row and setting RedemptionAmountOfPreferenceShare For
										// Each financial Year For Respective object;
										if (rowIdx == 32 && colIdx == 4) {

											iEPF2ExcelDataFY1.setSumOfOtherInvestmentType(
													iEPF2ExcelDataFY1.getSumOfOtherInvestmentType()
															+ cell.getNumericCellValue());
										}

										if (rowIdx == 32 && colIdx == 5) {

											iEPF2ExcelDataFY2.setSumOfOtherInvestmentType(
													iEPF2ExcelDataFY2.getSumOfOtherInvestmentType()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 32 && colIdx == 6) {

											iEPF2ExcelDataFY3.setSumOfOtherInvestmentType(
													iEPF2ExcelDataFY3.getSumOfOtherInvestmentType()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 32 && colIdx == 7) {

											iEPF2ExcelDataFY4.setSumOfOtherInvestmentType(
													iEPF2ExcelDataFY4.getSumOfOtherInvestmentType()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 32 && colIdx == 8) {

											iEPF2ExcelDataFY5.setSumOfOtherInvestmentType(
													iEPF2ExcelDataFY5.getSumOfOtherInvestmentType()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 32 && colIdx == 9) {

											iEPF2ExcelDataFY6.setSumOfOtherInvestmentType(
													iEPF2ExcelDataFY6.getSumOfOtherInvestmentType()
															+ cell.getNumericCellValue());
										}
										if (rowIdx == 32 && colIdx == 10) {

											iEPF2ExcelDataFY7.setSumOfOtherInvestmentType(
													iEPF2ExcelDataFY7.getSumOfOtherInvestmentType()
															+ cell.getNumericCellValue());
										}

									} else {

										System.out.println("Cell not found at [" + rowIdx + "," + colIdx + "]");

									}
									listofIEPF2ExcelData.put("FY-1", iEPF2ExcelDataFY1);
									listofIEPF2ExcelData.put("FY-2", iEPF2ExcelDataFY2);
									listofIEPF2ExcelData.put("FY-3", iEPF2ExcelDataFY3);
									listofIEPF2ExcelData.put("FY-4", iEPF2ExcelDataFY4);
									listofIEPF2ExcelData.put("FY-5", iEPF2ExcelDataFY5);
									listofIEPF2ExcelData.put("FY-6", iEPF2ExcelDataFY6);
									listofIEPF2ExcelData.put("FY-7", iEPF2ExcelDataFY7);

								}

							}
							rowCount++;
						}

					} else {

						System.out.println("Sheet not found.");

					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// System.out.println("listofIEPF2ExcelData**************************"+listofIEPF2ExcelData);

			}
			System.out.println("listofIEPF2TotalExcelFormData**************************" + listofIEPF2ExcelData);
		}
		return listofIEPF2ExcelData;
	}

	
	public  void validateIEPF2(String srn, Map<String, IEPF2FormData> mapOfformData, Map<String, IEPF2ExcelData> mapOfexceldata)

	{
		System.out.println("IEPF2FORMDATA::++++++++++++++++++++++++++::::::::--" + mapOfformData);
		System.out.println("IEPF2EXCELDATA::+++++++++++++++++:::::::--" + mapOfexceldata);
		String validationSuccess = "N";

	//	 
		System.out.println("******************************"+mapOfformData.get("FY-1"));
		// First we will Compare FY1 Data from Form and Excel
		
		if (mapOfformData.get("FY-1").equals(mapOfexceldata.get("FY-1"))
				&& mapOfformData.get("FY-2").equals(mapOfexceldata.get("FY-2"))
				&& mapOfformData.get("FY-3").equals(mapOfexceldata.get("FY-3"))
				&& mapOfformData.get("FY-4").equals(mapOfexceldata.get("FY-4"))
				&& mapOfformData.get("FY-5").equals(mapOfexceldata.get("FY-5"))
				&& mapOfformData.get("FY-6").equals(mapOfexceldata.get("FY-6"))
				&& mapOfformData.get("FY-7").equals(mapOfexceldata.get("FY-7"))) {
			validationSuccess = "y";
			System.out.println("Pending for DSC Payment");
		} else {

			System.out.println("Data Validation failed");
		}

	}

}
