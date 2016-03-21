package tests.report;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import objects.CrItem;

import org.jfree.data.general.DefaultPieDataset;
import org.json.simple.parser.ParseException;

import panes.secondarypanes.ListPane;
import supportive.JiraQueryResult;
import supportive.JiraSatApi;
import tests.PieChartBuilder;


public class ProductReport
{
	// Variables
	// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private String            name;
	private String            productID;
	private String[]          releases;
	private String            topIssueLabel;
	private String            dashboardLink;
	private String            spreadsheetLink;
	private Boolean           addChart;
	private String            chartBuild;
	private boolean           separatedCharts;
	private String            chartUserdebugIssues;
	private String            chartUserIssues;
	private Boolean           addHighlight;
	private String            highlights;
	private String            topIssues;
	private int               analyzedCRs;
	private String            htmlOutput;
	private String            user;
	private String            pass;
	private DefaultPieDataset issueDupsCount;
	private DefaultPieDataset issueUserDupsCount;
	private JiraSatApi        jira;
	
	// Final variables
	// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private final String      topIssuesQuery        = "labels = #top issues label#";
	private final String      topIssuesHTMLTemplate = "<li><a href=\"http://idart.mot.com/browse/#cr key#\" target=\"_blank\">#cr key#</a> - #summary#</li>";
	private final String      analyzedQuery         = "labels = cd_auto AND updated >= startOfDay() AND created >= startOfDay(-1d) AND (#releases#) ORDER BY key ASC";
	private final String      chartIssuesCountQuery = "\\\"Duplicate CR\\\" ~ #issue key# AND labels = cd_auto AND (description ~ #chart build#)";
	private final String      chartsOutputFolder    = "Data/complements/report/charts/";
	private final String      htmlTemplatesFolder   = "Data/complements/report/templates/";
	
	// Constructor ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public ProductReport()
	{
		name = "";
		productID = "";
		releases = null;
		dashboardLink = "";
		spreadsheetLink = "";
		highlights = "";
		topIssues = "";
		addChart = false;
		separatedCharts = false;
		chartBuild = "";
		chartUserdebugIssues = "";
		chartUserIssues = "";
		addHighlight = false;
		analyzedCRs = -1;
		htmlOutput = "";
	}
	
	// Constructor with fields ------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public ProductReport(String name)
	{
		super();
		this.name = name;
		name = "";
		productID = "";
		releases = null;
		dashboardLink = "";
		spreadsheetLink = "";
		highlights = "";
		topIssues = "";
		addChart = false;
		separatedCharts = false;
		chartBuild = "";
		chartUserdebugIssues = "";
		chartUserIssues = "";
		addHighlight = false;
		analyzedCRs = -1;
		htmlOutput = "";
	}
	
	// Data manipulation methods
	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private int getAnalyzedCRsFromJira()
	{
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
			topIssues += topIssuesHTMLTemplate.replace("#cr key#", queryResult.getItems().get(i).getKey()).replace("#summary#",
			                                                                                                       queryResult.getItems().get(i).getSummary());
		}
		
		System.out.println("Query results count: " + queryResult.getResultCount());
		
