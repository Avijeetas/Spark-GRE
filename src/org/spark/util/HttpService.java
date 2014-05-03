package org.spark.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.spark.pearson.PearsonHeadwordRequest;

public class HttpService {

	public static final String URL_PEARSON_API = "https://api.pearson.com/v2/dictionaries/entries";

	/**
	 * @param request
	 * @return response
	 * @throws IOException
	 */
	public String get(PearsonHeadwordRequest request, String dict)
			throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		StringBuilder builder = new StringBuilder();
		String params = getParams(request);
		StringBuilder requestUrl = new StringBuilder();
		requestUrl.append(URL_PEARSON_API);
		requestUrl.append(params);
		try {
			SparkUtils.getLogger().info(requestUrl.toString());
			HttpGet httpGet = new HttpGet(requestUrl.toString());
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			try {
				SparkUtils.getLogger().info(
						response1.getStatusLine().toString());
				HttpEntity entity1 = response1.getEntity();
				BufferedReader is = new BufferedReader(new InputStreamReader(
						entity1.getContent()));
				String r = null;
				while ((r = is.readLine()) != null) {
					builder.append(r);
					builder.append("\n");
				}
				SparkUtils.getLogger().info(builder.toString());
				EntityUtils.consume(entity1);
			} finally {
				response1.close();
			}
		} finally {
			httpclient.close();
		}
		return builder.toString();
	}

	private String getParams(AbstractRequest request) {
		StringBuilder params = new StringBuilder();
		params.append("?");
		Set<String> keys = request.params().keySet();
		for (String key : keys) {
			params.append(key);
			params.append("=");
			params.append(request.params().get(key));
			params.append("&");
		}
		params.deleteCharAt(params.length() - 1);
		return params.toString();
	}

	/**
	 * download a file from Internet and write it on local disk
	 * 
	 * @param fileUrl
	 * @param saveUrl
	 * @throws IOException
	 * @throws IOException
	 */
	public void downloadFile(String fileUrl, String saveUrl) throws IOException {
		SparkUtils.getLogger().info("download zip file: " + fileUrl);
		URL url = new URL(fileUrl);
		InputStream in = url.openStream();
		FileOutputStream out = new FileOutputStream(saveUrl);
		byte[] buffer = new byte[1024];
		int read = 0;
		while ((read = in.read(buffer)) >= 0) {
			out.write(buffer, 0, read);
		}
		SparkUtils.getLogger().info("downloaded zip file : " + saveUrl);
		in.close();
		out.close();
	}

	public static void main(String[] args) {

	}

}
