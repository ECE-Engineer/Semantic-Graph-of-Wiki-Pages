import java.util.*;

/**
 * Created by Kyle on 5/11/2017.
 */
public class Graph {
    private HashMap<String, Node> nodes;
    private HashMap<Integer, Edge> edges;

    public Graph() {
        this.nodes = new HashMap<String, Node>();
        this.edges = new HashMap<Integer, Edge>();
    }

    //takes the initial nodes to populate the graph with
    public Graph(ArrayList<Node> nodes) {
        this.nodes = new HashMap<String, Node>();
        this.edges = new HashMap<Integer, Edge>();

        for (Node n : nodes) {
            this.nodes.put(n.getLabel(), n);
        }
    }

    public boolean addEdge(Node src, Node dst) {
        return addEdge(src, dst, 1);
    }

    public boolean addEdge(Node src, Node dst, int weight) {
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

    public boolean containsEdge(Edge e){
        if(e.getSrc() == null || e.getDst() == null){
            return false;
        }
        return this.edges.containsKey(e.hashCode());
    }

    public Edge removeEdge(Edge e){
        e.getSrc().removeNeighbor(e);
        e.getDst().removeNeighbor(e);
        return this.edges.remove(e.hashCode());
    }

    public boolean containsNode(Node node){
        return this.nodes.get(node.getLabel()) != null;
    }

    public Node getNode(String label){
        return nodes.get(label);
    }

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

    public Node removeNode(String label){
        Node n = nodes.remove(label);

        while(n.getNeighborCount() > 0){
            this.removeEdge(n.getNeighbor((0)));
        }

        return n;
    }

    public Set<String> nodeKeys(){
        return this.nodes.keySet();
    }

    public Set<Edge> getEdges(){
        return new HashSet<Edge>(this.edges.values());
    }
}