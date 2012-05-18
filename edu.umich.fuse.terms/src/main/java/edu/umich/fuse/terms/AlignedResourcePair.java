package edu.umich.fuse.terms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlignedResourcePair
{
	final int NUM_CANDIDATES = 10;
	
	Resource corpus1, corpus2;
	TranslationScorer scorer;
	
	public AlignedResourcePair(Resource corpus1, Resource corpus2)
	{
		this.corpus1 = corpus1;
		this.corpus2 = corpus2;
	}
	
	public void setScorer(TranslationScorer scorer)
	{
		this.scorer = scorer;
	}
	
	public List<TranslationCandidate> generateCandidateList(String corpus1Term, List<String> corpus2Terms)
	{
		List<TranslationCandidate> candidates = new ArrayList<TranslationCandidate>();
		for(String corpus2Term : corpus2Terms)
		{
			double score = scorer.score(corpus1.searchForToken(corpus1Term), corpus2.searchForToken(corpus2Term));
			TranslationCandidate tc = new TranslationCandidate(corpus2Term);
			tc.addScore(getName(), score);
			candidates.add(tc);
		}
		Collections.sort(candidates, new TranslationCandidate.TranslationCandidateComparator(this.getName()));
		candidates = candidates.subList(0, NUM_CANDIDATES);
		
		return candidates;
	}
	
	public Map<String, List<TranslationCandidate>> generateCandidateLists(List<String> corpus1Terms, List<String> corpus2Terms)
	{
		Map<String, List<TranslationCandidate>> candidates = new HashMap<String, List<TranslationCandidate>>();
		for(String corpus1Term : corpus1Terms)
		{
			candidates.put(corpus1Term, generateCandidateList(corpus1Term, corpus2Terms));
		}
		
		return candidates;
	}
	
	public String getName()
	{
		return corpus1.getName() + "_to_" + corpus2.getName();
	}
}
