package edu.umich.fuse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utility
{
	public static String readTextFromFile(File file)
	{
		String text = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = reader.readLine()) != null)
			{
				if(text.length() > 0)
					text += "\n";
				text += line;
			}
		} catch (IOException e) {
			text = "";
		}
		try
		{
			reader.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return text;
	}
	
	public static List<String> readListFromFile(File file)
	{
		List<String> list = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = reader.readLine()) != null)
			{
				list.add(line);
			}
		} catch (IOException e) {
			
		}
		try
		{
			reader.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
}
