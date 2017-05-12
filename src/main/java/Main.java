import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Kyle on 4/28/2017.
 */
public class Main {


    public static void main(String[] args) throws IOException {
        /**pull the wiki page data*/
        String rootWikiPage = "https://en.wikipedia.org/wiki/Artificial_general_intelligence";

       ///////////////////////////////////////////////////////////////// webScraper.setWordMap();
//
//        System.out.println("This url is : " + webScraper.getUrl());
//        Set<String> wordsSet = webScraper.getWordKeys();
//
//        System.out.println(wordsSet);
//        System.out.println(linksSet);
        /////////////////////////////////////////////////////////////!(((int) tempLetterBuffer[i] >= 65) && ((int) tempLetterBuffer[i] <=90)) && !(((int) tempLetterBuffer[i] >= 97) && ((int) tempLetterBuffer[i] <=122)))
//        System.out.println("This most frequent word is : " + webScraper.getMostFreqWord(wordsSet));
//        System.out.println("Frequency of word id : " + webScraper.getFreq(webScraper.getMostFreqWord(wordsSet)));

        /**create the graph*/
        GUI gui = new GUI();
        final int MAX_DEPTH = 4;
        final int MAX_LINKS = 5;

        //initialize some nodes and add them to the graph
        //illustrate the fact that duplicate edges aren't added
        gui.connectNodes(rootWikiPage, MAX_DEPTH, MAX_LINKS);
        //System.out.println("FINISHED CONNECTING");

//        Set<String> nodeKeys = gui.graph.nodeKeys();
//        System.out.println(nodeKeys);
//        System.out.println(nodeKeys.size());

        //display the initial setup- all nodes adjacent to each other
//        for (String eachNodeKey : nodeKeys){
//            System.out.println(gui.graph.getNode(eachNodeKey));
//            for(int j = 0; j < gui.graph.getNode(eachNodeKey).getNeighborCount(); j++){
//                System.out.println(gui.graph.getNode(eachNodeKey).getNeighbor(j));
//            }
//            System.out.println();
//        }






















        














        /**integrate with the cosine simularity*/
        /**integrate with Word2vec*/
        /**combine the two by averaging their results*/


        /**persist the graph*/


        /**visualize the graph using VIZ.js and the sparkjava API*/


        /**perform the spanning tree---connectivity check*/


        /**make a priority queue utilizing additional reconstruction techniques*/


        /**perform dijkstra's shortest path algorithm*/
    }
}