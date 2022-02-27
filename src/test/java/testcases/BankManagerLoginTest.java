package testcases;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import base.TestBase;

public class BankManagerLoginTest extends TestBase{

	@Test
	public void loginAsBankManager() throws InterruptedException {
		log.debug("inside LoginTest");
		//Thread.sleep(5000);
		System.out.println(OR.getProperty("bmlBtn"));
		driver.findElement(By.xpath(OR.getProperty("bmlBtn"))).click();
		
		Thread.sleep(2000);
		Assert.assertTrue(isElementPresent(By.xpath(OR.getProperty("addCustomer"))),"Login not succesfull");
		log.debug("login successfull");

		//Assert.fail("Customer not added");

	}
}
