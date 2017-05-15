import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Kyle Zeller
 * This class is meant to keep track of the node's wescraper, label, and all the connected edges to a node.
 */
public class Node implements Serializable{
    private ArrayList<Edge> neighborhood;
    private String label;
    private WebScraper webScraper;

    /**
     * Creates the node of a label and webscraper
     * @param label is a string assigned to the node
     * @param webScraper is a WebScraper used to pull data from wiki pages
     */
    public Node(String label, WebScraper webScraper){
        this.label = label;
        this.webScraper = webScraper;
        this.neighborhood = new ArrayList<Edge>();
    }

    /**
     * Creates the node of a label
     * @param label is a string assigned to the node
     */
    public Node(String label){
        this.label = label;
        this.neighborhood = new ArrayList<Edge>();
    }

    /**
     * Adds an edge as a connection to the neighborhood of edges
     * @param edge is some Edge
     */
    public void addNeighbor(Edge edge){
        if(this.neighborhood.contains(edge)){
            return;
        }

        this.neighborhood.add(edge);
    }

    /**
     * Returns whether or not the node is connected to this edge
     * @param other some edge
     * @return returns whether or not the node is connected to this edge
     */
    public boolean containsNeighbor(Edge other){
        return this.neighborhood.contains(other);
    }

    /**
     * Returns the edge associated with the node and number
     * @param index is a number to find the edge
     * @return returns the edge associated with the node and number
     */
    public Edge getNeighbor(int index){
        return this.neighborhood.get(index);
    }

    /**
     * Removes some edge from the node
     * @param index is a number to find the edge to remove
     */
    Edge removeNeighbor(int index){
        return this.neighborhood.remove(index);
    }

    /**
     * Removes some edge from the node
     * @param e is the edge to remove
     */
    public void removeNeighbor(Edge e){
        this.neighborhood.remove(e);
    }

    /**
     * Returns the number of edges the node connects to
     * @return returns the number of edges the node connects to
     */
    public int getNeighborCount(){
        return this.neighborhood.size();
    }

    /**
     * Returns the string label of the node
     * @return returns the string label of the node
     */
    public String getLabel(){
        return this.label;
    }

    /**
     * Returns return the node and label string
     * @return returns the node and label string
     */
    public String toString(){
        return "Node " + label;
    }

    /**
     * Returns the hashcode associated with the label
     * @return returns the hashcode associated with the label
     */
    public int hashCode(){
        return this.label.hashCode();
    }

    /**
     * Returns whether or not the nodes are equivalent
     * @param other is another arbitrary node
     * @return returns whether or not the nodes are equivalent
     */
    public boolean equals(Object other){
        if(!(other instanceof Node)){
            return false;
        }

        Node n = (Node)other;
        return this.label.equals(n.label);
    }

    /**
     * Returns an arrayList of all the edges from this node to some other node
     * @return returnsan arrayList of all the edges from this node to some other node
     */
    public ArrayList<Edge> getNeighbors(){
        return new ArrayList<Edge>(this.neighborhood);
    }

    /**
     * Returnsthe Webscraper the node has
     * @return returns the Webscraper the node has
     */
    public WebScraper getWebScraper() {
        return webScraper;
    }
}