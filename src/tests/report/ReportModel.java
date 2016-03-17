package tests.report;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import core.XmlMngr;


public class ReportModel
{
	private ArrayList<ProductReport> productList;
	private ReportController         controller;
	private String                   reportOutput;
	private File                     reportFile;
	private String                   user;
	private String                   pass;
	
	private final String             htmlTemplatesFolder = "Data/complements/report/templates/";
	private final String             chartsOutputFolder  = "Data/complements/report/charts/";
	
	public ReportModel()
	{
		productList = null;
		controller = null;
		reportOutput = null;
	}
	
	public boolean loadProducts()
	{
		productList = XmlMngr.getAllProductsReport();
		
		return true;
	}
	
	@SuppressWarnings("resource")
	public String generateReport()
	{
		try
		{
			String htmlOutput = new Scanner(new File(htmlTemplatesFolder + "baseBody.tmpl")).useDelimiter("\\Z").next();
			String highlights = "";
			String productsReport = "";
			
			for (ProductReport pr : productList)
			{
				productsReport += pr.generateProductReport(user, pass);
				
				if (pr.getAddHighlight())
				{
					highlights += "<li>" + pr.getHighlights() + "</li>\n";
				}
			}
			
			if (highlights.equals(""))
			{
				highlights = "None";
			}
			
			htmlOutput = htmlOutput.replace("#products highlights#", highlights);
			htmlOutput = htmlOutput.replace("#product details#", productsReport);
			
			// Generate report file
			reportFile = new File("Report/report.html");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(reportFile));
			bw.write(htmlOutput);
			bw.close();
			
			// Desktop.getDesktop().browse(reportFile.toURI());
			
			reportOutput = htmlOutput;
			
			return htmlOutput;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void sendReportEmail()
	{
		try
		{
			System.out.println("Setting up mail server IP");
			// Ip address of your system
			String host = "localhost";
			
			System.out.println("Setting up email user/pass");
			// email address you configured in hmail server
			String from = "reporter@satreports.com";
			
			// password of email address
			String pwd = "reporter";
			
			System.out.println("Configuring SMTP server");
			// Configure your server
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.user", from);
			props.put("mail.smtp.password", pwd);
			props.put("mail.smtp.port", "25");
			props.put("mail.smtp.auth", "true");
			
			System.out.println("Configuring email");
			// Start session
			Session ses = Session.getDefaultInstance(props, null);
			
			MimeMessage message = new MimeMessage(ses);
			
			// Set text
			File file = new File("Report/report.html");
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			
			String str = new String(data, "UTF-8");
			message.setText(str, "utf-8", "html");
			
			// ---
			MimeMultipart content = new MimeMultipart("related");
			MimeBodyPart htmlPart = new MimeBodyPart();
			String cid;
			
			htmlPart.setText(str, "utf-8", "html");
			content.addBodyPart(htmlPart);
			
			for (ProductReport pr : productList)
			{
				cid = pr.getName().replace(" ", "_");
				MimeBodyPart imagePart = new MimeBodyPart();
				imagePart.attachFile(chartsOutputFolder + pr.getName().replace(" ", "_") + ".png");
				imagePart.setContentID("<" + cid + ">");
				imagePart.setDisposition(MimeBodyPart.INLINE);
				content.addBodyPart(imagePart);
			}
			message.setContent(content);
			
			// Setup "To" mail list
			String[] to = new String[1];
			to[0] = "cesarc@motorola.com";
			InternetAddress[] sendTo = new InternetAddress[1];
			for (int j = 0; j < 1; j++)
			{
				sendTo[j] = new InternetAddress(to[j]);
			}
			
			// Setup mail fields
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, sendTo);
			message.setSubject("Automatic Daily Report");
			
			// Send mail
			System.out.println("Sending ...");
			Transport transport = ses.getTransport("smtp");
			transport.connect(host, from, pwd);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			
			System.out.println("Sent");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}
	
	public void setController(ReportController rc)
	{
		controller = rc;
	}
	
	public ReportController getController()
	{
		return controller;
	}
	
	public ArrayList<ProductReport> getProductList()
	{
		return productList;
	}
	
	public void setProductList(ArrayList<ProductReport> productList)
	{
		this.productList = productList;
	}
	
	public void addNewProduct(ProductReport pr)
	{
		productList.add(pr);
	}
	
	public ProductReport removeProduct(int index)
	{
		return productList.remove(index);
	}
	
	public ProductReport removeProduct(ProductReport pr)
	{
		int index = -1;
		
		for (ProductReport aux : productList)
		{
			if (pr.getName().equals(aux.getName()))
			{
				index = productList.indexOf(aux);
			}
		}
		
		if (index > -1)
			return productList.remove(index);
		
		return null;
	}
	
	public String getReportOutput()
	{
		return reportOutput;
	}
	
	public void saveProductsToXML()
	{
		XmlMngr.setAllProductsReport(productList);
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
}
