package com.selenium.test.objectRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.selenium.test.data.ExcelUtils;

public class ErailDataInput {
	WebDriver driver;
	String month;
	String date;
	boolean datestatus;
	
	ExcelUtils excel;
	String filename;
	String sheet;
	String testSheet;
	
	int row;
	int col;
	
	public ErailDataInput(WebDriver driver, String file){
		this.driver=driver;
		this.filename=file;
		sheet="SearchData";
		testSheet="Testing";
		excel=new ExcelUtils(filename);
		
		PageFactory.initElements(driver, this);
	}
	
	//source stations menu
	@FindBy(id="txtStationFrom")
	WebElement start;
	
	//getting list of source stations from suggestion
	@FindBy(xpath="//*[@class='autocomplete'][1]/div")
	List<WebElement>startingStations;
	
	//destination stations menu
	@FindBy(id="txtStationTo")
	WebElement dest;
	
	//getting list of destination stations from suggestion
	@FindBy(xpath="//*[@class='autocomplete'][1]/div")
	List <WebElement> destStations;

	//visible calender table
	@FindBy(id="tdDateFromTo")
	WebElement calender;  
	
	//getting all visible months in list
	@FindBy(xpath="//*[@id='divCalender']//td/table/tbody/tr[1]")
	List <WebElement>availMonths;
	
	//get all quota in list
	@FindBy(xpath="//*[@id='cmbQuota']/option")
	List<WebElement> quota;
	
	//get all class in list
	@FindBy(xpath="//*[@id='selectClassFilter']/option")
	List<WebElement> classes;
	
	//get submit button
	@FindBy(id="buttonFromTo")
	WebElement submit;
	
	public void acceptAlert() {
		try {
			Alert myalert=driver.switchTo().alert();
			System.out.println("Got Alert: "+myalert.getText()+" Accepting...");
			myalert.accept();
		}
		catch(NoAlertPresentException e) {
			System.out.println("No Alert found...");
		}
	}
	
	public String actTitle() {
		return driver.getTitle();
	}
	
	public String actURL() {
		return driver.getCurrentUrl();
	}
	
	public void sourceStation() {
		row=1;
		col=0;
//		WebElement start=driver.findElement(By.id("txtStationFrom"));
		start.clear();
		
		//sending initial station name to input from excel
		String src=excel.getCellData(sheet, row, col);
		start.sendKeys(src);
		
		//getting list of source stations from suggestion
//		List <WebElement> startingStations=driver.findElements(By.xpath("//*[@class='autocomplete'][1]/div"));

		//searching based on desired starting station from excel
		row=3;
		col=1;
		String fromStn=excel.getCellData(testSheet, row, col);
		for(WebElement begin:startingStations) {
			if(begin.getText().contains(fromStn)) {
				begin.click();
			}
		}  
		
	}
	
	public void destStation() {
		row=1;
		col=1;
//		WebElement dest=driver.findElement(By.id("txtStationTo"));
		dest.clear();
		
		//sending initial station name to input from excel
		String destloc=excel.getCellData(sheet, row, col);
		dest.sendKeys(destloc);
		
		//getting list of destination stations from suggestion
//		List <WebElement> destStations=driver.findElements(By.xpath("//*[@class='autocomplete'][1]/div"));
		
		//searching based on required station
		row=4;
		col=1;
		String toStn=excel.getCellData(testSheet, row, col);
		for(WebElement end:destStations) {
			if(end.getText().contains(toStn)) {
				end.click();
			}
		}
	}
	
	public void getDateValues() {
		//getting journey date, i.e. 4 day after current date
		LocalDate today = LocalDate.now();
		LocalDate journeyDate = today.plusDays(4);

		//formatting date to match same as in website 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yy");
		month= journeyDate.format(formatter);
		date=String.valueOf(journeyDate.getDayOfMonth());
	}
	
	public void dateSelection() {
		int monthPos;
		
		//getting all visible months in list
		calender.click();
//		driver.findElement(By.id("tdDateFromTo")).click();
//		List <WebElement>availMonths=driver.findElements(By.xpath("//*[@id=\"divCalender\"]//td/table/tbody/tr[1]"));
		
		datestatus=false;
		
		outerloop:
		for(int mnth=0;mnth<availMonths.size();mnth++) {
			
			//checking if required month matches with any from list
			if(availMonths.get(mnth).getText().equals(month)) {
				monthPos=mnth+1;   //get location of month
				
				//getting each rows sequentially (row start from 4)
				for(int r=4;r<=9;r++) {	
					
					//getting each days in a row (day start from 1)
					for(int d=1;d<=7;d++) {
						
						//getting WebElement for each day
						WebElement founddate=driver.findElement(By.xpath("//*[@id='divCalender']//td["+monthPos+"]/table/tbody/tr["+r+"]/td["+d+"]"));
						
						//matching with journey day and selecting
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
		//invalid date is mentioned
		if (!datestatus) {
			System.out.println("Enter Valid Date, or range within 5 months");
		}
	}
	
	public void reserveQuota() {
		row=1;
		col=3;
		//get all quota in list
//		List<WebElement> quota=driver.findElements(By.xpath("//*[@id='cmbQuota']/option"));
		
		//match quota with excel sheet and select
		String qta=excel.getCellData(sheet, row, col);
		for(WebElement q:quota) {
			if(q.getText().contains(qta)) {
				q.click();
			}
		}
	}
	
	public void classSelection() {
		row=1;
		col=4;
		//get all class in list
//		List<WebElement> classes=driver.findElements(By.xpath("//*[@id='selectClassFilter']/option"));
		
		//match class with excel sheet and select
		String reqClass=excel.getCellData(sheet, row, col);
		for(WebElement cls:classes) {
			if(cls.getText().contains(reqClass)) {
				cls.click();
			}
		}
	}
	
	public void searchBt() {
//		WebElement submit=driver.findElement(By.id("buttonFromTo"));
		submit.click();
	}
}
