import org.jsoup.Jsoup;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

/**
 * @author Kyle Zeller
 * This class contains all the nodes and edges as well as how to store and create the graph.
 */
public class Graph implements Serializable{
    private HashMap<String, Node> nodes;
    private HashMap<Integer, Edge> edges;
    private String fileName = "./graph";
    private File file = new File(fileName);

    /**
     * Returns whether or not the file exists
     * @return returns whether or not the file exists
     */
    public boolean isFileCreated() {return file.exists();}

    /**
     * Persists the graph
     * @param graph is the graph of all the data
     */
    public void storeGraph(Graph graph) throws IOException, ClassNotFoundException {
        byte[] bytes = this.serialize(graph);
        Path path = Paths.get(fileName);
        Files.write(path, bytes);
    }

    /**
     * Returns the graph from the file
     * @return returns the graph from the file
     */
    public Graph createGraph() throws IOException, ClassNotFoundException {
        Path path = Paths.get(fileName);
        byte[] data = Files.readAllBytes(path);

        return (Graph) this.deserialize(data);
    }

    /**
     * Returns the filepath of the stored graph
     * @return returns the filepath of the stored graph
     */
    public String getFilePath() {
        return file.getAbsolutePath();
    }

    /**
     * Creates an array of bytes after serializing a given object.
     * @throws IOException is used for the IO exceptions that might occur
     * @param obj is the object you are serializing.
     */
    private byte[] serialize(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    /**
     * Returns a deserialized object given by a byte array.
     * @throws IOException is used for the IO exceptions that might occur
     * @param bytes is your object in bytes.
     * @return returns a deserialized object given by a byte array.
     */
    private Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            }
        }
    }

    /**
     * Creates the graph of nodes and edges
     */
    public Graph() {
        this.nodes = new HashMap<String, Node>();
        this.edges = new HashMap<Integer, Edge>();
    }

    /**
     * Creates the graph of nodes and edges
     * @param nodes is an arrayList to populate the graph with
     */
    public Graph(ArrayList<Node> nodes) {
        this.nodes = new HashMap<String, Node>();
        this.edges = new HashMap<Integer, Edge>();

        for (Node n : nodes) {
            this.nodes.put(n.getLabel(), n);
        }
    }

    /**
     * Returns a weight for the edge
     * @param a is a webscraper to collect all the data from one page
     * @param b is a webscraper to collect all the data from another page
     * @return returns a weight for the edge
     */
    public double createWeight(WebScraper a, WebScraper b) throws IOException {
        a.setWordMap(Jsoup.connect("https://en.wikipedia.org" + a.getUrl()).get());
        b.setWordMap(Jsoup.connect("https://en.wikipedia.org" + b.getUrl()).get());

        Set<String> setA = a.getWordKeys();
        Set<String> setB = b.getWordKeys();

        ArrayList<String> temp = new ArrayList<>();
        for (String ele : setA) {
            if (setB.contains(ele))
                temp.add(ele);
        }

        int sumOfProducts = 0, map1SumOfSquares = 0, map2SumOfSquares = 0;
        for (String word : temp) {
            sumOfProducts += (a.getWordMap().get(word)) * (b.getWordMap().get(word));
            map1SumOfSquares += (a.getWordMap().get(word)) * (a.getWordMap().get(word));
            map2SumOfSquares += (b.getWordMap().get(word)) * (b.getWordMap().get(word));
        }
        double cosineValue = (double) sumOfProducts / (Math.sqrt((double) map1SumOfSquares) * Math.sqrt((double) map2SumOfSquares));

        if (cosineValue == Double.NaN)
            cosineValue = 1;

        return cosineValue;
    }

    /**
     * Returns whether or not the edge was added to the graph
     * @param src is the start node
     * @param dst is the end node
     * @return returns whether or not the edge was added to the graph
     */
    public boolean addEdge(Node src, Node dst) {
        return addEdge(src, dst, 1);
    }

    /**
     * Returns whether or not the edge was added to the graph
     * @param src is the start node
     * @param dst is the end node
     * @param weight is the weight of the edge
     * @return returns whether or not the edge was added to the graph
     */
    public boolean addEdge(Node src, Node dst, double weight) {
        if (src.equals(dst)) {
            return false;
        }

        //checks to see if the edge is in the graph
        Edge e = new Edge(src, dst, weight);
        if (edges.containsKey(e.hashCode())) {
            return false;
        }

        //checks to see if the edge is already connected to a node in the graph
        else if (src.containsNeighbor(e) || dst.containsNeighbor(e)) {
            return false;
        }

        edges.put(e.hashCode(), e);
        src.addNeighbor(e);
        dst.addNeighbor(e);
        return true;
    }

    /**
     * Returns whether or not the edge was in the graph
     * @param e is an edge in the graph
     * @return returns whether or not the edge was in the graph
     */
    public boolean containsEdge(Edge e){
        if(e.getSrc() == null || e.getDst() == null){
            return false;
        }
        return this.edges.containsKey(e.hashCode());
    }

    /**
     * Returns the edge that was removed
     * @param e is an edge in the graph
     * @return returns the edge that was removed
     */
    public Edge removeEdge(Edge e){
        e.getSrc().removeNeighbor(e);
        e.getDst().removeNeighbor(e);
        return this.edges.remove(e.hashCode());
    }

    /**
     * Returns whether or not the node is in the graph
     * @param node is a node in the graph
     * @return returns whether or not the node is in the graph
     */
    public boolean containsNode(Node node){
        return this.nodes.get(node.getLabel()) != null;
    }

    /**
     * Returns the node to the label
     * @param label is the key to a node
     * @return returns the node to the label
     */
    public Node getNode(String label){
        return nodes.get(label);
    }

    /**
     * Returns whether or not the node was added to the graph
     * @return returns whether or not the node was added to the graph
     */
    //adds a node to the graph, overwrites the node if it already exits and disconnects all its edges
    public boolean addNode(Node node, boolean overwriteExisting){
        Node current = this.nodes.get(node.getLabel());
        if(current != null){
            if(!overwriteExisting){
                return false;
            }

            while(current.getNeighborCount() > 0){
                this.removeEdge(current.getNeighbor(0));
            }
        }

        nodes.put(node.getLabel(), node);
        return true;
    }

    /**
     * Returnsthe node that was removed
     * @param label is a key to a node
     * @return returns the node that was removed
     */
    public Node removeNode(String label){
        Node n = nodes.remove(label);

        while(n.getNeighborCount() > 0){
            this.removeEdge(n.getNeighbor((0)));
        }

        return n;
    }

    /**
     * Returns a set of all the keys to the nodes in the graph
     * @return returns a set of all the keys to the nodes in the graph
     */
    public Set<String> nodeKeys(){
        return this.nodes.keySet();
    }

    /**
     * Returns a set of all the edges
     * @return returns a set of all the edges
     */
    public Set<Edge> getEdges(){
        return new HashSet<Edge>(this.edges.values());
    }
}