import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Kyle on 5/8/2017.
 */
public class WebScraper {
    private String url;
    private HashMap<String, Integer> wordMap;
    private HashMap<String, Integer> linkMap;
    private Document doc;

    WebScraper (String u) throws IOException {
        url = u;
        doc = Jsoup.connect(url).get();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String u) throws IOException {
        this.url = u;
        wordMap.clear();
        linkMap.clear();
        doc = Jsoup.connect(url).get();
    }

    public Set<String> getLinkKeys() {
        //Get the keys
        return linkMap.keySet();
    }

    public String[] getLinkKeys(int num) {
        //Get the keys
        String[] temp = new String[num];
        int counter = 0;
        Set<String> linkSet = linkMap.keySet();
        for (String ele : linkSet) {
            if (counter == num)
                break;
            else
                temp[counter] = ele;
            counter++;
        }
        return temp;
    }

    public void setLinkMap() {
        //Collect all the links
        Elements links = doc.select(".mw-content-ltr a");
        linkMap = new HashMap<>();
        for (Element ele : links) {
            //Filter the links for the wiki pages ONLY as non-junk wiki pages
            if ((ele.attr("abs:href").toString().contains("https://en.wikipedia.org/wiki/")) && (!ele.attr("abs:href").toString().contains("Wikipedia:")) && (!ele.attr("abs:href").toString().contains("#")) && (!ele.attr("abs:href").toString().contains("%")) && (!ele.attr("abs:href").toString().contains("Special:")) && (!ele.attr("abs:href").toString().contains(".php")) && (!ele.attr("abs:href").toString().contains("File:")) && (!ele.attr("abs:href").toString().contains("Template"))) {
                //Check to see if the key is already in the HASHMAP
                String temp = ele.attr("abs:href").toString();
                if (linkMap.containsKey(temp))
                    linkMap.put(temp, (linkMap.get(temp) + 1));
                else
                    linkMap.put(temp, 1);
            }
        }
    }

    public int getFreq(String s) {
        return wordMap.get(s);
    }

    public String getMostFreqWord(Set<String> wKeys) {
        //Find the most frequent word in the document
        String mostFreqWord = "";
        int keySetCounter = 0;
        for (String eachWord : wKeys) {
            if (keySetCounter == 0)
                mostFreqWord = eachWord;
            else
            if (wordMap.get(mostFreqWord) < wordMap.get(eachWord))
                mostFreqWord = eachWord;
            keySetCounter++;
        }

        return mostFreqWord;
    }

    public Set<String> getWordKeys() {
        //Get the keys
        return wordMap.keySet();
    }

    public String[] getWordKeys(int num) {
        //Get the keys
        String[] temp = new String[num];
        int counter = 0;
        Set<String> linkSet = wordMap.keySet();
        for (String ele : linkSet) {
            if (counter == num)
                break;
            else
                temp[counter] = ele;
            counter++;
        }
        return temp;
    }

    public void setWordMap() throws IOException {
        //Pull all the words from the paragraphs of the Wiki Page
        Elements paragraphs = doc.select(".mw-content-ltr p");

        final int MIN_WORD_LENGTH = 1;

        //Store all the text in a String
        String allText = "";
        for (Element ele : paragraphs)
            allText += ele.text();

        //Create a temporary character array
        char[] tempLetterBuffer = new char[allText.length()];
        for (int i = 0; i < allText.length(); i++)
            tempLetterBuffer[i] = allText.charAt(i);

        //Replace all INVALID punctuation with whitespace
        //Ignore a-z and A-Z and whitespace and '
        for (int i = 0; i < tempLetterBuffer.length; i++)
            if (((int) tempLetterBuffer[i] != 32) && ((int) tempLetterBuffer[i] != 39) && !(((int) tempLetterBuffer[i] >= 65) && ((int) tempLetterBuffer[i] <=90)) && !(((int) tempLetterBuffer[i] >= 97) && ((int) tempLetterBuffer[i] <=122)))
                tempLetterBuffer[i] = (char) 32;

        //Rebuild the string
        allText = "";
        for (int i = 0; i < tempLetterBuffer.length; i++)
            allText += tempLetterBuffer[i];


        //Create an array of Strings, whereas every entry is a word
        String[] allTextArray = allText.split("\\s+");

        //Manually create a list of filler words to be filtered out later
        String[] fillerWords = {"a", "an", "as", "at", "the", "he", "she", "they", "them", "that",
                "just", "then", "to", "too", "was", "is", "can", "because", "but", "for",
                "this", "not", "of", "it's", "they're", "from", "can't", "in", "on", "do",
                "don't", "or", "does", "its", "like", "cannot", "onto", "until", "and", "when",
                "how", "where", "were", "why", "who", "whom", "they'll", "their", "are", "these"
                , "those", "get", "got", "with", "within", "about", "may", "must", "which", "by"
                , "be", "into", "also", "since", "such", "while", "really", "said", "goes", "our"};

        //Create a HashMap to add after deleting the filler words and keep track of the frequency of each occurrence of a word
        wordMap = new HashMap<>();
        for (int i = 0; i < allTextArray.length; i++) {
            if (allTextArray[i].length() > MIN_WORD_LENGTH) {
                //Check to see if the word is not "filler"
                boolean filler = false;
                for (int j = 0; j < fillerWords.length; j++) {
                    if (allTextArray[i].equalsIgnoreCase(fillerWords[j])) {
                        filler = true;
                        break;
                    }
                }
                if (!filler){
                    //Check to see if the key is already in the HASHMAP
                    //If it is increment the count to keep track of the word frequency
                    if (wordMap.containsKey(allTextArray[i]))
                        wordMap.put(allTextArray[i], (wordMap.get(allTextArray[i]) + 1));
                    else
                        wordMap.put(allTextArray[i], 1);
                }
            }
        }
    }
}
