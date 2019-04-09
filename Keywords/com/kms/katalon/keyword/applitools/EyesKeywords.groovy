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
public class EyesKeywords {

	/**
	 * Initialize a wrapped WebDriver to starts a test
	 *
	 * @param testName The name of the test. This name must be unique within the scope of the application name. It may be any string.
	 * @return A wrapped WebDriver which enables Eyes trigger recording and frame handling.
	 */
	@CompileStatic
	@Keyword
	static Eyes eyesOpen(String testName, RectangleSize viewportSize) throws IOException {
		Eyes eyes = eyesSetUp(testName)
		WebDriver driver = DriverFactory.getWebDriver()
		if (viewportSize == null){
			driver = eyes.open(driver, Utils.APPNAME, testName)
		}
		else {
			KeywordUtil.logInfo("Use view port " + viewportSize);
			driver = eyes.open(driver, Utils.APPNAME, testName, viewportSize)
		}
		return eyes
	}

	/**
	 * End the test.
	 *
	 * @param eyes The initialized eyes object.
	 * @return The list of results of the test can be obtained from the object returned.
	 */
	@CompileStatic
	@Keyword
	static List<TestResults> eyesClose(Eyes eyes) throws IOException {
		List<TestResults> results = new ArrayList<>()
		results.add(eyes.close(false))
		Utils.handleResult(results)
		return results
	}


	/**
	 * Initialize a wrapped WebDriver to starts a test with a specified baseline name.
	 *
	 * @param baselineName The environment name that defines baseline. If the name is undefined in the server when the test runs then the name will be created and defined on the server with values of the current test environment defined by a triplet <OS = baselineName, Browser, Viewport>.
	 * @param testName The name of the test. This name must be unique within the scope of the application name. It may be any string.
	 * @return A wrapped WebDriver which enables Eyes trigger recording and frame handling.
	 */
	@CompileStatic
	@Keyword
	static Eyes eyesOpenWithBaseline(String baselineName, String testName, RectangleSize viewportSize) throws IOException {
		Eyes eyes = eyesSetUp(testName)
		WebDriver driver = DriverFactory.getWebDriver()
		eyes.setHostOS(baselineName)
		eyes.setBaselineEnvName(baselineName)
		if (viewportSize == null){
			driver = eyes.open(driver, Utils.APPNAME, testName)
		}
		else {
			driver = eyes.open(driver, Utils.APPNAME, testName, viewportSize)
		}
		return eyes
	}

	/**
	 * Initialize an eyes instance.
	 *
	 * @param testName The name of the test. This name must be unique within the scope of the application name. It may be any string.
	 * @return An eyes instance.
	 */
	@CompileStatic
	static Eyes eyesSetUp(String testName) throws IOException {
		Eyes eyes = new Eyes()
		eyes.setApiKey(Utils.bundleSetting.getString('API Key', ''))
		eyes.setForceFullPageScreenshot(true)
		if (Utils.MATCH_LEVEL != null){
			eyes.setMatchLevel(Utils.MATCH_LEVEL)
		}
		return eyes
	}
}
