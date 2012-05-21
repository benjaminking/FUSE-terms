package edu.umich.fuse.terms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import weka.classifiers.functions.SMO;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;

import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.tools.weka.WekaClassifier;

import libsvm.LibSVM;

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
			File resourceFile = new File(args[5 + i]);
			mainLangResources.add(new InMemoryResource(resourceFile));
		}
		
		for(int j=0; j<numSecondLangResources; ++j)
		{
			File resourceFile = new File(args[5 + numMainLangResources + j]);
			secondLangResources.add(new InMemoryResource(resourceFile));
		}
		
		List<AlignedResourcePair> resourcePairs = new ArrayList<AlignedResourcePair>();
		
		for(int i=0; i<numMainLangResources; ++i)
		{
			for(int j=0; j<numSecondLangResources; ++j)
			{
				AlignedResourcePair arp = new AlignedResourcePair(mainLangResources.get(i), secondLangResources.get(j));
				arp.setScorer(new JaccardScorer());
				resourcePairs.add(arp);
			}
		}
		
		TranslationCandidate.setNumScores(resourcePairs.size());
		
		List<String> terminologyList = Utility.readListFromFile(terminologyFile);
		List<String> terminologyCandidateList = Utility.readListFromFile(terminologyCandidateFile);
		
		GoldStandard goldStandard = new GoldStandard(goldStandardFile);
		
		System.out.println("Compiling candidate translations");
		
		//Dataset allInstances = new DefaultDataset();
		List<TermTranslationCandidates> termTranslationCandidates = new ArrayList<TermTranslationCandidates>();
		for(String term : terminologyList)
		{
			TermTranslationCandidates currentTermCandidates = new TermTranslationCandidates(term);
			for(AlignedResourcePair resourcePair : resourcePairs)
			{
				for(TranslationCandidate candidate : resourcePair.generateCandidateList(term, terminologyCandidateList))
				{
					if(goldStandard.isTranslationOf(term, candidate.getCandidate()))
						candidate.markAsTranslation();
					currentTermCandidates.addCandidate(candidate);				
				}
			}
			termTranslationCandidates.add(currentTermCandidates);
			//allInstances.addAll(currentTermCandidates.toDataset());
		}
		
		System.out.println("Evaluating candidates");
		
		// J48 best so far F=0.576
		//Bagging b = new Bagging();
		//b.setClassifier(new RandomForest());
		RandomForest rf = new RandomForest();
		CustomCrossValidation crossValidation = new CustomCrossValidation(new WekaClassifier(rf));
		Map<Object, PerformanceMeasure> results = crossValidation.crossValidation(termTranslationCandidates, 10);
		
		ResultPrinter printer = new ResultPrinter(results.get(1));
		printer.printResults();
	}
	
	public static void main(String[] args) throws IOException
	{
		multiPairDriver(args);
	}
}