		return topIssues;
	}
	
	public boolean generateChart()
	{
		ListPane lp = new ListPane();
		CrItem crAux;
		long queryResultcount = -1;
		// long totalDupsCount = 0;
		String chartBuilds[];
		String queryBuilds;
		
		if (separatedCharts)
		{
			ListPane lp2 = new ListPane();
			lp.setListTitle(name + " Userdebug version results");
			lp2.setListTitle(name + "User version results");
			chartBuilds = chartBuild.trim().split(" ");
			queryBuilds = "";
			issueDupsCount = new DefaultPieDataset();
			issueUserDupsCount = new DefaultPieDataset();
			
			// Build the database for userdebug chart
			for (String key : chartUserdebugIssues.split("\n"))
			{
				queryResultcount = 0;
				
				// Check if key is valid
				if (key.contains("-") && key.length() > 5)
				{
					// Check how many builds shall be used to create the chart
					if (chartBuilds.length > 1)
					{
						queryBuilds = chartBuilds[0];
						for (int i = 1; i < chartBuilds.length; i++)
						{
							queryBuilds += " OR description ~ " + chartBuilds[i];
						}
					}
					else
					{
						queryBuilds = chartBuild;
					}
					
					try
					{
						// Check if the root CR should be included on the count
						crAux = jira.getCrData(key);
						for (String b : chartBuilds)
						{
							if (!crAux.getDescription().equals("") && crAux.getDescription().contains(b))
							{
								queryResultcount++;
								break;
							}
						}
						
						// Get dups count
						queryResultcount += jira.queryCount(chartIssuesCountQuery.replace("#chart build#", queryBuilds).replace("#issue key#", key.trim())
						                                    + " AND description ~ userdebug");
						System.out.println("Count result: " + queryResultcount);
						
						// totalDupsCount += queryResultcount;
						if (crAux.getSummary().length() > 100)
							crAux.setSummary(crAux.getSummary().substring(0, 101));
						issueDupsCount.setValue(key + " - " + crAux.getSummary(), queryResultcount);
						
						lp.addItemList1(key);
						lp.addItemList2("" + queryResultcount);
					}
					catch (ParseException e)
					{
						e.printStackTrace();
					}
				}
			}
			
			// Build the database for userdebug chart
			for (String key : chartUserIssues.split("\n"))
			{
				queryResultcount = 0;
				
				// Check if key is valid
				if (key.contains("-") && key.length() > 5)
				{
					// Check how many builds shall be used to create the chart
					if (chartBuilds.length > 1)
					{
						queryBuilds = chartBuilds[0];
						for (int i = 1; i < chartBuilds.length; i++)
						{
							queryBuilds += " OR description ~ " + chartBuilds[i];
						}
					}
					else
					{
						queryBuilds = chartBuild;
					}
					
					try
					{
						// Check if the root CR should be included on the count
						crAux = jira.getCrData(key);
						for (String b : chartBuilds)
						{
							if (!crAux.getDescription().equals("") && crAux.getDescription().contains(b))
							{
								queryResultcount++;
								break;
							}
						}
						
						// Get dups count
						queryResultcount += jira.queryCount(chartIssuesCountQuery.replace("#chart build#", queryBuilds).replace("#issue key#", key.trim())
						                                    + " AND description !~ userdebug");
						
						if (queryResultcount == 0)
							queryResultcount++;
						
						System.out.println("Count result: " + queryResultcount);
						
						// totalDupsCount += queryResultcount;
						if (crAux.getSummary().length() > 100)
							crAux.setSummary(crAux.getSummary().substring(0, 101));
						issueUserDupsCount.setValue(key + " - " + crAux.getSummary(), queryResultcount);
						
						lp2.addItemList1(key);
						lp2.addItemList2("" + queryResultcount);
					}
					catch (ParseException e)
					{
						e.printStackTrace();
					}
				}
			}
			
			lp.showWindow();
			lp2.showWindow();
			
			PieChartBuilder cb = new PieChartBuilder(name + " Userdebug", issueDupsCount, chartsOutputFolder + name.replace(" ", "_") + "_userdebug.png");
			cb.buildAndShow();
			PieChartBuilder cb2 = new PieChartBuilder(name + " User", issueUserDupsCount, chartsOutputFolder + name.replace(" ", "_") + "_user.png");
			cb2.buildAndShow();
			
			return true;
		}
		else
		{
			chartBuilds = chartBuild.trim().split(" ");
			queryBuilds = "";
			issueDupsCount = new DefaultPieDataset();
			
			for (String key : chartUserdebugIssues.split("\n"))
			{
				queryResultcount = 0;
				
				if (key.contains("-") && key.length() > 5)
				{
					if (chartBuilds.length > 1)
					{
						queryBuilds = chartBuilds[0];
						for (int i = 1; i < chartBuilds.length; i++)
						{
							queryBuilds += " OR description ~ " + chartBuilds[i];
						}
					}
					else
					{
						queryBuilds = chartBuild;
					}
					
					try
					{
						crAux = jira.getCrData(key);
						if (queryBuilds.contains(crAux.getBuild()))
						{
							queryResultcount++;
						}
						else
						{
							for (String b : chartBuilds)
							{
								if (crAux.getDescription().contains(b))
								{
									queryResultcount++;
								}
							}
						}
						
						queryResultcount += jira.queryCount(chartIssuesCountQuery.replace("#chart build#", queryBuilds).replace("#issue key#", key.trim()));
						System.out.println("Count result: " + queryResultcount);
						
						// totalDupsCount += queryResultcount;
						if (crAux.getSummary().length() > 100)
							crAux.setSummary(crAux.getSummary().substring(0, 101));
						issueDupsCount.setValue(key + " - " + crAux.getSummary(), queryResultcount);
						
						lp.addItemList1(key);
						lp.addItemList2("" + queryResultcount);
					}
					catch (ParseException e)
					{
						e.printStackTrace();
						return false;
					}
				}
			}
			
			lp.showWindow();
			lp.setListTitle(name + "All versions results");
			
			PieChartBuilder cb = new PieChartBuilder(name, issueDupsCount, chartsOutputFolder + name.replace(" ", "_") + ".png");
			cb.buildAndShow();
			
			return true;
		}
	}
	
	@SuppressWarnings("resource")
	private String configureProductHTML()
	{
		try
		{
			String product = new Scanner(new File(htmlTemplatesFolder + "addProductReport.tmpl")).useDelimiter("\\Z").next();
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
			product = product.replace("#spreadsheet link#", spreadsheetLink); // nao usado
			
			if (addChart)
			{
				String chartHTML = new Scanner(new File(htmlTemplatesFolder + "addChart.tmpl")).useDelimiter("\\Z").next();
				if (separatedCharts)
				{
					chartHTML = chartHTML.replace("</li>", "<img src=\"cid:#image cid userdebug#\" alt=\"\" />\n</li>");
					product = product.replace("#Wakelock Chart#",
					                          chartHTML.replace("#image cid#", name.replace(" ", "_") + "_user").replace("#image cid userdebug#",
					                                                                                                     name.replace(" ", "_") + "_userdebug"));
				}
				else
				{
					product = product.replace("#Wakelock Chart#", chartHTML.replace("#image cid#", name.replace(" ", "_")));
				}
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
	
	public String generateProductReport(String user, String pass, boolean separatedCharts)
	{
		jira = new JiraSatApi(JiraSatApi.DEFAULT_JIRA_URL, user, pass);
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
	
	public void setReleases(String releases)
	{
		this.releases = releases.split(" ");
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
		if (releases != null)
		{
			String sReleases = "";
			for (String r : releases)
			{
				sReleases += r + " ";
			}
			
			return sReleases.trim();
		}
		
		return "";
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
		return chartUserdebugIssues;
	}
	
	public void setChartUserdebugIssues(String chartIssues)
	{
		this.chartUserdebugIssues = chartIssues;
	}
	
	public String getChartUserIssues()
	{
		return chartUserIssues;
	}
	
	public void setChartUserIssues(String chartUserIssues)
	{
		this.chartUserIssues = chartUserIssues;
	}
	
	public String toString()
	{
		return name;
	}
	
	public String getUser()
	{
		return user;
	}
	
	public void setUser(String user)
	{
		this.user = user;
	}
	
	public String getPass()
	{
		return pass;
	}
	
	public void setPass(String pass)
	{
		this.pass = pass;
	}
	
	public boolean isSeparateCharts()
	{
		return separatedCharts;
	}
	
	public void setSeparateCharts(boolean separateCharts)
	{
		this.separatedCharts = separateCharts;
	}
	
}
