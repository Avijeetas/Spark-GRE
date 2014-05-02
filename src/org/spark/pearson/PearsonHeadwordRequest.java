package org.spark.pearson;

import java.util.HashMap;
import java.util.List;

public class PearsonHeadwordRequest extends AbstractPearsonRequest {

	private static final String KEY_PEARSON_HEAD_WORD = "headword";
	
	private static final String KEY_PEARSON_PART_OF_SPEECH = "part_of_speech";
	
	private String headWord;
	
	private String part_of_speech;
	
	@Override
	public HashMap<String, String> params() {
		HashMap<String, String> params = new HashMap<String, String>();
		if (headWord != null) {
			params.put(KEY_PEARSON_HEAD_WORD, headWord);
		}
		if (part_of_speech != null) {
			params.put(KEY_PEARSON_PART_OF_SPEECH, part_of_speech);
		}
		return params;
	}

	public String getHeadWord() {
		return headWord;
	}

	public void setHeadWord(String headWord) {
		this.headWord = headWord;
	}
	
	public void setHeadWord(List<String> phrase) {
		StringBuilder builder = new StringBuilder();
		for (String word : phrase) {
			builder.append(word);
			builder.append("+");
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		this.headWord = builder.toString();
	}

	public String getPart_of_speech() {
		return part_of_speech;
	}

	public void setPart_of_speech(String part_of_speech) {
		this.part_of_speech = part_of_speech;
	}
}
