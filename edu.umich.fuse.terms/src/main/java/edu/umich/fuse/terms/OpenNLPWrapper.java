package edu.umich.fuse.terms;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class OpenNLPWrapper
{
	String modelLoc = "/home/ben/Tools/apache-opennlp-1.5.2-incubating/models";
	
	public OpenNLPWrapper() { }
	
	public OpenNLPWrapper(String modelLoc)
	{
		this.modelLoc = modelLoc;
	}
	
	public String[] sentenceSegment(String text)
	{
		InputStream modelIn = null;
		try {
			modelIn = new FileInputStream(modelLoc + "/en-sent.bin");
			SentenceModel model = new SentenceModel(modelIn);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
			return sentenceDetector.sentDetect(text);
		}
		catch (IOException e) {
		  e.printStackTrace();
		}
		finally {
		  if (modelIn != null) {
		    try {
		      modelIn.close();
		    }
		    catch (IOException e) {
		    }
		  }
		}
		return null;
	}
	
	public String[] tokenize(String sentence)
	{
		InputStream modelIn = null;

		try {
			modelIn = new FileInputStream(modelLoc + "/en-token.bin");
			TokenizerModel model = new TokenizerModel(modelIn);
			Tokenizer tokenizer = new TokenizerME(model);
			return tokenizer.tokenize(sentence);
		}
		catch (IOException e) {
		  e.printStackTrace();
		}
		finally {
		  if (modelIn != null) {
		    try {
		      modelIn.close();
		    }
		    catch (IOException e) {
		    }
		  }
		}
		return null;
	}
	
	public String[] posTag(String[] tokens)
	{
		InputStream modelIn = null;

		try {
		  modelIn = new FileInputStream(modelLoc + "/en-pos-maxent.bin");
		  POSModel model = new POSModel(modelIn);
		  POSTaggerME tagger = new POSTaggerME(model);
		  return tagger.tag(tokens);
		}
		catch (IOException e) {
		  // Model loading failed, handle the error
		  e.printStackTrace();
		}
		finally {
		  if (modelIn != null) {
		    try {
		      modelIn.close();
		    }
		    catch (IOException e) {
		    }
		  }
		}
		return null;
	}
	
	public String[] chunk(String[] tokens, String[] tags)
	{
		InputStream modelIn = null;
		ChunkerModel model = null;

		try {
			modelIn = new FileInputStream(modelLoc + "/en-chunker.bin");
			model = new ChunkerModel(modelIn);
			ChunkerME chunker = new ChunkerME(model);
			return chunker.chunk(tokens, tags);
		}
		catch (IOException e) {
			// Model loading failed, handle the error
			e.printStackTrace();
		}
		finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				}
				catch (IOException e) {
				}
			}
		}
		return null;
	}
	
	public List<String> getNPChunks(String[] tokens, String[] tags)
	{
		List<String> npChunks = new ArrayList<String>();
		String[] chunks = chunk(tokens, tags);
		
		String currentChunk = "";
		for(int i=0; i<chunks.length; ++i)
		{
			if(chunks[i].equals("B-NP"))
			{
				if(currentChunk.length() > 0 && currentChunk.contains(" "))
				{
					npChunks.add(currentChunk);
					currentChunk = "";
				}
				if(!tags[i].equals("DT"))
					currentChunk = tokens[i];
			}
			else if(chunks[i].equals("I-NP"))
			{
				if(currentChunk.length() > 0)
					currentChunk += " ";
				currentChunk += tokens[i];
			}
			else if(currentChunk.length() > 0 && currentChunk.contains(" "))
			{
				npChunks.add(currentChunk);
				currentChunk = "";
			}
			else
				currentChunk = "";
		}
		return npChunks;
	}
	
	public List<String> getNPChunks(String sentence)
	{
		String[] tokens = tokenize(sentence);
		String[] tags = posTag(tokens);
		return getNPChunks(tokens, tags);
	}
}
