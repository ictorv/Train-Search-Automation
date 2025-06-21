package com.selenium.test.objectRepository;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
	}
  
	public HashMap<String,String> getAllTrains() {
		//getting list of trains, for size of table
		List <WebElement> trainlist=driver.findElements(By.xpath("//*[@id=\"divTrainsList\"]/table[1]/tbody/tr/td[2]"));
		
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
	
	
	public void printAvailTrains() {
		//print trains
		System.out.println("===========AVAILABLE TRAINS===========");
		for(String k: trains.keySet()) {
			System.out.println(k + "-" + trains.get(k));
		}
		System.out.println("======================================");
	}
		
	public String getSource() {
		//get changed source station
		String source=driver.findElement(By.xpath("//*[@id=\"tdFromOnly\"]/label")).getText();
		return source;
	}  
	
	public String getDest() {
		//get changed destination station
		String destination=driver.findElement(By.xpath("//*[@id=\"tdToOnly\"]/label")).getText();		 
		return destination;
	}
	
	public String getDate() {
		//get changed date value
		String searchdate=driver.findElement(By.xpath("//*[@id=\"tdDateFromTo\"]/input")).getAttribute("value");
		return searchdate;
	}
}
