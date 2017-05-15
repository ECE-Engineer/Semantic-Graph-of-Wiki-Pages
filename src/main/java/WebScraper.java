import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kyle Zeller
 * Webscraper is used to collect all the necessary information about a given wiki page.
 */
public class WebScraper implements Serializable {
    private String url;
    private HashMap<String, Integer> wordMap;
    private HashSet<String> linkMap;
    private String fileHeader;

    /**
     * Creates WebScraper for pulling all the wiki data
     * @param u is a url for a wiki page
     */
    WebScraper (String u) throws IOException {
        url = u;
        wordMap = new HashMap<>();
        linkMap = new HashSet<>();
    }

    /**
     * Returns the word HashMap
     * @return returns the word HashMap
     */
    public HashMap<String, Integer> getWordMap() {
        return wordMap;
    }

    /**
     * Returns the url used to create the WebScraper
     * @return returns the url used to create the WebScraper
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the HashSet of all the links in the wiki page
     * @return returns the HashSet of all the links in the wiki page
     */
    public HashSet<String> getLinkKeys() {
        //Get the keys
        return linkMap;
    }

    /**
     * Returns an arrayList of links
     * @param num is used to cap the number of links to be returned
     * @return returns an arrayList of links
     */
    public ArrayList<String> getLinkKeys(int num) {
        //Get the keys
        ArrayList<String> temp = new ArrayList<>();
        int size = linkMap.size();
        int counter = (size < num) ? size : num;
        for (String ele : linkMap) {
            if (counter == 0)
                break;
            else
                temp.add(ele);
            counter--;
        }
        return temp;
    }

    /**
     * Returns the fileHeader from the WebScraper
     * @param doc is used to set & get the fileHeader
     * @return returns the fileHeader from the WebScraper
     */
    public String getFileHeader(Document doc) throws IOException {
        fileHeader = doc.title();
        return fileHeader;
    }

    /**
     * It is used to set the HashSet with all the valid links in a wiki page
     * @param doc is used to set the HashSet with all the valid links in a wiki page
     */
    public void setLinkMap(Document doc) throws IOException {
        fileHeader = doc.title();
        //Collect all the links
        Element pageContent = doc.select("div.mw-content-ltr").first();
        Elements paragraphs = pageContent.select("p");
        Elements links = paragraphs.select("a[href]");

        for (Element link : links) {
            String temp = link.attr("href");
            //System.out.println(temp);
            //Filter the links for the wiki pages ONLY as non-junk wiki pages
            if ((temp.contains("/wiki/")) && (!temp.contains("Wikipedia:")) && (!temp.contains("%")) && (!temp.contains("Special:")) && (!temp.contains("Help")) && (!temp.contains(".php")) && (!temp.contains("cite")) && (!temp.contains("File:")) && (!temp.contains("Template"))) {
                //Check to see if the key is already in the HASHSET
                linkMap.add(temp);
            }
        }
    }

    /**
     * Returns the frequency of a word in a wiki page
     * @param s is used to get the frequency of a word in a wiki page
     * @return returns the frequency of a word in a wiki page
     */
    public int getFreq(String s) {
        return wordMap.get(s);
    }

    /**
     * Returns the most frequently occurring word on a wiki page
     * @param wKeys is used to identify the most frequently occurring word
     * @return returns the most frequently occurring word on a wiki page
     */
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

    /**
     * Returns a set of all the words in the word HashMap
     * @return a set of all the words in the word HashMap
     */
    public Set<String> getWordKeys() {
        //Get the keys
        return wordMap.keySet();
    }

    /**
     * Returns an array of a specified number
     * @param num is used to set a cap on the number of words to be returned
     * @return returns an array of Strings of a specified number
     */
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

    /**
     * Set the HashMap of words and their occurrences in the wiki page
     * @param doc is used to set the HashMap of words and their occurrences in the wiki page
     */
    public void setWordMap(Document doc) throws IOException {
        fileHeader = doc.title();
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