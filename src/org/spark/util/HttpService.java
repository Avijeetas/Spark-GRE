package org.spark.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.FutureRequestExecutionService;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.HttpRequestFutureTask;
import org.apache.http.util.EntityUtils;
import org.spark.pearson.AbstractPearsonRequest;

public class HttpService {

	public static final String URL_PEARSON_API = "https://api.pearson.com/v2/dictionaries/entries";

	/**
	 * @param request
	 * @return response
	 * @throws IOException
	 */
	public String get(AbstractPearsonRequest request, String dict) throws IOException {
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
				SparkUtils.getLogger().info(response1.getStatusLine().toString());
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

	private String getParams(AbstractPearsonRequest request) {
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

	public static void test() {
		HttpClient httpclient = HttpClientBuilder.create()
				.setMaxConnPerRoute(5).setMaxConnTotal(5).build();
		ExecutorService execService = Executors.newFixedThreadPool(5);
		FutureRequestExecutionService requestExecService = new FutureRequestExecutionService(
				httpclient, execService);
		try {
			// Because things are asynchronous, you must provide a
			// ResponseHandler
			ResponseHandler<Boolean> handler = new ResponseHandler<Boolean>() {
				public Boolean handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					// simply return true if the status was OK
					HttpEntity entity1 = response.getEntity();
					// do something useful with the response body
					// and ensure it is fully consumed

					BufferedReader is = new BufferedReader(
							new InputStreamReader(entity1.getContent()));
					String r = null;
					StringBuilder builder = new StringBuilder();
					while ((r = is.readLine()) != null) {
						builder.append(r);
						builder.append("\n");
					}
					EntityUtils.consume(entity1);
					System.out.println(builder.toString());
					return response.getStatusLine().getStatusCode() == 200;
				}
			};

			// Simple request ...
			HttpGet request1 = new HttpGet(
					"https://api.pearson.com/v2/dictionaries/entries?related_words=get+rid+of");
			HttpRequestFutureTask<Boolean> futureTask1 = requestExecService
					.execute(request1, HttpClientContext.create(), handler);
			Boolean wasItOk1 = futureTask1.get();
			System.out.println("It was ok? " + wasItOk1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				requestExecService.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		test();
	}
}
