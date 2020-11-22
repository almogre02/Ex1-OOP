package ex1.src;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

/**
 *This class represents undirectional weighted graph.
 */
public class WGraph_DS implements weighted_graph, Serializable {
    private HashMap<Integer, HashMap<Integer,Edge>> map;
    private HashMap<Integer, node_info> numOfNodes;
    private int numOfEdges;
    private int numOfChanges;

    public WGraph_DS(){
        this.map=new HashMap<Integer, HashMap<Integer,Edge>>() ;
        this.numOfNodes =new HashMap<Integer, node_info>();
        this.numOfEdges =0;
        this.numOfChanges =0;
    }
    /**
     * This function returns the node_info by using the method get.
     * @param key - the node_id
     * @return the node_info by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {
        if(this.map.containsKey(key))
            return this.numOfNodes.get(key);
        else
            return null;
    }
    /**
     * This function checks if there is an edge between 2 nodes by using the method containsKes in the hashMap .
     * @param node1
     * @param node2
     * @return true iff there is edge between node1 and node 2, else false
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if(node1==node2)
            return false;
        if(map.get(node1).containsKey(node2))
            return true;
        return false;
    }
    /**
     * This function returns the weight of the edge between 2 nodes
     * @param node1
     * @param node2
     * @return the edge between node1 and node2, returns -1 if there isn't an edge.
     */
    @Override
    public double getEdge(int node1, int node2) {
        if(hasEdge(node1, node2))
            return this.map.get(node1).get(node2).weight;
        else
            return -1;
    }
    /**
     *This function adds a new node to the graph (if this node doesn't exist)
     * by creating a new node and new hashMap for this node.
     * Finally it updates and adds the node to the hashMap (that represents the graph);
     * @param key
     */
    @Override
    public void addNode(int key) {
        if(map.containsKey(key)==false) {
            numOfChanges++;
            node_info temp = new NodeInfo(key);
            HashMap<Integer, Edge> hash = new HashMap<Integer, Edge>();
            this.map.put(key, hash);
            this.numOfNodes.put(key,temp);
        }
    }
    /**
     * This function creates an edge between node1 and node2 with given wight>=0
     * By creating 2 edges and put the same weight to each hashMap of the nodes.
     * @param node1
     * @param node2
     * @param w
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if(hasEdge(node1,node2)==true){
            map.get(node1).get(node2).setWeight(w);
            map.get(node2).get(node1).setWeight(w);
        }
        else if(node1!=node2) {
            numOfEdges++;
            numOfChanges++;
            Edge first = new Edge(getNode(node1), getNode(node2), w);
            Edge second = new Edge(getNode(node2), getNode(node1), w);
            map.get(node1).put(node2, first);
            map.get(node2).put(node1, second);
        }
    }
    /**
     * This function returns a collection of all the nodes in the graph
     * @return Collection<node_info>
     */
    @Override
    public Collection<node_info> getV() {
        return this.numOfNodes.values();
    }

    /**
     * This function returns the collection of neighbors that belongs to the given node.
     * The collection of the neighbors represents by hashMap.
     * @param node_id
     * @return
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        int index=0;
        HashMap<Integer, node_info> temp = new HashMap<Integer, node_info>();
        Iterator<Integer> it = map.get(node_id).keySet().iterator();
        while(it.hasNext()) {
            temp.put(index, getNode(it.next()));
            index=index+1;
        }
        return temp.values();
    }
    /**
     *First,this function removes the neighbors of the given node and the edges between them,
     * after that is removes the given node from the graph.
     * @param key
     * @return The data of the removed node, return null if the node doesn't exist.
     */
    @Override
    public node_info removeNode(int key) {

        if (this.map.containsKey(key)==false)
            return null;
        node_info temp =getNode(key);
        Iterator<node_info> it=getV(key).iterator();
        while (it.hasNext())
        {
            node_info temp1=it.next();
            map.get(temp1.getKey()).remove(key);
            numOfEdges--;
            numOfChanges++;
        }
        this.map.remove(key);
        this.numOfNodes.remove(key);
        return temp;
    }

