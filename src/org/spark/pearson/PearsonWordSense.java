package org.spark.pearson;

import org.json.JSONException;
import org.json.JSONObject;

public class PearsonWordSense {
	private final static String KEY_PEARSON_TRANSLATION = "translation";

	private final static String KEY_PEARSON_DEFINITION = "definition";

	private final static String KEY_PEARSON_SYNONYM = "synonym";

	private final static String KEY_PEARSON_RELATED_WORD = "related_words";

	private final static String KEY_PEARSON_TRANSLATIONS = "translations";

	private final static String KEY_PEARSON_EXAMPLES = "examples";

	private final static String KEY_PEARSON_EXAMPLE_TEXT = "text";

	private final static String kEY_PEARSON_SUBSENSES = "subsenses";

	private String translation;

	private String definition;

	private String synonym;

	private String relatedWords;

	private String exampleText;

	public String getTranslation() {
		return translation;
	}

	public String getDefinition() {
		return definition;
	}

	public String getSynonym() {
		return synonym;
	}

	public String getRelatedWords() {
		return relatedWords;
	}

	public String getExampleText() {
		return exampleText;
	}

	public static PearsonWordSense fromJSON(JSONObject obj)
			throws JSONException {
		PearsonWordSense sense = new PearsonWordSense();
		sense.definition = obj.optString(KEY_PEARSON_DEFINITION);
		sense.relatedWords = obj.optString(KEY_PEARSON_RELATED_WORD);
		sense.synonym = obj.optString(KEY_PEARSON_SYNONYM);
		return sense;
	}

}
