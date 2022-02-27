package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import utilities.TestUtil;

public class CustomListeners implements ITestListener{

	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		Reporter.log("login successfully executed");
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		//Reporter.log("<a href=\"C:\\Users\\BHAWUK\\Pictures\\Untitled.png\">Screenshot</a>");
		//Reporter.log("<a target=\"_blank\" href=\"C:\\Users\\BHAWUK\\Pictures\\Untitled.png\">Screenshot</a>");
		TestUtil.captureScreenshot();
		Reporter.log("<a target=\"_blank\" href=" + TestUtil.screenshotName + ">Screenshot</a>");
		
	}

	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		Reporter.log("test failed");
		Reporter.log("capturing screenshot");
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		//Reporter.log("<a href=\"C:\\Users\\BHAWUK\\Pictures\\Untitled.png\">Screenshot</a>");
		
		TestUtil.captureScreenshot();
		Reporter.log("<a target=\"_blank\" href=" + TestUtil.screenshotName + ">Screenshot</a>");
		
		
		
	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

}
