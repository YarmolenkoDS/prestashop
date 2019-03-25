package ua.com.qatestlab.prestashopautomation.yarmolenko;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * The type Header to discribe of the site header
 */
public class Header {
    private WebDriver driver;

    /**
     * Installed currency in the site header
     */
    @FindBy(xpath = "//span[@class=\"expand-more _gray-darker hidden-sm-down\"]")
    private WebElement selectedCurrency;

    /**
     * The list of currency choices in the site header
     */
    @FindBy(xpath = "//div[@class=\"currency-selector dropdown js-dropdown open\"]//a[@class=\"dropdown-item\"]")
    private List<WebElement> dropDawnListOfCurrency;

    /**
     * Constructor
     */
    public Header(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    /**
     * Method to get the installed currency in the header of the site
     *
     * @return the installed currency in the header of the site
     */
    public String getSelectedCurrency() {
        return selectedCurrency.getText();
    }

    /**
     * Method to get the list of currency choices in the site header
     *
     * @return the list of currency choices in the site header
     */
    private List<WebElement> getDropDawnListOfCurrency() {
        selectedCurrency.click();
        WebDriverWait wait = (new WebDriverWait(driver, 10));
        wait.until(ExpectedConditions.visibilityOfAllElements(dropDawnListOfCurrency));
        return dropDawnListOfCurrency;
    }

    /**
     * Method to set the currency in the header in the site header
     *
     * @param currency is the string "USD" or "UAH" or "EUR"
     * @throws IllegalArgumentException the illegal argument exception
     */
    public void setTheCurrencyOfPrices(String currency) throws IllegalArgumentException {
        if (currency.equals("USD") || currency.equals("UAH") || currency.equals("EUR")) {
            for (WebElement we : getDropDawnListOfCurrency()) {
                if (we.getText().contains(currency)) {
                    we.click();
                    return;
                }
            }
        } else {
            throw new IllegalArgumentException("The method parameter must be the"
                    + " string \"USD\" or \"UAH\" or \"EUR\"");
        }
    }
}
