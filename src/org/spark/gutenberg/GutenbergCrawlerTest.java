package org.spark.gutenberg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.spark.util.CoreNLPHelper;
import org.spark.util.SparkUtils;


public class GutenbergCrawlerTest {
	public static void main(String[] args) {
		try {
			toLowerCase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void toLowerCase() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File("wiki/wiki_latest_article.txt")));
		String line = null;
		File output = new File("wiki/wiki_lowcase.txt");
		if (!output.exists()) {
			output.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		while ((line = br.readLine()) != null) {
			bw.write(line.toLowerCase());
			bw.write("\n");
		}
		bw.close();
		br.close();
	}
	
	private static void combineFiles() {
		try {
			CoreNLPHelper.getHelper();
			SparkUtils.combineFiles("gutenberg/txt/", "gutenberg/guterberg_1g.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void unzipFiles() {
		try {
			SparkUtils.unzipFiles("gutenberg/zip/", "gutenberg/txt/");
//			SparkUtils.combineFiles("gutenberg/txt/", "gutenberg/guterberg.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
