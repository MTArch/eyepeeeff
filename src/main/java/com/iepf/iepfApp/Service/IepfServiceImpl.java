package com.iepf.iepfApp.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.iepf.batchprocess.beans.AccessToken;
import com.iepf.iepfApp.Constant.IepfConstant;


/**
* This class is used to get Acess Token and making status change call
* @author Satish5 kumar, Saumya Pandey LTIM
*
*/
public class IepfServiceImpl implements IepfService {

	private static final Logger logger = LoggerFactory.getLogger(IepfServiceImpl.class);
	static RestTemplate restTemplate = new RestTemplate();

//Get SRN Details
	@Override
	public String getAllSrn() throws JsonMappingException, JsonProcessingException {
		IEPF1AInvestorDataValidation iepf1AInvestorDataValidation = new IEPF1AInvestorDataValidation();
		IEPF4InvestorDataValidation iepf4InvestorDataValidation = new IEPF4InvestorDataValidation();
		IEPF1InvestorDataValidation iepf1InvestorDataValidation=new IEPF1InvestorDataValidation();
		List<String> srnIEPF1A = new ArrayList<String>();
		List<String> srnIEPF1 = new ArrayList<String>();
		List<String> srnIEPF2 = new ArrayList<String>();
		List<String> srnIEPF4 = new ArrayList<String>();
		try {
			JSONArray jsonArray;
			JSONObject jsonObject;
			JSONObject firstjArray;
			HttpHeaders headers = new HttpHeaders();
			headers.set(IepfConstant.authorization, IepfConstant.bearer + getAcessToken());
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<String> response = restTemplate.exchange(IepfConstant.srnDetailsUrl, HttpMethod.GET, entity,
					String.class);
			String responseBody = response.getBody();
			jsonObject = new JSONObject(responseBody);
			jsonArray = jsonObject.getJSONArray("data");
			String data = null;//
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.length(); i++) {
					firstjArray = jsonArray.getJSONObject(i);
					// For IEPF1A
					if (firstjArray.getString("formName").equals("IEPF-1A")) {
						srnIEPF1A.add(firstjArray.getString("srn"));

						// For IEPF-2
					} else if (firstjArray.getString("formName").equals("IEPF-2")) {
						srnIEPF2.add(firstjArray.getString("srn"));

						// For IEPF-1
					} else if (firstjArray.getString("formName").equals("IEPF-1")) {
						srnIEPF1.add(firstjArray.getString("srn"));

						// For IEPF-4
					} else if (firstjArray.getString("formName").equals("IEPF-4")) {
						srnIEPF4.add(firstjArray.getString("srn"));

					} else {
						logger.error("data not present");
					}

				}

			}
			
			logger.info("calling OnLoad API to get the SRN(s) for IEPF1A forms..................................");
			// IEPF1A
			for (String srn1A : srnIEPF1A) {
				
				logger.info("SRN1A:::" + srn1A);
				iepf1AInvestorDataValidation.iepf1AFormData(srn1A);
			}

			// IEPF2
			/*
			 * for (String str : srnIEPF2) { iepfServiceImpl.iepf2FormData(srnNumber); }
			 */
			 //IEPF1
			logger.info("Calling OnLoad API to get the SRN(s) for IEPF1 forms..................................");

			  for (String str : srnIEPF1) { 
				  logger.info("SRNForIEPF1AND7::::" + str);
				  iepf1InvestorDataValidation.getIEPF1FormData(str); 
				  }
			 
		  logger.info("calling OnLoad API to get the SRN(s) for IEPF4 forms..................................");

			for (String srn4 : srnIEPF4) {
				logger.info("SRN4::::" + srn4);
				iepf4InvestorDataValidation.iepf4FormData(srn4);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	// Validation method
	public static String changeStatusAfterValidationServiceCall(String validationSuccess, String srNumber) {
		Map<String, String> inputData = new HashMap<String, String>();
		Map<String, Map> finalinputData = new HashMap<String, Map>();
		try {
			JSONObject json = null;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(IepfConstant.authorization, IepfConstant.bearer + getAcessToken());
			inputData.put(IepfConstant.validationSuccess, validationSuccess);
			inputData.put(IepfConstant.srNumber, srNumber);
			finalinputData.put("requestBody", inputData);
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonBody = objectMapper.writeValueAsString(finalinputData);
			HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
			ResponseEntity<String> response = restTemplate.exchange(IepfConstant.changeStatusUrl, HttpMethod.POST,
					request, String.class);
			String responseBody = response.getBody();
			json = new JSONObject(responseBody);
			String status = json.getString("status");
			logger.info("Status after validation : " + status);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

// Getting Token 
	public static String getAcessToken() throws JsonMappingException, JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();
		JSONObject json;
		String token = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			String basicAuth = IepfConstant.basic + " " + IepfConstant.basicAuthUat;
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.set(IepfConstant.authorization, basicAuth);
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add(IepfConstant.Grant_type, IepfConstant.Password);
			body.add(IepfConstant.UserName, IepfConstant.Admin);
			body.add(IepfConstant.Password, IepfConstant.Admin);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
			ResponseEntity<String> response = restTemplate.exchange(IepfConstant.tokenUrlUat, HttpMethod.POST, request,
					String.class);
			String responseBody = response.getBody();
			json = new JSONObject(responseBody);
			token = json.getString("access_token");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token;

	}
	


	public static byte[] getExcelByte(String dmsId) {

		byte[] excelData = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			Map<String, String> elements = new HashMap<>();
			elements.put("cabinetName", IepfConstant.cabinetName);
			elements.put("userName", IepfConstant.userName);
			elements.put("userPassword", IepfConstant.userPassword);
			elements.put("locale", IepfConstant.locale);
			Map<String, Map<String, String>> dataelement = new HashMap<String, Map<String, String>>();
			dataelement.put("NGOGetDocumentBDO", elements);
			elements.put("docIndex",dmsId);
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonBody;
			jsonBody = objectMapper.writeValueAsString(dataelement);
			HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
			logger.info("Before Get DMS Execution.....");
			ResponseEntity<byte[]> response = restTemplate.exchange(IepfConstant.iepfexcelFormUrl, HttpMethod.POST,
					request, byte[].class);
			excelData = response.getBody();
		} catch (JsonProcessingException e) {

			logger.error("There is Issue in getting Excel From DMS");
			e.printStackTrace();

		}

		return excelData;

	}

}
