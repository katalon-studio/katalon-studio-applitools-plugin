package com.kms.katalon.keyword.applitools

import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.events.EventFiringWebDriver

import com.applitools.eyes.BatchInfo
import com.applitools.eyes.MatchLevel
import com.applitools.eyes.RectangleSize
import com.applitools.eyes.TestResults
import com.applitools.eyes.selenium.Eyes
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.logging.ErrorCollector
import com.kms.katalon.core.setting.BundleSettingStore
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory

import groovy.json.JsonSlurper
import groovy.transform.CompileStatic


@CompileStatic
class Utils {
	static BundleSettingStore bundleSetting
	static String API_KEY
	static String APPNAME
	static MatchLevel MATCH_LEVEL
	static String RESULT_MESSAGE
	static List<int[]> APPLITOOLS_VIEW_PORT

	static {
		try {
			bundleSetting = new BundleSettingStore(RunConfiguration.getProjectDir(), 'com.kms.katalon.keyword.Applitools-Keywords', true)

			API_KEY = bundleSetting.getString('API Key', '')
			if (StringUtils.isBlank(API_KEY)) {
				throw new IllegalStateException("Applitools's API Key is missing.")
			}

			APPNAME = bundleSetting.getString('Application Name', '')
			MATCH_LEVEL = MatchLevel.valueOf(bundleSetting.getString('Match Level', '').toUpperCase())
			RESULT_MESSAGE = ""

			String visualGridBundleSetting = bundleSetting.getString('Visual Grid View Port', '')
			if (StringUtils.isBlank(visualGridBundleSetting)) {
				APPLITOOLS_VIEW_PORT = null;
			} else {
				APPLITOOLS_VIEW_PORT = (List<int[]>) new JsonSlurper().parseText(visualGridBundleSetting)
			}
		} catch (Exception e) {
			e.printStackTrace()
			throw e
		}
	}

	/**
	 * Get result (in boolean) and result message from the list of returned results (TestResults).
	 *
	 * @param results The list of returned results (TestResults).
	 * @return The result of the test (in boolean).
	 */
	@CompileStatic
	private static boolean getResult(List<TestResults> results) throws IOException {
		boolean exception = true
		String message = ""
		String failureMsg= ""
		for (TestResults result : results ){
			if (result == null) {
				message += "Test aborted. See URl: Undefined." + "\n"
				exception = false
			} else {
				if (result.isNew()){
					message += 'New Baseline created. See URL: '+ result.getUrl() +  "\n"
				}
				else if ((result.getMismatches()!=0 || result.getMissing()!=0 )){
					failureMsg += 'Test Failed. See URL: ' + result.getUrl() + "\n"
					exception = false
				}
				else {
					message += 'Test Passed. See URL: ' + result.getUrl() + "\n"
				}
			}
		}

		if (!exception){
			RESULT_MESSAGE = failureMsg
		}
		else{
			RESULT_MESSAGE = message
		}
		return exception
	}

	/**
	 * Handle the result and thrown exception (if any) from the list of returned results (TestResults).
	 *
	 * @param results The list of returned results (TestResults).
	 */
	@CompileStatic
	static void handleResult(List<TestResults> results) throws IOException {
		if (!getResult(results)){
			KeywordUtil.logInfo(RESULT_MESSAGE)
			KeywordUtil.markFailed(RESULT_MESSAGE)
		}
		else {
			KeywordUtil.logInfo(RESULT_MESSAGE)
		}
	}

	/**
	 * Get a RectangleSize object from a specific width and height.
	 *
	 * @param width The width
	 * @param height. The height
	 * @return a RectangleSize object.
	 */
	@CompileStatic
	static RectangleSize getViewport(int width, int height) {
		return new RectangleSize(width, height)
	}

	/**
	 * Since 7.0 Katalon's Web Driver is wrapped inside EventFiringWebDriver which is incompatible
	 * with RemoteWebDriver. This method attempts to unwrap it and get back the original web driver
	 * @return The original WebDriver
	 */
	@CompileStatic
	static WebDriver getDriverForEyes() {
		WebDriver katalonWebDriver = DriverFactory.getWebDriver();
		if(katalonWebDriver instanceof EventFiringWebDriver) {
			EventFiringWebDriver eventFiring = (EventFiringWebDriver) DriverFactory.getWebDriver();
			return eventFiring.getWrappedDriver();
		}
		return katalonWebDriver;
	}
}
