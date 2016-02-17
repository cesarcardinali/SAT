package supportive;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import tests.JiraUser;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import core.Logger;


/**
 * Jira rest interface for SAT
 */
public class BigQuerySatApi
{
	private String             username;
	private String             password;
	private String             baseURL;
	private Client             client;
	private final String       TAG              = "JIRA REST";
	private final String       SEARCH_TAG       = "search";
	public static final String DEFAULT_JIRA_URL = "http://idart.mot.com/";
	public static final String DEFAULT_BQ_URL   = "https://www.googleapis.com/bigquery/v2/projects/motorola.com:dogfood-analytics/queries";
	
	
	public static void main(String args[])
	{
		BigQuerySatApi bq = new BigQuerySatApi(JiraUser.getUser(), JiraUser.getPass());
	}
	
	
	/**
	 * Create a new instance of jira rest interface
	 * 
	 * @param url Base url to jira (http://idart.com/)
	 * @param username Your username
	 * @param passwd Your password
	 */
	public BigQuerySatApi(String username, String passwd)
	{
		this.username = username;
		this.password = passwd;
		this.baseURL = DEFAULT_BQ_URL;
		
		this.client = Client.create();
	}
	
	public String authBQ()
	{
		
		WebResource webResource = client.resource("https://accounts.google.com/o/oauth2/auth?redirect_uri=https%3A%2F%2Fdevelopers.google.com%2Foauthplayground&response_type=code&client_id=407408718192.apps.googleusercontent.com&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fbigquery&approval_prompt=force&access_type=offline");
		// webResource.header("Email", "cesarc@motorola.com");
		// webResource.header("Passwd", JiraUser.getPass());
		String output;
		String input = "Email=cesarc@motorola.com&Passwd=" + JiraUser.getPass();
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "BQ Auth: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null, "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	public String authCloudPlatformRead()
	{
		
		WebResource webResource = client.resource("https://www.googleapis.com/auth/cloud-platform.read-only");
		webResource.header("Authorization", "Bearer ya29.eAIWxFZ6LriYwr0q3My_T3VRJeflrBwXXsZqhaVGPlmOs3VJN1LZuBSqFYPmjJ2E-949");
		String output;
		ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "BQ Auth: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null, "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	public String authCloudPlatform()
	{
		
		WebResource webResource = client.resource("https://www.googleapis.com/auth/cloud-platform");
		webResource.header("username", "cesarc@motorola.com");
		webResource.header("user", "cesarc@motorola.com");
		webResource.header("Authorization", "Bearer ya29.eAIWxFZ6LriYwr0q3My_T3VRJeflrBwXXsZqhaVGPlmOs3VJN1LZuBSqFYPmjJ2E-949");
		String output;
		ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "cloud-platform Auth: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null, "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	/**
	 * Query issues
	 * 
	 * @param Jira query
	 * @return Server response
	 */
	public String query(String query)
	{
		WebResource webResource = client.resource(baseURL);
		webResource.header("Authorization", "Bearer ya29.eAIWxFZ6LriYwr0q3My_T3VRJeflrBwXXsZqhaVGPlmOs3VJN1LZuBSqFYPmjJ2E-949");
		String output;
		String input = prepareInputFromFile("bqquery");
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "HTTP Request input: " + input + "\n" + baseURL);
		Logger.log(TAG, "Query issues: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null, "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	// Supportive methods
	// Read file
	private String prepareInputFromFile(String file)
	{
		BufferedReader br = null;
		String currentLine;
		String input = "";
		
		try
		{
			br = new BufferedReader(new FileReader("Data/complements/jira/" + file + ".tmpl"));
			
			while ((currentLine = br.readLine()) != null)
			{
				input = input + currentLine;
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
		
		return input;
	}
	
	// Getters and Setters:
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public String getBaseURL()
	{
		return baseURL;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public void setBaseURL(String baseURL)
	{
		this.baseURL = baseURL;
	}
}
