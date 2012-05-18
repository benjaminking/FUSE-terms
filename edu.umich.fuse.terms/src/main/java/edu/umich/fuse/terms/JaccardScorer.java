package edu.umich.fuse.terms;

import java.util.Map;

public class JaccardScorer implements TranslationScorer
{
	public double score(Map<String, Integer> corpus1Occ, Map<String, Integer> corpus2Occ)
	{
		int intersectionCount = 0;
				
		for(String docID : corpus1Occ.keySet())
		{
			if(corpus2Occ.containsKey(docID))
				intersectionCount++;
		}
		
		int unionCount = corpus1Occ.size() + corpus2Occ.size() - intersectionCount;
		
		if(unionCount > 0)
			return (double) intersectionCount / unionCount;
		return 0.0;
	}
	
}
