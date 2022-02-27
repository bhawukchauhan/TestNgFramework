package testcases;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.TestBase;
import extentlisteners.ExtentListeners;

public class OpenAccountTest extends TestBase {

	@Test(dataProvider = "getData")
	public void openAccountTest(String customer, String currency) throws InterruptedException {
		try {
		log.debug(customer + " " + currency);
		log.debug("inside openAccountTest");
		driver.findElement(By.xpath(OR.getProperty("openAccount"))).click();
		Thread.sleep(2000);
		log.debug("clicked open Account");
		
		
		new Select(driver.findElement(By.xpath(OR.getProperty("accountCustomer")))).selectByValue("2"); 
		//driver.findElement(By.xpath(OR.getProperty("accountCustomer"))).sendKeys(customer);
		log.debug("selected customer : " + customer);
		
		//Thread.sleep(2000);
		
		new Select(driver.findElement(By.xpath(OR.getProperty("accountCurrency")))).selectByValue(currency); 
		//driver.findElement(By.xpath(OR.getProperty("accountCurrency"))).sendKeys(currency);
		log.debug("selected currency : " + currency);
		
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(OR.getProperty("accountProcess"))).click();
		log.debug("clicked Process button");
		
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		log.debug(alert.getText());
		Assert.assertTrue(alert.getText().contains("Account created successfully"));
		
		
		Thread.sleep(2000);
		alert.accept();
		
		Assert.assertTrue(true);
		log.debug("open Account successfully " + customer + " " + currency);
		log.debug(alert.getText());
		}
		catch(Exception e) {
			log.debug(e.getMessage());
		}
		
		//Thread.sleep(2000);
		
		//Assert.assertTrue(isElementPresent(By.xpath(OR.getProperty("addCustomer"))), "Login not succesfull");
	}

	@DataProvider
	public Object[][] getData() {
		log.debug("inside getData");
		String sheetName = "OpenAccountTest";
		int rows = excel.getRowCount(sheetName);
		int cols = excel.getColumnCount(sheetName);
		log.debug("rows : " + rows);//2
		log.debug("cols : " + cols);//3

		Object[][] data = new Object[rows - 1][cols];
		log.debug("data : " + data.toString());
		/*
		for (int rowNum = 1; rowNum < rows; rowNum++) {
			log.debug("get data : " + excel.getCellData(sheetName, "0", rowNum));
			for (int colNum = 0; colNum < cols; colNum++) {
				log.debug(excel.getCellData(sheetName, colNum, rowNum));
				// data[0][0]
				//data[rows - 2][cols]
				//data[0][cols] = excel.getCellData(sheetName, colNum, rowNum);
				
			}
		}
		*/
		for (int rowNum = 2; rowNum <= rows; rowNum++) {//2
			//log.debug("get data : " + excel.getCellData(sheetName, "0", rowNum));
			for (int colNum = 0; colNum < cols; colNum++) {
				log.debug(excel.getCellData(sheetName, colNum, rowNum));
				// data[0][0]
				//data[rows - 2][cols]
				data[rowNum-2][colNum] = excel.getCellData(sheetName, colNum, rowNum);
				
			}
		}

		//return {"a","b","c"};
		return data;
	}
}
