package org.spark.pearson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.spark.util.AbstractRequsetListener;
import org.spark.util.HttpService;
import org.spark.util.SparkUtils;

/**
 * @author Haoyu
 *
 */
public class PearsonDictionary {

	private static final PearsonDictionary pd = new PearsonDictionary();
	
	public static final PearsonDictionary getDictionary() {
		return pd;
	}
	
	ExecutorService executorService = Executors.newCachedThreadPool();

	public PearsonHeadwordResponse lookupDictionary(
			PearsonHeadwordRequest request) throws JSONException, IOException {
		HttpService httpService = new HttpService();
		PearsonHeadwordResponse headwordResponse = null;
		String response = httpService.get(request, "");
		headwordResponse = new PearsonHeadwordResponse(response);
		return headwordResponse;
	}

	public void lookupDictionary(final PearsonHeadwordRequest request,
			final AbstractRequsetListener<PearsonHeadwordResponse> listener) {
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					PearsonHeadwordResponse response = lookupDictionary(request);
					if (response != null) {
						if (listener != null) {
							listener.onComplete(response);
						}
					}
				} catch (JSONException e) {
					if (listener != null) {
						listener.onException(e);
					}
				} catch (IOException e) {
					if (listener != null) {
						listener.onException(e);
					}
				}
			}
		});
	}
	
	public PearsonHeadwordResponse lookupDictionary(
			PearsonDictionaryType dictionary, PearsonHeadwordRequest request)
			throws IOException, JSONException {
		HttpService httpService = new HttpService();
		PearsonHeadwordResponse headwordResponse = null;
		String response = httpService.get(request,
				dictionary.getDictionaryName());
		headwordResponse = new PearsonHeadwordResponse(response);
		return headwordResponse;
	}
	
	public void lookupDictionary(final PearsonDictionaryType dictionary, final PearsonHeadwordRequest request,
			final AbstractRequsetListener<PearsonHeadwordResponse> listener) {
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					PearsonHeadwordResponse response = lookupDictionary(dictionary, request);
					if (response != null) {
						if (listener != null) {
							listener.onComplete(response);
						}
					}
				} catch (JSONException e) {
					if (listener != null) {
						listener.onException(e);
					}
				} catch (IOException e) {
					if (listener != null) {
						listener.onException(e);
					}
				}
			}
		});
	}
	
	/**
	 * get the first definition in the entries
	 * 
	 * @param entries
	 * @return the first definition in the entries
	 */
	public List<String> getFirstDefinition(Collection<PearsonDictionaryEntry> entries) {
		List<String> definition = new ArrayList<String>();
		for (PearsonDictionaryEntry entry : entries) {
			for (PearsonWordSense sense : entry.getSenses()) {
				if (sense.getDefinition() != null && !"".equals(sense.getDefinition())) {
					String[] ems = sense.getDefinition().split(" ");
					definition = Arrays.asList(ems);
					break;
				}
			}
		}
		return definition;
	}
	
	/**
	 * @param entries
	 * @return
	 */
	public List<String> getAllDefinitions(Collection<PearsonDictionaryEntry> entries) {
		List<String> definitions = new ArrayList<String>();
		for (PearsonDictionaryEntry entry : entries) {
			for (PearsonWordSense sense : entry.getSenses()) {
				if (!SparkUtils.isBlank(sense.getDefinition())) {
					definitions.add(sense.getDefinition());
				}
			}
		}
		return definitions;
	}
}
