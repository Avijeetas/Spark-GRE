package org.spark.gre;

import java.util.ArrayList;
import java.util.List;

import org.spark.util.SparkUtils;

/**
 * @author Haoyu
 * 
 */
public class GRESCOption {

	public static final String GRE_OPTION_A = "A";

	public static final String GRE_OPTION_B = "B";

	public static final String GRE_OPTION_C = "C";

	public static final String GRE_OPTION_D = "D";

	public static final String GRE_OPTION_E = "E";
	
	public static final String GRE_MULTIBLANK_INDICATOR = "\\.\\.";

	/**
	 * the option_id indicates each option
	 */
	private String optionId;

	/**
	 * each option may contain several blanks need to fill in the sentence
	 */
	private List<String> blanks;

	/**
	 * validate a option in string format <br/>
	 * 
	 * @param option
	 * @return true if the option in string format is valid
	 */
	public static boolean isValidOption(String option) {
		if (GRE_OPTION_A.equals(option) || GRE_OPTION_B.equals(option)
				|| GRE_OPTION_C.equals(option) || GRE_OPTION_D.equals(option)
				|| GRE_OPTION_E.equals(option)) {
			return true;
		}
		return false;
	}

	/**
	 * load the gre option from text in the format: option_id
	 * option_word..option_word..<br/>
	 * 
	 * @param text
	 * @return
	 */
	public static GRESCOption fromText(String text, int index) {
		GRESCOption option = new GRESCOption();
		option.optionId = indexToOptionID(index);
		option.blanks = new ArrayList<String>();
		
		String[] multiBlank = text.split(GRE_MULTIBLANK_INDICATOR);
		for (int i = 0; i < multiBlank.length; i++) {
			option.blanks.add(SparkUtils.trimSectionBeginEndBlanks(multiBlank[i]));
		}
		
		return option;
	}

	public String getOptionId() {
		return optionId;
	}

	public List<String> getBlanks() {
		return blanks;
	}
	
	/**
	 * concatenate all blanks with character '+' if the option is a phrase
	 * 
	 * @return the concatenated string
	 */
	public String toConcatenatedString() {
		StringBuilder builder = new StringBuilder();
		for (String blank : blanks) {
			builder.append(blank);
			builder.append("+");
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}
	
	public static String indexToOptionID(int index) {
		String optionId = null;
		switch (index) {
		case 1:
			optionId = GRE_OPTION_A;
			break;
		case 2:
			optionId = GRE_OPTION_B;
			break;
		case 3:
			optionId = GRE_OPTION_C;
			break;
		case 4:
			optionId = GRE_OPTION_D;
			break;
		case 5:
			optionId = GRE_OPTION_E;
			break;
		default:
			break;
		}
		return optionId;
	}

}
