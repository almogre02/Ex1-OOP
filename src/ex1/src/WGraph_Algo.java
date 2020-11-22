package ex1.src;
import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms, Serializable {
    private weighted_graph gr;

    public WGraph_Algo() {
    }

    @Override
    public void init(weighted_graph g) {
        this.gr = g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.gr;
    }

    /**
     * This function creates a new graph. It adds the nodes from the original graph to the new graph
     * and also connects between nodes in the new graph as same as the connections and the weight
     * of the edges in the original graph.
     * @return the copied graph
     */
    @Override
    public weighted_graph copy() {
        if (this.gr != null)
        {
            weighted_graph newGraph = new WGraph_DS();
            Iterator<node_info> it = this.gr.getV().iterator();
            while (it.hasNext())
            {
                node_info nodes=it.next();
                newGraph.addNode(nodes.getKey());
            }
            Iterator<node_info> it2 = this.gr.getV().iterator();
            while (it2.hasNext())
            {
                node_info temp = it2.next();
                Iterator<node_info> it3 = this.gr.getV(temp.getKey()).iterator();
                while (it3.hasNext())
                {
                    node_info nighbors = it3.next();
                    newGraph.connect(temp.getKey(), nighbors.getKey(), this.gr.getEdge(temp.getKey(), nighbors.getKey()));
                }
            }
            return newGraph;
        }
        return null;
    }

    @Override
    public boolean isConnected()
    {
        if (this.gr.nodeSize()<=1)
            return true;
        Queue<node_info> q = new LinkedList<node_info>();
        Iterator<node_info> it = this.gr.getV().iterator();
        node_info temp = it.next();
        q.add(temp);
        temp.setTag(0);
        while (q.isEmpty()==false)
        {
            node_info peek=q.peek();
            Iterator<node_info> neighbors = this.gr.getV(peek.getKey()).iterator();
            while (neighbors.hasNext())
            {
                node_info neigh = neighbors.next();
                if (neigh.getTag() == Double.MAX_VALUE)
                {
                    q.add(neigh);
                    neigh.setTag(0);
                }
            }
            q.poll();
        }
        Iterator<node_info> newIt = this.gr.getV().iterator();
        while (newIt.hasNext()) {
            temp = newIt.next();
            if (temp.getTag() == Double.MAX_VALUE)
            {
                Iterator<node_info> reset = this.gr.getV().iterator();
                while(reset.hasNext())
                {
                    node_info n=reset.next();
                    n.setTag(Double.MAX_VALUE);
                }
                return false;
            }
        }

        Iterator<node_info> reset = this.gr.getV().iterator();
        while(reset.hasNext())
        {
            node_info n=reset.next();
            n.setTag(Double.MAX_VALUE);
        }
        return true;
    }

    /**
     *This function calculates the shortest path distance between the 2 given nodes.
     * If the function finds a shortest path to a a node, she checks and update the tag
     * of the nodes (if its necessary) by recursive method.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        Queue<node_info> q = new LinkedList<node_info>();
        if (src == dest)
            return 0;
        gr.getNode(src).setTag(0);
        q.add(gr.getNode(src));
        gr.getNode(src).setInfo("visited");
        while (q.isEmpty()==false) {
            node_info temp_src = gr.getNode(q.peek().getKey());
            Iterator<node_info> it = this.gr.getV(q.peek().getKey()).iterator();
            while (it.hasNext()) {
                node_info nighbors = it.next();
                double temp_weight = temp_src.getTag() + gr.getEdge(q.peek().getKey(), nighbors.getKey());
                if (nighbors.getInfo() != "visited") {
                    q.add(nighbors);
                    nighbors.setInfo("visited");
                }
                if (temp_weight < nighbors.getTag()) {
                    nighbors.setTag(temp_weight);
                    nighbors.setInfo("visited");
                    update(nighbors);
                }
            }
            q.remove();
        }
        if (gr.getNode(dest).getTag() == Double.MAX_VALUE)
            return -1;
        double shortestDist = gr.getNode(dest).getTag();
        Iterator<node_info> it = gr.getV().iterator();
        while (it.hasNext()) {
            node_info temp = it.next();
            temp.setTag(Double.MAX_VALUE);
            temp.setInfo("");
        }
        return shortestDist;
    }

    private void update(node_info temp) {
        Iterator<node_info> neighbors = this.gr.getV(temp.getKey()).iterator();
        while (neighbors.hasNext()) {
            node_info neigh = neighbors.next();
            double edge=gr.getEdge(temp.getKey(), neigh.getKey());
            if (neigh.getTag() > temp.getTag() + edge) {
                neigh.setTag(temp.getTag() + edge);
                update(neigh);
            }
        }
    }

    /**
     * The function using the same algorithm from the previous method.
     * Also using recursive function that checks and adds the currect node for the shortest path by using the
     * tag of the node and the wight between them and making a list by starting from the dest to the src.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        List<node_info> list = new ArrayList<node_info>();
        Queue<node_info> q = new LinkedList<node_info>();
        Queue<node_info> newQ = new LinkedList<node_info>();
        if (src == dest) {
            list.add(this.gr.getNode(src));
            return list;
        }
        gr.getNode(src).setTag(0);
        q.add(gr.getNode(src));
        gr.getNode(src).setInfo("visited");
        while (q.isEmpty()==false) {
            node_info temp_src = gr.getNode(q.peek().getKey());
            Iterator<node_info> it = this.gr.getV(q.peek().getKey()).iterator();
            while (it.hasNext()) {
                node_info temp = it.next();
                double temp_weight = temp_src.getTag() + gr.getEdge(q.peek().getKey(), temp.getKey());
                if (temp.getInfo() != "visited") {
                    q.add(temp);
                    temp.setInfo("visited");
                }
                if (temp_weight < temp.getTag()) {
                    temp.setTag(temp_weight);
                    temp.setInfo("visited");
                    update(temp);
                }
            }
            q.remove();
        }
        if (gr.getNode(dest).getTag() == Double.MAX_VALUE)
            return null;
        list.add(gr.getNode(dest));
        updateList(gr.getNode(dest), list);

        int i=list.size()-1;
        while(!list.isEmpty())
        {
            newQ.add(list.remove(i));
            i--;
        }
        while (!newQ.isEmpty()) {
            list.add(newQ.peek());
            newQ.poll();
        }
        return list;
    }

    private void updateList(node_info dest, List<node_info> list) {
        Iterator<node_info> ni = gr.getV(dest.getKey()).iterator();
        while (ni.hasNext()) {
            node_info temp = ni.next();
            double edge=gr.getEdge(dest.getKey(), temp.getKey());
            if (dest.getTag() == temp.getTag() + edge) {
                list.add(temp);
                updateList(temp, list);
            }
        }
    }

    /**
     * This function uses Serializable to save the object to the given file.
     * The structure of the code has taken from https://www.geeksforgeeks.org/serialization-in-java/
     * @param file - the file name (may include a relative path).
     * @return true is the file was successfully saved, otherwise return false
     */
    @Override
    public boolean save(String file) {

        try
        {
            FileOutputStream fileName = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileName);

            out.writeObject(this.gr);
            out.close();
            fileName.close();

        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
            return false;
        }
        return true;
    }

    /**
     * This function uses Serializable by loading the saved file into the graph.
     * The structure of the code has taken from https://www.geeksforgeeks.org/serialization-in-java/
     * @param file - file name
     * @return
     */
    @Override
    public boolean load(String file) {
        try
        {
            FileInputStream fileName = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileName);

            this.gr = (WGraph_DS)in.readObject();
            in.close();
            fileName.close();
        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
            return false;
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
            return false;
        }
        return true;
    }

}
