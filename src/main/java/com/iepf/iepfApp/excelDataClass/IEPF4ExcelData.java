package com.iepf.iepfApp.excelDataClass;


/**
* This class is POJO for IEPF4 Excel Data
* @author Satish5 kumar, Saumya Pandey LTIM
*
*/
public class IEPF4ExcelData {
	private	double nominalValueOfShares;

	public double getNominalValueOfShares() {
		return nominalValueOfShares;
	}

	public void setNominalValueOfShares(double nominalValueOfShares) {
		this.nominalValueOfShares = nominalValueOfShares;
	}

	@Override
	public String toString() {
		return "IEPF4ExcelData [nominalValueOfShares=" + nominalValueOfShares + "]";
	}
	
	
}
