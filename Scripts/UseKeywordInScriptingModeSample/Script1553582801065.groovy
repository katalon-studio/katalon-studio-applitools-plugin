import org.openqa.selenium.By as By
import org.openqa.selenium.WebDriver as WebDriver
import org.openqa.selenium.WebElement as WebElement

import com.applitools.eyes.selenium.Eyes as Eyes
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.keyword.applitools.Utils

//-----------------------------------------check eyesOpen/checkWindow/checkRegion/eyesClose ------------------------------------------------------------
//use 'https://applitools.com/helloworld2?diff1' to make changes to page
WebUI.openBrowser('https://applitools.com/helloworld2?diff2')

//initialize eyes
Eyes eyes = CustomKeywords.'com.kms.katalon.keyword.applitools.EyesKeywords.eyesOpen'('uniqueString0', null)

WebDriver driver = Utils.getDriverForEyes()

driver.findElement(By.id('name')).sendKeys('My Name')

By locatorBy = By.id('name')

eyes.checkRegion(locatorBy, 'check checkRegion by selector keyword')

driver.findElement(By.tagName('button')).click()

WebElement element = driver.findElement(By.xpath('//img[contains(@class,\'diff2\')]'))

//check region by web element
CustomKeywords.'com.kms.katalon.keyword.applitools.BasicKeywords.checkElement'(eyes, element)

//check window not including eyes set up
eyes.checkWindow('check checkWindow')

CustomKeywords.'com.kms.katalon.keyword.applitools.EyesKeywords.eyesClose'(eyes)

WebUI.closeBrowser()

