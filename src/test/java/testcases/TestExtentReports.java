package testcases;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class TestExtentReports {

	public ExtentSparkReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest test;
	
	@BeforeTest
	public void setReport() {
		htmlReporter = new ExtentSparkReporter("./reports/extent.html");
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setDocumentTitle("W2A Automation Report");
		htmlReporter.config().setReportName("Automation test Results");
		htmlReporter.config().setTheme(Theme.STANDARD);
		
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		
		extent.setSystemInfo("Automation tester", "John Wick");
		extent.setSystemInfo("Build No : ", "1234");
		extent.setSystemInfo("Organization", "BMO");
	}
	
	@Test
	public void doLogin() {
		test = extent.createTest("Login Test");
		test.log(Status.INFO, "Entering username");
		test.log(Status.INFO, "Entering password");
		test.pass("Passing the test");
	}
	
	@Test
	public void doUserReg() {
		test = extent.createTest("User reg Test");
		test.log(Status.INFO, "Entering username");
		test.log(Status.INFO, "Entering password");
		//test.fail("Failing the test");
		Assert.fail();
	}
	
	@Test
	public void isSkip() {
		test = extent.createTest("User skip Test");
		test.log(Status.INFO, "Entering username");
		test.log(Status.INFO, "Entering password");
		//test.skip("skipping the test");
		throw new SkipException("Skipping the test");
	}
	
	@AfterMethod
	public void updateResults(ITestResult result) {
		if(result.getStatus()==ITestResult.FAILURE) {
			String methodName = result.getMethod().getMethodName();
			Markup m = MarkupHelper.createLabel("Failing the test : " + methodName.toUpperCase(), ExtentColor.RED);
			test.fail(m);
		}
		else if(result.getStatus()==ITestResult.SUCCESS) {
			String methodName = result.getMethod().getMethodName();
			//test.pass("Passing the test");
			Markup m = MarkupHelper.createLabel("Passing the test : " + methodName.toUpperCase(), ExtentColor.GREEN);
			test.pass(m);
		}
		else if(result.getStatus()==ITestResult.SKIP) {
			String methodName = result.getMethod().getMethodName();
			//test.skip("skipping the test");
			Markup m = MarkupHelper.createLabel("skipping the test : " + methodName.toUpperCase(), ExtentColor.AMBER);
			test.skip(m);
		}
	}
	
	@AfterTest
	public void endReport() {
		extent.flush();
	}
}
