package ex1.tests;
import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class WGraph_DSTest2 {
    private static Random _rnd = null;

    public static weighted_graph graph_creator()
    {
        weighted_graph g = new WGraph_DS();
        for(int i=0;i<11;i++) {
            g.addNode(i);
        }
        g.connect(0,1,17);
        g.connect(0,5,3);
        g.connect(1,2,5);
        g.connect(1,3,2);
        g.connect(2,4,8);
        g.connect(3,5,1);
        g.connect(3,7,4);
        g.connect(9,0,3);
        g.connect(6,8,6);
        g.connect(3,10,14);
        g.connect(3,8,8);
        return g;
    }

    @Test
    void getNode() {
        weighted_graph gr=graph_creator();
        gr.getNode(0).setTag(8);
        gr.getNode(0).setInfo("check");
        Assert.assertEquals(0,gr.getNode(0).getKey());
        Assert.assertEquals(8,gr.getNode(0).getTag(),0.0000001);
        Assert.assertEquals(8,gr.getNode(0).getTag(),0.0000001);
        Assert.assertEquals("check",gr.getNode(0).getInfo());
        Assertions.assertNull(gr.getNode(17));
    }

    @Test
    void hasEdge() {
        weighted_graph gr=graph_creator();
        Assertions.assertEquals(17,gr.getEdge(0,1));
        Assert.assertEquals(-1,gr.getEdge(4,8),0.0000001);
    }

    @Test
    void getEdge() {
        weighted_graph gr=graph_creator();
    }

    @Test
    void addNode() {
        weighted_graph gr=graph_creator();
        gr.addNode(11);
        Assert.assertEquals(12,gr.nodeSize());
    }

    @Test
    void connect() {
        weighted_graph gr=graph_creator();
        gr.connect(9,7,25);
        Assert.assertEquals(25,gr.getEdge(7,9),0.00000001);
    }

    @Test
    void getV() {
        weighted_graph gr=graph_creator();
        Queue<node_info> q = new LinkedList<node_info>();
        Queue<node_info> q1 = new LinkedList<node_info>();
        for (int i=0;i<11;i++)
        {
            q.add(gr.getNode(i));
            q1.add(gr.getNode(i));
        }
        Iterator<node_info> it=gr.getV().iterator();
        while (it.hasNext())
        {
            node_info temp= it.next();
            Assertions.assertEquals(temp,q.poll());
        }
        gr.removeNode(0);
        Iterator<node_info> it1=gr.getV().iterator();
        while (it1.hasNext())
        {
            node_info temp= it1.next();
            Assertions.assertNotEquals(temp,q1.poll());
        }
    }

    @Test
    void testGetV() {
        weighted_graph gr=graph_creator();
        Iterator<node_info> it=gr.getV(gr.getNode(0).getKey()).iterator();
        Queue<node_info> q = new LinkedList<node_info>();
        for (node_info temp:gr.getV(gr.getNode(0).getKey()))
        {
            q.add(temp);
        }
        while (it.hasNext())
        {
            node_info temp= it.next();
            Assertions.assertEquals(temp,q.poll());
        }
    }

    @Test
    void removeNode() {
        weighted_graph gr=graph_creator();
        node_info temp=gr.getNode(0);
        node_info temp1=gr.removeNode(0);
        Assertions.assertEquals(temp,temp1);
        Assertions.assertNull(gr.getNode(0));
    }

    @Test
    void removeEdge() {
        weighted_graph gr=graph_creator();
        Assertions.assertEquals(5,gr.getEdge(1,2),0.0000001);
        gr.removeEdge(1,2);
        Assertions.assertNotEquals(5,gr.getEdge(1,2),0.0000001);
    }

    @Test
    void nodeSize() {
        weighted_graph gr=graph_creator();
        Assertions.assertEquals(11,gr.nodeSize());
        gr.removeNode(0);
        gr.removeNode(1);
        Assertions.assertEquals(9,gr.nodeSize());
    }

    @Test
    void edgeSize() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        Assertions.assertEquals(0,g.edgeSize());
        g.connect(0,1,5);
        g.connect(0,2,3);
        Assertions.assertEquals(2,g.edgeSize());
    }

    @Test
    void getMC() {
        weighted_graph g = new WGraph_DS();
        Assertions.assertEquals(0,g.getMC());
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        Assertions.assertEquals(3,g.getMC());
        g.connect(0,1,5);
        Assertions.assertNotEquals(3,g.getMC());
        g.removeNode(0);
        Assertions.assertEquals(5,g.getMC());
    }

    @Test
    void testEquals() {
        weighted_graph gr=graph_creator();
        weighted_graph g = new WGraph_DS();
        for(int i=0;i<11;i++) {
            g.addNode(i);
        }
        g.connect(0,1,17);
        g.connect(0,5,3);
        g.connect(1,2,5);
        g.connect(1,3,2);
        g.connect(2,4,8);
        g.connect(3,5,1);
        g.connect(3,7,4);
        g.connect(9,0,3);
        g.connect(6,8,6);
        g.connect(3,10,14);
        g.connect(3,8,8);
        Assertions.assertEquals(gr,g);
        g.removeNode(0);
        Assertions.assertNotEquals(gr,g);
    }

    public static weighted_graph graph_creator(int v_size, int e_size, int seed) {
        weighted_graph g = new WGraph_DS();
        _rnd = new Random(seed);
        for(int i=0;i<v_size;i++) {
            g.addNode(i);
        }

        int[] nodes = nodes(g);
        while(g.edgeSize() < e_size) {
            int a = nextRnd(0,v_size);
            int b = nextRnd(0,v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i,j, w);
        }
        return g;
    }
    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
     * @param g
     * @return
     */
    private static int[] nodes(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodes = new node_info[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }

        public static void main(String[] args){
            System.out.println("The running time for crating this graph is:");
            long startTime = System.currentTimeMillis();
            weighted_graph g = graph_creator(1000000,10000000,1);
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);

        }
}

