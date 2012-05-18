package edu.umich.fuse.terms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoldStandard
{
	Map<String, List<String>> correctTranslations = new HashMap<String, List<String>>();
	
	public GoldStandard(File file)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = reader.readLine()) != null)
			{
				String mainLang = line.split(" : ")[0];
				String translations = line.split(" : ")[1];
				
				List<String> transList = new ArrayList<String>();
				for(String secondLang : translations.split("; "))
					transList.add(secondLang);
				correctTranslations.put(mainLang, transList);
			}
		} catch(IOException ioe) {	}
	}
	
	public void addTranslation(String mainLang, String secondLang)
	{
		if(correctTranslations.containsKey(mainLang))
		{
			List<String> trans = correctTranslations.get(mainLang);
			trans.add(secondLang);
			correctTranslations.put(mainLang, trans);
		}
		else
		{
			List<String> trans = new ArrayList<String>();
			trans.add(secondLang);
			correctTranslations.put(mainLang, trans);
		}
	}
	
	public List<String> getTranslations(String mainLang)
	{
		if(correctTranslations.containsKey(mainLang))
		{
			return correctTranslations.get(mainLang);
		}
		return new ArrayList<String>();
	}
}
