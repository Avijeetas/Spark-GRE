package org.spark.pearson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PearsonDictionaryEntry {
	
	private final static String KEY_PEARSON_DICT_DATASET = "datasets";
	
	private final static String KEY_PEARSON_HEAD_WORD = "headword";
	
	private final static String KEY_PEARSON_ID = "id";
	
	private final static String KEY_PEARSON_POS = "part_of_speech";
	
	private final static String KEY_PEARSON_SENSES = "senses";
	
	private final static String KEY_PEARSON_URL = "url";

	private String headword;
	
	private String id;
	
	private String posTag;
	
	private String url;
	
	private List<String> datasets;
	
	private List<PearsonWordSense> senses;
	
	public static PearsonDictionaryEntry fromJSON(JSONObject obj) throws JSONException {
		PearsonDictionaryEntry entry = new PearsonDictionaryEntry();
		entry.headword = obj.optString(KEY_PEARSON_HEAD_WORD);
		entry.id = obj.optString(KEY_PEARSON_ID);
		entry.posTag = obj.optString(KEY_PEARSON_POS);
		entry.url = obj.optString(KEY_PEARSON_URL);
		entry.datasets = new ArrayList<String>();
		JSONArray datasets = obj.optJSONArray(KEY_PEARSON_DICT_DATASET);
		if (datasets != null) {
			for (int i = 0; i < datasets.length(); i++) {
				entry.datasets.add(datasets.optString(i));
			}
		}
		entry.senses = new ArrayList<PearsonWordSense>();
		JSONArray senses = obj.optJSONArray(KEY_PEARSON_SENSES);
		if (senses != null) {
			for (int i = 0; i < senses.length(); i++) {
				entry.senses.add(PearsonWordSense.fromJSON(senses.optJSONObject(i)));
			}
		}
		return entry;
	}

	public String getHeadword() {
		return headword;
	}

	public String getId() {
		return id;
	}

	public String getPosTag() {
		return posTag;
	}

	public String getUrl() {
		return url;
	}

	public List<String> getDatasets() {
		return datasets;
	}

	public List<PearsonWordSense> getSenses() {
		return senses;
	}
}
