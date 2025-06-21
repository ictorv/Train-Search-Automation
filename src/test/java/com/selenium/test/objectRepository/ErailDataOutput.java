package com.selenium.test.objectRepository;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.selenium.test.data.ExcelUtils;

public class ErailDataOutput {
	HashMap<String,String> trains;
	WebDriver driver;
	ExcelUtils excel;
	String sheet;

	public ErailDataOutput(WebDriver driver, String filename) {
		trains=new HashMap<String,String>();
		this.driver=driver;
		excel=new ExcelUtils(filename);
		
		PageFactory.initElements(driver, this);
	}
  
	//getting list of trains, for size of table
	@FindBy(xpath="//*[@id=\"divTrainsList\"]/table[1]/tbody/tr/td[2]")
	List <WebElement> trainlist;				
	
	//get changed source station
	@FindBy(xpath="//*[@id=\"tdFromOnly\"]/label")
	WebElement source;
	
	//get changed destination station
	@FindBy(xpath="//*[@id=\"tdToOnly\"]/label")
	WebElement destination;
	
	//get changed date value
	@FindBy(xpath="//*[@id=\"tdDateFromTo\"]/input")
	WebElement searchdate;
	
	/**
	 * finds all trains available from source to destination
	 * @return hashMap of train number as key and train name as values
	 */
	public HashMap<String,String> getAllTrains() {

		//looping to list of trains (train starts from row 2)
		for(int i=2;i<trainlist.size();i++) {
				
			//stopping criteria to end loop, for getting only journey date
			String textVal=driver.findElement(By.xpath("//*[@id=\"divTrainsList\"]/table[1]/tbody/tr["+i+"]/td")).getText();
			if(textVal.contains("Below trains not departing on")) {
					break;
			}	
			
			//getting source and destination stations 
			WebElement  src=driver.findElement(By.xpath("//*[@id=\"divTrainsList\"]/table[1]/tbody/tr["+i+"]/td[3]"));
			WebElement  destin=driver.findElement(By.xpath("//*[@id=\"divTrainsList\"]/table[1]/tbody/tr["+i+"]/td[6]"));
			
			//match if given source station and destination are required stations from excel
			sheet="Testing";
			int srcRow=3;
			int srcCol=1;
			
			int destRow=4;
			int destCol=1;
			String srcStn=excel.getCellData(sheet, srcRow, srcCol);
			String destStn=excel.getCellData(sheet, destRow, destCol);
			
			if (src.getText().equals(srcStn) && destin.getText().equals(destStn)) {	
				//finding train no and putting in HashMap
				WebElement  trainno=driver.findElement(By.xpath("//*[@id=\"divTrainsList\"]/table[1]/tbody/tr["+i+"]/td[1]"));
				WebElement  trainname=driver.findElement(By.xpath("//*[@id=\"divTrainsList\"]/table[1]/tbody/tr["+i+"]/td[2]"));
				trains.put(trainno.getText(), trainname.getText());
			}
		}
		return trains;
	}
	
	
	/**
	 * print trains for required source and destination
	 */
	public void printAvailTrains() {
		//print trains
		System.out.println("===========AVAILABLE TRAINS===========");
		for(String k: trains.keySet()) {
			System.out.println(k + "-" + trains.get(k));
		}
		System.out.println("======================================");
	}
	
		
	/**
	 * get changed source train for later validation
	 * @return source train name
	 */
	public String getSource() {
		return source.getText();
	}  
	
	
	/**
	 * get changed destination train for later validation
	 * @return destination train name
	 */
	public String getDest() {
		return destination.getText();
	}
	
	
	/**
	 * get changed date in page 
	 * @return date in string format
	 */
	public String getDate() {
		return searchdate.getAttribute("value");
	}
}
