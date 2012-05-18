package edu.umich.fuse.terms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.umich.fuse.Utility;

public class Driver
{
	public static void singlePairDriver(String[] args) throws IOException
	{
		File resource1Folder = new File(args[0]);
		File resource2Folder = new File(args[1]);
		
		File terminologyList = new File(args[2]);
		
		Resource english = new InMemoryResource(resource1Folder);
		Resource spanish = new InMemoryResource(resource2Folder);
		
		AlignedResourcePair pair = new AlignedResourcePair(english, spanish);
		pair.setScorer(new JaccardScorer());
		
		List<String> spanishLexicon = new ArrayList<String>();
		BufferedReader lexiconReader = new BufferedReader(new FileReader(terminologyList));
		String line = "";
		while((line = lexiconReader.readLine()) != null)
		{
			spanishLexicon.add(line);
		}
		
		List<TranslationCandidate> candidates = pair.generateCandidateList("neural network", spanishLexicon);
		for(TranslationCandidate cand : candidates)
		{
			System.out.println(cand.toString());
		}
	}
	
	public static void multiPairDriver(String[] args)
	{
		File terminologyFile = new File(args[0]);
		File terminologyCandidateFile = new File(args[1]);
		File goldStandardFile = new File(args[2]);
		
		int numMainLangResources = Integer.parseInt(args[3]);
		int numSecondLangResources = Integer.parseInt(args[4]);
		
		List<Resource> mainLangResources = new ArrayList<Resource>();
		List<Resource> secondLangResources = new ArrayList<Resource>();
		
		for(int i=0; i<numMainLangResources; ++i)
		{
			File resourceFile = new File(args[4 + i]);
			mainLangResources.add(new InMemoryResource(resourceFile));
		}
		
		for(int j=0; j<numSecondLangResources; ++j)
		{
			File resourceFile = new File(args[4 + numMainLangResources + j]);
			secondLangResources.add(new InMemoryResource(resourceFile));
		}
		
		List<AlignedResourcePair> resourcePairs = new ArrayList<AlignedResourcePair>();
		
		for(int i=0; i<numMainLangResources; ++i)
		{
			for(int j=0; j<numSecondLangResources; ++j)
			{
				resourcePairs.add(new AlignedResourcePair(mainLangResources.get(i), mainLangResources.get(j)));
			}
		}
		
		List<String> terminologyList = Utility.readListFromFile(terminologyFile);
		List<String> terminologyCandidateList = Utility.readListFromFile(terminologyCandidateFile);
		
		GoldStandard goldStandard = new GoldStandard(goldStandardFile);
	}
	
	public static void main(String[] args) throws IOException
	{
		singlePairDriver(args);
	}
}
