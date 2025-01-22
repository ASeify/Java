package identifyingtopn;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class IdentifyingTopN {

    static JFrame f;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String ANSI_MAUVE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static String userDirectory;

    public static void main(String[] args) throws FileNotFoundException, CloneNotSupportedException, InterruptedException {
        JFrame f = new JFrame();
        Graph g = new Graph();
        KCore kcore = new KCore();
        long algorithmEndTime = 0, printEndTime = 0, drawEndTime = 0, readFileEndTime = 0, endTime = 0;

        userDirectory = System.getProperty("user.dir") + "\\Dataset";
        JFileChooser chooser = new JFileChooser(userDirectory);
        chooser.setDialogTitle("Choice Graph File (.gml)");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAutoscrolls(true);
        chooser.setBackground(Color.BLACK);
        chooser.setDragEnabled(true);
        chooser.setForeground(Color.GREEN);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Only .gml File", "gml");
        chooser.setFileFilter(filter);
        chooser.setEnabled(true);
        chooser.showOpenDialog(f);
        if (chooser.getSelectedFile() == null || !(chooser.getSelectedFile().getName()
                .substring(chooser.getSelectedFile().getName().length() - 4).equalsIgnoreCase(".gml"))) {
            JOptionPane.showMessageDialog(f, "you dont choice any Acceptable File");
            System.exit(0);
        } else {
            String fileName = chooser.getSelectedFile().getName();
            userDirectory = chooser.getCurrentDirectory().toString();
            List<String> paramsList = new ArrayList<String>();
            String[] sortedNodes;
            File folder = new File(userDirectory);
            File[] graphFile = folder.listFiles();

            long startTime = System.nanoTime();

            g = readFile(graphFile, fileName, paramsList, 1);

            readFileEndTime = System.nanoTime();

            float gSize = g.nodeMap.size();

            paramsList = kcore.kCoreCalc(g);

            paramsList.add(Float.toString(g.getMk()));
            if (gSize != 0) {
                g = readFile(graphFile, fileName, paramsList, gSize);
                g = RLGIcalc(g);

                algorithmEndTime = System.nanoTime();

                sortedNodes = sortNodes(g);
                float maxRLGI = g.nodeMap.get(sortedNodes[0]).RLGI;
                g.setMaxRLGI(maxRLGI);
                g.printGraphSorted(sortedNodes);

                printEndTime = System.nanoTime();
                //g.printGraph();
                //g.printGraphEdges();
            }

            g.draw();
            drawEndTime = System.nanoTime();

            System.out.println(ANSI_RED + "_________________________________________________________________________________" + ANSI_RESET);
            System.out.println("");

            runTimeCalc(startTime, readFileEndTime, "Read file Time: ");

            runTimeCalc(readFileEndTime, algorithmEndTime, "Algorithm run Time: ");

            runTimeCalc(algorithmEndTime, printEndTime, "Sort and print Time: ");

            runTimeCalc(printEndTime, drawEndTime, "Graph Draw Time: ");

            endTime = System.nanoTime();
            runTimeCalc(startTime, endTime, "Total run Time: ");

            System.out.println(ANSI_RED + "_________________________________________________________________________________" + ANSI_RESET);

        }
    }

    public static Graph readFile(File[] files, String fileName, List paramsList, float gSize) throws FileNotFoundException {
        Graph g = new Graph();
        String[] tokens;
        String readedLine = null;
        int fileIndex = -1;
        boolean graphStartFlag = false;
        boolean nodesStartFlag = false;
        boolean edgesStartFlag = false;
        List<String> paramList = new ArrayList<String>();
        paramList = paramsList;

        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equalsIgnoreCase(fileName)) {
                fileIndex = i;
            }
        }
        if (fileIndex != -1) {
            try ( Scanner in = new Scanner(new FileReader(files[fileIndex]))) {
                while (in.hasNextLine()) {
                    readedLine = in.nextLine().trim();
                    if (readedLine.equalsIgnoreCase("graph")) {
                        graphStartFlag = true;
                    }
                    if (readedLine.equalsIgnoreCase("node")) {
                        nodesStartFlag = true;
                        edgesStartFlag = false;
                    }
                    if (readedLine.trim().equalsIgnoreCase("edge")) {
                        edgesStartFlag = true;
                        nodesStartFlag = false;
                    }

                    if (nodesStartFlag) {
                        Node node = new Node();
                        String identifier = null;
                        if (readedLine.equalsIgnoreCase("[")) {
                            while (!readedLine.equalsIgnoreCase("]")) {
                                readedLine = in.nextLine().trim();
                                if (!readedLine.equals("\n")) {
                                    tokens = (readedLine).split(" ");
                                    if (tokens[0].trim().equalsIgnoreCase("id")) {
                                        identifier = tokens[1].trim();
                                        node.setIdentifier(identifier);
                                    } else if ((readedLine.split(" ")[0].trim()).equalsIgnoreCase("label")) {
                                        tokens = readedLine.split("\"");
                                        node.updateLable(tokens[1].trim());
                                    } else if (readedLine.equalsIgnoreCase("value")) {
                                        tokens = readedLine.split(" ");
                                        node.value = Integer.parseInt(tokens[1].trim());
                                    } else if ((readedLine.split(" ")[0].trim()).equalsIgnoreCase("source")) {
                                        tokens = readedLine.split(" ");
                                        tokens[1] = tokens[1].replaceAll("\"", "");
                                        String[] source = (tokens[1].trim()).split(",");
                                        node.setSource(source);
                                    } else if (readedLine.equalsIgnoreCase("graphics")) {
                                        readedLine = in.nextLine().trim();
                                        if (readedLine.trim().equalsIgnoreCase("[")) {
                                            while (!readedLine.trim().equalsIgnoreCase("]")) {
                                                readedLine = in.nextLine().trim();
                                                if (!readedLine.equals("\n")) {
                                                    tokens = readedLine.split(" ");
                                                    if (tokens[0].trim().equalsIgnoreCase("x")) {
                                                        node.x = Float.parseFloat(tokens[1].trim());
                                                    } else if (tokens[0].trim().equalsIgnoreCase("y")) {
                                                        node.y = Float.parseFloat(tokens[1].trim());
                                                    } else if (tokens[0].trim().equalsIgnoreCase("z")) {
                                                        node.z = Float.parseFloat(tokens[1].trim());
                                                    } else if (tokens[0].trim().equalsIgnoreCase("w")) {
                                                        node.w = Float.parseFloat(tokens[1].trim());
                                                    } else if (tokens[0].trim().equalsIgnoreCase("h")) {
                                                        node.h = Float.parseFloat(tokens[1].trim());
                                                    } else if (tokens[0].trim().equalsIgnoreCase("d")) {
                                                        node.d = Float.parseFloat(tokens[1].trim());
                                                    } else if (tokens[0].trim().equalsIgnoreCase("fill")) {
                                                        tokens[1] = tokens[1].replaceAll("\"", "");
                                                        node.fill = tokens[1].trim();
                                                    }
                                                }
                                            }
                                        }

                                    }
                                    for (String params : paramList) {
                                        String[] param = params.split(":");
                                        if (param[0].equalsIgnoreCase(identifier)) {
                                            float mk = Float.parseFloat(paramList.get(paramList.size() - 1));
                                            float nk = Float.parseFloat(param[2]);
                                            float degree = Float.parseFloat(param[3]);
                                            float delta = (1 + (nk / mk));
                                            float KD = Float.parseFloat(param[1]);
                                            node.delta = delta;
                                            float NGI = ((degree * KD * delta) / gSize);
                                            node.Nk = (int) nk;
                                            node.KD = (int) KD;
                                            node.degree = (int) degree;
                                            node.NGI = NGI;
                                            g.setMk(mk);
                                            paramList.remove(params);
                                            break;
                                        }
                                    }
                                    if (identifier != null) {
                                        //node.printNodeInfo();
                                        g.addNode(node);
                                    }
                                }
                            }
                        }

                    } else if (edgesStartFlag) {
                        if (in.hasNextLine()) {
                            readedLine = in.nextLine().trim();
                            if (readedLine.equalsIgnoreCase("[")) {
                                String edgeId = null, edgeSourceNodeId = null, edgeDestinationNodeId = null, edgeDuration = null, edgeCount = null;
                                float edgeWeight = Float.NEGATIVE_INFINITY, edgeValue = Float.NEGATIVE_INFINITY;
                                Node sourceNode = null;
                                Node destinationNode = null;
                                while (!readedLine.equalsIgnoreCase("]")) {
                                    readedLine = in.nextLine().trim();
                                    if (!readedLine.equals("\n")) {
                                        tokens = readedLine.split(" ");
                                        if (tokens[0].trim().equalsIgnoreCase("id")) {
                                            edgeId = tokens[1].trim();
                                        } else if (tokens[0].trim().equalsIgnoreCase("source")) {
                                            edgeSourceNodeId = tokens[1].trim();
                                        } else if (tokens[0].trim().equalsIgnoreCase("target")) {
                                            edgeDestinationNodeId = tokens[1].trim();
                                        } else if (tokens[0].equalsIgnoreCase("weight")) {
                                            edgeWeight = Float.parseFloat(tokens[1].trim());
                                        } else if (tokens[0].equalsIgnoreCase("value")) {
                                            edgeValue = Float.parseFloat(tokens[1].trim());
                                        } else if (tokens[0].equalsIgnoreCase("duration")) {
                                            edgeDuration = tokens[1].trim();
                                        } else if (tokens[0].equalsIgnoreCase("count")) {
                                            tokens[1] = tokens[1].replaceAll("\"", "");
                                            edgeDuration = tokens[1].trim();
                                        }
                                    }
                                }
                                if (edgeSourceNodeId != null && edgeDestinationNodeId != null) {
                                    sourceNode = g.nodeMap.get(edgeSourceNodeId);
                                    destinationNode = g.nodeMap.get(edgeDestinationNodeId);
                                    if (sourceNode.identifier != null && destinationNode.identifier != null) {
                                        Edge edge = new Edge(edgeId, sourceNode, destinationNode, edgeWeight,
                                                edgeValue, edgeDuration, edgeCount);
                                        g.addEdge(edge);
                                    }
                                }
                            }
                        }

                    } else if (graphStartFlag) {
                        if (!readedLine.equalsIgnoreCase("[") && !readedLine.equalsIgnoreCase("]")) {
                            tokens = readedLine.split(" ");
                            if (tokens[0].trim().equalsIgnoreCase("directed")) {
                                if (tokens[1].trim().equalsIgnoreCase("0")) {
                                    g.setIsDirected(false);
                                } else if (tokens[1].trim().equalsIgnoreCase("1")) {
                                    g.setIsDirected(true);
                                }
                            }
                            if (tokens[0].trim().equalsIgnoreCase("weighted")) {
                                if (tokens[1].equalsIgnoreCase("0")) {
                                    g.setIsWeighted(false);
                                } else if (tokens[1].trim().equalsIgnoreCase("1")) {
                                    g.setIsWeighted(true);
                                }
                            }
                        }
                    }

                }
            }

        } else {
            JOptionPane.showMessageDialog(f, "Sorry file " + fileName + " not found.");
        }
        return g;
    }

    public static Graph RLGIcalc(Graph g) {

        for (String key : g.nodeMap.keySet()) {
            float NGI = 0;
            for (int i = 0; i < g.nodeMap.get(key).neighbors.size(); i++) {
                NGI = NGI + (g.nodeMap.get(g.nodeMap.get(key).neighbors.get(i))).NGI;
            }
            if (NGI > 0) {
                float RLGI = ((g.nodeMap.get(key).getDegree() * g.nodeMap.get(key).getNGI()) / NGI);
                g.nodeMap.get(key).setRLGI(RLGI);
            } else {
                float RLGI = (g.nodeMap.get(key).getDegree() * g.nodeMap.get(key).getNGI());
                g.nodeMap.get(key).setRLGI(RLGI);
            }
        }

        return g;
    }

    public static String[] sortNodes(Graph graph) {
        String[] sortedNodes = new String[graph.nodeMap.size()];
        String tempStr = null;
        int counter1 = 0;
        for (String key : graph.nodeMap.keySet()) {
            sortedNodes[counter1] = key;
            counter1++;
        }
        for (int i = 0; i < sortedNodes.length; i++) {
            for (int j = i + 1; j < sortedNodes.length; j++) {
                if ((graph.nodeMap.get(sortedNodes[i]).RLGI < graph.nodeMap.get(sortedNodes[j]).RLGI)) {
                    tempStr = sortedNodes[i];
                    sortedNodes[i] = sortedNodes[j];
                    sortedNodes[j] = tempStr;

                }
            }
        }
        return sortedNodes;
    }

    public static void runTimeCalc(long startRunTime, long finishRunTime, String str) {
        long[] times = new long[3];
        long totalTimeMili = (finishRunTime - startRunTime) / 1000000;
        times[0] = totalTimeMili / 60000;
        times[1] = ((totalTimeMili) - (times[0] * 60000)) / 1000;
        times[2] = (totalTimeMili - ((times[0] * 60000) + (times[1] * 1000)));

        String outputString = String.format("%-25s %5d %10s %2d %10s %3d %12s", str, times[0], " minutes ",
                times[1], " seconds ", times[2], " milisecond ");
        System.out.println(outputString);
    }

    public static void printArray(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}
