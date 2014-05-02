package org.spark.gre;

import java.util.ArrayList;
import java.util.List;

import org.spark.util.SparkUtils;

/**
 * @author Haoyu
 *
 */
public class GRESCTask {
	
	static final String GRE_FORMAT_BLANK_INDICATOR = "%s";
	
	static final String GRE_BLANK_INDICATOR = "___";
	
	private String sentence;

	private GRESCAnswer answer;

	private List<List<GRESCOption>> options;
	
	/**
	 * The sentence completion task is in the format: <br/>
	 * the first line is the sentence that needs to be complete and followed by all options, <br/>
	 * the last line is the answer to this sentence completion task
	 * 
	 * @param text
	 * @return GRESCTask object
	 */
	public static GRESCTask fromText(List<String> texts) {
		if (texts == null || texts.size() < 7) {
			return null;
		}
		GRESCTask task = new GRESCTask();
		task.sentence = texts.get(0).replaceAll("(___)+", " %s ");
		int blankNum = 0;
		for (int i = 0; i < task.sentence.length(); i++) {
			if (task.sentence.charAt(i) == '%') {
				blankNum++;
			}
		}
		if (!task.sentence.contains("%s")) {
			SparkUtils.getLogger().severe(task.sentence);
		} 
		task.options = new ArrayList<List<GRESCOption>>();
		List<GRESCOption> options = new ArrayList<GRESCOption>();
		for (int i = 1; i < texts.size() - 1; i++) {
			GRESCOption option = GRESCOption.fromText(texts.get(i), i);
			if (option.getBlanks().size() != blankNum) {
				SparkUtils.getLogger().severe(task.sentence);
			}
			options.add(option);
			if (i % 5 == 0) {
				task.options.add(options);
				options = new ArrayList<GRESCOption>();
			}
		}
		task.answer = GRESCAnswer.fromText(texts.get(texts.size() - 1));
		texts.clear();
		return task;
	}

	public String getSentence() {
		return sentence;
	}

	public GRESCAnswer getAnswer() {
		return answer;
	}

	public List<List<GRESCOption>> getOptions() {
		return options;
	}
}
