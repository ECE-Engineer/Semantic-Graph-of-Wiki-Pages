/**
 * @author Kyle Zeller
 * This class provides a way to store two different objects together to be used.
 */
public class Tuple {
    private String url;
    private WebScraper webScraper;

    /**
     * Creates the String / Webscraper tuple
     * @param url is the string representation of a website url
     * @param webScraper is a WebScraper that is used to pull information off websites
     */
    Tuple (String url, WebScraper webScraper) {
        this.url = url;
        this.webScraper = webScraper;
    }

    /**
     * Returns the string representation of a website url
     * @return returns the string representation of a website url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the WebScraper that is used to pull information off websites
     * @return returns the WebScraper that is used to pull information off websites
     */
    public WebScraper getWebScraper() {
        return webScraper;
    }
}
