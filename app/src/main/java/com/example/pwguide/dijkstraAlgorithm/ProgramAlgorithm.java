package com.example.pwguide.dijkstraAlgorithm;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProgramAlgorithm  extends AppCompatActivity {

    private Map<String, Vertex> nodes;
    private List<Edge> edges;
    private String[] bufferArray;

    public LinkedList<Vertex> programExcute( String classNumber,InputStream input ) throws IOException {
        nodes = new HashMap<String, Vertex>();
        edges = new ArrayList<Edge>();

        load(input);
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        // narazie przypiany początkowy, pozniej bedzie to wejscie (lub podana sala)
        dijkstra.execute(nodes.get("w1"));

//        StringBuilder sb = new StringBuilder();
//        for (Vertex vertex : path) {
//            sb.append(vertex.getName()).append(" ");
//            System.out.println(vertex);
//        }
        return dijkstra.getPath(nodes.get(classNumber));
    }

    private void addVertex(String name, double x, double y, int floor){
        Vertex location = new Vertex( name,x,y,floor);
        nodes.put(name,location);
    }

    private void addLane( String sourceLocNo, String destLocNo,
                          double duration) {
        Edge lane = new Edge(nodes.get(sourceLocNo), nodes.get(destLocNo), duration );
        edges.add(lane);
    }

    public void load( InputStream input ) throws IOException {

        nodes = new HashMap<String, Vertex>();
        edges = new ArrayList<Edge>();
        String[] bufferArray = new String[0];
        String buffer;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            while ((buffer = reader.readLine()) != null) {

                if (buffer.contains("#")) {
                    break;
                }
                bufferArray = buffer.split(",", 5);
                addVertex(bufferArray[0], Double.parseDouble(bufferArray[1]), Double.parseDouble(bufferArray[2]), Integer.parseInt(bufferArray[3]));
            }

            while ((buffer = reader.readLine()) != null) {
                bufferArray = buffer.split(",", 6);
                addLane(bufferArray[0], bufferArray[1], Double.parseDouble(bufferArray[2]));
                addLane(bufferArray[1], bufferArray[0], Double.parseDouble(bufferArray[2]));
            }
            reader.close();
            input.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("File has to be accessible!");
        }

    }

    }
