
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.junit.Test;

public class TestDijkstraAlgorithm {

    private Map<String, Vertex>  nodes;
    private List<Edge> edges;
    private String[] bufferArray;

    @Test
    public void testExcute() {
        nodes = new HashMap<String, Vertex>();
        edges = new ArrayList<Edge>();

        load("data/gg_1_edges.txt","data/gg_1_points.txt");
        Graph graph = new Graph(nodes, edges);

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get("102"));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get("k3"));

        for (Vertex vertex : path) {
            System.out.println(vertex);
        }
    }

    private void addVertex(String name, double x, double y, int floor){
        Vertex location = new Vertex( name,x,y,floor);
        nodes.put(name,location);
    }

    private void addLane( String sourceLocNo, String destLocNo,
                          double duration, int floor) {
        Edge lane = new Edge(nodes.get(sourceLocNo), nodes.get(destLocNo), duration, floor );
        edges.add(lane);
    }

    public void load(String fileName_egdes, String fileName_points) {

        nodes = new HashMap<String, Vertex>();
        edges = new ArrayList<Edge>();
        String[] bufferArray = new String[0];

        String buffer;
        int lineNumber = 0;
        int id = 0;

        try {
            File file = new File(fileName_points);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            while ((buffer = reader.readLine()) != null) {
                lineNumber++;

                bufferArray = buffer.split(",", 5);
                addVertex( bufferArray[0],Double.parseDouble(bufferArray[1]),Double.parseDouble(bufferArray[2]),Integer.parseInt(bufferArray[3]));
            }

            reader.close();
            fileReader.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("File has to be accessible!");
        }

        String buffer2;
        lineNumber = 0;

        try {
            File file = new File(fileName_egdes);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            while ((buffer2 = reader.readLine()) != null) {
                lineNumber++;

                bufferArray = buffer2.split(",", 6);

                addLane( bufferArray[0],bufferArray[1],  Double.parseDouble(bufferArray[2]), Integer.parseInt(bufferArray[3]));
                addLane( bufferArray[1],bufferArray[0],  Double.parseDouble(bufferArray[2]), Integer.parseInt(bufferArray[3]));
            }

            reader.close();
            fileReader.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("File has to be accessible!");
        }


    }
}