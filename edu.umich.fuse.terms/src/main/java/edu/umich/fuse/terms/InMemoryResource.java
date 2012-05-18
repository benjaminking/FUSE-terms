package edu.umich.fuse.terms;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryResource implements Resource
{
	Map<String, IndexEntry> index = new HashMap<String, IndexEntry>();
	Map<String, Document> docsByID = new HashMap<String, Document>();
	String name;
	
	public InMemoryResource(File containingFolder)
	{
		System.out.println("Loading resource " + containingFolder.getName());
		
		this.name = containingFolder.getName();
		
		for(String fileName : containingFolder.list())
		{
			String id = "";
			if(fileName.contains("."))
				id = fileName.substring(0, fileName.indexOf("."));
			else
				id = fileName;
			String fullPath = containingFolder.getAbsolutePath() + File.separator + fileName;
			Document d = new Document(id, new File(fullPath));
			docsByID.put(id, d);
			indexDoc(id, d);
		}
	}
	
	void indexDoc(String id, Document d)
	{
		for(String token : d.getTokens())
		{
			if(index.containsKey(token))
			{
				index.get(token).addOccurence(id);
			}
			else
			{
				index.put(token, new IndexEntry(id));
			}
		}
	}

	public Map<String, Integer> searchForToken(String token)
	{
		if(index.containsKey(token))
			return index.get(token).getOccurenceCounts();
		return new HashMap<String, Integer>();
	}

	public List<String> getTokensById(String docID)
	{
		return docsByID.get(docID).getTokens();
	}
	
	
	
	class IndexEntry
	{
		Map<String, Integer> occurenceCounts = new HashMap<String, Integer>();
		
		public IndexEntry(String docID)
		{
			occurenceCounts.put(docID, 1);
		}
		
		public void addOccurence(String docID)
		{
			if(occurenceCounts.containsKey(docID))
			{
				occurenceCounts.put(docID, occurenceCounts.get(docID)+ 1);
			}
			else
			{
				occurenceCounts.put(docID, 1);
			}
		}
		
		public Map<String, Integer> getOccurenceCounts()
		{
			return occurenceCounts;
		}
	}



	public String getName()
	{
		return name;
	}
}
