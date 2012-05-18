package edu.umich.fuse.terms;

import java.util.Map;

public interface TranslationScorer
{
	public double score(Map<String, Integer> corpus1Occ, Map<String, Integer> corpus2Occ);
}
