package com.iepf.iepfApp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.iepf.iepfApp.Service.IepfServiceImpl;


/**
* This class is  sets up and starts the Application
* @author Satish5 kumar, Saumya Pandey LTIM
*
*/

@SpringBootApplication
@EnableScheduling
public class IepfServiceCallApplication {
	
			
	private static final Logger logger = LoggerFactory.getLogger(IepfServiceCallApplication.class);

	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
		SpringApplication.run(IepfServiceCallApplication.class, args);
		logger.info("--------------------------Investor Data Validation-------------------------------------");
	}
	

	 @Scheduled(cron="0 0 18 * * ?")
	 public void scheduled() throws JsonMappingException, JsonProcessingException
	 { 
		 logger.info("Application started to validate all form at "+System.currentTimeMillis());
         IepfServiceImpl iepfServiceImpl=new IepfServiceImpl();
	      iepfServiceImpl.getAllSrn(); 
	      logger.info("Application Completed validation at "+System.currentTimeMillis());
	 }
	 
     }
