import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Kyle on 4/28/2017.
 */
public class Main {
    //task 1 is to pull data from online wiki pages
    //before doing that, I will get familiar with jsoup

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
        Elements newsHeadlines = doc.select("#mp-itn b a");////////THIS ENDS UP GETTING THE headings in the "NEWS" section of the wiki-page

        for (Element ele : newsHeadlines) {
            System.out.println(ele.html());
        }
    }
}
