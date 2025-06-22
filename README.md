# Train Search Automation
Used [erail.in](https://erail.in) for searching purpose   

## Features Implemented

- **Multi-browser execution** – Supports **Chrome** and **Edge** (using `DriverSetup` and  `testng.xml`).
- **Data-driven testing** – Input data (e.g., source, destination, date) is read from a **TrainSearchData.xlsx**.
- **Reusable components** – Core functionalities separated into helper classes/methods for readability and maintainability.
- **Exception handling** – Proper try-catch blocks with informative error messages.
- **Synchronization** – Handled using **WebDriverWait** `implicit wait`.
- **Locators** – Used **id** and **name** locators wherever possible.
- **Console logging** – Train count and names are logged into the console.
- **Screenshot capture** – Results page is captured as an image.
- **Clean code** – Follows standard Java naming conventions and uses comments/header blocks.

### Folder Structure
```
TrainSearchAutomation
├─ report/
├─ screenshot/
├─ src
│  ├─ main
│  │  ├─ java/
│  │  └─ resources/
│  └─ test/java/com/selenium
│     ├─ test
│     │    ├─ data
│     │    │  ├─ ExcelUtils.java
│     │    │  └─ TrainSearchData.xlsx
│     │    ├─ erailUtilities
│     │    │  ├─ DriverSetup.java
│     │    │  ├─ ExtentReportManager.java
│     │    │  └─ ScreenShot.java
│     │    ├─ objectRepository
│     │    │  ├─ ErailDataInput.java
│     │    │  └─ ErailDataOutput.java
│     │    └─ testCases
│     │       └─ TestMain.java
│     └─ resources/
├─ target/
├─ test-output/
├─ README.md
├─ pom.xml
└─ testng.xml
```
## Files Functionality
`ExcelUtils.java` : Utility file for reading, writing and colour filling in excel  
`TrainSearchData.xlsx` : Take Input, Write output and Validate with expectation.  

`ErailDataInput.java` : Methods for input into webpage  
`ErailDataOutput.java` : Methods for output from webpage  
`TestMain.java` : Test cases with function call of ErailDataInput and ErailDataOutput  

`DriverSetup.java` : Setup driver  
`ExtentReportManager.java` : Handles Extent Report  
`ScreenShot.java` : For Screenshot  

## Steps Performed

1. Launch [erail.in](https://erail.in) , on edge then chrome 
2. Accept alert or popup if present.
3. Enter **"Hyd"** → Select **"HYDERABAD DECAN HYB"**
4. Enter **"Pune"** → Select **"PUNE JN PUNE"**
5. Select date **4 days from today**
6. Choose class → **"Sleeper"**
7. Choose  Quota → **"Handicaped"**
8. Click on **"Get trains"**
9. Verifies `Title` and `URL` of webpage
9. Verifies that `Source` Station and `Destionation` stations are correctly chosen
10. Verifies that `date` is chosen correctly
10. Print **Train number and their names** in console.
11. Take a **screenshot** of the result.
12. Close the browser.

## Steps to Run

```
>> cd __yourdir__/TrainSearchAutomation
>> mvn clean test
```
> Note: For changing parameters change in excel sheet only

### Included Industry-Standard Java Implementation:
>Page Object Model (POM)  
Apache POI Integration  
TestNG Framework  
Data-Driven Testing  
Extent Reports  

## Tech Stack
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-FF4D4D?style=for-the-badge)
![Apache POI](https://img.shields.io/badge/Apache_POI-1D2D50?style=for-the-badge&logo=apache&logoColor=white)
![Excel](https://img.shields.io/badge/Excel-217346?style=for-the-badge&logo=microsoft-excel&logoColor=white)
![Extent Reports](https://img.shields.io/badge/Extent_Reports-007ACC?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Google Chrome](https://img.shields.io/badge/Chrome-4285F4?style=for-the-badge&logo=googlechrome&logoColor=white)
![Microsoft Edge](https://img.shields.io/badge/Edge-0078D7?style=for-the-badge&logo=microsoft-edge&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)

## License

![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)

This project is licensed under the **[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)**.

You may use, modify, and distribute this code in compliance with the license terms.

> ⚠️ **Disclaimer**:  
> This project is developed strictly for **educational and demonstration purposes**.  
> It is not affiliated with, maintained by, or officially connected to **IRCTC** or **ERAIL**.  
> Use of automation tools on public railway services may violate their terms of service.  
> **Please use responsibly and at your own discretion.**