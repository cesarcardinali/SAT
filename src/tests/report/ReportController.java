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
		view.resetFields();
	}
	
	public void updateFieldsForSelectedProduct(int productId)
	{
		int id = productId;
		ProductReport pr = model.getProductList().get(id);
		view.setupFields(pr);
	}
	
	public void setupCombobox()
	{
		ArrayList<ProductReport> productList = model.getProductList();
		view.setupComboBox(productList);
		if(productList.size() > 0)
			view.setupFields(productList.get(0));
	}
	
	// Model manipulation methods ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void loadProducts()
	{
		model.loadProducts();
		setupCombobox();
	}
	
	public void updateProducts()
	{
		
	}
	
	// Controller connection methods ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void generateProductReport()
	{
		model.generateReport();
	}
	
	public void sendReportMail()
	{
		if(model.getReportOutput() != null && !model.getReportOutput().equals(""))
			model.sendReportEmail();
		else
			JOptionPane.showMessageDialog(null, "There is no report to be sent");
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
