package Objects;

public class crItem {
	
	private String b2gID;
	private String jiraID;
	private String status;
	private String resolution;
	private String comment;
	
	
	/**
	 * Constructor
	 */
	public crItem(String b2gID) {
		super();
		this.b2gID = b2gID;
		jiraID = null;
		status = "New";
		resolution = "Unresolved";
		comment = null;
	}
	
	
	/**
	 * Getters and Setters
	 */
	public String getB2gID() {
		return b2gID;
	}
	public String getJiraID() {
		return jiraID;
	}
	public String getStatus() {
		return status;
	}
	public String getResolution() {
		return resolution;
	}
	public String getComment() {
		return comment;
	}
	public void setB2gID(String b2gID) {
		this.b2gID = b2gID;
	}
	public void setJiraID(String jiraID) {
		this.jiraID = jiraID;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String toString(){
		return "Jira: " + jiraID + " || B2gID: " + b2gID + " || Status: " + status;  
	}
}
