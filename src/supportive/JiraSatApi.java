package supportive;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import core.Logger;
import core.SharedObjs;
import objects.CrItem;


/**
 * Jira rest interface for SAT
 */
public class JiraSatApi
{
	private String             username;
	private String             password;
	private String             baseURL;
	private Client             client;
	private final String       TAG                             = "JIRA REST";
	private final String       COMMENT_TAG                     = "/comment";
	private final String       ASSIGN_TAG                      = "/assignee";
	private final String       TRANSITION_TAG                  = "/transitions";
	public static final String DEFAULT_JIRA_URL                = "http://idart.mot.com/";
	public static final String CANCELLED                       = "7";
	public static final String INVALID                         = "13";
	public static final String INCOMPLETE                      = "4";
	public static final String UNREPRODUCIBLE                  = "5";
	public static final String FIXED_ON_3RD_PARTY_LOAD         = "16";
	public static final String BUSINESS_DECISION_NOT_TO_FIX    = "17";
	public static final String FIXED_IN_GOOGLE_ANDROID_RELEASE = "14";
	
	/**
	 * Create a new instance of jira rest interface
	 * 
	 * @param url Base url to jira (http://idart.com/)
	 * @param username Your username
	 * @param passwd Your password
	 */
	public JiraSatApi(String url, String username, String passwd)
	{
		this.username = username;
		
		if (url.charAt(url.length() - 1) == '/')
		{
			this.baseURL = url + "rest/api/2/issue/";
		}
		else
		{
			this.baseURL = url + "/rest/api/2/issue/";
		}
		
		this.password = passwd;
		this.client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(username, passwd));
	}
	
	/**
	 * Add a comment
	 * 
	 * @param key Jira issue key
	 * @param comment Your comment
	 * @return Server response
	 */
	public String addComment(String key, String comment)
	{
		WebResource webResource = client.resource(baseURL + key + COMMENT_TAG);
		
		ClientResponse response = webResource.type("application/json")
		                                     .post(ClientResponse.class, "{\"body\": \"" + comment + "\"}");
		
		String output = response.getEntity(String.class);
		
		Logger.log(TAG, "Add comment: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null,
			                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	/**
	 * Edit your last comment
	 * 
	 * @param key Jira issue key
	 * @param comment Your comment
	 * @return Server response
	 * @throws ParseException
	 */
	public String editLastComment(String key, String newComment) throws ParseException
	{
		JSONParser jsonParser = new JSONParser();
		JSONObject aux;
		JSONObject jsonObj = (JSONObject) jsonParser.parse(this.getComments(key));
		JSONArray comments = (JSONArray) jsonObj.get("comments");
		
		String id = "";
		String output = "You have no comments to edit.";
		
		for (int i = 0; i < comments.size(); i++)
		{
			jsonObj = (JSONObject) comments.get(i);
			aux = (JSONObject) jsonObj.get("author");
			
			if (aux.get("name").equals(username))
			{
				id = "/" + (String) jsonObj.get("id");
			}
		}
		
		if (!id.equals(""))
		{
			WebResource webResource = client.resource(baseURL + key + COMMENT_TAG + id);
			ClientResponse response = webResource.type("application/json").put(ClientResponse.class,
			                                                                   "{\"body\": \"" + newComment
			                                                                                   + "\"}");
			
			if (response.getStatus() != 204)
			{
				output = response.getEntity(String.class);
			}
			else
			{
				output = response.toString();
			}
			
			Logger.log(TAG, "Edit last comment: Output from Server:\n" + output);
			
			if (output.contains("error") && output.contains("403"))
			{
				JOptionPane.showMessageDialog(null,
				                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
			}
		}
		
		return output;
	}
	
	/**
	 * Return all comments as a JSON
	 * 
	 * @param key Jira issue key
	 * @return A JSON string with all comments
	 */
	public String getComments(String key)
	{
		WebResource webResource = client.resource(baseURL + key + COMMENT_TAG);
		ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
		String output = response.getEntity(String.class);
		
		Logger.log(TAG, "Get comments: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null,
			                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	/**
	 * Add a label
	 * 
	 * @param key Jira issue key
	 * @param label Label name
	 * @return Server response
	 */
	public String addLabel(String key, String label)
	{
		WebResource webResource = client.resource(baseURL + key);
		String output = "";
		String input = prepareInputFromFile("addLabel");
		input = input.replace("#given_label#", label);
		ClientResponse response = webResource.type("application/json").put(ClientResponse.class, input);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "Add label: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null,
			                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	/**
	 * Add many labels
	 * 
	 * @param key Jira issue key
	 * @param label Array of labels
	 * @return Server response
	 */
	public String addLabel(String key, String[] labels)
	{
		WebResource webResource = client.resource(baseURL + key);
		String input = prepareInputFromFile("addLabel");
		String output;
		
		for (int i = 1; i < labels.length; i++)
		{
			input = input.replace("#given_label#\"}", labels[i] + "\"}, {\"add\": \"#given_label#\"}");
		}
		
		input = input.replace("#given_label#", labels[0]);
		ClientResponse response = webResource.type("application/json").put(ClientResponse.class, input);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "Add many labels: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null,
			                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	/**
	 * Remove a label
	 * 
	 * @param key Jira issue key
	 * @param label Label name
	 * @return Server response
	 */
	public String removeLabel(String key, String label)
	{
		WebResource webResource = client.resource(baseURL + key);
		String output = "";
		String input = prepareInputFromFile("deleteLabel");
		input = input.replace("#given_label#", label);
		ClientResponse response = webResource.type("application/json").put(ClientResponse.class, input);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "Remove label: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null,
			                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	/**
	 * Remove many label
	 * 
	 * @param key Jira issue key
	 * @param label Array of label names
	 * @return Server response
	 */
	public String removeLabel(String key, String labels[])
	{
		WebResource webResource = client.resource(baseURL + key);
		String input = prepareInputFromFile("deleteLabel");
		String output;
		
		for (int i = 1; i < labels.length; i++)
		{
			input = input.replace("#given_label#\"}", labels[i] + "\"}, {\"remove\": \"#given_label#\"}");
		}
		
		input = input.replace("#given_label#", labels[0]);
		System.out.println("Input:\n" + input);
		
		ClientResponse response = webResource.type("application/json").put(ClientResponse.class, input);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "Remove many labels: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null,
			                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	/**
	 * Assign issue
	 * @param key Jira issue key
	 * @param user Username (coreid)
	 * @return Server response
	 */
	public String assignIssue(String key, String user)
	{
		WebResource webResource = client.resource(baseURL + key + ASSIGN_TAG);
		String output;
		String input = prepareInputFromFile("assignCR");
		input = input.replace("#given_coreid#", user);
		ClientResponse response = webResource.type("application/json").put(ClientResponse.class, input);
		
		System.out.println("Input:\n" + input);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "Assign issue: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null,
			                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	/**
	 * Assign issue
	 * @param key Jira issue key
	 * @return Server response
	 */
	public String assignIssue(String key)
	{
		WebResource webResource = client.resource(baseURL + key + ASSIGN_TAG);
		String output;
		String input = prepareInputFromFile("assignCR");
		input = input.replace("#given_coreid#", SharedObjs.getUser());
		ClientResponse response = webResource.type("application/json").put(ClientResponse.class, input);
		
		System.out.println("Input:\n" + input);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "Assign issue: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null,
			                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	/**
	 * Unassigns an issue
	 * @param key Jira issue key
	 * @return Server response
	 */
	public String unassignIssue(String key)
	{
		WebResource webResource = client.resource(baseURL + key + ASSIGN_TAG);
		String output;
		String input = prepareInputFromFile("assignCR");
		input = input.replace("\"#given_coreid#\"", "null");
		ClientResponse response = webResource.type("application/json").put(ClientResponse.class, input);
		
		System.out.println("Input:\n" + input);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "Unassign issue: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null,
			                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	/**
	 * Edit issue summary
	 * 
	 * @param key Jira issue key
	 * @param summary New summary
	 * @return Server response
	 */
	public String editSummary(String key, String summary)
	{
		WebResource webResource = client.resource(baseURL + key);
		String output;
		String input = prepareInputFromFile("editSummary");
		input = input.replace("#given_summary#", summary);
		ClientResponse response = webResource.type("application/json").put(ClientResponse.class, input);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "HTTP Request input: " + input);
		Logger.log(TAG, "Edit summary: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null,
			                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	/**
	 * Duplicate the issue to another CR
	 * 
	 * @param key Issue key
	 * @param dupkey Key of issue to be duplicate for
	 * @param comment Your comment
	 * @return Response from server
	 */
	public String dupIssue(String key, String dupkey, String comment)
	{
		WebResource webResource = client.resource(baseURL + key + TRANSITION_TAG);
		String output;
		String input = prepareInputFromFile("dupCr");
		input = input.replace("#given_dup#", dupkey);
		input = input.replace("#given_comment#", comment);
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "HTTP Request input: " + input);
		Logger.log(TAG, "Duplicate issue: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null,
			                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	/**
	 * Close a CR
	 * 
	 * @param key Jira key
	 * @param as Jira transition ID (use JiraSatApi.CANCELLED and so on)
	 * @param comment Your comment
	 * @return Server response
	 */
	public String closeIssue(String key, String as, String comment)
	{
		WebResource webResource = client.resource(baseURL + key + TRANSITION_TAG);
		String output;
		String input = prepareInputFromFile("closeCr");
		input = input.replace("#given_as#", as);
		input = input.replace("#given_comment#", comment);
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "HTTP Request input:\n" + input);
		Logger.log(TAG, "Close issue: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null,
			                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	/**
	 * Reopen a closed issue
	 * 
	 * @param key Issue key
	 * @return Server response
	 */
	public String reopenIssue(String key)
	{
		WebResource webResource = client.resource(baseURL + key + TRANSITION_TAG);
		String output;
		String input = prepareInputFromFile("reopenCr");
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
		
		if (response.getStatus() != 204)
		{
			output = response.getEntity(String.class);
		}
		else
		{
			output = response.toString();
		}
		
		Logger.log(TAG, "HTTP Request input: " + input);
		Logger.log(TAG, "Reopen issue: Output from Server:\n" + output);
		
		if (output.contains("error") && output.contains("403"))
		{
			JOptionPane.showMessageDialog(null,
			                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
		}
		
		return output;
	}
	
	@SuppressWarnings("unchecked")
	public CrItem getCrData(String key) throws ParseException
	{
		CrItem cr = new CrItem();
		
		WebResource webResource = client.resource(baseURL + key);
		ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
		
		String output = response.getEntity(String.class);
		
		if (output.contains("Unauthorized (401)"))
		{
			return null;
		}
		else if (output.contains("Issue Does Not Exist"))
		{
			JOptionPane.showMessageDialog(SharedObjs.crsManagerPane, "The CR " + key + " does not exists");
			return null;
		}
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = (JSONObject) jsonParser.parse(output);
		JSONObject fields = (JSONObject) jsonObj.get("fields");
		JSONObject aux;
		JSONArray labels;
		
		cr.setJiraID(jsonObj.get("key").toString()); // Get CR key
		
		aux = (JSONObject) fields.get("status"); // Get CR status
		cr.setStatus(aux.get("name").toString());
		if (cr.getStatus().equals("New"))
		{
			cr.setResolution("");
		}
		else
		{
			aux = (JSONObject) fields.get("resolution"); // Get CR resolution
			cr.setResolution(aux.get("name").toString());
		}
		
		cr.setSummary(fields.get("summary").toString()); // Get CR summary
		
		aux = (JSONObject) fields.get("assignee"); // Get CR assignee
		if (aux == null) // Check if it is unassigned
		{
			cr.setAssignee("");
		}
		else
		{
			cr.setAssignee(aux.get("name").toString());
		}
		
		aux = (JSONObject) fields.get("customfield_18027"); // Get CR assignee
		if (aux == null) // Check if it is unassigned
		{
			cr.setProduct("");
		}
		else
		{
			cr.setProduct(aux.get("value").toString());
		}
		
		labels = (JSONArray) fields.get("labels"); // Get CR labels
		if (labels != null) // Check if it is not null
		{
			cr.setLabels(labels);
		}
		
		if (fields.get("customfield_10622") == null
		    || fields.get("customfield_10622").toString().equals("null")) // Check if it has dups
		{
			cr.setDup("");
		}
		else
		{
			cr.setDup(fields.get("customfield_10622").toString()); // Get CR dups
		}
		
		if (output.contains("{\"errorMessages\":"))
		{
			Logger.log(TAG, "Get CR data: Output from Server: \n" + output);
			if (output.contains("403"))
			{
				JOptionPane.showMessageDialog(null,
				                              "Jira login needs a captcha answer.\nPlease, login manually to Jira to solve it.");
			}
		}
		else
		{
			Logger.log(TAG, "Get CR data: Output from Server: (Parsed CR data)\n" + cr);
		}
		
		return cr;
	}
	
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
