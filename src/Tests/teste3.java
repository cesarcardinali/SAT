package Tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class teste3 {
	
	public static void main(String[] args) {
		
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] {"--web-security=no", "--ignore-ssl-errors=yes"});
		
		//DesireCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "Data\\complements\\phantomjs.exe");
		//WebDriver driver = new PhantomJSDriver(DesireCaps);
		System.setProperty("phantomjs.binary.path", "Data\\complements\\phantomjs.exe");
		WebDriver driver = new PhantomJSDriver(caps);
		
		driver = new PhantomJSDriver();
		
		driver.get("http://google.com");
		System.out.println(driver.getPageSource());
		driver.close();
		
		System.exit(0);
	}

}
