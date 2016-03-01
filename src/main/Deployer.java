package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import supportive.JiraSatApi;
import tests.JiraUser;

public class Deployer
{
	
	public static void main(String[] args)
	{
		try
        {
			JiraSatApi jira;
			jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL, JiraUser.getUser(), JiraUser.getPass());
			
			File deployFile = new File("deploy.ini");
	        BufferedReader br = new BufferedReader(new FileReader(deployFile));
        }
        catch (FileNotFoundException e)
        {
	        e.printStackTrace();
        }
	}
}
