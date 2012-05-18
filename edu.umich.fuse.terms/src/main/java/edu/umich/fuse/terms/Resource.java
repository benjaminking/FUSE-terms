package edu.umich.fuse.terms;

import java.util.List;
import java.util.Map;

public interface Resource
{
	public Map<String, Integer> searchForToken(String token);
	
	public List<String> getTokensById(String token);
	
	public String getName();
}
