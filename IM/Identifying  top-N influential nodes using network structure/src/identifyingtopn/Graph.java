package identifyingtopn;

import static identifyingtopn.IdentifyingTopN.ANSI_CYAN;
import static identifyingtopn.IdentifyingTopN.ANSI_RESET;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Graph extends JFrame {

    boolean isDirected, isWeighted;

    Map<String, Node> nodeMap = new HashMap<>();
    List<Edge> edgeList = new ArrayList<>();
    float mk = 0, edgeAverage;
    float maxRLGI = -1;

    public void addNode(Node node) {
        if (this.nodeMap.get(node.identifier) == null) {
            nodeMap.putIfAbsent(node.identifier, node);
        }
    }

    public void addEdge(Edge edge) {
        if (edge.source != null && edge.destination != null) {
            this.edgeList.add(edge);
            if (isDirected == false) {
                this.nodeMap.get(edge.source.identifier).addNeighbor(edge.destination);
                this.nodeMap.get(edge.destination.identifier).addNeighbor(edge.source);
            } else {
                this.nodeMap.get(edge.source.identifier).addNeighbor(edge.destination);
            }
        }
    }

    public void deleteNode(String nodeIdentifier) {
        int i = 0;
        List<Edge> edges = new ArrayList<>();
        while (i < this.edgeList.size()) {
            Edge edge = this.edgeList.get(i);
            if (edge.source.identifier.equalsIgnoreCase(nodeIdentifier)) {
                if (this.nodeMap.get(edge.destination.identifier) != null
                        && this.nodeMap.get(edge.destination.identifier).neighbors.size() > 0) {
                    this.nodeMap.get(edge.destination.identifier).neighbors.remove(nodeIdentifier);
                    edges.add(edge);
                }
            }
            if (edge.destination.identifier.equalsIgnoreCase(nodeIdentifier)) {
                if (this.nodeMap.get(edge.source.identifier) != null
                        && this.nodeMap.get(edge.source.identifier).neighbors.size() > 0) {
                    this.nodeMap.get(edge.source.identifier).neighbors.remove(nodeIdentifier);
                    edges.add(edge);
                }
            }
            i++;
        }

        for (Edge edge : edges) {
            this.edgeList.remove(edge);
        }

        this.nodeMap.remove(nodeIdentifier);
    }

    public void deleteEdge(Edge deleteEdge) {
        for (Edge edge : edgeList) {
            if (edge.source.identifier.equalsIgnoreCase(deleteEdge.source.identifier)
                    && edge.destination.identifier.equalsIgnoreCase(deleteEdge.destination.identifier)) {
                this.nodeMap.get(edge.source.identifier).neighbors.remove(edge.destination.identifier);
                this.nodeMap.get(edge.destination.identifier).neighbors.remove(edge.source.identifier);
                edgeList.remove(edge);
                break;
            }

        }
    }

    public void printGraph() {
        String lable = null;
        if (this.nodeMap.size() > 0 && this.isDirected) {
            this.setEdgeAverage(((float) this.edgeList.size() / (float) this.nodeMap.size()));
        } else if (this.nodeMap.size() > 0 && !(this.isDirected)) {
            this.setEdgeAverage(((float) (this.edgeList.size() * 2) / (float) this.nodeMap.size()));
        }

        System.err.println("Nodes Count: " + this.nodeMap.size());
        System.err.println("Edges Count: " + this.edgeList.size());
        System.err.println("Graph  isDirected Status: " + this.isDirected);
        System.err.println("Graph  isWeighted Status: " + this.isWeighted);
        System.err.println("Graph  Max(mk): " + (int) this.mk);
        String edgeAverageStr = String.format("%20s %4.5f", "Graph Edge Average: ", this.edgeAverage);
        System.err.println(edgeAverageStr);

        int i = 0;
        for (String key : this.nodeMap.keySet()) {
            if (this.nodeMap.get(key).lable.length() > 30) {
                lable = this.nodeMap.get(key).lable.substring(0, 30);
            } else {
                lable = this.nodeMap.get(key).lable;
            }
            String outputString = String.format("%3s %-10s %7s %-30s %3s %-4s %3s %-4s %7s %-6s %6s %4.4f %8s %3.4f %10s %6.6f",
                    "id:", this.nodeMap.get(key).identifier, "Lable:", lable,
                    "KD:", (int) this.nodeMap.get(key).KD, "nk:", (int) this.nodeMap.get(key).Nk,
                    "Degree:", (int) this.nodeMap.get(key).degree, "delta:", this.nodeMap.get(key).delta,
                    "NGI:", this.nodeMap.get(key).NGI, "RLGI:", this.nodeMap.get(key).RLGI);
            if (i % 2 == 0) {
                System.out.println(outputString);
            } else {
                System.out.println(ANSI_CYAN + outputString + ANSI_RESET);
            }
            i++;
        }
    }

    public void printGraphSorted(String[] keys) {
        String lable = null;
        if (this.nodeMap.size() > 0 && this.isDirected) {
            this.setEdgeAverage(((float) this.edgeList.size() / (float) this.nodeMap.size()));
        } else if (this.nodeMap.size() > 0 && !(this.isDirected)) {
            this.setEdgeAverage(((float) (this.edgeList.size() * 2) / (float) this.nodeMap.size()));
        }

        System.err.println("Nodes Count: " + this.nodeMap.size());
        System.err.println("Edges Count: " + this.edgeList.size());
        System.err.println("Graph  isDirected Status: " + this.isDirected);
        System.err.println("Graph  isWeighted Status: " + this.isWeighted);
        System.err.println("Graph  Max(mk): " + (int) this.mk);
        String edgeAverageStr = String.format("%20s %4.5f", "Graph Edge Average: ", this.edgeAverage);
        System.err.println(edgeAverageStr);

        for (int i = 0; i < keys.length; i++) {
            if (this.nodeMap.get(keys[i]).lable != null && this.nodeMap.get(keys[i]).lable.length() > 30) {
                lable = this.nodeMap.get(keys[i]).lable.substring(0, 30);
            } else {
                lable = this.nodeMap.get(keys[i]).lable;
            }
            String outputString = String.format("%3s %-10s %7s %-30s %3s %-4s %3s %-4s %7s %-6s %6s %4.4f %8s %3.4f %10s %6.6f",
                    "id:", this.nodeMap.get(keys[i]).identifier, "Lable:", lable,
                    "KD:", (int) this.nodeMap.get(keys[i]).KD, "nk:", (int) this.nodeMap.get(keys[i]).Nk,
                    "Degree:", (int) this.nodeMap.get(keys[i]).degree, "delta:", this.nodeMap.get(keys[i]).delta,
                    "NGI:", this.nodeMap.get(keys[i]).NGI, "RLGI:", this.nodeMap.get(keys[i]).RLGI);
            if (i % 2 == 0) {
                System.out.println(outputString);
            } else {
                System.out.println(ANSI_CYAN + outputString + ANSI_RESET);
            }
        }
    }

    public void printGraphEdges() {
        int i = 0;
        for (Edge edge : this.edgeList) {
            if (i % 2 == 0) {
                System.out.println("source:" + edge.source.lable + " ,destination:" + edge.destination.lable);
            } else {
                System.out.println(ANSI_CYAN + "source:" + edge.source.lable + " ,destination:" + edge.destination.lable + ANSI_RESET);
            }
            i++;
        }
    }

    public List getEdgesList() {
        return this.edgeList;
    }

    public Map getNodeMap() {
        return this.nodeMap;
    }

    public boolean getIsDirected() {
        return this.isDirected;
    }

    public boolean getIsWeighted() {
        return this.isWeighted;
    }

    public void setEdgesList(List edList) {
        this.edgeList = edList;
    }

    public void setNodeMap(Map nodeMap) {
        this.nodeMap = nodeMap;
    }

    public void setIsDirected(boolean isDirected) {
        this.isDirected = isDirected;
    }

    public void setIsWeighted(boolean isWeighted) {
        this.isWeighted = isWeighted;
    }

    public float getMk() {
        return this.mk;
    }

    public void setMk(float mk) {
        this.mk = mk;
    }

    public float getEdgeAverage() {
        return this.edgeAverage;
    }

    public void setMaxRLGI(float maxRLGI) {
        this.maxRLGI = maxRLGI;
    }

    public float getMaxRLGI() {
        return this.maxRLGI;
    }

    public void setEdgeAverage(float edgeAverage) {
        this.edgeAverage = edgeAverage;
    }

    public Edge getEdgeViaSourceNodeAndDestinationNode(String SourceNodeKey, String DestinationNodeKey) {
        Edge edge = new Edge(null, null, null, (float) Double.NEGATIVE_INFINITY, (float) Double.NEGATIVE_INFINITY, null, null);
        int findeIndex = -1;
        for (int n = 0; n < this.edgeList.size(); n++) {
            if (this.edgeList.get(n).source.identifier.equals(SourceNodeKey) && this.edgeList.get(n).destination.identifier.equals(DestinationNodeKey)) {
                findeIndex = n;
                edge = this.edgeList.get(n);
            }
        }
        if (findeIndex != -1) {
            return edge;
        }
        return null;
    }

    public void draw() {

        // Create a frame
        JFrame frame = new JFrame();
        // Add a component with a custom paint method
        frame.add(new CustomPaintComponent(this));
        // Display the frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        frame.setIconImage(getFDImage());
        frame.setLocation(0, 0);
        frame.setSize(width, height);
        frame.setTitle("Identifying TopN");
        frame.setVisible(true);
        frame.show();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    protected static Image getFDImage() {
        java.net.URL imgURL = Graph.class.getResource("D.JPG");
        if (imgURL != null) {
            return new ImageIcon(imgURL).getImage();
        } else {
            return null;
        }
    }
}