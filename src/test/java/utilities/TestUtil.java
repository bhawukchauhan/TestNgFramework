package utilities;

import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;

import base.TestBase;

public class TestUtil extends TestBase{
	
	public static String screenshotPath = System.getProperty("user.dir") + "\\target\\surefire-reports\\html\\";
	public static String screenshotName;

	public static String captureScreenshot() {
		File scrFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		screenshotName = screenshotPath + "error" + fnGetCurrentTimeStamp() + ".jpg";
		try {
			FileUtils.copyFile(scrFile, new File(screenshotName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return screenshotName;
	}
}
