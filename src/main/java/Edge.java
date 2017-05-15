import java.io.Serializable;

/**
 * @author Kyle Zeller
 * This class is meant to keep track of a start node, an end node, and the edge weight between them.
 */
public class Edge  implements Serializable {
    private Node src, dst;
    private double costWeight;

    /**
     * Creates an edge between the two nodes
     * @param src is the start node
     * @param dst is the end node
     */
    public Edge(Node src, Node dst) {
        this(src, dst, 1);
    }

    /**
     * Creates an edge between the two nodes
     * @param src is the start node
     * @param dst is the end node
     * @param costWeight is the connection weight
     */
    public Edge(Node src, Node dst, double costWeight) {
        this.src = (src.getLabel().compareTo(dst.getLabel()) <= 0) ? src : dst;
        this.dst = (this.src == src) ? dst : src;
        this.costWeight = costWeight;
    }

    /**
     * Returns the opposite node
     * @param current is the node is the start or end node
     * @return returns the opposite node
     */
    public Node getNeighbor(Node current){
        if(!(current.equals(src) || current.equals(dst))){
            return null;
        }
        return (current.equals(src)) ? dst : src;
    }

    /**
     * Returns the start node
     * @return returns the start node
     */
    public Node getSrc(){
        return this.src;
    }

    /**
     * Returns the end node
     * @return returns the end node
     */
    public Node getDst(){
        return this.dst;
    }

    /**
     * Returns the weight of this edge
     * @return returns the weight of this edge
     */
    public double getWeight(){
        return this.costWeight;
    }

    /**
     * Sets the new connection weight
     * @param costWeight is the new weight for the edge
     */
    public void setWeight(double costWeight){
        this.costWeight = costWeight;
    }

    /**
     * Returns the difference between the edge weights
     * @param other is some other edge
     * @return returns the difference between the edge weights
     */
    public double compareTo(Edge other){
        return this.costWeight - other.costWeight;
    }

    /**
     * Returns the start & end nodes, and edge weight
     * @return returns the start & end nodes, and edge weight
     */
    public String toString(){
        return "({" + src + ", " + dst + "}, " + costWeight + ")";
    }

    /**
     * Returns the sum of the hashcodes
     * @return returns the sum of the hashcodes
     */
    public int hashCode(){
        return (src.getLabel() + dst.getLabel()).hashCode();
    }

    /**
     * Returns whether or not the two edges are equivalent
     * @param other is some other edge
     * @return returns whether or not the two edges are equivalent
     */
    public boolean equals(Object other){
        if(!(other instanceof Edge)){
            return false;
        }
        Edge e = (Edge)other;

        return e.src.equals(this.src) && e.dst.equals(this.dst);
    }
}