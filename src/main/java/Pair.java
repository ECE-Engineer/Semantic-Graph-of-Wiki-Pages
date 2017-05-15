/**
 * @author Kyle Zeller
 * This class provides a way to store two different objects together to be used.
 */
public class Pair implements Comparable<Pair>{
    private Node node;
    private Edge edge;

    /**
     * Creates the node / edge pair
     * @param node is a node from the constructed graph
     * @param edge is an edge from the constructed graph
     */
    Pair (Node node, Edge edge) {
        this.node = node;
        this.edge = edge;
    }

    /**
     * Returns the node from the constructed graph
     * @return returns the node from the constructed graph
     */
    public Node getNode() {
        return node;
    }

    /**
     * Returns the edge from the constructed graph
     * @return returns the edge from the constructed graph
     */
    public Edge getEdge() {
        return edge;
    }

    @Override
    public int compareTo(Pair o) {
        return Double.compare(this.getEdge().getWeight(), o.getEdge().getWeight());
    }
}
