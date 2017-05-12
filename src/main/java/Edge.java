/**
 * Created by Kyle on 5/10/2017.
 */
public class Edge {
    private Node src, dst;
    private double costWeight;

    public Edge(Node src, Node dst) {
        this(src, dst, 1);
    }

    public Edge(Node src, Node dst, double costWeight) {
        this.src = (src.getLabel().compareTo(dst.getLabel()) <= 0) ? src : dst;
        this.dst = (this.src == src) ? dst : src;
        this.costWeight = costWeight;
    }

    public Node getNeighbor(Node current){
        if(!(current.equals(src) || current.equals(dst))){
            return null;
        }
        return (current.equals(src)) ? dst : src;
    }

    public Node getSrc(){
        return this.src;
    }

    public Node getDst(){
        return this.dst;
    }

    public double getWeight(){
        return this.costWeight;
    }

    public void setWeight(int costWeight){
        this.costWeight = costWeight;
    }

    public double compareTo(Edge other){
        return this.costWeight - other.costWeight;
    }

    public String toString(){
        return "({" + src + ", " + dst + "}, " + costWeight + ")";
    }

    public int hashCode(){
        return (src.getLabel() + dst.getLabel()).hashCode();
    }

    public boolean equals(Object other){
        if(!(other instanceof Edge)){
            return false;
        }
        Edge e = (Edge)other;

        return e.src.equals(this.src) && e.dst.equals(this.dst);
    }
}