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
	
	
	/**
	 * accepts alert if available
	 */
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
	
	/**
	 * get title of web page
	 * @return actual title
	 */
	public String actTitle() {
		return driver.getTitle();
	}
	
	
	/**
	 * get URL of web page
	 * @return actual URL
	 */
	public String actURL() {
		return driver.getCurrentUrl();
	}
	
	
	/**
	 * start.clear()- clear pre-available train
	 * Get source train from excel
	 * Pass into input box
	 * Get list of available train
	 * Loop over list of available train to get desired train (match if contains expected source)
	 */
	public void sourceStation() {
		row=1;
		col=0;
		start.clear();
		
		//sending initial station name to input from excel
		String src=excel.getCellData(sheet, row, col);
		start.sendKeys(src);

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
	
	
	/**
	 * dest.clear()- clear pre-available train
	 * Get destination train from excel
	 * Pass into input box
	 * Get list of available train
	 * Loop over list of available train to get desired train (match if contains expected destination)
	 */
	public void destStation() {
		row=1;
		col=1;
		dest.clear();
		
		//sending initial station name to input from excel
		String destloc=excel.getCellData(sheet, row, col);
		dest.sendKeys(destloc);
		
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
	
	
	/**
	 * Get number of days after which we want journey
	 * Finds required date and month in proper format
	 */
	public void getDateValues() {
		row=1;
		col=2;
		//getting journey date, i.e. nday mentioned in excel day after current date
		int nday= Integer.parseInt(excel.getCellData(sheet, row, col));
		LocalDate today = LocalDate.now();
		LocalDate journeyDate = today.plusDays(nday);

		//formatting date to match same as in web site 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yy");
		month= journeyDate.format(formatter);
		date=String.valueOf(journeyDate.getDayOfMonth());
	}
	
	
	/**
	 * @param mnth for month parameter
	 * @param weekRow for row
	 * @param day for day
	 * @return true if bug found in calender and skip
	 */
	public boolean bugSkip (int mnth, int weekRow, int day) {
		//last table, starting from last 2 rows, last 2 rows does not contains 1 to 15 days
		if(mnth==5 && weekRow>=8 && day<15) {
			return true;
		}
		return false;	
	}
	
	
	/**
	 * choose date from calendar from desired date 
	 * checks if month matches from visible date table
	 * pass through each row (starting from 4 , ending at 9)
	 *    - check if giver row contains required date
	 *    - break complete loop once found 
	 */
	public void dateSelection() {
		int monthPos;
		
		//getting all visible months in list
		calender.click();		
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
						if(date.equals(founddate.getText()) && !bugSkip (monthPos,r,d)) {
							founddate.click();
							datestatus=true;
							break outerloop;
						}
					}
				}					
			}				
		}
	}
	
	
	/**
	 * Log whether provided date is valid or not
	 */
	public void dateSelectionStatus() {
		//invalid date is mentioned
		if (!datestatus) {
			System.out.println("Enter Valid Date, or range within 5 months");
		}
	}
	
	
	/**
	 * Choose Reservation quota
	 */
	public void reserveQuota() {
		row=1;
		col=3;
	
		//match quota with excel sheet and select
		String qta=excel.getCellData(sheet, row, col);
		for(WebElement q:quota) {
			if(q.getText().contains(qta)) {
				q.click();
			}
		}
	}
	
	
	/**
	 * choose Reservation class
	 */
	public void classSelection() {
		row=1;
		col=4;
		
		//match class with excel sheet and select
		String reqClass=excel.getCellData(sheet, row, col);
		for(WebElement cls:classes) {
			if(cls.getText().contains(reqClass)) {
				cls.click();
			}
		}
	}
	
	
	/**
	 * Search Trains
	 */
	public void searchBt() {
		submit.click();
	}
}
