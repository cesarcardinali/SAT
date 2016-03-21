package tests.report;


import java.util.ArrayList;

import javax.swing.JOptionPane;


public class ReportController
{
	// Local variables -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private ReportFrame view;
	private ReportModel model;
	
	// Class Constructor
	public ReportController()
	{
		view = null;
		model = null;
	}
	
	// Frame manipulation methods ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void resetTxtFields()
	{
		view.clearFields();
	}
	
	public void updateViewFieldsForSelectedProduct(int productId)
	{
		int id = productId;
		if (id != -1)
		{
			ProductReport pr = model.getProductList().get(id);
			setupViewFields(pr);
		}
	}
	
	public void setupViewCombobox()
	{
		ArrayList<ProductReport> productList = model.getProductList();
		view.setupComboBox(productList);
		
		if (productList.size() > 0)
			setupViewFields(productList.get(0));
		else
			setupViewFields(new ProductReport());
	}
	
	public void setupViewFields(ProductReport pr)
	{
		view.setReleases(pr.getReleasesString());
		view.setTopIssuesLabel(pr.getTopIssueLabel());
		view.setDashboardLink(pr.getDashboardLink());
		view.setSpreadsheetLink(pr.getSpreadsheetLink());
		view.setAddChart(pr.getAddChart());
		view.setChartBuild(pr.getChartBuild());
		view.setSeparatedCharts(pr.isSeparateCharts());
		view.setUserIssuesFieldVisible(pr.isSeparateCharts());
		view.setChartUserdebugIssues(pr.getChartIssues());
		view.setChartUserIssues(pr.getChartUserIssues());
		view.setAddHighlights(pr.getAddHighlight());
		view.setHighlights(pr.getHighlights());
	}
	
	// Model manipulation methods ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	// Controller connection methods ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Model<->View tunnel methods
	public void loadProducts()
	{
		model.loadProducts();
		setupViewCombobox();
	}
	
	public void addNewProduct()
	{
		ProductReport pr = new ProductReport();
		pr.setName(JOptionPane.showInputDialog(view, "Product name:"));
		model.addNewProduct(pr);
		setupViewCombobox();
		view.setComboboxItem(model.getProductList().size() - 1);
	}
	
	public void editProductName(ProductReport pr, String newName)
	{
		pr.setName(newName);
		
		setupViewCombobox();
		
		view.setComboboxItem(model.getProductList().indexOf(pr));
	}
	
	public void removeProduct(int index)
	{
		if (index >= 0)
		{
			model.removeProduct(index);
			
			setupViewCombobox();
			
			if (index > 0)
				view.setComboboxItem(index - 1);
		}
	}
	
	public void updateProductFields(ProductReport pr)
	{
		pr.setName(view.getProductName());
		pr.setProductID(view.getProductIDs());
		pr.setReleases(view.getReleases());
		pr.setTopIssueLabel(view.getTopIssueLabel());
		pr.setDashboardLink(view.getDashboardLink());
		pr.setSpreadsheetLink(view.getSpreadsheetLink());
		pr.setAddChart(view.addChart());
		pr.setSeparateCharts(view.getSeparatedCharts());
		pr.setChartBuild(view.getChartBuilds());
		pr.setChartUserdebugIssues(view.getChartUserdebugIssues());
		pr.setChartUserIssues(view.getChartUserIssues());
		pr.setAddHighlight(view.addHighlights());
		pr.setHighlights(view.getHighlights());
	}
	
	// Report building methods
	public void generateProductReport(boolean separatedCharts)
	{
		model.setUser(view.getUser());
		model.setPass(view.getPass());
		model.generateReport(separatedCharts);
	}
	
	public void sendReportMail()
	{
		if (model.getReportOutput() != null && !model.getReportOutput().equals(""))
			model.sendReportEmail();
		else
			JOptionPane.showMessageDialog(null, "There is no report to be sent");
	}
	
	// Saving report generator settings
	public void saveProductsToXML()
	{
		model.saveProductsToXML();
	}
	
	// Getters and Setters -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// View setup
	public ReportFrame getFrame()
	{
		return view;
	}
	
	public void setFrame(ReportFrame frame)
	{
		this.view = frame;
	}
	
	// ----------------
	
	// Model setup
	public ReportModel getModel()
	{
		return model;
	}
	
	public void setModel(ReportModel model)
	{
		this.model = model;
	}
	// ----------------
}
