package edu.umich.fuse.terms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import edu.umich.fuse.Utility;

public class BatchChunker
{
	// removes punctuation and normalizes whitespace
	public static String clean(String str)
	{
		String clean = str.replaceAll("\\p{Punct}", " ");
		clean = clean.replaceAll("\\s+", " ");
		return clean.toLowerCase();
	}
	
	public static void main(String[] args) throws IOException
	{
		File inputDir = new File(args[0]);
		File outputDir = new File(args[1]);
		
		OpenNLPWrapper openNLP = new OpenNLPWrapper();
		File[] fileList = inputDir.listFiles();
		Arrays.sort(fileList);
		
		for(File inputFile : fileList)
		{
			System.out.println("Processing file " + inputFile.getName());
			String text = Utility.readTextFromFile(inputFile);
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputDir + File.separator + inputFile.getName())));
			
			for(String sentence : openNLP.sentenceSegment(text))
			{
				String[] tokens = openNLP.tokenize(sentence);
				String[] tags = openNLP.posTag(tokens);
				List<String> chunks = openNLP.getNPChunks(tokens, tags);
				
				for(String chunk : chunks)
				{
					writer.write("\"" + clean(chunk) + "\" ");
				}
				
				String tokenAccumulator = "";
				for(String token : tokens)
				{
					tokenAccumulator += token + " ";
				}
				writer.write(clean(tokenAccumulator) + "\n");
			}
			writer.close();
		}
	}
}
