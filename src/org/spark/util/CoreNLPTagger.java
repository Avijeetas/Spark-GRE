package org.spark.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class CoreNLPTagger {
	private static CoreNLPTagger tagger;

	public static CoreNLPTagger getTagger() {
		if (tagger == null) {
			tagger = new CoreNLPTagger();
		}
		return tagger;
	}
	
	private StanfordCoreNLP posPipeline;

	private CoreNLPTagger() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos");
		posPipeline = new StanfordCoreNLP(props);
	}

	public String tag(String text) {
		Annotation document = new Annotation(text);
		posPipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		StringBuilder builder = new StringBuilder();
		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String pos = token.get(PartOfSpeechAnnotation.class);
				if (word.equals("-LRB-")) {
					builder.append("(");
				} else if (word.equals("-RRB-")) {
					builder.append(")");
				} else if (word.equals("``") || word.equals("''")) {
					builder.append("\"");
				} else {
					builder.append(word);
				}
				builder.append("/");
				builder.append(pos);
				builder.append(" ");
			}
		}
		return builder.toString();
	}
	
	/**
	 * 
	 * 
	 * @param sentence
	 * @param blanks
	 * @return
	 */
	public List<String> tag(String sentence, List<String> blanks) {
		List<Integer> blanksIndices = new ArrayList<Integer>();
		String trimedSentence = sentence.trim();
		System.out.println(trimedSentence);
		int index = 0;
		int blankIndex = 0;
		int sum = 0;
		for (int i = 0; i < sentence.length(); i++) {
			if (sentence.charAt(i) != ' ') {
				if (sentence.charAt(i) == '%') {
					if (sum != 0) {
						sum -= 2;
					}
					sum += blanks.get(blankIndex).trim().length();
					blanksIndices.add(index + sum);
					blankIndex++;
				}
				index++;
			}
		}
		
		sentence = String.format(sentence, blanks.toArray());
		System.out.println(sentence.trim());
		index = 0;
		
		Annotation document = new Annotation(sentence);
		posPipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		List<String> tags = new ArrayList<String>();
		
		for (CoreMap tagS : sentences) {
			for (CoreLabel token : tagS.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String tag = token.get(PartOfSpeechAnnotation.class);
				if (word.equals("-LRB-")) {
					index++;
				} else if (word.equals("-RRB-")) {
					index++;
				} else if (word.equals("``") || word.equals("''")) {
					index++;
				} else {
					index += word.length();
				}
				for (Integer bi : blanksIndices) {
					if (bi.intValue() == index) {
						System.out.println(word);
						System.out.println(tag);
						tags.add(tag);
					}
				}
			}
		}
		return tags;
	}

	public String tokenize(String text) {
		Annotation document = new Annotation(text);
		posPipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		StringBuilder builder = new StringBuilder();
		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				if (word.equals("-LRB-")) {
					builder.append("(");
				} else if (word.equals("-RRB-")) {
					builder.append(")");
				} else if (word.equals("``") || word.equals("''")) {
					builder.append("\"");
				} else {
					builder.append(word);
				}
				builder.append(" ");
			}
		}
		return builder.toString();
	}
	
	public static void main(String[] args) throws IOException {
		
		String text = "Early critics of Emily Dickinson's poetry %s for simplemindedness the surface of artlessness that in fact she constructed with such   %s  . ";
		List<String> blank = new ArrayList<String>();
		blank.add(" mistook  ");
		blank.add(" astonishment ");
		List<String> tags = CoreNLPTagger.getTagger().tag(text, blank);
		SparkUtils.outputText(tags);
	}
}
