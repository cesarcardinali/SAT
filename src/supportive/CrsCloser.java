package supportive;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import main.SAT;

public class CrsCloser implements Runnable {

	static SAT BaseWindow;	
	
	
	public CrsCloser(SAT parent){
		BaseWindow = parent;
	}
	
	
	/*
	 * closeDiagCrs
	 */
	public static void closeDiagCrs(){
		
		Object[] options = { "It's OK. Go!", "Cancel, I need to check" };
		int n = JOptionPane.showOptionDialog(BaseWindow.getCrsManager(),
				"Please, make sure that your username and password are set correctly at \"CRs and Jira\" tab. Otherwise, cancel this window and " +
						"check your login data.",
				"Warning",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options,
				options[1]);
		
		System.out.println("Resposta: " + n);
		BaseWindow.logWrite("Resposta: " + n);
		
		if(n == 0){
			try{
				WebDriver driver;
				FirefoxProfile profile;
				
				String user, pass;
				
				//Configuring Firefox
				System.out.println("Generating Firefox profile");
				BaseWindow.logWrite("Generating Firefox profile");
				profile = new FirefoxProfile(new File("Data\\complements\\profiles\\y2fvgaq0.bot"));
				driver = new FirefoxDriver(profile);
				user = BaseWindow.getCrsManager().getUserData()[0];
				pass = BaseWindow.getCrsManager().getUserData()[1];
		
				// Open up a browser
				System.out.println("Starting browser");
				driver.navigate().to("http://google.com");
				
				
				for (String s : BaseWindow.getCrsManager().getTextDownload().getText().split("\n")) {
					if(s.length() < 4)
						continue;
					
					s.replace("\r", "").replace(" ", "");
					
					//Open CR page
					System.out.println("Opening CR page");
					System.out.println("CR: " + s);
					driver.navigate().to("http://idart.mot.com/browse/" + s);
					
					//Jira Login
					while(driver.getTitle().contains("Log")){
						System.out.println("Trying to Log in");
						driver.findElement(By.name("os_username")).sendKeys(user);		
						driver.findElement(By.name("os_password")).sendKeys(pass);
						//driver.findElement(By.name("os_cookie")).click();
						driver.findElement(By.name("login")).click();
						sleep(500);
					}
					
					//Closing
					System.out.println("Clicking CLOSE");
					driver.findElement(By.id("action_id_21")).click();
					sleep(1700);
					
					Exception e = new Exception();
					e = new Exception();
					while(e != null){
						e = null;
						try{
							System.out.println("Inserting comment");
							driver.findElement(By.xpath("//div[@id=\"workflow-transition-21-dialog\"]//textarea[@id='comment']")).sendKeys("Closing old CRs and focusing on newer releases");
							sleep(500);
							
							System.out.println("Closing");
							driver.findElement(By.id("issue-workflow-transition-submit")).submit();
							sleep(500);
							
						} catch(Exception e1){
							e1.printStackTrace();
							e=e1;
						}
					}
					
					System.out.println("Opening new tab");
					sleep(500);
					driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
				}
				
				driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "w");
				System.out.println("Done");
				
			} catch (Exception e){
				e.printStackTrace();
			}
		} else {
			System.out.println("Action cancelled");
		}
	}
	
	
	public static void copyScript(File source, File dest)
			throws IOException {
		FileUtils.copyFile(source, dest);
	}
	
	private static void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	@Override
	public void run() {
		closeDiagCrs();
	}
}
