package tests.planToJira.complexObjects;


import java.util.ArrayList;


/**
 * Defines a CR item
 */
public class CrItem
{
	private String            b2gID;       // Represents the b2g ID
	private String            jiraID;      // Represents the Jira ID
	private String            status;      // Represents CR status
	private String            resolution;  // Represents CR resolution
	private String            summary;     // Represents CR summary
	private String            assignee;    // Represents CR assignee
	private String            product;    // Represents CR assignee
	private ArrayList<String> labels;      // Represents CR labels
	private String            dup;         // Represents CR dups
	private String            comment;     // Represents a comment to be inserted at this CR
	private String            closure_date;
	
	/**
	 * Class constructor
	 * 
	 * @param b2gID CR b2g ID
	 */
	public CrItem(String b2gID)
	{
		this.b2gID = b2gID;
		jiraID = "";
		status = "New";
		resolution = "Unresolved";
		comment = "";
		summary = "";
		assignee = "";
		dup = "";
	}
	
	/**
	 * Class constructor
	 */
	public CrItem()
	{
		b2gID = "";
		jiraID = "";
		status = "";
		resolution = "";
		comment = "No last comment loaded";
		summary = "";
		assignee = "";
		dup = "";
	}
	
	// Class to string
	public String toString()
	{
		return "Jira: " + jiraID + " || B2gID: " + b2gID + " || Status: " + status + " || Resolution: "
		       + resolution + " || Summary: " + summary + " || Assignee: " + assignee + " || Labels: " + labels + " || Dup: " + dup;
	}
	
	// Getters and Setters
	public String getB2gID()
	{
		return b2gID;
	}
	
	public String getJiraID()
	{
		return jiraID;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public String getResolution()
	{
		return resolution;
	}
	
	public String getComment()
	{
		return comment;
	}
	
	public void setB2gID(String b2gID)
	{
		this.b2gID = b2gID;
	}
	
	public void setJiraID(String jiraID)
	{
		this.jiraID = jiraID;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	public void setResolution(String resolution)
	{
		this.resolution = resolution;
	}
	
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	public String getSummary()
	{
		return summary;
	}
	
	public void setSummary(String summary)
	{
		this.summary = summary;
		
		if (summary.contains("B2GID:") && b2gID.equals(""))
		{
			int index = summary.lastIndexOf("B2GID:");
			b2gID = summary.substring(index + 6);
		}
	}
	
	public String getAssignee()
	{
		return assignee;
	}
	
	public void setAssignee(String assignee)
	{
		this.assignee = assignee;
	}
	
	public String getDup()
	{
		return dup;
	}
	
	public void setDup(String dup)
	{
		this.dup = dup;
	}
	
	public ArrayList<String> getLabels()
	{
		return labels;
	}
	
	public void setLabels(ArrayList<String> labels)
	{
		this.labels = labels;
	}
	
	public String getClosureDate()
	{
		return closure_date;
	}
	
	public void setClosureDate(String closure_date)
	{
		this.closure_date = closure_date;
	}

	public String getProduct()
	{
		return product;
	}

	public void setProduct(String product)
	{
		this.product = product;
	}
}
