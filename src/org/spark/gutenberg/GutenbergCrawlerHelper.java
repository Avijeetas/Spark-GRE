package org.spark.gutenberg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.spark.util.HttpService;
import org.spark.util.SparkUtils;

public class GutenbergCrawlerHelper {
	static final ExecutorService EXECUTOR_SERVICE = Executors
			.newFixedThreadPool(1000);

	static BufferedWriter downloadedUrlWriter;

	static HashSet<String> downloadedUrls;

	static HashSet<String> failedUrls;

	static BufferedWriter failedUrlWriter;

	static HashSet<String> unzippedFiles;

	static void unzipInit() {
		unzippedFiles = new HashSet<String>();
		File dir = new File(GutenbergCrawler.GUTENBERG_TXT_DIR);
		File[] files = dir.listFiles();
		for (File file : files) {
			int index = file.getName().lastIndexOf(".");
			String name = file.getName();
			if (index != -1) {
				name = file.getName().substring(0, index);
			}
			unzippedFiles.add(name);
		}
	}

	static void init() throws IOException {
		File downloadedUrlRecord = new File("gutenberg/url2.txt");
		if (!downloadedUrlRecord.exists()) {
			downloadedUrlRecord.createNewFile();
		}
		downloadedUrls = new HashSet<String>();
		BufferedReader br = new BufferedReader(new FileReader(
				downloadedUrlRecord));
		String line = null;
		while ((line = br.readLine()) != null) {
			downloadedUrls.add(line);
		}
		br.close();
		downloadedUrlWriter = new BufferedWriter(new FileWriter(
				downloadedUrlRecord));

		failedUrls = new HashSet<String>();
		File failedUrlRecord = new File("gutenberg/failurl2.txt");
		if (!failedUrlRecord.exists()) {
			failedUrlRecord.createNewFile();
		}
		failedUrlWriter = new BufferedWriter(new FileWriter(failedUrlRecord));
	}

	static void downloadFile(final String url) throws IOException {

		synchronized (downloadedUrls) {
			if (downloadedUrls.contains(url)) {
				SparkUtils.getLogger().info("url already downloaded:" + url);
				return;
			} else {
				downloadedUrls.add(url);
				synchronized (downloadedUrlWriter) {
					downloadedUrlWriter.write(url);
					downloadedUrlWriter.write("\n");
				}
			}
		}

		EXECUTOR_SERVICE.execute(new Runnable() {

			@Override
			public void run() {
				try {
					HttpService httpService = new HttpService();
					String[] ems = url.split("/");
					String saveUrl = GutenbergCrawler.GUTENBERG_ZIP_DIR
							+ ems[ems.length - 1];
					httpService.downloadFile(url, saveUrl);
					// SparkUtils.unzipFile(saveUrl,
					// GutenbergCrawler.GUTENBERG_TXT_DIR);
				} catch (IOException e) {
					try {
						synchronized (failedUrls) {
							failedUrls.add(url);
							synchronized (failedUrlWriter) {
								failedUrlWriter.write(url);
								failedUrlWriter.write("\n");
							}
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
	}

	static void downloadFailedFile(final String url) throws IOException {

		synchronized (downloadedUrls) {
			if (downloadedUrls.contains(url)) {
				SparkUtils.getLogger().info("url already downloaded:" + url);
				return;
			} else {
				downloadedUrls.add(url);
				synchronized (downloadedUrlWriter) {
					downloadedUrlWriter.write(url);
					downloadedUrlWriter.write("\n");
				}
			}
		}

		EXECUTOR_SERVICE.execute(new Runnable() {

			@Override
			public void run() {
				try {
					HttpService httpService = new HttpService();
					String[] ems = url.split("/");
					String saveUrl = GutenbergCrawler.GUTENBERG_FAILED_ZIP_DIR
							+ ems[ems.length - 1];
					httpService.downloadFile(url, saveUrl);
					// SparkUtils.unzipFile(saveUrl,
					// GutenbergCrawler.GUTENBERG_TXT_DIR);
				} catch (IOException e) {
					try {
						synchronized (failedUrls) {
							failedUrls.add(url);
							synchronized (failedUrlWriter) {
								failedUrlWriter.write(url);
								failedUrlWriter.write("\n");
							}
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
	}

	static void unzipFiles(final File file) throws IOException {

		EXECUTOR_SERVICE.execute(new Runnable() {

			@Override
			public void run() {
				int index = file.getName().lastIndexOf(".");
				String name = file.getName();
				if (index != -1) {
					name = file.getName().substring(0, index);
				}
				synchronized (GutenbergCrawlerHelper.unzippedFiles) {
					if (GutenbergCrawlerHelper.unzippedFiles.contains(name)) {
						return;
					}
				}
				SparkUtils.unzipFile(file.getAbsolutePath(),
						GutenbergCrawler.GUTENBERG_TXT_DIR);
				synchronized (unzippedFiles) {
					unzippedFiles.add(name);
				}
			}
		});
	}

	static BufferedWriter getBufferedWriter() {
		return downloadedUrlWriter;
	}
}
