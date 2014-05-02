package org.spark.pearson;

public class PearsonDictionaryHelper {
	
	/**
	 * convert the core NLP tag into the pearson pos tag 
	 * @param coreNLPTag
	 * @return
	 */
	public static String convert(String coreNLPTag) {
		if (coreNLPTag.startsWith("NN")) {
			return "noun";
		} else if (coreNLPTag.startsWith("VB")) {
			return "verb";
		} else if (coreNLPTag.startsWith("JJ")) {
			return "adjective";
		} else if (coreNLPTag.startsWith("RB")) {
			return "adverb";
		} else if (coreNLPTag.startsWith("DT")) {
			return "determiner";
		} else if (coreNLPTag.startsWith("IN")) {
			return "preposition";
		} else if (coreNLPTag.startsWith("TO")) {
			return "to";
		}
		return null;
	}
}
