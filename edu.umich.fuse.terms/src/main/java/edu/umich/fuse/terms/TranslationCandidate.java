package edu.umich.fuse.terms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TranslationCandidate
{
	String candidate;
	Map<String, Double> scores = new HashMap<String, Double>();
	
	public TranslationCandidate(String candidate) {
		super();
		this.candidate = candidate;
	}

	public String getCandidate() {
		return candidate;
	}

	public double getScore(String key) {
		return scores.get(key);
	}
	
	public void addScore(String key, double value)
	{
		scores.put(key, value);
	}
	
	public static class TranslationCandidateComparator implements Comparator<TranslationCandidate>
	{
		String key;
		public TranslationCandidateComparator(String key)
		{
			this.key = key;
		}

		public int compare(TranslationCandidate arg0, TranslationCandidate arg1) {
			if(arg0.getScore(key) == arg1.getScore(key))
				return arg0.getCandidate().compareTo(arg1.getCandidate());
			return (new Double(arg1.getScore(key))).compareTo(arg0.getScore(key));		
		}
		
	}
	
	
}
