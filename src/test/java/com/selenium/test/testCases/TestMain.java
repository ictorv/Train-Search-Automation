package com.selenium.test.testCases;

import java.io.IOException;
import java.util.HashMap;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import com.selenium.test.data.ExcelUtils;
import com.selenium.test.erailUtilities.DriverSetup;
import com.selenium.test.objectRepository.ErailDataInput;
import com.selenium.test.objectRepository.ErailDataOutput;
import com.selenium.test.erailUtilities.ExtentReportManager;
import com.selenium.test.erailUtilities.ScreenShot;


import org.testng.Assert;
import org.testng.ITestResult;

public class TestMain {
	public WebDriver driver;
	DriverSetup dset;
	
	ErailDataInput pageInp;
	ErailDataOutput pageOut;
	String filename;
	
	ExcelUtils excel;
	ExtentReportManager erm;

	@BeforeTest
	@Parameters("browser")
	public void setDriver(String browser) {
		dset=new DriverSetup();
		driver=dset.driverLaunch(browser);
	}
	
	@BeforeClass
	public void setParams() {
		filename="TrainSearchData.xlsx";
		pageInp=new ErailDataInput(driver,filename);
		pageOut=new ErailDataOutput(driver);
		excel=new ExcelUtils(filename);
	}
	
	@BeforeTest
	public void startReporter()
	   {
		erm = new ExtentReportManager();
		erm.createTest("Erail Train Search");
	   }
  
	@Test 
	public void testTitle() throws IOException {
		String actTitle=pageInp.actTitle();
		
		String expTitle=excel.getCellData("Testing", 1, 1);
		excel.setCellData("Testing", 1, "Actual", actTitle);
		try {
			Assert.assertEquals(actTitle, expTitle);
			excel.setCellData("Testing",1,"Validation Result","PASS");
			excel.fillGreenColor("Testing", 1, 3);
		}
		catch(AssertionError e){
			excel.setCellData("Testing", 1, "Validation Result", "FAIL");
			excel.fillRedColor("Testing", 1, 3);
			throw e;
		}
	}
	
	@Test 
	public void testURL() throws IOException {
		String actUrl=pageInp.actURL();
		String expUrl=excel.getCellData("Testing", 2, 1);
		excel.setCellData("Testing", 2, "Actual", actUrl);
		try {
			Assert.assertEquals(actUrl, expUrl);
			excel.setCellData("Testing",2,"Validation Result","PASS");
			excel.fillGreenColor("Testing", 2, 3);
		}
		catch(AssertionError e){
			excel.setCellData("Testing", 2, "Validation Result", "FAIL");
			excel.fillRedColor("Testing", 2, 3);
			throw e;
		}
	}
	
	@Test(priority=1)
	public void testSrcStation() throws IOException {
		pageInp.sourceStation();
		
		ScreenShot.screenShotTC(driver, "01_SourceStation");
		erm.logPass("Searched Source Station");
		
		String actSrc=pageOut.getSource();
		String expSrc=excel.getCellData("Testing", 3, 1);
//		System.out.println(actsrc +"-"+expsrc);
		excel.setCellData("Testing", 3, "Actual", actSrc);
		try {
			Assert.assertEquals(actSrc,expSrc);
			excel.setCellData("Testing",3,"Validation Result","PASS");
			excel.fillGreenColor("Testing", 3, 3);
		}
		catch(AssertionError e){
			excel.setCellData("Testing", 3, "Validation Result", "FAIL");
			excel.fillRedColor("Testing", 3, 3);
			throw e;
		}
	}
	
	@Test(priority=2)
	public void testDestStation() throws IOException {
		pageInp.destStation();
		ScreenShot.screenShotTC(driver, "02_DestinationStation");
		erm.logPass("Searched Destination Station");
		
		String actDest= pageOut.getDest();
		String expDest=excel.getCellData("Testing", 4, 1);
		excel.setCellData("Testing", 4, "Actual", actDest);
		try {
			Assert.assertEquals(actDest,expDest);
			excel.setCellData("Testing",4,"Validation Result","PASS");
			excel.fillGreenColor("Testing", 4, 3);
		}
		catch(AssertionError e){
			excel.setCellData("Testing", 4, "Validation Result", "FAIL");
			excel.fillRedColor("Testing", 4, 3);
			throw e;
		}
		
	}
	
	@Test(priority=3)
	public void testDateSelection() throws IOException {
		pageInp.getDateValues();
		pageInp.dateSelection();

		ScreenShot.screenShotTC(driver, "03_DateSelection");
		erm.logPass("Selected Date");
		pageInp.dateSelectionStatus();

		String expDate=excel.getCellData("Testing", 5, 1);
		String actDate=pageOut.getDate();  
		excel.setCellData("Testing", 5, "Actual", actDate);
		try {
			Assert.assertEquals(expDate,actDate);
			excel.setCellData("Testing",5,"Validation Result","PASS");
			excel.fillGreenColor("Testing", 5, 3);
		}
		catch(AssertionError e){
			excel.setCellData("Testing", 5, "Validation Result", "FAIL");
			excel.fillRedColor("Testing", 5, 3);
			throw e;
		}
	}
	
	@Test(priority=4)
	public void selectChoices() {
		pageInp.reserveQuota();
		erm.logPass("Selected Reservation Quota");
		
		pageInp.classSelection();
		erm.logPass("Selected Class");
		
		pageInp.searchBt();	
		erm.logPass("Searched Trains");
		ScreenShot.screenShotTC(driver, "04_Choices");
		
	}
	
	@Test(priority=5)
	public void availableTrain() throws IOException {
		 HashMap<String,String>trains= pageOut.getAllTrains();
		 
		ScreenShot.screenShotTC(driver, "05_TrainList");
		erm.logPass("Got Train List");
		 
		 int i=1;
		 for(String k: trains.keySet()) {
			 System.out.println("Train no:"+k+" Train Name: "+trains.get(k));
			 excel.setCellData("SearchData", i, "Train Number",k);
			 excel.setCellData("SearchData", i, "Train Name",trains.get(k));
			 i+=1;
		 }
		 
		 
	 }
	
	@AfterMethod
	   public void getResult(ITestResult result) {
	       if(result.getStatus() == ITestResult.FAILURE) {
	    	 //MarkupHelper is used to display the output in different colors
	    	   erm.testLogger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
	    	   erm.testLogger.log(Status.FAIL, MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
	    	   String screenshotPath = ScreenShot.screenShotTC(driver, result.getName());
	    	 //To add it in the extent report 
	    	   erm.testLogger.fail("Test Case Failed Snapshot is below " + erm.testLogger.addScreenCaptureFromPath("."+screenshotPath));	    	  
	       }
	       else if(result.getStatus() == ITestResult.SUCCESS) {
	    	   erm.testLogger.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" Test Case PASSED", ExtentColor.GREEN));
	       }
	       else if(result.getStatus() == ITestResult.SKIP){
	    	   erm.testLogger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE)); 
	       }
	   }
	
	@AfterClass
	public void closeParams() {
		if (driver != null) {
			dset.driverClose();
	    }
	}
	
	@AfterTest
	   public void flushReport() {
		erm.flushReports();
	}
	
}
