package com.selenium.test.objectRepository;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ErailDataOutput {
	HashMap<String,String> trains;
	WebDriver driver;
	
	public ErailDataOutput(WebDriver driver) {
		trains=new HashMap<String,String>();
		this.driver=driver;
	}
  
	public HashMap<String,String> getAllTrains() {
		List <WebElement> trainlist=driver.findElements(By.xpath("//*[@id=\"divTrainsList\"]/table[1]/tbody/tr/td[2]"));
		int i;
		for(i=2;i<trainlist.size();i++) {

			String ne=driver.findElement(By.xpath("//*[@id=\"divTrainsList\"]/table[1]/tbody/tr["+i+"]/td")).getText();
//			System.out.println(ne);
			if(ne.contains("Below trains not departing on")) {
					break;
			}	
			WebElement  src=driver.findElement(By.xpath("//*[@id=\"divTrainsList\"]/table[1]/tbody/tr["+i+"]/td[3]"));
			WebElement  destin=driver.findElement(By.xpath("//*[@id=\"divTrainsList\"]/table[1]/tbody/tr["+i+"]/td[6]"));
			System.out.println(src.getText());
			System.out.println(destin.getText());
			
			if (src.getText().equals("HYB") && destin.getText().equals("PUNE")) {
				System.out.println("find train");
				WebElement  trainno=driver.findElement(By.xpath("//*[@id=\"divTrainsList\"]/table[1]/tbody/tr["+i+"]/td[1]"));
				WebElement  trainname=driver.findElement(By.xpath("//*[@id=\"divTrainsList\"]/table[1]/tbody/tr["+i+"]/td[2]"));
				trains.put(trainno.getText(), trainname.getText());
			}
		}
		return trains;
	}
	
	
	public void printDestTrains() {
		for(String k: trains.keySet()) {
			System.out.println(k + "-" + trains.get(k));
		}
	}
		
	public String getSource() {
		String source=driver.findElement(By.xpath("//*[@id=\"tdFromOnly\"]/label")).getText();
		return source;
	}
	
	public String getDest() {
		String destination=driver.findElement(By.xpath("//*[@id=\"tdToOnly\"]/label")).getText();		 
		return destination;
	}
	
	public String getDate() {
		String searchdate=driver.findElement(By.xpath("//*[@id=\"tdDateFromTo\"]/input")).getAttribute("value");
		return searchdate;
	}
}
