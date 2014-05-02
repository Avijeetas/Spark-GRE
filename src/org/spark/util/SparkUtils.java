package org.spark.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SparkUtils {
	/**
	 * get the Spark Logger
	 * 
	 * @return spark logger
	 */
	public static Logger getLogger() {
		return Logger.getLogger("spark");
	}

	/**
	 * the text is a blank
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isBlank(String text) {
		if (text != null && !"".equals(text)) {
			return false;
		}
		return true;
	}

	/**
	 * Write all sentences into the file. If the output file doesn't exist,
	 * create the file at the given path
	 * 
	 * @param sentences
	 * @param outputFilePath
	 * @throws IOException
	 */
	public static void writeAllSentences(List<List<String>> sentences,
			String outputFilePath) throws IOException {
		File output = new File(outputFilePath);
		if (!output.exists()) {
			output.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		StringBuilder builder = new StringBuilder();
		for (List<String> group : sentences) {
			for (String sentence : group) {
				builder.append(sentence);
				builder.append("\n");
			}
			builder.append("\n");
		}
		bw.write(builder.toString());
		bw.close();
	}

	public static void outputTexts(List<List<String>> texts) {
		for (List<String> text : texts) {
			for (String t : text) {
				System.out.print(t + " ");
			}
			System.out.println();
		}
	}

	public static void outputText(List<String> texts) {
		for (String text : texts) {
			System.out.println(text);
		}
		System.out.println();
	}

	/**
	 * trim all blanks at the beginning of the text
	 * 
	 * @param text
	 * @return the trimmed text
	 */
	public static String trimSectionBegin(String text) {

		if (isBlank(text)) {
			return text;
		}

		boolean isBlank = false;

		int firstIndex = 0;

		if (text.length() >= 1) {
			if (text.charAt(0) == ' ') {
				isBlank = true;
				firstIndex++;
			}
		}

		for (int i = 1; i < text.length(); i++) {
			if (!(text.charAt(i) == ' ' && isBlank)) {
				break;
			} else {
				firstIndex++;
			}
		}
		return text.substring(firstIndex);
	}

	/**
	 * trim all blanks from the end of the text
	 * 
	 * @param text
	 * @return the trimmed text
	 */
	public static String trimSectionEndBlanks(String text) {
		if (isBlank(text)) {
			return text;
		}

		boolean isBlank = false;

		int endIndex = text.length();

		if (text.length() >= 1) {
			if (text.charAt(text.length() - 1) == ' ') {
				isBlank = true;
				endIndex--;
			}
		}

		for (int i = text.length() - 1; i > 1; i--) {
			if (!(text.charAt(i) == ' ' && isBlank)) {
				break;
			} else {
				endIndex--;
			}
		}
		if (endIndex + 1 < text.length()) {
			endIndex++;
		}
		return text.substring(0, endIndex);
	}

	/**
	 * trim all blanks at the beginning and at the end of text
	 * 
	 * @param text
	 * @return
	 */
	public static String trimSectionBeginEndBlanks(String text) {
		if (isBlank(text)) {
			return text;
		}

		boolean isBlank = false;

		int firstIndex = 0;

		if (text.length() >= 1) {
			if (text.charAt(0) == ' ') {
				isBlank = true;
				firstIndex++;
			}
		}

		for (int i = 1; i < text.length(); i++) {
			if (!(text.charAt(i) == ' ' && isBlank)) {
				break;
			} else {
				firstIndex++;
			}
		}
		
		isBlank = false;
		
		int endIndex = text.length();

		if (text.length() >= 1) {
			if (text.charAt(text.length() - 1) == ' ') {
				isBlank = true;
				endIndex--;
			}
		}

		for (int i = text.length() - 1; i >= 1; i--) {
			if (!(text.charAt(i) == ' ' && isBlank)) {
				break;
			} else {
				endIndex--;
			}
		}
		if (endIndex + 1 < text.length()) {
			endIndex++;
		}
		return text.substring(firstIndex, endIndex);
	}
	
	/**
	 * enumerate the texts and return all combinations. 
	 * 
	 * @param texts
	 * @return
	 */
	public static List<List<String>> enumerate(List<List<String>> texts) {
		enums = texts;
		result = new ArrayList<List<String>>();
		enumerate(0, new ArrayList<String>());
		return result;
	}
	
	static List<List<String>> enums = null;
	static List<List<String>> result = null;
	
	private static void enumerate(int currentIndex, List<String> temp) {
		
		if (currentIndex > enums.size() - 1) {
			result.add(temp);
			return;
		}
		
		for (String text : enums.get(currentIndex)) {
			temp.add(text);
			enumerate(currentIndex + 1, new ArrayList<String>(temp));
			temp.remove(text);
		}
	}
	
	public static boolean isPhrase(String token) {
		boolean wordStart = false;
		boolean blank = false;
		for (int i = 0; i < token.length(); i++) {
			if (token.charAt(i) != ' ') {
				if (!wordStart) {
					wordStart = true;
				}
				if (wordStart && blank) {
					return true;
				}
			} else {
				if (wordStart) {
					blank = true;
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(isPhrase("   hahah   h   "));;
	}
}
