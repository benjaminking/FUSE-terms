package edu.umich.fuse.terms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

public class TranslationCandidate
{
	static int maxNumScores = 0;
	
	String candidate;
	Map<Integer, Double> scores = new HashMap<Integer, Double>();
	boolean isTranslation = false; // used for evaluation and training, but not testing
	
	public TranslationCandidate(String candidate) {
		super();
		this.candidate = candidate;
	}
	
	public static void setNumScores(int numScores)
	{
		maxNumScores = numScores;
	}

	public String getCandidate() {
		return candidate;
	}

	public double getScore(int key) {
		if(scores.containsKey(key))
			return scores.get(key);
		return 0.0;
	}
	
	public void addScore(int key, double value)
	{
		scores.put(key, value);
	}
	
	public boolean isTranslation()
	{
		return isTranslation;
	}
	
	public TranslationCandidate merge(TranslationCandidate other)
	{
		for(int key : other.scores.keySet())
		{
			scores.put(key, other.scores.get(key));
		}
		return this;
	}
	
	public static class TranslationCandidateComparator implements Comparator<TranslationCandidate>
	{
		int key;
		public TranslationCandidateComparator(int key)
		{
			this.key = key;
		}

		public int compare(TranslationCandidate arg0, TranslationCandidate arg1) {
			if(arg0.getScore(key) == arg1.getScore(key))
				return arg0.getCandidate().compareTo(arg1.getCandidate());
			return (new Double(arg1.getScore(key))).compareTo(arg0.getScore(key));		
		}
	}

	public void markAsTranslation()
	{
		isTranslation = true;
	}
	
	public Instance toInstance()
	{
		Instance instance = new SparseInstance(maxNumScores);
		for(int key : scores.keySet())
		{
			instance.put(key, scores.get(key));
		}
		if(isTranslation)
			instance.setClassValue(1);
		else
			instance.setClassValue(0);
		
		return instance;
	}

	@Override
	public String toString()
	{
		return "TranslationCandidate [candidate=" + candidate
				+ ", isTranslation=" + isTranslation + "]";
	}
	
	
}
