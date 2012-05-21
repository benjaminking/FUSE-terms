package edu.umich.fuse.terms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.umich.fuse.Utility;

public class Document
{
	String id;
	List<String> tokens = new ArrayList<String>();
	
	public Document(String id)
	{
		this.id = id;
	}
	
	public Document(String id, String text)
	{
		this.id = id;
		
		text = addAndRemoveQuotedTerms(text);
		addOtherTerms(text);
	}
	
	String addAndRemoveQuotedTerms(String text)
	{
		Pattern p = Pattern.compile("\\\"(.*?)\\\""); // Phrases in double quotes
		Matcher m = p.matcher(text);
		while(m.find())
		{
			tokens.add(m.group(1).toLowerCase());
		}
		text = m.replaceAll("");
		return text;
	}
	
	void addOtherTerms(String text)
	{
		for(String token : text.split("\\W+"))
		{
			tokens.add(token.toLowerCase());
		}
	}
	
	public Document(String id, File location)
	{
		this.id = id;
		
		String text = Utility.readTextFromFile(location);
		text = addAndRemoveQuotedTerms(text);
		addOtherTerms(text);
	}
	
	public String getId() {
		return id;
	}

	public List<String> getTokens() {
		return tokens;
	}
}
