package supportive;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import objects.CrItem;
import objects.CrItemList;
import tests.JiraUser;

public class B2gIdFromJiraKeys
{
	public static void main(String[] args)
	{
		JiraSatApi jira = new JiraSatApi("http://idart.mot.com/rest/api/2/issue/", JiraUser.getUser(), JiraUser.getPass());
		
		CrItemList crsList = new CrItemList();
		
		for (String jiraKey : loadJiraKeys("crs.txt")){
			try
			{
				CrItem aux = jira.getCrData(jiraKey);
				System.out.println(aux.getB2gID());
				crsList.add(aux);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	private static ArrayList<String> loadJiraKeys(String filePath){
		BufferedReader br = null;
		String currentLine = "";
		ArrayList<String> jiraKeys = null;
		
		try
		{
			br = new BufferedReader(new FileReader(filePath));
			
			jiraKeys = new ArrayList<String>();
			
			while ((currentLine = br.readLine()) != null)
			{
				jiraKeys.add(currentLine);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (br != null)
					br.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		
		return jiraKeys;
	}
}
