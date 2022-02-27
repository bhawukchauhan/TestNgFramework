package base;

import java.awt.Robot;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import utilities.ExcelReader;

public class TestBase {
	/*
	 * WebDriver Properties Logs ExtentReports DB Excel Mail
	 */

	public static WebDriver driver;
	public static Properties config = new Properties();
	public static Properties OR = new Properties();
	public static FileInputStream fis;
	public static FileInputStream fisOR;
	// public static Logger log = LogManager.getLogger(TestBase.class.getName());
	public static Logger log = null;
	public static String strLogFilePath = "";
	public static String strExecutionDate = "";
	public static String strExecutionTime = "";
	public static String strExecutionResultPath = System.getProperty("user.dir") + "\\src\\test\\resources\\logs\\TestLogFolder";
	public static String  strLocalSystemIP;
	public ExcelReader excel = new ExcelReader(System.getProperty("user.dir") + "\\src\\test\\resources\\excel\\testdata.xlsx");
	public static WebDriverWait wait;
	

	@BeforeSuite
	public void setUp() {

		try {
			fnLoadEnvironmentParameters();
			fnCreateResultsFolder();
			
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (driver == null) {

			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\Config.properties");
				config.load(fis);
				log.debug("Config file loaded");

				fisOR = new FileInputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\OR.properties");
				OR.load(fisOR);
				log.debug("OR file loaded");

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (config.getProperty("browser").equals("edge")) {
				System.setProperty("webdriver.edge.driver",
						System.getProperty("user.dir") + "\\src\\test\\resources\\executables\\msedgedriver.exe");
				driver = new EdgeDriver();
			} else if (config.getProperty("browser").equals("chrome")) {
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "\\src\\test\\resources\\executables\\chromedriver.exe");
				driver = new ChromeDriver();
				log.debug("Chrome launched");
				log.error("Chrome launched");
			}

			driver.get(config.getProperty("testsiteurl"));
			log.debug("Navigated to : " + config.getProperty("testsiteurl"));
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.getProperty("implicit.wait")),
					TimeUnit.SECONDS);
			wait = new WebDriverWait(driver, 5);

		}
	}

	@AfterSuite
	public void tearDown() {
		if (driver != null)
			driver.quit();
		log.debug("test execution complete");
	}

	public void InitializeLogger(String strFilePath) {
		strLogFilePath = strFilePath;
		ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

		// builder.setStatusLevel(Level.ERROR);
		builder.setConfigurationName("RollingBuilder");

		// create a console appender
		AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE").addAttribute("target",
				ConsoleAppender.Target.SYSTEM_OUT);
		// appenderBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern",
		// "%d [%t] %-5level %C{1}.%M{1} : %msg%n%throwable"));
		appenderBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern",
				"%d{YYYY-MM-dd HH:mm:ss.SSS} %-5level [%C{1}.%M{1}] : %msg%n%throwable"));
		builder.add(appenderBuilder);

		// create a rolling file appender
		LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout").addAttribute("pattern",
				"%d{YYYY-MM-dd HH:mm:ss.SSS} %-5level [%C{1}.%M{1}] : %msg%n");
		ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
				.addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
				.addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "100M"));
		// appenderBuilder = builder.newAppender("rolling",
		// "RollingFile").addAttribute("fileName", "target/rolling.log")

		appenderBuilder = builder.newAppender("rolling", "RollingFile").addAttribute("fileName", strLogFilePath)
				.addAttribute("filePattern", "target/archive/rolling-%d{MM-dd-yy}.gz").add(layoutBuilder)
				.addComponent(triggeringPolicy);
		builder.add(appenderBuilder);

		// create the new logger
		// builder.add(builder.newLogger("TestLogger",
		// Level.DEBUG).add(builder.newAppenderRef("rolling")).addAttribute("additivity",
		// false));
		builder.add(builder.newRootLogger(Level.DEBUG).add(builder.newAppenderRef("rolling")).addAttribute("additivity",
				false));
		LoggerContext ctx = Configurator.initialize(builder.build());
	}

	// function to search for the todays folder exists or not, if no create one
	// ==================================================================================================
	public void fnCreateResultsFolder() throws Throwable, Throwable {
		System.out.println("strExecutionDate : " + strExecutionDate);
		System.out.println("strExecutionTime : " + strExecutionTime);
		 
		//strExecutionResult = strExecutionResult + "\\\\" + strExecutionDate;
		strExecutionResultPath = strExecutionResultPath + "\\" + strExecutionDate;
			
			// step to create the release -> date wise -> count folder
			File folder = new File(strExecutionResultPath);
			File[] foldList = folder.listFiles();//to find out how many folders are already present in Execution Result sub folder
			int intFldCount=1;

			if (folder.exists() == false){
				try { folder.mkdir(); }
				catch(Exception exp){ System.out.println("[ERROR] Failed to create the results folder, error : " + exp.getMessage()); } }
			else
				intFldCount = (foldList.length) + 1;
			fnAppSync(1);
			
			//strExecutionResultPath = strExecutionResultPath + "\\\\" + strExecutionDate + "_" + intFldCount + "_" + new SimpleDateFormat("HH.mm").format(new Date());
			strExecutionResultPath = strExecutionResultPath + "\\" + strExecutionDate + "_" + intFldCount + "_" + new SimpleDateFormat("HH.mm").format(new Date());
			System.out.println("Folder to create ... " + strExecutionDate);
			File newFolder = new File(strExecutionDate);
			newFolder.mkdirs();//creating sub folder for execution time
			System.out.println("Created sub Folder " + strExecutionResultPath);
			
			//strExecutionDate = strExecutionDate + "\\\\";
			strExecutionResultPath = strExecutionResultPath + "\\";
			
			//Creating log file
			strLogFilePath = strExecutionResultPath + "Log_" + strExecutionDate + "_" + new SimpleDateFormat("hhmmss").format(new Date()) + ".log";
			System.out.println("Log file path : " + strLogFilePath);
	        InitializeLogger(strLogFilePath);
	        log = LogManager.getLogger(TestBase.class.getName());
	        log.info("Log file Created : " + strLogFilePath);
	        //redirect system.out to log file
	        PrintStream outStream = new PrintStream(new FileOutputStream(strLogFilePath,true));
	        //Redirecting console output to file (System.out.println)
	 		System.setOut(outStream);
	 		System.out.println("logging from sysout after redirecting");


	
			try{
				log.info("Copying chromedriver.exe to the Results folder");
				//FileUtils.copyFile(new File(strChromeDriverPath), new File(strExecutionResultPath + "chromedriver.exe"));
				log.info("Copying chromedriver.exe to the Results folder is successful");
				//strChromeDriverPath = strExecutionResultPath + "chromedriver.exe";				
				fnAppSync(5);
			}
			catch (Exception exp) { 
				log.error(exp.getMessage());
			}	
			
	 		log.info("Java temp files directory : " + System.getProperty("java.io.tmpdir"), "");
			//Step to delete the files/ folders from the temp
			//fnDeleteTmpFilesFolder(System.getProperty("java.io.tmpdir"), ""); 
			}

	// function to wait/ sync for given amount of time in
	// seconds==================================================================s==============================================
	public void fnAppSync(int intTime) throws Exception {
		Robot robot = new Robot();
		//TimeUnit.SECONDS.sleep(intTime);
		if (intTime > 1)
			log.info("Waiting for " + (intTime/2) + " second");
		
		//robot.delay(1000); // 1 SECOND	
		//Robot has a MAX_DELAY constant value that allows to wait at most for 60 seconds
		//private static final int MAX_DELAY = 60000;

		robot.delay(intTime *500);	
	}
	
	
	public void fnGetIPAddress() throws UnknownHostException {
		
		try {
			InetAddress IP = InetAddress.getLocalHost();
			log.info("Running on machine - IP == " + IP.getHostAddress());
			strLocalSystemIP = IP.getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			log.info("Trying to get IP again ...");
			try {
				InetAddress IP = InetAddress.getLocalHost();
				log.info("Running on machine - IP == " + IP.getHostAddress());
				strLocalSystemIP = IP.getHostAddress().toString();
			} catch (UnknownHostException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	//function set the pre-configuration parameters from the input file and other env variables =========================================================================
	public void fnLoadEnvironmentParameters() throws Throwable {
		strExecutionDate = fnGetCurrentDate("yyyymmdd");
		strExecutionTime = fnGetCurrentTime();
		//strBaseURL = "http://www.letskodeit.com";
		//strEnvironmentType = "UAT";
		
	}
	//function to get the current date and time =========================================================================================================================
	public String fnGetCurrentDate(String strFormat) throws Throwable, Exception{
		String strYear, strMonth, strDay, strRet = null;
		
		GregorianCalendar date = new GregorianCalendar();
			strYear = String.valueOf(date.get(Calendar.YEAR));
		
		if ((date.get(Calendar.MONTH) + 1) < 10)
			strMonth = "0" + String.valueOf(date.get(Calendar.MONTH)+1);
		else
			strMonth = String.valueOf((date.get(Calendar.MONTH))+1);
		
		if (date.get(Calendar.DAY_OF_MONTH) < 10)
			strDay = "0" + String.valueOf(date.get(Calendar.DAY_OF_MONTH));
		else
			strDay = String.valueOf(date.get(Calendar.DAY_OF_MONTH));

		if (strFormat.trim().toLowerCase().contentEquals("yyyymmdd") == true)
			strRet = strYear + strMonth + strDay;
		else if (strFormat.trim().toLowerCase().contentEquals("yyyy/mm/dd") == true)
			strRet =  strYear + "/" + strMonth + "/" + strDay;
		else if (strFormat.trim().toLowerCase().contentEquals("mm-dd-yyyy") == true)
			strRet =  strDay + "-" + strMonth + "-" + strYear;
		else if (strFormat.trim().toLowerCase().contentEquals("yyyy-mm-dd") == true)
			strRet =  strYear + "-" + strMonth + "-" + strDay;
		else if (strFormat.trim().toLowerCase().contentEquals("dd-mm-yyyy") == true)
			strRet =  strDay + "-" + strMonth + "-" + strYear;
		else
			strRet = strYear + strMonth + strDay; //yyyymmdd
		
		return strRet; 	}
	
	//function to get the current time ==========================================================================================================================================
	public String fnGetCurrentTime(){
		String strCurrTime1 = new SimpleDateFormat("HH.mm").format(new Date());
		return strCurrTime1; }
	
	//function to get the current time stamp ==========================================================================================================================================
	public static String fnGetCurrentTimeStamp(){
		String strtimeStamp1 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return strtimeStamp1; }
	
	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			log.debug("element found");
			return true;
		}
		catch(NoSuchElementException e) {
			log.debug(e.getMessage());
			return false;
		}
	}
}