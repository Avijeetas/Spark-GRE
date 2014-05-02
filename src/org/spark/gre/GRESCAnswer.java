package org.spark.gre;

import java.util.ArrayList;
import java.util.List;

import org.spark.util.SparkUtils;

public class GRESCAnswer {

	private List<String> answers = new ArrayList<String>();

	public static final String GRE_ANSWER_INDICATOR = "answer:";

	/**
	 * load the answer of a gre sentence completion task from text. <br/>
	 * The texts is in the format: "answer:"
	 * {@link GRESCAnswer.GRE_ANSWER_INDICATOR} followed by answers separated by
	 * blanks.
	 * 
	 * @param text
	 * @return
	 */
	public static GRESCAnswer fromText(String text) {
		
		GRESCAnswer gAnswer = new GRESCAnswer();
		
		if (text != null && text.length() > GRE_ANSWER_INDICATOR.length()) {
			int answerIndex = text.indexOf(GRE_ANSWER_INDICATOR);
			if (answerIndex != -1) {
				String[] answers = text.substring(answerIndex + GRE_ANSWER_INDICATOR.length(), text.length()).split(" ");
				for (String answer : answers) {
					if (GRESCOption.isValidOption(answer)) {
						gAnswer.answers.add(answer);
					}
				}
			}
		}
		if (gAnswer.answers.size() == 0) {
			SparkUtils.getLogger().severe("ai");
		}
		return gAnswer;
	}
}
