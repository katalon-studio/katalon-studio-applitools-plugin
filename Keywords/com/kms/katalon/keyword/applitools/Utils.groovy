package com.kms.katalon.keyword.applitools

import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement

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
	static String APPNAME
	static boolean HIDE_CARET
	static MatchLevel MATCH_LEVEL
	static String RESULT_MESSAGE
	static List<int[]> APPLITOOLS_VIEW_PORT

	static {
		try {
			bundleSetting = new BundleSettingStore(RunConfiguration.getProjectDir(), 'com.kms.katalon.keyword.Applitools-Keywords',
					true)
			APPNAME = bundleSetting.getString('Application Name', '')
			HIDE_CARET = Boolean.parseBoolean(bundleSetting.getString('Hide Caret', ''))
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
	 * Hide the caret (cursor) before an image is captured.
	 *
	 * @param driver The initialized driver.
	 * @param status Set True to enable hiding the caret, false to disable hiding the caret. By default it will hide the caret before capturing the image so don't need to call this function when you don't want to change the default behavior.
	 * @return The active object.
	 */
	@CompileStatic
	static Object setHideCaret(WebDriver driver, boolean status) throws IOException {
		Object activeElement = null
		if (status){
			activeElement = ((JavascriptExecutor)driver).executeScript("var activeElement = document.activeElement; activeElement && activeElement.blur(); return activeElement;", new Object[0])
		}
		else {
			HIDE_CARET = false
		}
		return activeElement
	}

	/**
	 * Set focus on a specified object.
	 *
	 * @param driver The initialized driver.
	 * @param activeElement The object needs to set focus on.
	 */
	//	@CompileStatic
	//	static void setFocus(WebDriver driver, Object activeElement) throws IOException {
	//		if (activeElement != null) {
	//			try {
	//				((JavascriptExecutor)driver).executeScript("arguments[0].focus();", activeElement)
	//			}
	//			catch (WebDriverException var15) {
	//				KeywordUtil.logInfo("Could not return focus to active element! " + var15.getMessage())
	//			}
	//		}
	//	}

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
}
