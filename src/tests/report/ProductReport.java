package tests.report;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

import panes.secondarypanes.ListPane;
import supportive.JiraQueryResult;
import supportive.JiraSatApi;
import tests.JiraUser;


public class ProductReport
{
	// Variables
	// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private String       name;
	private String       productID;
	private String[]     releases;
	private String       topIssueLabel;
	private String       dashboardLink;
	private String       spreadsheetLink;
	private Boolean      addChart;
	private String       chartBuild;
	private String       chartIssues;
	private Boolean      addHighlight;
	private String       highlights;
	private String       topIssues;
	private int          analyzedCRs;
	private String       htmlOutput;
	private HashMap<String, Long> issueDupsCount;
	
	// Final variables
	// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private final String topIssuesQuery = "labels = #top issues label#";
	private final String analyzedQuery  = "labels = cd_auto AND updated >= startOfDay() AND created >= startOfDay(-1d) AND (#releases#) ORDER BY key ASC";
	private final String chartIssuesCountQuery = "\\\"Duplicate CR\\\" ~ #issue key# AND labels = cd_auto AND (description ~ #chart build#)";
	
	// Constructor
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public ProductReport()
	{
		name = "";
		productID = null;
		releases = null;
		dashboardLink = "";
		spreadsheetLink = "";
		highlights = "";
		topIssues = "";
		addChart = false;
		chartBuild = "";
		chartIssues = "";
		addHighlight = false;
		analyzedCRs = -1;
		htmlOutput = "";
	}
	
	// Constructor with fields
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public ProductReport(String name, String[] releases, String dashboardLink, String spreadsheetLink, Boolean addHighlight, String highlights,
	                     Boolean addChart, String chartBuild, String chartIssues)
	{
		super();
		this.name = name;
		productID = null;
		;
		this.releases = releases;
		this.dashboardLink = dashboardLink;
		this.spreadsheetLink = spreadsheetLink;
		this.highlights = highlights;
		this.topIssues = "";
		this.addChart = addChart;
		this.chartBuild = chartBuild;
		this.chartIssues = chartIssues;
		this.addHighlight = addHighlight;
		this.analyzedCRs = -1;
	}
	
	// Data manipulation methods
	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private int getAnalyzedCRsFromJira()
	{
		JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL, JiraUser.getUser(), JiraUser.getPass());
		String queryReleases;
		
		if (releases.length > 1)
		{
			queryReleases = "description ~ " + releases[0].trim();
			for (int i = 1; i < releases.length; i++)
			{
				queryReleases += " OR description ~ " + releases[i];
			}
		}
		else
		{
			queryReleases = "description ~ " + releases[0].trim();
		}
		
		JiraQueryResult queryResult = new JiraQueryResult(jira.query(analyzedQuery.replace("#releases#", queryReleases)));
		analyzedCRs = (int) queryResult.getResultCount();
		
		System.out.println("Query results count: " + queryResult.getResultCount());
		
