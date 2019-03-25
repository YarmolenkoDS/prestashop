package ua.com.qatestlab.prestashopautomation.yarmolenko.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.events.EventFiringWebDriver;
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


    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\webdriver\\chromedriver.exe");
        WebDriver chromeDriver = new ChromeDriver();
        chromeDriver.manage().window().maximize();
        driver = new EventFiringWebDriver(chromeDriver);
        driver.register(new EventHandler());
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
    }

    @Test
    public void testContents() {
        MainPage mainPage = new MainPage(driver);
        ProductCards productCards = new ProductCards(driver);
        Header header = new Header(driver);

        System.out.println("Currency in the site header:" + header.getSelectedCurrency());
        System.out.println("Product cards currency:" + productCards.getCurrencyOfProductCardsPrice());
        System.out.println(productCards.getProductCardsPriceListWithDiscont());
        Assert.assertTrue("The mismatch of currencies in the header and on the cards",(header
                .getSelectedCurrency()).contains(productCards.getCurrencyOfProductCardsPrice()));
        System.out.println("Check for compliance with currencies in the header and on the cards of goods");

        System.out.println("-------------------------------------------------------------------------");

        header.setTheCurrencyOfPrices("USD");
        System.out.println("Currency in the site header:" + header.getSelectedCurrency());
        System.out.println("Set the currency USD");

        System.out.println("-------------------------------------------------------------------------");

        SearchResultsPage searchResultsPage = mainPage.searchInCatalog("dress");
        System.out.println("Searched the product catalog by word \"dress\"");

        System.out.println("-------------------------------------------------------------------------");

        Assert.assertTrue("Page \"Результаты поиска\" does not contain an inscription \"Товаров: \""
                + productCards.getProductCardsList().size(),driver.getPageSource().contains("Товаров: " + productCards
                .getProductCardsList().size()));
        System.out.println("Page \"Результаты поиска\" contains an inscription \"Товаров: " + productCards
                .getProductCardsList().size() + "\" : " + driver.getPageSource().contains("Товаров: " + productCards
                .getProductCardsList().size()));
        System.out.println("Check that page \"Результаты поиска\" contains an inscription \"Товаров: x\","
                        + "\nwhere x - the number of actually found goods");

        System.out.println("-------------------------------------------------------------------------");

        System.out.println("Product cards currency:" + productCards.getCurrencyOfProductCardsPrice());
        System.out.println(productCards.getProductCardsPriceListWithDiscont());
        Assert.assertEquals("The currency of the commodity cards is not a dollar", "USD", productCards
                .getCurrencyOfProductCardsPrice());
        System.out.println("Check that the prices of goods are in USD");

        System.out.println("-------------------------------------------------------------------------");

        searchResultsPage.setTheSortCondition("от высокой к низкой");
        System.out.println(productCards.getProductCardsPriceListWithDiscont());
        System.out.println("Sort goods from high to low prices");

        System.out.println("-------------------------------------------------------------------------");

        Assert.assertTrue("The goods sorting from high to low prices without discount is not true",productCards
                        .areSortedFromHighToLowPricesWithoutDiscount());
        System.out.println(productCards.getProductCardsPriceListWithoutDiscount());
        System.out.println("Checking the sorting of goods at high to low prices without discount");

        System.out.println("-------------------------------------------------------------------------");

        System.out.println(productCards.infoAboutProductsWithDiscont());
        System.out.println("Check that the discounted products have a percentage discount with the price before"
                + " and after the discount");

        System.out.println("-------------------------------------------------------------------------");

        Assert.assertTrue("Price before and after the discount does not match the specified discount"
                + " size",productCards.checkingPricesConsideringDiscounts());
        System.out.println("The price before and after the discount coincides with the specified discount amount."
                + " (if round up\n to two decimal places):   " + productCards
                .checkingPricesConsideringDiscounts());

        System.out.println("-------------------------------------------------------------------------");

    }

    @After
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