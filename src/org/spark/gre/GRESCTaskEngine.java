package org.spark.gre;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.spark.gre.GRESCBlank.GREBlankType;
import org.spark.pearson.PearsonDictionary;
import org.spark.pearson.PearsonDictionaryEntry;
import org.spark.pearson.PearsonDictionaryEntryFilter;
import org.spark.pearson.PearsonDictionaryHelper;
import org.spark.pearson.PearsonHeadwordRequest;
import org.spark.pearson.PearsonHeadwordResponse;
import org.spark.util.AbstractRequsetListener;
import org.spark.util.CoreNLPTagger;
import org.spark.util.SparkUtils;

/**
 * @author Haoyu
 *
 */
public class GRESCTaskEngine {
	
	public static final String GRE_SC_DEV_FILE_PATH = "/Users/apple/graduate/Courses/544NLP/Workspace/Spark/gre/GREVerbal.txt";
	
	/**
	 * load the GRE Sentence Completion Task
	 * 
	 * @return
	 * @throws IOException
	 */
	public static List<GRESCTask> load() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(GRE_SC_DEV_FILE_PATH)));
		List<GRESCTask> greTasks = new ArrayList<GRESCTask>();
		String line = null;
		List<String> task = new ArrayList<String>();
		while ((line = br.readLine()) != null) {
			if ("".equals(line.trim())) {
				continue;
			}
			task.add(line);
			if (line.contains(GRESCAnswer.GRE_ANSWER_INDICATOR)) {
				GRESCTask gTask = GRESCTask.fromText(task);
				greTasks.add(gTask);
			}
		}
		br.close();
		return greTasks;
	}
	
	/**
	 * generate all possible sentences in plain text given a GRE sentence completion task
	 * 
	 * @param task
	 * @return all possible sentences
	 */
	public static List<String> generateAllSentencesPlainText(GRESCTask task) {
		List<String> pSentences = new ArrayList<String>();
		List<GRESCOption> options = task.getOptions().get(0);
		for (GRESCOption option : options) {
			String sentence = String.format(task.getSentence(), option.getBlanks().toArray());
			pSentences.add(sentence);
		}
		return pSentences;
	}
	
	/**
	 * generate all possible sentences in plain text given a GRE sentence completion task. <br/>
	 * 
	 * @param task
	 * @return all possible sentences with blank surrounded by parentheses 
	 */
	public static List<String> generateAllSentences(GRESCTask task) {
		List<String> pSentences = new ArrayList<String>();
		List<GRESCOption> options = task.getOptions().get(0);
		for (GRESCOption option : options) {
			String sentence = task.getSentence().replaceAll("%s", "\\(%s\\)");
			sentence = String.format(sentence, option.getBlanks().toArray());
			pSentences.add(sentence);
		}
		return pSentences;
	}
	
	/**
	 * generate all possible sentences in plain text given a GRE sentence completion task. <br/>
	 * All blanks will also be replaces by their definitions found on Pearson Dictionary. 
	 * 
	 * @param task
	 * @return all possible sentences with blank surrounded by parentheses. 
	 */
	public static List<String> generateAllSentencesWithAllDefinitions(GRESCTask task) {
		List<String> pSentences = new ArrayList<String>();
		List<GRESCOption> options = task.getOptions().get(0);
		for (GRESCOption option : options) {
			String sentence = task.getSentence().replaceAll("%s", "\\(%s\\)");
			String formattedSentence = String.format(sentence, option.getBlanks().toArray());
			
			List<String> singleWord = new ArrayList<String>();
			List<String> blank = new ArrayList<String>();
			List<String> allWord = new ArrayList<String>();
			// dictionary look up
			for (String token : option.getBlanks()) {
				if (!SparkUtils.isPhrase(token)) {
					// it is a word
					allWord.add("%s");
					singleWord.add(token);
					blank.add(token);
				} else {
					// it is a phrase
					System.out.println(token);
					allWord.add(token);
				}
			}
			
			if (singleWord.size() > 0) {
				String dictSentence = String.format(sentence, allWord.toArray());
				List<List<String>> singleWordDefinitions = new ArrayList<List<String>>(); 
				
				// iterate the single word list and look up all single word using Pearson Dictionary
				for (int i = 0; i < singleWord.size(); i++) {
					PearsonHeadwordRequest request = new PearsonHeadwordRequest();
					request.setHeadWord(singleWord.get(i));
					try {
						PearsonHeadwordResponse response = PearsonDictionary.getDictionary().lookupDictionary(request);
						if (response != null) {
							response.response(response.getJsonResponse());
							List<PearsonDictionaryEntry> entries = response.getEntries();
							singleWordDefinitions.add(PearsonDictionary.getDictionary().getAllDefinitions(entries));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					singleWordDefinitions.get(i).add(singleWord.get(i));
				}

				// enumerate all possible combinations of the single word definitions
				List<List<String>> enumerations = SparkUtils.enumerate(singleWordDefinitions);
				for (List<String> ems : enumerations) {
					StringBuilder dictFormattedSentence = new StringBuilder(); 
					dictFormattedSentence.append(option.getOptionId());
					dictFormattedSentence.append(":");
					dictFormattedSentence.append(String.format(dictSentence, ems.toArray()));
					pSentences.add(dictFormattedSentence.toString());
				}
			} else {
				pSentences.add(formattedSentence);	
			}
		}
		return pSentences;
	}
	
	/**
	 * generate all possible sentences in plain text given a GRE sentence completion task. <br/><br/>
	 * All blanks will also be replaces by their definitions found on Pearson Dictionary.<br/> 
	 * The definition will be filtered by the pos tag of the blank if the blank only contains a single word.
	 * 
	 * @param task
	 * @return all possible sentences with blank surrounded by parentheses. 
	 */
	public static List<String> generateAllSentencesWithFilteredDefinitions(GRESCTask task) {
		List<String> pSentences = new ArrayList<String>();
		List<GRESCOption> options = task.getOptions().get(0);
		for (GRESCOption option : options) {
			String sentence = task.getSentence().replaceAll("%s", "\\(%s\\)");
			String formattedSentence = String.format(sentence, option.getBlanks().toArray());
			
			List<String> singleWord = new ArrayList<String>();
			List<String> blank = new ArrayList<String>();
			List<String> allWord = new ArrayList<String>();
			// dictionary look up
			for (String token : option.getBlanks()) {
				if (!SparkUtils.isPhrase(token)) {
					// it is a word
					allWord.add("%s");
					singleWord.add(token);
					blank.add(token);
				} else {
					// it is a phrase
					System.out.println(token);
					allWord.add(token);
				}
			}
			
			if (singleWord.size() > 0) {
				String dictSentence = String.format(sentence, allWord.toArray());
				List<String> singleWordTags = CoreNLPTagger.getTagger().tag(dictSentence, singleWord);
				List<List<String>> singleWordDefinitions = new ArrayList<List<String>>(); 
				
				// iterate the single word list and look up all single word using Pearson Dictionary
				for (int i = 0; i < singleWord.size(); i++) {
					PearsonHeadwordRequest request = new PearsonHeadwordRequest();
					request.setHeadWord(singleWord.get(i));
					try {
						PearsonHeadwordResponse response = PearsonDictionary.getDictionary().lookupDictionary(request);
						if (response != null) {
							response.response(response.getJsonResponse());
							List<PearsonDictionaryEntry> entries = response.getEntries();
							
							Collection<PearsonDictionaryEntry> filteredEntries = new PearsonDictionaryEntryFilter(entries, singleWord.get(i)).
									addPosTagFilter(PearsonDictionaryHelper.convert(singleWordTags.get(i))).
										filter();
							singleWordDefinitions.add(PearsonDictionary.getDictionary().getAllDefinitions(filteredEntries));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					singleWordDefinitions.get(i).add(singleWord.get(i));
				}

				// enumerate all possible combinations of the single word definitions
				List<List<String>> enumerations = SparkUtils.enumerate(singleWordDefinitions);
				for (List<String> ems : enumerations) {
					StringBuilder dictFormattedSentence = new StringBuilder(); 
					dictFormattedSentence.append(option.getOptionId());
					dictFormattedSentence.append(":");
					dictFormattedSentence.append(String.format(dictSentence, ems.toArray()));
					pSentences.add(dictFormattedSentence.toString());
				}
			} else {
				pSentences.add(formattedSentence);
			}
		}
		return pSentences;
	}
	
	/**
	 * generate all possible sentences given a GRE sentence completion task. <br/> 
	 * The context is all the words in the sentence that are given. The blanks are all possible combinations of all options. <br/>
	 * 
	 * @param task
	 * @param context
	 * @param blanks
	 */
	public static void generateContextAndBlanks(GRESCTask task, List<String> context, List<List<GRESCBlank>> blanks) {
		String[] tokens = task.getSentence().split(" ");
		for (String token : tokens) {
			if (! GRESCTask.GRE_FORMAT_BLANK_INDICATOR.equals(token)) {
				context.add(token);
			}
		}
		
		List<GRESCOption> options = task.getOptions().get(0);
		// iterate five options
		for (GRESCOption option : options) {
			final List<GRESCBlank> lb = new ArrayList<GRESCBlank>();
			// iterate option words
			if (option.getBlanks().size() == 1) {
				lb.add(new GRESCBlank(GREBlankType.Word, option.getBlanks()));
			} else if (GRESCRule.isRequiredLookupDictionaty(option)) {
				PearsonHeadwordRequest request = new PearsonHeadwordRequest();
				request.setHeadWord(option.getBlanks());
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
							List<String> definition = PearsonDictionary.getDictionary().getFirstDefinition(entries);
							lb.add(new GRESCBlank(GREBlankType.Definition, definition));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			} else {
				lb.add(new GRESCBlank(GREBlankType.Phrase, option.getBlanks()));
			}
		}
	}
	
	public static void main(String[] args) {
		
	}
}
