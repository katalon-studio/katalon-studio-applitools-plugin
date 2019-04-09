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
public class BasicKeywords {

	/**
	 * Initialize an eyes instance and runs a checkpoint of the entire application (e.g. browser) window.
	 *
	 * @param testName The name of the test. This name must be UNIQUE within the scope of the application name.
	 * 	It may be any string.
	 */
	@CompileStatic
	@Keyword
	static void checkWindow(String testName) throws IOException {
		List<TestResults> results = new ArrayList<>()
		RectangleSize viewportSize = null
		Eyes eyes = EyesKeywords.eyesSetUp(testName)
		BatchInfo batchInfo = new BatchInfo(testName);
		eyes.setBatch(batchInfo);
		WebDriver driver = DriverFactory.getWebDriver()
		Object activeElement = Utils.setHideCaret(driver, Utils.HIDE_CARET)
		if (Utils.APPLITOOLS_VIEW_PORT == null) {
			driver = eyes.open(driver, Utils.APPNAME, testName)
			doCheckWindow(eyes, driver, activeElement, results)
		} else {
			for (int[] viewport: Utils.APPLITOOLS_VIEW_PORT) {
				viewportSize = new RectangleSize(viewport[0], viewport[1])
				driver = eyes.open(driver, Utils.APPNAME, testName, viewportSize)
				doCheckWindow(eyes, driver, activeElement, results)
			}
		}
		Utils.handleResult(results)
	}

	private static void doCheckWindow(Eyes eyes, WebDriver driver, activeElement, List results) {
		AdvancedKeywords.checkOpenedWindow(eyes, null)
		Utils.setFocus(driver, activeElement)
		def result = eyes.close(false)
		results.add(result)
	}

	/**
	 * Initialize an eyes instance. Takes a snapshot of the application under test and matches an element specified by the given test object with the expected region output.
	 *
	 * @param testName The name of the test. This name must be unique within the scope of the application name. It may be any string.
	 */
	@CompileStatic
	@Keyword
	static void checkElement(TestObject testObject, String testName) throws IOException {
		WebDriver driver = DriverFactory.getWebDriver()
		List<TestResults> results = new ArrayList<>()
		RectangleSize viewportSize = null
		Eyes eyes = EyesKeywords.eyesSetUp(testName)
		BatchInfo batchInfo = new BatchInfo(testName);
		eyes.setBatch(batchInfo);
		List rows = WebUiCommonHelper.findWebElements(testObject, 60)
		if (rows.size() >= 1) {
			WebElement element = (WebElement)rows.get(0);
			Object activeElement = Utils.setHideCaret(driver, Utils.HIDE_CARET)
			if (Utils.APPLITOOLS_VIEW_PORT == null) {
				driver = eyes.open(driver, Utils.APPNAME, testName)
				eyes.checkRegion(element, true)
				Utils.setFocus(driver, activeElement)
				def result = eyes.close(false)
				results.add(result)
			} else {
				for (int[] viewport: Utils.APPLITOOLS_VIEW_PORT) {
					viewportSize = new RectangleSize(viewport[0], viewport[1])
					driver = eyes.open(driver, Utils.APPNAME, testName, viewportSize)
					doCheckElement(eyes, element, driver, activeElement, results)
				}
			}
		}
		Utils.handleResult(results)
	}

	private static doCheckElement(Eyes eyes, WebElement element, WebDriver driver, activeElement, List results) {
		eyes.checkRegion(element, true)
		Utils.setFocus(driver, activeElement)
		def result = eyes.close(false)
		results.add(result)
	}
}
