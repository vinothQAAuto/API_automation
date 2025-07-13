package Utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
	
	 private static ExtentReports extent;

	    public static ExtentReports createInstance() {
	        ExtentSparkReporter spark = new ExtentSparkReporter("Reports/ExtentReport1.html");
	        spark.config().setDocumentTitle("API Automation Report");
	        spark.config().setReportName("ReqRes API Test Results");

	        extent = new ExtentReports();
	        extent.attachReporter(spark);
	        extent.setSystemInfo("Tester", "Vinothkumar");
	        extent.setSystemInfo("Framework", "RestAssured + TestNG");

	        return extent;
	    }


}
