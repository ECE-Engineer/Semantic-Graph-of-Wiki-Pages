import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Kyle Zeller
 *
 */
public class Node implements Serializable {
    private ArrayList<Edge> neighborhood;
    private String label;

    public Node(String label){
        this.label = label;
        this.neighborhood = new ArrayList<Edge>();
    }

    public void addNeighbor(Edge edge){
        if(this.neighborhood.contains(edge)){
            return;
        }

        this.neighborhood.add(edge);
    }

    public boolean containsNeighbor(Edge other){
        return this.neighborhood.contains(other);
    }

    public Edge getNeighbor(int index){
        return this.neighborhood.get(index);
    }

    Edge removeNeighbor(int index){
        return this.neighborhood.remove(index);
    }

    public void removeNeighbor(Edge e){
        this.neighborhood.remove(e);
    }

    public int getNeighborCount(){
        return this.neighborhood.size();
    }

    public String getLabel(){
        return this.label;
    }

    public String toString(){
        return "Node " + label;
    }

    public int hashCode(){
        return this.label.hashCode();
    }

    public boolean equals(Object other){
        if(!(other instanceof Node)){
            return false;
        }

        Node n = (Node)other;
        return this.label.equals(n.label);
    }

    public ArrayList<Edge> getNeighbors(){
        return new ArrayList<Edge>(this.neighborhood);
    }
}