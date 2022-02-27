package testcases;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.TestBase;

public class AddCustomerTest extends TestBase {

	@Test(dataProvider = "getData")
	public void addCustomerTest(String firstName, String lastName, String postCode, String alertext) throws InterruptedException {
		log.debug(firstName + " " + lastName + " " + postCode);
		log.debug("inside addCustomer");
		driver.findElement(By.xpath(OR.getProperty("addCustomer"))).click();
		log.debug("clicked Add Customer");
		
		driver.findElement(By.xpath(OR.getProperty("firstName"))).sendKeys(firstName);
		log.debug("clicked firstName");
		
		driver.findElement(By.xpath(OR.getProperty("lastName"))).sendKeys(lastName);
		log.debug("clicked lastName");
		
		driver.findElement(By.xpath(OR.getProperty("postCode"))).sendKeys(postCode);
		log.debug("clicked postCode");
		
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(OR.getProperty("addCustButton"))).click();
		log.debug("clicked addCustButton");
		
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		log.debug(alert.getText());
		Assert.assertTrue(alert.getText().contains(alertext));
		
		//Thread.sleep(2000);
		alert.accept();
		Thread.sleep(2000);
		//Thread.sleep(2000);
		
		//Assert.assertTrue(isElementPresent(By.xpath(OR.getProperty("addCustomer"))), "Login not succesfull");
	}

	@DataProvider
	public Object[][] getData() {
		log.debug("inside getData");
		String sheetName = "AddCustomerTest";
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
