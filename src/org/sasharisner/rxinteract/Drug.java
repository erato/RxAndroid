package org.sasharisner.rxinteract;

public class Drug {
	private String drug1;
	private String drug2;		
	private String effect;
	private double likelihood;
	private double severity;

	public void setDrug1(String value) {
	   this.drug1 = value;
	}

	public String getDrug1() {
	   return this.drug1;
	}
	
	public void setDrug2(String value) {
	   this.drug2 = value;
	}

	public String getDrug2() {
	   return this.drug2;
	}
		
	public void setEffect(String value) {
	   this.effect = value;
	}

	public String getEffect() {
	   return this.effect;
	}
	
	public void setLikelihood(double value) {
	   this.likelihood = value;
	}

	public double getLikelihood() {
	   return this.likelihood;
	}
	
	public void setSeverity(double value) {
	   this.severity = value;
	}

	public double getSeverity() {
	   return this.severity;
	}
	
	
	
	// constructor
	
	public Drug(String sDrug1, String sDrug2, String sEffect, double dLikelihood, double dSeverity)
	{
		drug1 = sDrug1;
		drug2 = sDrug2;
		effect = sEffect;
		likelihood = dLikelihood;
		severity = dSeverity;
	}
	
	
}