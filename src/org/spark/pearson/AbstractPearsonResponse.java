package org.spark.pearson;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractPearsonResponse<T> {
	
	static final String KEY_PEARSON_RESPONSE_STATUS = "status";
	
	static final String KEY_PEARSON_RESPONSE_OFFSET = "offset";
	
	static final String KEY_PEARSON_URL = "url";
	
	static final String KEY_PEARSON_RESULTS = "results";
	
	protected int status;
	
	protected int offset;
	
	protected String url;
	
	protected JSONObject jsonResponse;
	
	public AbstractPearsonResponse(String response) throws JSONException {
		jsonResponse = new JSONObject(response);
		status = jsonResponse.optInt(KEY_PEARSON_RESPONSE_STATUS);
		offset = jsonResponse.optInt(KEY_PEARSON_RESPONSE_OFFSET);
		url = jsonResponse.optString(KEY_PEARSON_URL);
	}

	public abstract T response(JSONObject jsonResponse) throws JSONException;
	
	public int getStatus() {
		return status;
	}

	public int getOffset() {
		return offset;
	}

	public String getUrl() {
		return url;
	}

	public JSONObject getJsonResponse() {
		return jsonResponse;
	}
	
}
