import java.io.IOException;
import java.util.Set;

/**
 * Created by Kyle on 5/12/2017.
 */
public class GUI {
    Graph graph;
    //int globalCount = 0;

    GUI() throws IOException {
        graph = new Graph();
    }

    public void connectNodes(String url, int count, int linkNum) throws IOException {
        //System.out.println(count);
        //System.out.println(globalCount);
        //globalCount++;
        if (count < 1)
            return;
        else {
            WebScraper webScraper = new WebScraper(url);
            webScraper.setLinkMap();
            String[] linksSet = webScraper.getLinkKeys(linkNum);
            for (int i = 0; i < linksSet.length; i++){
                Node n = new Node(linksSet[i]);
                if (url != null && linksSet[i] != null) {
                    //System.out.println("SRC\t" + url + "DST\t" + linksSet[i]);
                    if (!graph.containsNode(n)) {
                        graph.addNode(n, true);
                        graph.addEdge(new Node(url), n);
                        connectNodes(linksSet[i], count--, linkNum);
                    } else {
                        graph.addEdge(new Node(url), n);
                        return;
                    }
                }
            }
        }
    }
}
