package com.selenium.test.erailUtilities;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
 
public class DriverSetup {
	public WebDriver driver;
	public static String url = "https://erail.in/";
	public static String usebrowser;
	
	public WebDriver driverLaunch(String browser) {
		usebrowser = browser;
		if (usebrowser.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		}else if (usebrowser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		}else { 
            throw new IllegalArgumentException("Unsupported browser: " + browser);
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
