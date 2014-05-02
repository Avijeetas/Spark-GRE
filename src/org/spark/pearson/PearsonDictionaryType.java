package org.spark.pearson;

public enum PearsonDictionaryType {
	
	Ldoce5("ldoce5"), Lasde("lasde"), Ldec("ldec"), Wordwise("wordwise");
	
	private String dictionaryName;

	PearsonDictionaryType(String dictionaryName) {
		this.dictionaryName = dictionaryName;
	}

	public String getDictionaryName() {
		return dictionaryName;
	}
}