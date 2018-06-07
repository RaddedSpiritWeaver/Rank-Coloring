package graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

//todo: for now lets assume we know the vertex count, but lets make another version where we don't know it

public class Graph {

    // adjacency list representation of a graph
    private Vector<Vector> graph;
    private int vertexCount = 0;
    private boolean directed = false;

    public Graph(String fileAddress, boolean directed){
        this.graph = new Vector<>();
        this.directed = directed;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileAddress));
            String line = br.readLine();
            if(line != null) // the vertex count of the graph would be the entry form the file
                this.vertexCount = Integer.parseInt(line);
            for(int i = 0; i <= vertexCount; i++) // initialise the graph
                graph.add(new Vector<Integer>());
            line = br.readLine();
            while (line != null){
                String[] tokens = line.split(",");
                int source = Integer.parseInt(tokens[0].trim());
                int destination = Integer.parseInt(tokens[1].trim());
                System.out.println(source + ", " + destination); // just for debugging and checking
                graph.get(source).add(destination); // todo: don't know how should i fix this :D ... but lets leave it for later
                if(!directed)
                    graph.get(destination).add(source);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printGrapth(){
        System.out.println(" printing the graph \n No special order or traversal :)");
        for(int i = 0; i <= this.vertexCount; i++){
            Vector source_adjacent = graph.get(i);
            int adj_count = source_adjacent.size();
            for(int j = 0; j < adj_count; j++)
                System.out.println(i + ", " + source_adjacent.elementAt(j));
        }
    }

    public static void main(String[] args) {
        new Graph("personal_test.txt", false).printGrapth();
    }
}
