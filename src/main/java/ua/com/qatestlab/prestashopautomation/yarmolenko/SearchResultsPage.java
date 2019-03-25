package ua.com.qatestlab.prestashopautomation.yarmolenko;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * The type SearchResultsPage to discribe of the search results page
 */
public class SearchResultsPage {
    private WebDriver driver;

    /**
     * The selected condition for sorting goods
     */
    @FindBy(xpath = "//a[@class=\"select-title\"]")
    private WebElement selectedSortCondition;

    /**
     * Selection list of conditions for sorting goods
     */
    @FindBy(xpath = "//div[@class=\"dropdown-menu\"]//a")
    private List<WebElement> dropDawnListOfSortConditions;

    SearchResultsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    /**
     * Method to get the list of selection conditions for sorting goods
     *
     * @return the list of selection conditions for sorting goods
     */
    private List<WebElement> getDropDawnListOfSortConditions() {
        selectedSortCondition.click();
        WebDriverWait wait = (new WebDriverWait(driver, 10));
        wait.until(ExpectedConditions.visibilityOfAllElements(dropDawnListOfSortConditions));
        return dropDawnListOfSortConditions;
    }

    /**
     * Method for seting condition for sorting goods
     *
     * @param sortCondition is the string for seting condition for sorting goods
     * @throws IllegalArgumentException the illegal argument exception
     */
    public void setTheSortCondition(String sortCondition) throws IllegalArgumentException{
        if (sortCondition.equals("от высокой к низкой") || sortCondition.equals("от низкой к высокой")
                || sortCondition.equals("от А к Я") || sortCondition.equals("от Я к А")
                || sortCondition.equals("Релевантность")) {
            for (WebElement we : getDropDawnListOfSortConditions()) {

                if (we.getText().contains(sortCondition)) {
                    String url = "";
                    switch (sortCondition) {
                        case "от высокой к низкой":
                            url = "search?controller=search&order=product.price.desc&s=";
                            break;
                        case "от низкой к высокой":
                            url = "search?controller=search&order=product.price.asc&s=";
                            break;
                        case "от А к Я":
                            url = "search?controller=search&order=product.name.asc&s=";
                            break;
                        case "от Я к А":
                            url = "search?controller=search&order=product.name.desc&s=";
                            break;
                        case "Релевантность":
                            url = "search?controller=search&order=product.position.asc&s=";
                            break;
                    }

                    we.click();
                    WebDriverWait wait = (new WebDriverWait(driver, 10));
                    wait.until((ExpectedConditions.urlContains(url)));
                    return;
                }
            }
        } else {
            throw new IllegalArgumentException("The method parameter must be the"
                    + " string \"от высокой к низкой\" or \"от низкой к высокой\" or \"от А к Я\" or \"от Я к А\"");
        }

    }
}
