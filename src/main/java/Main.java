import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Kyle Zeller
 * This project was intended to create a graph by webscraping wiki-pages, to perform connectivity checks with spanning
 * trees, to perform dijkstra's shortest path algorithm between the root website and any other website within a certain
 * depth, and to be able to visualize the graph and the results dijkstra's shortest path algorithm.
 *
 * Non-functional parts of this program:
 * -dijkstra's shortest path algorithm (INC)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        //assign a root website
        String rootWikiPage = "/wiki/Artificial_general_intelligence";
        Tuple tuple = new Tuple(rootWikiPage, new WebScraper(rootWikiPage));

        //create the graph
        GUI gui = new GUI();
        if (gui.graph.isFileCreated()) {
            System.out.println("LOADING FROM FILE.....");
            gui.graph = gui.graph.createGraph();
        }
        else {
            System.out.println("LOADING FROM WIKI.....");
            final int MAX_DEPTH = 4;
            final int MAX_LINKS = 5;

            //assign weights to the edges using the cosine similarity of the frequencies of the common words in the scr & dst website
            gui.initGraph(rootWikiPage, MAX_DEPTH, MAX_LINKS);
            System.out.println("FINISHED CONNECTING");
            System.out.println("TOTAL NUMBER OF NODES IN GRAPH:\t" + gui.graph.nodeKeys().size());

            //persist the graph
            gui.graph.storeGraph(gui.graph);
        }

        //visualize the graph
        File tempFile = new File("data/graph.dot");
        String filePath = tempFile.getAbsolutePath();

        RandomAccessFile file = new RandomAccessFile("data/graph.dot", "rw");
        file.setLength(0);
        file.write("digraph G {\n".getBytes("latin1"));
        gui.graph.getEdges().forEach(edge -> {
            try {
                file.write(("\"" + edge.getSrc().getLabel().replaceAll("-", " ") + "\" -> \"" + edge.getDst().getLabel().replaceAll("-", " ") + "\"[label=\"" + edge.getWeight() + "\"];\n").getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        file.write("}".getBytes("UTF-8"));

        //perform the connectivity check to make sure that the amount of nodes visited in the spanning tree matches that of the amount of nodes in the graph
        int spannedNodes = gui.connectivityCheck(tuple);
        System.out.println("TOTAL NUMBER OF NODES IN SPANNING TREE:\t" + spannedNodes);
        System.out.println("Connectivity Test :" + (gui.graph.nodeKeys().size() == gui.connectivityCheck(tuple) ? "PASSES" : "FAILS"));

        //perform dijkstra's shortest path algorithm utilizing a priority queue
        System.out.println(gui.dijkstraSPA("Artificial general intelligence - Wikipedia", "Second Life - Wikipedia"));

        //save the visualization of the graph to a file
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("C:\\Users\\Kyle\\release\\bin\\dot.exe -Tpng " + filePath + " -o " + filePath.substring(0,filePath.lastIndexOf("\\")) + "\\graph.png");
    }
}