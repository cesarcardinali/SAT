package tests.report;


import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComboBox;


public class ReportController
{
	private ReportFrame frame;
	private ReportModel model;
	
	public ReportController()
	{
		frame = null;
		model = null;
	}
	
	// Frame manipulation methods ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void resetTxtFields()
	{
		frame.resetFields();
	}
	
	public void setupFields()
	{
		int id = frame.getComboBox().getSelectedIndex();
		frame.setReleases(model.getProductList().get(id).getReleasesString());
		frame.setTopIssuesLabel(model.getProductList().get(id).getTopIssueLabel());
		frame.setDashboardLink(model.getProductList().get(id).getDashboardLink());
		frame.setSpreadsheetLink(model.getProductList().get(id).getSpreadsheetLink());
		frame.setAddChart(model.getProductList().get(id).getAddChart());
		frame.setChartBuild(model.getProductList().get(id).getChartBuild());
		frame.setChartIssues(model.getProductList().get(id).getChartIssues());
		frame.setAddHighlights(model.getProductList().get(id).getAddHighlight());
		frame.setHighlights(model.getProductList().get(id).getHighlights());
	}
	
	public void setupCombobox()
	{
		JComboBox<String> comboBox = frame.getComboBox();
		comboBox.removeAllItems();
		
		for (ProductReport pr : model.getProductList())
		{
			comboBox.addItem(pr.getName());
		}
		
		setupFields();
	}
	
	// Model manipulation methods ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void loadProducts()
	{
		model.loadProducts();
		setupCombobox();
	}
	
	// Controller connection methods ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	@SuppressWarnings("resource")
	public File generateProductReport()
	{
		File reportFile = new File("Report/report.html");
		
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(reportFile));
			bw.write(model.generateReport());
			bw.close();
			
			Desktop.getDesktop().browse(reportFile.toURI());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return reportFile;
	}
	
	// Getters and Setters -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public ReportFrame getFrame()
	{
		return frame;
	}
	
	public void setFrame(ReportFrame frame)
	{
		this.frame = frame;
	}
	
	public ReportModel getModel()
	{
		return model;
	}
	
	public void setModel(ReportModel model)
	{
		this.model = model;
	}
}
