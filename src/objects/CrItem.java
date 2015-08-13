package objects;


/**
 * Defines a CR item
 */
public class CrItem
{
    private String b2gID;      // Represents the b2g ID
    private String jiraID;     // Represents the Jira ID
    private String status;     // Represents CR status
    private String resolution; // Represents CR resolution
    private String comment;    // Represents a comment to be inserted
			       // at this CR
    
    /**
     * Class constructor
     * 
     * @param b2gID
     *            CR b2g ID
     */
    public CrItem(String b2gID)
    {
	super();
	this.b2gID = b2gID;
	jiraID = null;
	status = "New";
	resolution = "Unresolved";
	comment = null;
    }
    
    // Class to string
    public String toString()
    {
	return "Jira: " + jiraID + " || B2gID: " + b2gID + " || Status: " + status;
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
}
