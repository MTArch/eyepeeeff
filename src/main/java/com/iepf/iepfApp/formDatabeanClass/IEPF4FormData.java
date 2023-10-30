package com.iepf.iepfApp.formDatabeanClass;

/**
* This class is POJO for IEPF4 Form Data
* @author Satish5 kumar, Saumya Pandey LTIM
*
*/
public class IEPF4FormData {
	private	double nominalValueOfShares;

	public double getNominalValueOfShares() {
		return nominalValueOfShares;
	}

	public void setNominalValueOfShares(double nominalValueOfShares) {
		this.nominalValueOfShares = nominalValueOfShares;
	}

	@Override
	public String toString() {
		return "IEPF4FormData [nominalValueOfShares=" + nominalValueOfShares + "]";
	}

	

}
