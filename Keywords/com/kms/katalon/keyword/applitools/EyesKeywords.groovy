package com.kms.katalon.keyword.applitools

import org.openqa.selenium.WebDriver

import com.applitools.eyes.RectangleSize
import com.applitools.eyes.TestResults
import com.applitools.eyes.selenium.Eyes
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.driver.DriverFactory

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
		Eyes eyes = eyesOpenWithBaseline(null, testName, viewportSize)
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
		try {
			List<TestResults> results = new ArrayList<>()
			results.add(eyes.close(false))
			Utils.handleResult(results)
			return results
		} finally {
			eyes.abortIfNotClosed()
		}
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
		Eyes eyes = eyesInit()
		final WebDriver driver = Utils.getDriverForEyes();
		if (baselineName != null) {
			eyes.setBaselineEnvName(baselineName)
		}
		if (viewportSize == null) {
			eyes.open(driver, Utils.APPNAME, testName)
		} else {
			eyes.open(driver, Utils.APPNAME, testName, viewportSize)
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
	static Eyes eyesSetUp() throws IOException {
		Eyes eyes = eyesInit()
		eyes.setForceFullPageScreenshot(true)
		if (Utils.MATCH_LEVEL != null) {
			eyes.setMatchLevel(Utils.MATCH_LEVEL)
		}
		return eyes
	}

	/**
	 * Initialize an Eyes instance without any configuration.
	 */
	@CompileStatic
	@Keyword
	static Eyes eyesInit() {
		Eyes eyes = new Eyes()
		eyes.setApiKey(Utils.API_KEY)
		return eyes
	}
}
