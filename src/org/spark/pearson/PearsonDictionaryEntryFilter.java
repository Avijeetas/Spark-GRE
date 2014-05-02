package org.spark.pearson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class PearsonDictionaryEntryFilter {
	
	Collection<PearsonDictionaryEntry> entries;
	
	private HashSet<String> posTagConstrains = new HashSet<String>();
	
	String headword;
	
	public PearsonDictionaryEntryFilter(
			Collection<PearsonDictionaryEntry> entries, String headword) {
		super();
		this.entries = entries;
		this.headword = headword;
	}

	public PearsonDictionaryEntryFilter addPosTagFilter(String tag) {
		posTagConstrains.add(tag);
		return this;
	}
	
	/**
	 * filter the entries based on constrains. <br/> <br/>
	 * If the entry's head word equals to the head word, then the entry is filtered by its pos tag. If not, the entry is kept in the result set.
	 * 
	 * @return the filtered entries
	 */
	public Collection<PearsonDictionaryEntry> filter() {
		List<PearsonDictionaryEntry> retVal = new ArrayList<PearsonDictionaryEntry>();
		for (PearsonDictionaryEntry entry : entries) {
			if (entry.getHeadword() != null && headword != null) {
				if (entry.getHeadword().equals(headword)) {
					if (posTagConstrains.contains(entry.getPosTag())) {
						retVal.add(entry);
					}
				} else {
					retVal.add(entry);
				}
			}
		}
		return retVal;
	}
	
}
