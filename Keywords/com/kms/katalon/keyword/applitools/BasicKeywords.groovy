package com.kms.katalon.keyword.applitools

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.applitools.eyes.BatchInfo
import com.applitools.eyes.RectangleSize
import com.applitools.eyes.TestResults
import com.applitools.eyes.selenium.Eyes
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory

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
		Eyes eyes = EyesKeywords.eyesSetUp()
		BatchInfo batchInfo = new BatchInfo(testName)
		eyes.setBatch(batchInfo)
		final WebDriver driver = Utils.getDriverForEyes();
		if (Utils.APPLITOOLS_VIEW_PORT == null) {
			driver = eyes.open(driver, Utils.APPNAME, testName)
			doCheckWindow(eyes, results)
		} else {
			for (int[] viewport: Utils.APPLITOOLS_VIEW_PORT) {
				RectangleSize viewportSize = new RectangleSize(viewport[0], viewport[1])
				KeywordUtil.logInfo("Use view port " + viewportSize)
				driver = eyes.open(driver, Utils.APPNAME, testName, viewportSize)
				doCheckWindow(eyes, results)
			}
		}
		Utils.handleResult(results)
	}

	private static void doCheckWindow(Eyes eyes, List results) {
		try {
			eyes.checkWindow()
			def result = eyes.close(false)
			results.add(result)
		} finally {
			eyes.abortIfNotClosed()
		}
	}

	/**
	 * Initialize an eyes instance. Takes a snapshot of the application under test and matches an element specified by the given test object with the expected region output.
	 *
	 * @param testName The name of the test. This name must be unique within the scope of the application name. It may be any string.
	 */
	@CompileStatic
	@Keyword
	static void checkTestObject(TestObject testObject, String testName) throws IOException {
		List<TestResults> results = new ArrayList<>()
		Eyes eyes = EyesKeywords.eyesSetUp()
		BatchInfo batchInfo = new BatchInfo(testName)
		eyes.setBatch(batchInfo)
		List rows = WebUiCommonHelper.findWebElements(testObject, 60)
		final WebDriver driver = Utils.getDriverForEyes();
		if (rows.size() >= 1) {
			WebElement element = (WebElement)rows.get(0)
			if (Utils.APPLITOOLS_VIEW_PORT == null) {
				eyes.open(driver, Utils.APPNAME, testName)
				doCheckTestObject(eyes, element, results)
			} else {
				for (int[] viewport: Utils.APPLITOOLS_VIEW_PORT) {
					RectangleSize viewportSize = new RectangleSize(viewport[0], viewport[1])
					KeywordUtil.logInfo("Use view port " + viewportSize)
					eyes.open(driver, Utils.APPNAME, testName, viewportSize)
					doCheckTestObject(eyes, element, results)
				}
			}
		}
		Utils.handleResult(results)
	}

	private static doCheckTestObject(Eyes eyes, WebElement element, List results) {
		try {
			eyes.checkRegion(element)
			def result = eyes.close(false)
			results.add(result)
		} finally {
			eyes.abortIfNotClosed()
		}
	}
}
