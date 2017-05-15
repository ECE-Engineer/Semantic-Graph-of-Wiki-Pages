import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.*;

/**
 * @author Kyle Zeller
 * This class is used primarily to perform the main features of the program, to put together the graph,
 * perform the connectivity check, and perform dijkstra's shortest path algorithm.
 */
public class GUI extends PriorityQueue<Pair> {
    Graph graph;
    HashSet<String> allLabels;
    int count = 0;
    int counter = 0;
    int globalCount = 0;

    GUI() throws IOException {
        graph = new Graph();
    }

    /**
     * Returns the number of nodes in the spanning tree
     * @param tuple is a Tuple for accessing the label and webscraper
     * @return returns the number of nodes in the spanning tree
     */
    public int connectivityCheck(Tuple tuple) throws IOException {
        allLabels = new HashSet<>();
        Document doc = Jsoup.connect("https://en.wikipedia.org" + tuple.getWebScraper().getUrl()).get();
        allLabels.add(tuple.getWebScraper().getFileHeader(doc));
        count = 0;
        count++;
        visitNodes(graph.getNode(tuple.getWebScraper().getFileHeader(doc)));
        System.out.println(allLabels.size());
        return count;
    }

    /**
     * Visits all the nodes in the graph in a depth first manner
     */
    public void visitNodes(Node init) {
        Stack<Node> stack = new Stack<>();
        stack.push(init);
        while (!stack.isEmpty()) {
            Node n = stack.pop();
            for (int i = 0; i < n.getNeighborCount(); i++) {
                Node neighbor = n.getNeighbor(i).getNeighbor(n);

                if (!allLabels.contains(neighbor.getLabel())) {
                    count++;
                    System.out.println(count);
                    allLabels.add(neighbor.getLabel());
                    stack.push(neighbor);
                }
            }
        }
    }

    /**
     * Initializes the connecting of the graph
     * @param tuple is a Tuple for accessing the label and webscraper
     * @param count is the MAX depth that a node can go
     * @param linkNum is the MAX amount of links that can be pulled for each wiki page
     */
    public void initGraph(String tuple, int count, int linkNum) throws IOException {
        connectNodes(tuple, count, linkNum, null);
        setWeights();
    }

    /**
     * Initializes the connecting of the graph
     * @param count is the MAX depth that a node can go
     * @param linkNum is the MAX amount of links that can be pulled for each wiki page
     * @param parentUrl is the label associated with the src node
     */
    public void connectNodes(String url, int count, int linkNum, String parentUrl) throws IOException {
        globalCount++;
        WebScraper simpleScraper = new WebScraper(url);
        Document doc = Jsoup.connect("https://en.wikipedia.org" + simpleScraper.getUrl()).get();
        simpleScraper.setLinkMap(doc);
        ArrayList<String> linksSet = simpleScraper.getLinkKeys(linkNum);
        Node n = graph.getNode(simpleScraper.getFileHeader(doc));
        if (n == null) {
            n = new Node(simpleScraper.getFileHeader(doc), simpleScraper);
            graph.addNode(n, false);
            counter += 1;
        }

        if (count < 1) {
            graph.addEdge(graph.getNode(parentUrl), n);
        }
        else {
            if (n.getNeighborCount() == 0) {
                if (parentUrl != null) {
                    graph.addEdge(graph.getNode(parentUrl), n);
                }
                for (int i = 0; i < linksSet.size(); i++) {
                    if (linksSet.get(i) != null) {
                        connectNodes(linksSet.get(i), count - 1, linkNum, n.getLabel());
                    }
                }
            }
        }
    }

    /**
     * Sets the weights by taking the cosine of the frequency of the common words between two wiki pages
     */
    public void setWeights() {
        graph.getEdges().forEach(edge -> {
            try {
                edge.setWeight(graph.createWeight(edge.getSrc().getWebScraper(), edge.getDst().getWebScraper()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Returns whether or not the pair was able to be added to the priority queue
     * @param pair is a Pair of a node and edge
     * @return returns whether or not the pair was able to be added to the priority queue
     */
    @Override
    public boolean add(Pair pair) {
        boolean replaceCheck = false;
        Pair temp = null;
        Iterator<Pair> set = super.iterator();
        while (set.hasNext()) {
            temp = set.next();
            if (temp.getNode().getLabel().equalsIgnoreCase(pair.getNode().getLabel())) {
                if (pair.getEdge().getWeight() < temp.getEdge().getWeight()) {
                    replaceCheck = true;
                }
            }
        }
        if (replaceCheck) {
            super.remove(temp);
        }
        return super.add(pair);
    }

    /**
     * Returns the shortest path from the src to dst
     * @param src is the start node
     * @param dst is the end node
     * @return returns the shortest path from the src to dst
     */
    public ArrayList<String> dijkstraSPA(String src, String dst) {
        PriorityQueue < Pair >  prq = new PriorityQueue < Pair > ();
        //get the string keys to all the nodes
        Set<String> nodeSet = graph.nodeKeys();
        //keep track of where you visited
        ArrayList<String> path = new ArrayList<>();
        while (!nodeSet.isEmpty()) {
            String nodeKey = (String) nodeSet.toArray()[0];
            nodeSet.remove(nodeKey);
            if (!prq.isEmpty()) {
                if (prq.poll().getEdge().getDst().getLabel().equalsIgnoreCase(dst))
                    return path;
                else {
                    path.add(nodeKey);
                    Edge e1 = prq.poll().getEdge();
                    Node n1 = e1.getDst();
                    int edgesSize = n1.getNeighborCount();
                    for (int i = 0; i < edgesSize; i++) {
                        if (!path.contains(n1.getNeighbor(i).getNeighbor(n1).getLabel()))
                            prq.add(new Pair(n1, n1.getNeighbor(i)));
                    }
                }
            }
        }
        return path;
    }
}