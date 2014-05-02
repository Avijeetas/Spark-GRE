package org.spark.gre;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.spark.pearson.PearsonDictionary;
import org.spark.pearson.PearsonDictionaryEntry;
import org.spark.pearson.PearsonHeadwordRequest;
import org.spark.pearson.PearsonHeadwordResponse;
import org.spark.util.AbstractRequsetListener;
import org.spark.util.SparkUtils;

public class GRESCTaskEngineTest {
	public static void testPearsonRequest() {
		PearsonHeadwordRequest request = new PearsonHeadwordRequest();
		request.setHeadWord("mildness");
		PearsonDictionary.getDictionary().lookupDictionary(request, new AbstractRequsetListener<PearsonHeadwordResponse>() {
			
			@Override
			public void onException(Exception exception) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(Error error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(PearsonHeadwordResponse response) {
				try {
					response.response(response.getJsonResponse());
					List<PearsonDictionaryEntry> entries = response.getEntries();
//					List<String> definition = PearsonDictionary.getDictionary().getFirstDefinition(entries);
					List<String> definitions = PearsonDictionary.getDictionary().getAllDefinitions(entries);
					for (String d : definitions) {
						System.out.println(d);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void loadGRETask() {
		GRESCTaskEngine engine = new GRESCTaskEngine();
		try {
			List<GRESCTask> tasks = engine.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadGRETaskAndWriteAllSentencePlainText() {
		GRESCTaskEngine engine = new GRESCTaskEngine();
		try {
			List<GRESCTask> tasks = engine.load();
			List<List<String>> sentences = new ArrayList<List<String>>();
			int index = 0;
			for (GRESCTask task : tasks) {
				sentences.add(engine.generateAllSentencesPlainText(task));
				index++;
			}
			System.out.println(index);
			SparkUtils.writeAllSentences(sentences, "test/plaintext.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadGRETaskAndWriteAllSentence() {
		GRESCTaskEngine engine = new GRESCTaskEngine();
		try {
			List<GRESCTask> tasks = engine.load();
			List<List<String>> sentences = new ArrayList<List<String>>();
			int index = 0;
			for (GRESCTask task : tasks) {
				sentences.add(engine.generateAllSentences(task));
				index++;
			}
			System.out.println(index);
			SparkUtils.writeAllSentences(sentences, "test/bi_plaintext.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadGRETaskAndWriteSentenceWithAllDefinition() {
		GRESCTaskEngine engine = new GRESCTaskEngine();
		try {
			List<GRESCTask> tasks = engine.load();
			List<List<String>> sentences = new ArrayList<List<String>>();
			List<String> se = engine.generateAllSentencesWithAllDefinitions(tasks.get(0));
			sentences.add(se);
			SparkUtils.writeAllSentences(sentences, "test/bi_all_d_plaintext.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadGRETaskAndWriteSentenceWithFilteredDefinition() {
		GRESCTaskEngine engine = new GRESCTaskEngine();
		try {
			List<GRESCTask> tasks = engine.load();
			List<List<String>> sentences = new ArrayList<List<String>>();
			for (GRESCTask task : tasks) {
				List<String> se = engine.generateAllSentencesWithFilteredDefinitions(task);
				sentences.add(se);
			}
			SparkUtils.writeAllSentences(sentences, "test/bi_d_plaintext.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		GRESCTaskEngineTest.loadGRETask();
//		GRESCTaskEngineTest.loadGRETaskAndWriteAllSentencePlainText();
		GRESCTaskEngineTest.loadGRETaskAndWriteAllSentence();
//		GRESCTaskEngineTest.loadGRETaskAndWriteSentenceWithAllDefinition();
//		GRESCTaskEngineTest.loadGRETaskAndWriteSentenceWithFilteredDefinition();
//		System.out.println(SparkUtils.trimSectionBeginEndBlanks("weferer"));;
//		GRESCTaskEngineTest.testPearsonRequest();
	}
}
