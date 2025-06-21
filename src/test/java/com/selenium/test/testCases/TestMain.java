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
	int row;
	int validCol;
	int expCol;
	
	ExtentReportManager erm;
	
	
	/**
	 * setup setDriver class
	 * launch browser
	 * @param browser passes browser value for opening different browser, received from testng.xml
	 */
	@BeforeTest
	@Parameters("browser")
	public void setDriver(String browser) {
		dset=new DriverSetup();
		driver=dset.driverLaunch(browser);
	}
	
	
	/**
	 * Set parameters:
	 * File name to use
	 * Initialize ErailDataInput class - passes input into web page
	 * Initialize ErailDdataOutput - fetching various outputs from web page
	 * Initialize ExcelUtils - get values, set values, apply colors in cell\
	 * expCol - expected values column for comparison
	 * validCol - for writing validation report
	 */
	@BeforeClass
	public void setParams() {
		filename="TrainSearchData.xlsx";
		pageInp=new ErailDataInput(driver,filename);
		pageOut=new ErailDataOutput(driver,filename);
		excel=new ExcelUtils(filename);
		
		expCol=1;	
		validCol=3;
	}
	
	
	/**
	 * Initialize Extent Report
	 */
	@BeforeTest
	public void startReporter()
	   {
		erm = new ExtentReportManager();
		erm.createTest("Erail Train Search");
	   }
  
	
	/**
	 * actTitle - stores actual title and write in excel
	 * expTitle - get expected title from excel
	 * Validate actual title and expected title
	 * @throws IOException if fails to read and write from Excel sheet
	 */
	@Test 
	public void testTitle() throws IOException {
		row=1;
		String actTitle=pageInp.actTitle();
		excel.setCellData("Testing", row, "Actual", actTitle);

		String expTitle=excel.getCellData("Testing", row, expCol);
		
		try {
			Assert.assertEquals(actTitle, expTitle);
			excel.setCellData("Testing",row,"Validation Result","PASS");
			excel.fillGreenColor("Testing", row, validCol);
			System.out.println("Got correct title...");
		}
		catch(AssertionError e){
			excel.setCellData("Testing", row, "Validation Result", "FAIL");
			excel.fillRedColor("Testing", row, validCol);
			System.out.println("Got incorrect title...");
			throw e;
		}
	}  
	
	
	/**
	 * actUrl - stores actual URL and write in excel
	 * expUrl - get expected URL from excel
	 * Validate actual URL and expected URL
	 * @throws IOException if fails to read and write from Excel sheet
	 */
	@Test 
	public void testURL() throws IOException {
		row=2;

		String actUrl=pageInp.actURL();
		excel.setCellData("Testing", row, "Actual", actUrl);
		
		String expUrl=excel.getCellData("Testing", row, expCol);

		try {
			Assert.assertEquals(actUrl, expUrl);
			excel.setCellData("Testing",row,"Validation Result","PASS");
			excel.fillGreenColor("Testing", row, validCol);
			System.out.println("Got correct URL...");
		}
		catch(AssertionError e){
			excel.setCellData("Testing", row, "Validation Result", "FAIL");
			excel.fillRedColor("Testing", row, validCol);
			System.out.println("Got incorrect URL...");
			throw e;
		}
	}
	
	
	/**
	 * Accepts alert if appears
	 */
	@Test(priority=0)
	public void alertHandle() {
		pageInp.acceptAlert();
		erm.logPass("Handled Alert");
	}
	
	
	/**
	 * sourceStation() - select source station, data fetched from excel
	 * actSrc - stores actual source station
	 * expUrl - get expected URL from excel
	 * Validate actual source and expected source
	 * @throws IOException if fails to read and write from Excel sheet
	 */
	@Test(priority=1)
	public void testSrcStation() throws IOException {
		row=3;
		
		pageInp.sourceStation();
		System.out.println("-----Selected Source Station-----");
		ScreenShot.screenShotTC(driver, "1_SourceStation");
		erm.logPass("Searched Source Station");
		
		String actSrc=pageOut.getSource();
		excel.setCellData("Testing", row, "Actual", actSrc);
		
		String expSrc=excel.getCellData("Testing",row, expCol);

		try {
			Assert.assertEquals(actSrc,expSrc);
			excel.setCellData("Testing",row,"Validation Result","PASS");
			excel.fillGreenColor("Testing", row, validCol);
			System.out.println("Got correct source station...");
		}
		catch(AssertionError e){
			excel.setCellData("Testing", row, "Validation Result", "FAIL");
			excel.fillRedColor("Testing", row, validCol);
			System.out.println("Got incorrect source station...");
			throw e;
		}
	}
	
	
	/**
	 * destStation() - select destination station, data fetched from excel
	 * actDest - stores actual source station and write into excel
	 * expDest - get expected URL from excel
	 * Validate actual destination and expected destination
	 * @throws IOException if fails to read and write from Excel sheet
	 */
	@Test(priority=2)
	public void testDestStation() throws IOException {
		row=4;
		
		pageInp.destStation();
		System.out.println("-----Selected Destination Station-----");
		ScreenShot.screenShotTC(driver, "2_DestinationStation");
		erm.logPass("Searched Destination Station");
		
		String actDest= pageOut.getDest();
		excel.setCellData("Testing", row, "Actual", actDest);
		 
		String expDest=excel.getCellData("Testing", row, expCol);
		try {
			Assert.assertEquals(actDest,expDest);
			excel.setCellData("Testing",row,"Validation Result","PASS");
			excel.fillGreenColor("Testing", row, validCol);
			System.out.println("Got correct destination station...");
		}
		catch(AssertionError e){
			excel.setCellData("Testing", row, "Validation Result", "FAIL");
			excel.fillRedColor("Testing", row, validCol);
			System.out.println("Got incorrect destination station...");
			throw e;
		}
	}
	
	
	/**
	 * getDateValues() - fetch number of days and finds journey date
	 * dateSelection() - select date 
	 * dateSelectionStatus() - provide status if invalid journey date is mentioned
	 * actDate - stores actual source station and write into excel
	 * expDest - get expected source station from excel
	 * @throws IOException if fails to read and write from Excel sheet
	 */
	@Test(priority=3)
	public void testDateSelection() throws IOException {
		row=5;
		
		pageInp.getDateValues();
		pageInp.dateSelection();
		System.out.println("-----Selected Journey Date-----");
		ScreenShot.screenShotTC(driver, "3_DateSelection");
		erm.logPass("Selected Date");
		
		pageInp.dateSelectionStatus();

		String actDate=pageOut.getDate(); 
		excel.setCellData("Testing", row, "Actual", actDate);
		
		String expDate=excel.getCellData("Testing", row, expCol);
		
		try {
			Assert.assertEquals(expDate,actDate);
			excel.setCellData("Testing",row,"Validation Result","PASS");
			excel.fillGreenColor("Testing", row, validCol);
			System.out.println("Got correct journey date...");
		}
		catch(AssertionError e){
			excel.setCellData("Testing", row, "Validation Result", "FAIL");
			excel.fillRedColor("Testing", row, validCol);
			System.out.println("Got incorrect journey date...");
			throw e;
		}
	}
	
	
	/**
	 * reserveQuota() - select reservation quota from excel
	 * classSelection() - select reservation class from excel
	 * searchBt() - search trains
	 */
	@Test(priority=4)
	public void selectChoices() {
		pageInp.reserveQuota();
		System.out.println("Selected reservation quota...");
		ScreenShot.screenShotTC(driver, "4_QuotaSelection");
		erm.logPass("Selected Reservation Quota");
		
		pageInp.classSelection();
		System.out.println("Selected reservation class...");
		ScreenShot.screenShotTC(driver, "5_ClassSelection");
		erm.logPass("Selected Class");
		
		pageInp.searchBt();	
		System.out.println("Searched Trains...");
		erm.logPass("Searched Trains");
	}
	
	
	/**
	 * getAllTrains() - get list of trains from source to destination
	 * printAvailTrains() - print all list of trains
	 */
	@Test(priority=5)
	public void availableTrain(){
		HashMap<String,String>trains= pageOut.getAllTrains(); 
		System.out.println("Got Train List Found "+trains.size()+"...");
		ScreenShot.screenShotTC(driver, "6_TrainList");
		erm.logPass("Got Train List");

		pageOut.printAvailTrains();
	 }
	
	
	/**
	 * For handling Extent Report
	 * @param result
	 */
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
	
	
	/**
	 * Closes WebDriver parameter
	 */
	@AfterClass
	public void closeParams() {
		if (driver != null) {
			dset.driverClose();
	    }
	}
	
	
	/**
	 * Closes ExtentReport parameter
	 */
	@AfterTest
	   public void flushReport() {
		erm.flushReports();
	}
	
}
