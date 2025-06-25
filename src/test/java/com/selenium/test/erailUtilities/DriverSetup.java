package com.selenium.test.erailUtilities;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
 
public class DriverSetup {
	public WebDriver driver;
	public static String url = "https://erail.in/";
	public static String usebrowser;
	ChromeOptions optionsChrome;
	EdgeOptions optionsEdge;
	
	public WebDriver driverLaunch(String browser) {
		usebrowser = browser;
		if (usebrowser.equalsIgnoreCase("chrome")) {
			optionsChrome = new ChromeOptions();
			optionsChrome.setAcceptInsecureCerts(true);
			
			driver = new ChromeDriver(optionsChrome);
		}else if (usebrowser.equalsIgnoreCase("edge")) {
			optionsEdge = new EdgeOptions();
			optionsEdge.setAcceptInsecureCerts(true);
			
			driver = new EdgeDriver(optionsEdge);
		}
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.get(url);
		return driver;
	}
	
	public void driverClose() {
		driver.quit();
	}
}
