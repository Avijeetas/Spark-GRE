package org.spark.gutenberg;

import java.io.IOException;


public class GutenbergCrawlerTest {
	public static void main(String[] args) {
		GutenbergCrawler crawler = new GutenbergCrawler();
		try {
			crawler.downloadFailedFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			SparkUtils.combineFiles("gutenberg/txt/", "gutenberg/guterberg.txt");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
