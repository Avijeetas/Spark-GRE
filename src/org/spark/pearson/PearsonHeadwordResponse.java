package org.spark.pearson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PearsonHeadwordResponse extends
		AbstractPearsonResponse<PearsonHeadwordResponse> {

	public PearsonHeadwordResponse(String response) throws JSONException {
		super(response);
		// TODO Auto-generated constructor stub
	}

	private List<PearsonDictionaryEntry> entries;

	@Override
	public PearsonHeadwordResponse response(JSONObject jsonResponse)
			throws JSONException {
		JSONArray results = jsonResponse.getJSONArray(KEY_PEARSON_RESULTS);
		entries = new ArrayList<PearsonDictionaryEntry>();
		for (int i = 0; i < results.length(); i++) {
			entries.add(PearsonDictionaryEntry.fromJSON(results.optJSONObject(i)));
		}
		return this;
	}

	public List<PearsonDictionaryEntry> getEntries() {
		return entries;
	}
}
