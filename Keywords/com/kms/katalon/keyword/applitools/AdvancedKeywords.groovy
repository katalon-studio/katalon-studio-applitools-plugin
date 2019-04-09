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
public class AdvancedKeywords {

	/**
	 * Runs a checkpoint of the entire application (e.g. browser) window.
	 *
	 * @param eyes The initialized eyes object.
	 * @param message An message to be associated with the snapshot.
	 */
	@CompileStatic
	@Keyword
	static void checkWindow(Eyes eyes, String message) throws IOException {
		eyes.checkWindow(message)
	}

	/**
	 * Takes a snapshot of the application under test and matches a region ( or element) specified by the given selector with the expected region output.
	 *
	 * @param eyes The initialized eyes object.
	 * @param selector Selects the region/element to check.
	 * @param matchTimeout The amount of time to retry matching. (Milliseconds).
	 * @param message An message to be associated with the snapshot.
	 * @param stitchContent If stitchContent is false, it will match a region of a specific element with the expected region output. Otherwise, matches an specified element with the expected region output.
	 */
	@CompileStatic
	@Keyword
	static void checkRegion(Eyes eyes, By selector, int matchTimeout, String message, boolean stitchContent ) throws IOException {
		WebDriver driver = DriverFactory.getWebDriver()
		Object activeElement = Utils.setHideCaret(driver, Utils.HIDE_CARET)
		eyes.checkRegion(selector, matchTimeout, message, stitchContent)
		Utils.setFocus(driver, activeElement)
	}

	/**
	 * Takes a snapshot of the application under test and matches a region ( or element) of a specific element with the expected region output.
	 *
	 * @param eyes The initialized eyes object.
	 * @param element The element which represents the region/element to check.
	 * @param matchTimeout The amount of time to retry matching. (Milliseconds).
	 * @param message An message to be associated with the snapshot.
	 * @param stitchContent If stitchContent is false, it will match a region of a specific element with the expected region output. Otherwise, matches an specified element with the expected region output.
	 */
	@CompileStatic
	@Keyword
	static void checkRegion(Eyes eyes, WebElement element, int matchTimeout, String message, boolean stitchContent ) throws IOException {
		eyes.checkRegion(element, matchTimeout, message, stitchContent)
	}
}
