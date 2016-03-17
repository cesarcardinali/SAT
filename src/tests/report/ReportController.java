package tests.report;


import java.util.ArrayList;

import javax.swing.JOptionPane;


public class ReportController
{
	private ReportFrame view;
	private ReportModel model;
	
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
	
	public void updateFieldsForSelectedProduct(int productId)
	{
		int id = productId;
		if(id != -1)
		{
			ProductReport pr = model.getProductList().get(id);
			view.setupFields(pr);
		}
	}
	
	public void setupCombobox()
	{
		ArrayList<ProductReport> productList = model.getProductList();
		view.setupComboBox(productList);
		
		if(productList.size() > 0)
			view.setupFields(productList.get(0));
		else
			view.setupFields(new ProductReport());
	}
	
	// Model manipulation methods ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void loadProducts()
	{
		model.loadProducts();
		setupCombobox();
	}
	
	public void addNewProduct()
	{
		ProductReport pr = new ProductReport();
		pr.setName(JOptionPane.showInputDialog(view, "Product name:"));
		model.addNewProduct(pr);
		setupCombobox();
		view.setComboboxItem(model.getProductList().size() - 1);
	}
	
	public void editProductName(ProductReport pr, String newName)
	{
		pr.setName(newName);
		
		setupCombobox();
		
		view.setComboboxItem(model.getProductList().indexOf(pr));
	}
	
	public void removeProduct(int index)
	{
		if(index >= 0)
		{
			model.removeProduct(index);
			
			setupCombobox();
			
			if(index > 0)
				view.setComboboxItem(index - 1);
		}
	}
	
	public void updateProductFields(ProductReport pr)
	{
		pr.setName(view.getProductName());
		pr.setReleases(view.getReleases().split(" "));
		pr.setTopIssueLabel(view.getTopIssueLabel());
		pr.setDashboardLink(view.getDashboardLink());
		pr.setSpreadsheetLink(view.getSpreadsheetLink());
		pr.setAddChart(view.addChart());
		pr.setChartBuild(view.getChartBuilds());
		pr.setChartIssues(view.getChartIssues());
		pr.setAddHighlight(view.addHighlights());
		pr.setHighlights(view.getHighlights());
	}
	
	// Controller connection methods ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void generateProductReport()
	{
		model.generateReport();
		model.setUser(view.getUser());
		model.setPass(view.getPass());
	}
	
	public void sendReportMail()
	{
		if(model.getReportOutput() != null && !model.getReportOutput().equals(""))
			model.sendReportEmail();
		else
			JOptionPane.showMessageDialog(null, "There is no report to be sent");
	}
	
	public void saveProductsToXML()
	{
		model.saveProductsToXML();
	}
	
	// Getters and Setters -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public ReportFrame getFrame()
	{
		return view;
	}
	
	public void setFrame(ReportFrame frame)
	{
		this.view = frame;
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