    /**
     *This function removes the edge between the 2 given nodes by remove the edge on each hashMap.
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if(hasEdge(node1,node2)==true) {
            this.map.get(node1).remove(node2);
            this.map.get(node2).remove(node1);
            numOfEdges--;
            numOfChanges++;
        }
    }

    @Override
    public int nodeSize() {
        return numOfNodes.size();
    }

    @Override
    public int edgeSize() {
        return numOfEdges;
    }

    @Override
    public int getMC() {
        return numOfChanges;
    }

    /**
     *This function checks if 2 weighted graphs are equal, the only difference between them is the place in memory.
     * By using iterators, this function compare between each node to his twin in the second graph.
     * @param o
     * @return true if the graphs are equal, otherwise returns false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WGraph_DS)) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;

        if (numOfEdges != wGraph_ds.numOfEdges || nodeSize()!= wGraph_ds.nodeSize())
            return false;

        Iterator<node_info> it=wGraph_ds.getV().iterator();
        Iterator<node_info> my=this.numOfNodes.values().iterator();
        while (it.hasNext() && my.hasNext())
        {
            node_info first= my.next();
            node_info second= it.next();
            if (first.getTag()!=second.getTag() || first.getKey()!=second.getKey() || !first.getInfo().equals(second.getInfo()))
                return false;
            else
            {
                Iterator<node_info> it1=wGraph_ds.getV(second.getKey()).iterator();
                Iterator<node_info> my1=this.getV(first.getKey()).iterator();
                while (it1.hasNext() && my1.hasNext())
                {
                    node_info firstNigh= my1.next();
                    node_info secondNigh= it1.next();
                    if (first.getTag()!=second.getTag() || first.getKey()!=second.getKey() || !first.getInfo().equals(second.getInfo()))
                        return false;
                    if (getEdge(first.getKey(), firstNigh.getKey())!=wGraph_ds.getEdge(second.getKey(), secondNigh.getKey()))
                        return false;
                }
            }
        }
        return true;
    }
    @Override
    public int hashCode() {
        return Objects.hash(map, numOfNodes, numOfEdges);
    }

    private class NodeInfo implements node_info,Serializable {
        private int key;
        private String info;
        private double tag;
        private HashMap<Integer, Edge> edges;

        public NodeInfo(int key){
            this.key=key;
            this.info="";
            this.tag=Double.MAX_VALUE;
        }

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info=s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NodeInfo)) return false;
            NodeInfo nodeInfo = (NodeInfo) o;
            return key == nodeInfo.key &&
                    Double.compare(nodeInfo.tag, tag) == 0 &&
                    Objects.equals(info, nodeInfo.info) &&
                    Objects.equals(edges, nodeInfo.edges);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, info, tag, edges);
        }
    }

    /**
     * This class represents the distance between 2 nodes.
     * The distance is the weight of the edge between the 2 nodes.
     */
    private class Edge implements Serializable{
        private node_info src;
        private node_info dest;
        private double weight;

        public Edge(node_info src, node_info dest, double weight){
            this.src=src;
            this.dest=dest;
            this.weight=weight;
        }
        public node_info getSrc(node_info src)
        {
            return this.src;
        }
        public double getWeight(node_info dest)
        {
            return this.weight;
        }
        public void setWeight(double weight)
        {
            this.weight=weight;
        }
        public node_info getDest()
        {
            return this.dest;
        }
        public void setDest(node_info dest)
        {
            this.dest=dest;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Edge)) return false;
            Edge edge = (Edge) o;
            return Double.compare(edge.weight, weight) == 0 &&
                    Objects.equals(src, edge.src) &&
                    Objects.equals(dest, edge.dest);
        }

        @Override
        public int hashCode() {
            return Objects.hash(src, dest, weight);
        }
    }
}