		return analyzedCRs;
	}
	
	private String getTopIssuesFromJira()
	{
		topIssues = "";
		
		JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL, JiraUser.getUser(), JiraUser.getPass());
		
		String productIDsArray[] = productID.split(";");
		String queryProducts;
		
		if (productIDsArray.length > 1)
		{
			queryProducts = "\\\"Product Affected\\\" = \\\"" + productIDsArray[0].trim() + "\\\"";
			for (int i = 1; i < productIDsArray.length; i++)
			{
				queryProducts += " OR \\\"Product Affected\\\" = \\\"" + productIDsArray[i].trim() + "\\\"";
			}
		}
		else
		{
			queryProducts = "\\\"Product Affected\\\" = \\\"" + productIDsArray[0].trim() + "\\\"";
		}
		
		JiraQueryResult queryResult;
		queryResult = new JiraQueryResult(jira.query(topIssuesQuery.replace("#productIDs#", queryProducts).replace("#top issues label#", topIssueLabel.trim())));
		
		for (int i = 0; i < queryResult.getItems().size(); i++)
		{
			topIssues += "<li>" + queryResult.getItems().get(i).getKey() + " - " + queryResult.getItems().get(i).getSummary() + "</li>\n";
		}
		
		System.out.println("Query results count: " + queryResult.getResultCount());
		
		return topIssues;
	}
	
	@SuppressWarnings("resource")
	private String configureProductHTML()
	{
		try
		{
			String product = new Scanner(new File("test files/html structures/addProductReport.txt")).useDelimiter("\\Z").next();
			product = product.replace("#product name#", name);
			product = product.replace("#analyzed crs#", "" + analyzedCRs);
			String releasesHTML = "";
			
			for (String r : releases)
			{
				releasesHTML += "<li>" + r + "</li>\n";
			}
			product = product.replace("#releases under analysis#", releasesHTML);
			
			if (!topIssues.equals(""))
			{
				product = product.replace("#top issues#", topIssues);
			}
			else
			{
				product = product.replace("#top issues#", "None");
			}
			product = product.replace("#dashboard link#", dashboardLink);
			product = product.replace("#spreadsheet link#", spreadsheetLink);
			
			if (addChart)
			{
				String chartHTML = new Scanner(new File("test files/html structures/addChart.txt")).useDelimiter("\\Z").next();
				product = product.replace("#Wakelock Chart#", chartHTML.replace("#image cid#", name.replace(" ", "_") + ""));
			}
			else
			{
				product = product.replace("#Wakelock Chart#", "");
			}
			
			htmlOutput = product;
			
			return product;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean generateChart()
	{
		JiraSatApi jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL, JiraUser.getUser(), JiraUser.getPass());
		long queryResultcount = -1;
		String issuesDupCount = "";
		ListPane lp = new ListPane();
		String chartBuilds[] = chartBuild.trim().split(" ");
		String queryBuilds = "";
		issueDupsCount = new HashMap<String, Long>();
		
		for(String key : chartIssues.split("\n"))
		{
			queryResultcount = 0;
			
			if(key.contains("-") && key.length() > 5)
			{
				if(chartBuilds.length > 1)
				{
					queryBuilds = chartBuilds[0];
					for(int i = 1; i < chartBuilds.length; i++)
					{
						queryBuilds += " OR description ~ " + chartBuilds;
					}
				}
				else
				{
					queryBuilds = chartBuild;
				}
				
				try
                {
	                if(queryBuilds.contains(jira.getCrData(key).getBuild()))
	                {
	                	queryResultcount++;
	                }
	                else
	                {
	                	for(String b : chartBuilds)
	                	{
	                		if(jira.getCrData(key).getDescription().contains(b))
	                		{
	                			queryResultcount++;
	                		}
	                	}
	                }
                }
                catch (ParseException e)
                {
	                e.printStackTrace();
                }
				
				System.out.println("Count result: " + queryResultcount);
				queryResultcount += jira.queryCount(chartIssuesCountQuery.replace("#chart build#", queryBuilds).replace("#issue key#", key.trim()));
				issuesDupCount += key + "\t" + queryResultcount;
				issueDupsCount.put(key, new Long(queryResultcount));
				lp.addItemList1(key);
				lp.addItemList2("" + queryResultcount);
			}
		}
		
		lp.showWindow();
		
		return false;
	}
	
	public String generateProductReport()
	{
		getAnalyzedCRsFromJira();
		getTopIssuesFromJira();
		generateChart();
		configureProductHTML();
		
		return htmlOutput;
	}
	
	// Getters and Setters
	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getProductID()
	{
		return productID;
	}
	
	public void setProductID(String productID)
	{
		this.productID = productID;
	}
	
	public String[] getReleases()
	{
		return releases;
	}
	
	public void setReleases(String[] releases)
	{
		this.releases = releases;
	}
	
	public String getDashboardLink()
	{
		return dashboardLink;
	}
	
	public void setDashboardLink(String dashboardLink)
	{
		this.dashboardLink = dashboardLink;
	}
	
	public String getSpreadsheetLink()
	{
		return spreadsheetLink;
	}
	
	public void setSpreadsheetLink(String spreadsheetLink)
	{
		this.spreadsheetLink = spreadsheetLink;
	}
	
	public String getHighlights()
	{
		return highlights;
	}
	
	public void setHighlights(String highlights)
	{
		this.highlights = highlights;
	}
	
	public Boolean getAddChart()
	{
		return addChart;
	}
	
	public void setAddChart(Boolean addChart)
	{
		this.addChart = addChart;
	}
	
	public Boolean getAddHighlight()
	{
		return addHighlight;
	}
	
	public void setAddHighlight(Boolean addHighlight)
	{
		this.addHighlight = addHighlight;
	}
	
	public String getHtmlOutput()
	{
		return htmlOutput;
	}
	
	public String getTopIssueLabel()
	{
		return topIssueLabel;
	}
	
	public void setTopIssueLabel(String topIssueLabel)
	{
		this.topIssueLabel = topIssueLabel;
	}
	
	public void setHtmlOutput(String htmlOutput)
	{
		this.htmlOutput = htmlOutput;
	}
	
	public int getAnalyzedCRs()
	{
		return analyzedCRs;
	}
	
	public String getReleasesString()
	{
		String sReleases = "";
		for (String r : releases)
		{
			sReleases += r + " ";
		}
		
		return sReleases.trim();
	}
	
	public void setAnalyzedCRs(int analyzedCRs)
	{
		this.analyzedCRs = analyzedCRs;
	}
	
	public String getChartBuild()
	{
		return chartBuild;
	}
	
	public void setChartBuild(String chartBuild)
	{
		this.chartBuild = chartBuild;
	}
	
	public String getChartIssues()
	{
		return chartIssues;
	}
	
	public void setChartIssues(String chartIssues)
	{
		this.chartIssues = chartIssues;
	}
}
