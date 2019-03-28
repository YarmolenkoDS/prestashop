package ua.com.qatestlab.prestashopautomation.yarmolenko.test;



import io.qameta.allure.Description;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ua.com.qatestlab.prestashopautomation.yarmolenko.Header;
import ua.com.qatestlab.prestashopautomation.yarmolenko.MainPage;
import ua.com.qatestlab.prestashopautomation.yarmolenko.ProductCards;
import ua.com.qatestlab.prestashopautomation.yarmolenko.SearchResultsPage;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class PrestashopTest {

    private EventFiringWebDriver driver;
    private MainPage mainPage;
    private ProductCards productCards;
    private Header header;
    private SearchResultsPage searchResultsPage;


    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\webdriver\\chromedriver.exe");
        WebDriver chromeDriver = new ChromeDriver();
        chromeDriver.manage().window().maximize();
        driver = new EventFiringWebDriver(chromeDriver);
        driver.register(new EventHandler());
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
        mainPage = new MainPage(driver);
        productCards = new ProductCards(driver);
        header = new Header(driver);
    }

    @Test(priority = 0)
    public void comparisonOfTheCurrencyInTheHeaderWithTheCurrencyOfTheCommodityCards() {
        Assert.assertTrue((header.getSelectedCurrency()).contains(productCards.getCurrencyOfProductCardsPrice()),
                "The mismatch of currencies in the header and on the cards");
    }


    @Test(priority = 1)
    public void checkThatTheSearchPageContainsAnInscription() {
        header.setTheCurrencyOfPrices("USD");
        searchResultsPage = mainPage.searchInCatalog("dress");
        Assert.assertTrue(driver.getPageSource().contains("Товаров: " + productCards
                .getProductCardsList().size()),"Page \"Результаты поиска\" does not contain "
                + "an inscription \"Товаров: \""
                + productCards.getProductCardsList().size());
    }

    @Test(priority = 2)
    public void checkThatThePricesOfGoodsAreInUSD() {
        Assert.assertEquals( productCards
                .getCurrencyOfProductCardsPrice(),"USD", "The currency of the commodity cards is not a dollar");
    }

    @Test(priority = 3)
    public void checkingTheSortingOfGoodsAtHighToLowPricesWithoutDiscount() {
        searchResultsPage.setTheSortCondition("от высокой к низкой");
        Assert.assertTrue(productCards.areSortedFromHighToLowPricesWithoutDiscount(),
                "The goods sorting from high to low prices without discount is not true");
    }

    @Test(priority = 4)
    public void checkThatThediscountedProductsHaveAPercentageDiscountWithThePriceBeforeAndAfterTheDiscount() {
        System.out.println(productCards.infoAboutProductsWithDiscont());
    }

    @Test(priority = 5)
    public void checkPricesBeforeAndAfterTheDiscount() {
        Assert.assertTrue(productCards.checkingPricesConsideringDiscounts(),"Price before and after the"
                + " discount does not match the specified discount size");
    }

    @Test(priority = 6)
    public void checkAddingProductToCart() throws InterruptedException {
        productCards.methodToAddProductToCart("Printed Dress");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
        File logsFile = new File("ChromeLogs.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logsFile))) {
            bw.write(EventHandler.sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventHandler.sb = new StringBuilder();
    }
}