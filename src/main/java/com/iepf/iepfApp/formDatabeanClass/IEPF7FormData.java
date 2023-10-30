package com.iepf.iepfApp.formDatabeanClass;


/**
* This class is POJO for IEPF7 Form Data
* @author Satish5 kumar, Saumya Pandey LTIM
*
*/
public class IEPF7FormData {
	
	 private double  sumOfDividendOnSharesTransferredToIEPF;
	 private double proceedsRealizedOnDelistingOfCompanies;
	 private double proceedsRealizedOnWindingUpOfCompanies;
	 private double surrenderOfSharesUnderSection236;
	 private double sumOfOtherInvestmentTypes;
	 
	 
	public double getSumOfDividendOnSharesTransferredToIEPF() {
		return sumOfDividendOnSharesTransferredToIEPF;
	}
	public void setSumOfDividendOnSharesTransferredToIEPF(double sumOfDividendOnSharesTransferredToIEPF) {
		this.sumOfDividendOnSharesTransferredToIEPF = sumOfDividendOnSharesTransferredToIEPF;
	}
	public double getProceedsRealizedOnDelistingOfCompanies() {
		return proceedsRealizedOnDelistingOfCompanies;
	}
	public void setProceedsRealizedOnDelistingOfCompanies(double proceedsRealizedOnDelistingOfCompanies) {
		this.proceedsRealizedOnDelistingOfCompanies = proceedsRealizedOnDelistingOfCompanies;
	}
	public double getProceedsRealizedOnWindingUpOfCompanies() {
		return proceedsRealizedOnWindingUpOfCompanies;
	}
	public void setProceedsRealizedOnWindingUpOfCompanies(double proceedsRealizedOnWindingUpOfCompanies) {
		this.proceedsRealizedOnWindingUpOfCompanies = proceedsRealizedOnWindingUpOfCompanies;
	}
	public double getSurrenderOfSharesUnderSection236() {
		return surrenderOfSharesUnderSection236;
	}
	public void setSurrenderOfSharesUnderSection236(double surrenderOfSharesUnderSection236) {
		this.surrenderOfSharesUnderSection236 = surrenderOfSharesUnderSection236;
	}
	public double getSumOfOtherInvestmentTypes() {
		return sumOfOtherInvestmentTypes;
	}
	public void setSumOfOtherInvestmentTypes(double sumOfOtherInvestmentTypes) {
		this.sumOfOtherInvestmentTypes = sumOfOtherInvestmentTypes;
	}
	@Override
	public String toString() {
		return "IEPF7FormData [sumOfDividendOnSharesTransferredToIEPF=" + sumOfDividendOnSharesTransferredToIEPF
				+ ", proceedsRealizedOnDelistingOfCompanies=" + proceedsRealizedOnDelistingOfCompanies
				+ ", proceedsRealizedOnWindingUpOfCompanies=" + proceedsRealizedOnWindingUpOfCompanies
				+ ", surrenderOfSharesUnderSection236=" + surrenderOfSharesUnderSection236
				+ ", sumOfOtherInvestmentTypes=" + sumOfOtherInvestmentTypes + "]";
	}
	 
	 
}
