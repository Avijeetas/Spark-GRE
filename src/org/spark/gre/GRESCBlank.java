package org.spark.gre;

import java.util.List;

public class GRESCBlank {
	public static enum GREBlankType {
		Word, Phrase, Definition;
	}
	
	GREBlankType type;
	
	List<String> words;
	
	public GRESCBlank(GREBlankType type, List<String> words) {
		super();
		this.type = type;
		this.words = words;
	}

	public GREBlankType getType() {
		return type;
	}

	public void setType(GREBlankType type) {
		this.type = type;
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}
}
