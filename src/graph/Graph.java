package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

//todo: for now lets assume we know the vertex count, but lets make another version where we don't know it
// in undirected graphs, being acyclic is being a single node :)
public class Graph {

    // adjacency list representation of a graph
    private Vector<Vector> graph;
    private int vertexCount = 0;
    private boolean directed = false;
    private boolean[] available_vertices = null;

    public Graph(Vector<Vector> graph, int vertexCount, boolean directed, boolean[] available_vertices){
        this.graph = graph;
        this.vertexCount = vertexCount;
        this.directed = directed;
        this.available_vertices = available_vertices;
    }

    public Graph(String fileAddress, boolean directed){
        this.graph = new Vector<>();
        this.directed = directed;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileAddress));
            String line = br.readLine();
            if(line != null) // the vertex count of the graph would be the entry form the file
                this.vertexCount = Integer.parseInt(line);
            available_vertices = new boolean[vertexCount + 1];
            for(int i = 0; i <= vertexCount; i++){
                // initialise the graph and make all vertices
                graph.add(new Vector<Integer>());
                available_vertices[i] = true;
            }
            line = br.readLine();
            while (line != null){
                String[] tokens = line.split(",");
                int source = Integer.parseInt(tokens[0].trim());
                int destination = Integer.parseInt(tokens[1].trim());
                // System.out.println(source + ", " + destination); // just for debugging and checking
                graph.get(source).add(destination); // todo: don't know how should i fix this :D ... but lets leave it for later
                if(!directed)
                    graph.get(destination).add(source);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disable_a_vertex(int vertex){
        this.available_vertices[vertex] = false;
    }

    private Graph createSubGraph(int vertex){
        boolean[] new_stuff = this.available_vertices.clone();
        new_stuff[vertex] = false;
        return new Graph(this.graph, this.vertexCount, this.directed, new_stuff); // maybe we have to send vertex count - 1 but i guess not ?
    }

    public void printGrapth(){
        System.out.println(" printing the graph \n No special order or traversal :)");
        for(int i = 0; i <= this.vertexCount; i++){
            if(!available_vertices[i]) // if this vertex didn't exist skip it
                continue;
            Vector source_adjacent = graph.get(i);
            int adj_count = source_adjacent.size();
            for(int j = 0; j < adj_count; j++){
                if(!available_vertices[(int) source_adjacent.get(j)])
                    continue;
                System.out.println(i + ", " + source_adjacent.elementAt(j));
            }
        }
    }

    private boolean[] DFS_Caller(int vertex){
        boolean[] visitedList = new boolean[vertexCount + 1];
        for(int i = 0; i <= vertexCount; i++)
            visitedList[i] = false;
        DFS(vertex, visitedList);
        return visitedList;
    }

    private void DFS (int vertex, boolean[] visitedList){ // should be called with a full false visited list and the first vertex
        visitedList[vertex] = true;
        Vector adjacent = graph.get(vertex);
        for(int i = 0; i < adjacent.size(); i++){
            int adjacent_vertex = (int) adjacent.get(i);
            if(!visitedList[adjacent_vertex] && available_vertices[adjacent_vertex]) // if it is not visited and is available
                DFS(adjacent_vertex, visitedList);
        }
    }

    private boolean visited_all(boolean[] check_me){
        for(int i = 1; i < vertexCount + 1; i ++){
            if(!available_vertices[i])
                continue;
            if(!check_me[i]) // if there was a false entry, return fase
                return false; // return b
        }
        return true;
    }

    private boolean isStronglyConnected(){
        // we have to check if we can reach every other vertex from each vertex
        // to this end we will check if the dfs traversal from each vertex visits every other vertex as well
        for(int i = 1; i <= vertexCount; i++){
            if(!available_vertices[i])
                continue;
            if(!visited_all(DFS_Caller(i)))
                return false;
        }
        return true;
    }

    private int get_min(Vector v){
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < v.size(); i++){
            int currnet_element = (int) v.elementAt(i);
            if(currnet_element < min)
                min = currnet_element;
        }
        return min;
    }

    private boolean singleNode(){
        boolean found_one = false;
        for(int i = 1; i <= vertexCount; i++){
            if(this.available_vertices[i] && found_one)
                return false;
            if(this.available_vertices[i]){
                found_one = true;
                continue;
            }
        }
        return true;
    }

    public int cycle_rank(){
        int rank = 0;
        if(!this.singleNode()){
            // now we need to calculate minimum rank of the sub graphs
            Vector subGraph_ranks = new Vector();
            for(int i = 0; i <= this.vertexCount; i++){
                if(!available_vertices[i])
                    continue;
                int current_rank = this.createSubGraph(i).cycle_rank();
                subGraph_ranks.add(current_rank);
            }
            rank = 1 + get_min(subGraph_ranks);
        }
        return rank;
    }

    public static void main(String[] args) {
       Graph g = new Graph("personal_test.txt", false);
//       g.printGrapth();
//       System.out.println(Arrays.toString(g.DFS_Caller(2)));
//       System.out.println(g.isStronglyConnected());
//        Vector v = new Vector();
//        v.add("hello"); v.add("!&$#&*&&"); v.add(100);
//        System.out.println(Arrays.toString(v.toArray()));
//        Vector vv = new Vector();
//        vv = (Vector) v.clone();
//        vv.add("this is adfslkdfjlsdj");
//        System.out.println(Arrays.toString(vv.toArray()));
//        Graph gg = null;
//        vv = (Vector) g.getGraph().clone();
//        gg = new Graph(vv, g.getVertexCount(), g.isDirected());
//        gg.printGrapth();

        // now to test the sub graphs

//        Graph g1, g2, g3 = null;
//        g1 = g.createSubGraph(1);
//        g1.printGrapth();
//        System.out.println(g1.isStronglyConnected());
//        Graph g12 = g1.createSubGraph(2);
//        g12.printGrapth();
        g.printGrapth();
        System.out.println(g.cycle_rank());


    }

    public Vector<Vector> getGraph() {
        return graph;
    }

    public void setGraph(Vector<Vector> graph) {
        this.graph = graph;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }
}
