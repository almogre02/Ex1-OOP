package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.*;

class WGraph_AlgoTest2 {

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
    void init() {
        weighted_graph gr=graph_creator();
    }

    @Test
    void getGraph() {
        weighted_graph gr=graph_creator();
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(gr);
        assertEquals(gr,wga.getGraph());

    }

    private void assertEquals(weighted_graph gr, weighted_graph graph) {
    }

    @Test
    void copy() {
        weighted_graph gr=graph_creator();
        weighted_graph_algorithms wga = new WGraph_Algo();
        weighted_graph newGr = new WGraph_DS();
        wga.init(gr);
        weighted_graph_algorithms copied = new WGraph_Algo();
        copied.init(newGr);
        Assertions.assertNotEquals(copied.getGraph(),wga.getGraph());
        Assertions.assertFalse(copied.equals(wga));
        newGr=wga.copy();
        copied.init(newGr);
        Assertions.assertTrue(copied.getGraph().equals(wga.getGraph()));
    }

    @Test
    void isConnected() {
        weighted_graph gr=graph_creator();
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(gr);
        Assertions.assertTrue(wga.isConnected());
        gr.removeNode(2);
        wga.init(gr);
        Assertions.assertFalse(wga.isConnected());
    }

    @Test
    void shortestPathDist() {
        weighted_graph gr=graph_creator();
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(gr);
        double dist= wga.shortestPathDist(0,2);
        Assertions.assertEquals(11,dist);
        gr.connect(0,1,1);
        double newDist= wga.shortestPathDist(0,2);
        Assertions.assertEquals(6,newDist);
    }

    @Test
    void shortestPath() {
        weighted_graph gr=graph_creator();
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(gr);
        List<node_info> shortest=wga.shortestPath(0,2);
        int[] checkList = {0, 5, 3, 1, 2};
        int i=0;
        for (node_info temp:shortest)
        {
            Assertions.assertEquals(temp.getKey(),checkList[i]);
            i++;
        }
    }

    @Test
    void save() {

    }

    @Test
    void load() {
        weighted_graph gr=graph_creator();
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(gr);
        String str="wga";
        wga.save(str);
        weighted_graph gr1=new WGraph_DS();
        weighted_graph_algorithms wga1 = new WGraph_Algo();
        wga1.init(gr1);
        Assertions.assertFalse(wga.equals(wga1));
        Assertions.assertNotEquals(wga.getGraph(),wga1.getGraph());
        wga1.load(str);
        Assertions.assertEquals(wga.getGraph(),wga1.getGraph());
    }

}