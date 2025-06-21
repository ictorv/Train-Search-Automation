package com.selenium.test.objectRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.selenium.test.data.ExcelUtils;

public class ErailDataInput {
	WebDriver driver;
	String month;
	String date;
	boolean datestatus;
	ExcelUtils ex;
	String filename;
	String sheet;
	public ErailDataInput(WebDriver driver, String file){
		this.driver=driver;
		this.filename=file;
	}

	public String actTitle() {
		return driver.getTitle();
	}
	
	public String actURL() {
		return driver.getCurrentUrl();
	}
	
	
	public void sourceStation() {
		sheet="SearchData";
		ex=new ExcelUtils(filename);
		
		WebElement start=driver.findElement(By.id("txtStationFrom"));
		start.clear();
		
		String src=ex.getCellData(sheet, 1, 0);
		
		start.sendKeys(src);
		
		List <WebElement> startingStations=driver.findElements(By.xpath("//*[@class=\"autocomplete\"][1]/div"));
		System.out.println(startingStations.size());
		for(WebElement begin:startingStations) {
			if(begin.getText().contains("HYB")) {
//				System.out.println("Entered startStation:)");
				begin.click();
			}
		}  
		
	}
	
	public void destStation() {

		WebElement dest=driver.findElement(By.id("txtStationTo"));
		dest.clear();
		
		String destloc=ex.getCellData(sheet, 1, 1);
		
		dest.sendKeys(destloc);
		List <WebElement> destStations=driver.findElements(By.xpath("//*[@class=\"autocomplete\"][1]/div"));
		System.out.println(destStations.size());
		for(WebElement end:destStations) {
			if(end.getText().contains("PUNE")) {
//				System.out.println("Entered Dest:)");
				end.click();
			}
		}
	}
	
	public void getDateValues() {
		LocalDate today = LocalDate.now();
		LocalDate futureDate = today.plusDays(4);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yy");
		month= futureDate.format(formatter);
		date=String.valueOf(futureDate.getDayOfMonth());
	}
	
	public void dateSelection() {
		driver.findElement(By.id("tdDateFromTo")).click();
		List <WebElement>allmonths=driver.findElements(By.xpath("//*[@id=\"divCalender\"]//td/table/tbody/tr[1]"));
		int totalmnth=allmonths.size();
		datestatus=false;
		
		outerloop:
			for(int mnth=0;mnth<totalmnth;mnth++) {
				if(allmonths.get(mnth).getText().equals(month)) {
					int pos=mnth+1;
					
					for(int r=4;r<=9;r++) {
//						List<WebElement> row=driver.findElements(By.xpath("//*[@id=\"divCalender\"]/center/table/tbody/tr[1]/td["+pos+"]/table/tbody/tr["+r+"]"));	
						for(int d=1;d<=7;d++) {
							WebElement founddate=driver.findElement(By.xpath("//*[@id=\"divCalender\"]//td["+pos+"]/table/tbody/tr["+r+"]/td["+d+"]"));
							if(date.equals(founddate.getText())) {
								founddate.click();
								datestatus=true;
								break outerloop;
							}
						}
					}					
				}				
			}
	}
	
	public void dateSelectionStatus() {
		if (!datestatus) {
			System.out.println("Enter Valid Date, or range within 5 months");
		}
	}
	
	public void reserveQuota() {
		List<WebElement> quota=driver.findElements(By.xpath("//*[@id=\"cmbQuota\"]/option"));
		System.out.println(quota.size());
		
		String qta=ex.getCellData(sheet, 1, 3);
		
		for(WebElement q:quota) {
			if(q.getText().contains(qta)) {
//				System.out.println("Entered Quota:)");
				q.click();
			}
		}
	}
	
	public void classSelection() {
		List<WebElement> classes=driver.findElements(By.xpath("//*[@id=\"selectClassFilter\"]/option"));
		System.out.println(classes.size());
		
		String cl=ex.getCellData(sheet, 1, 4);
		
		for(WebElement cls:classes) {
			if(cls.getText().contains(cl)) {
//				System.out.println("Entered class:)");
				cls.click();
			}
		}
	}
	
	public void searchBt() {
		WebElement submit=driver.findElement(By.id("buttonFromTo"));
		submit.click();
	}
}
