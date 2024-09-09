package com.aisera.qa.automation;

import com.aisera.qa.automation.DriverFactory;
import com.aisera.qa.automation.ExcelDataProvider;
import com.aisera.qa.automation.ReportWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;

public class TenantUrlTest {
    WebDriver driver;
    WebDriverWait wait;
    ReportWriter reportWriter;

    @BeforeClass
    public void setUp() throws IOException {
        // Initialize the WebDriver using DriverFactory
        driver = DriverFactory.getDriver();
        
        // Set an implicit wait of 10 seconds
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Initialize WebDriverWait with a timeout of 15 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Initialize the ReportWriter
        reportWriter = new ReportWriter("test-report.csv");
    }

    @Test(dataProvider = "urlProvider", dataProviderClass = ExcelDataProvider.class)
    public void testUrlLoading(String websiteName, String url) throws IOException {
        driver.get(url);

        wait.until(ExpectedConditions.titleIs(driver.getTitle()));
        String pageTitle = driver.getTitle();

        // Validate that the page title is not empty or null, indicating the page loaded properly
        boolean isPageLoaded = pageTitle != null && !pageTitle.isEmpty();

        boolean isChatbotLoaded = false;
        boolean isChatbotOpened = false;
        try {
            // Explicit wait for the iFrame to be available
            WebElement chatbotIframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("awc-webchat")));
            driver.switchTo().frame(chatbotIframe);
            isChatbotOpened = chatbotIframe.isEnabled();

            // Explicit wait for a known element within the chatbot
            //WebElement chatbotElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("chat-opener")));
            //isChatbotLoaded = chatbotElement.isDisplayed();

            //driver.wait(10000);
           // driver.findElement(By.className("chat-opener")).click();

            //WebElement openingMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("chat-answer")));
           // assert openingMessage.isDisplayed();
           // isChatbotOpened = openingMessage.isDisplayed();
            //System.out.println("Opening message is displayed.");

            // Switch back to the main content
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            isChatbotLoaded = false; // If any exception occurs, assume chatbot is not loaded
        }

        // Log the result using ReportWriter
        String status = isPageLoaded && isChatbotLoaded ? "Pass" : "Fail";
        reportWriter.logResult(websiteName, status);

        // Assert that the page loaded
        Assert.assertTrue(isPageLoaded, "The page failed to load: " + url);
        //Assert.assertTrue(isChatbotLoaded, "The chatbot failed to load on the page: " + url);
        Assert.assertTrue(isChatbotOpened, "The chatbot failed loaded on the :" + url);
        //Assert.assertTrue(isLoaded, "The page failed to load: " + url);
    }

    @AfterClass
    public void tearDown() throws IOException {
        if (reportWriter != null) {
            reportWriter.close();
        }
        DriverFactory.quitDriver();
    }
}

