package irp;

import  irp.Learner;
import static irp.Learner.firstFlag;

//************************************************
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GetTokens {

    File[] tokensFile;
    File folder;
    FileReader fileReader;
    StreamTokenizer tokenizer;
    static JFrame f;
    public static String[] tokensArray = new String[1];
    //public static String[][] allTokensArray = new String[1][5];
    static int fileIndex = -1;
    
    Learner learner = new Learner();

    GetTokens(String dataPath) throws IOException {
            
        if (dataPath != null) {
            long startTime = System.nanoTime();
            Learner.gPath = dataPath;
            JOptionPane.showMessageDialog(f, "Please wait. Loading is running.");
            firstFlag = true;
            folder = new File(dataPath);
            tokensFile = folder.listFiles();
            readFile(tokensFile, "tokens.txt");
            //System.err.println("Sytem get " + allTokensArray.length + " token from tokens.txt file");
            if (fileIndex != -1) {
                JOptionPane.showMessageDialog(f, "Thank You. Load complete.");
            }
             long endTime = System.nanoTime();
            long totalTime = (endTime - startTime) / 1000000000;
            long totalTimeMinet = (int) totalTime / 60;
            long totalTimeSec = totalTime - (totalTimeMinet * 60);
            System.out.println(" -------------------- "+learner.allTokensArray.length + " term loded. " + "Time: " + totalTimeMinet + ":" + totalTimeSec + " S -------------------- ");

            //print2DArray(allTokensArray);

        }

    }

    public void print2DArray(String[][] arr2D) {
        for (int i = 0; i < arr2D.length; i++) {
            for (int j = 0; j < arr2D[i].length; j++) {
                System.out.print(arr2D[i][j]);
                if (j < arr2D[i].length - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        }
    }

    public void readFile(File[] files, String fileName) throws FileNotFoundException {

        String[] tokArr;
        StringBuilder sb = null;
        String tok = null;

        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equalsIgnoreCase(fileName)) {
                fileIndex = i;
            }
        }
        if (fileIndex != -1) {
            try (Scanner in = new Scanner(new FileReader(files[fileIndex]))) {
                sb = new StringBuilder();
                while (in.hasNext()) {
                    sb.append(in.next());
                    sb.append('\n');
                }
            }

            tok = sb.toString();
            tokArr = tok.split("\n");
            for (int i = 0; i < tokArr.length; i++) {
                String[] token = tokArr[i].split(",");
                boolean findFlag = false;
                if (token[0] != null && !token[0].isEmpty() && token[0].length() > 2 && i < tokArr.length - 1) {
                    if (learner.allTokensArray.length == 1 && firstFlag == true) {
                        learner.allTokensArray[0][0] = token[0];
                        learner.allTokensArray[0][1] = token[1];
                        learner.allTokensArray[0][2] = token[2];
                        learner.allTokensArray[0][3] = token[3];
                        learner.allTokensArray[0][4] = token[4];
                        learner.allTokensArray[0][5] = token[5];
                        learner.allTokensArray[0][6] = token[6];
                        learner.allTokensArray[0][7] = token[7];
                        findFlag = true;
                        firstFlag = false;
                    } else {
                        for (int j = 0; j < learner.allTokensArray.length; j++) {
                            if (learner.allTokensArray[j][0] != null) {
                                if (token[0].equalsIgnoreCase(learner.allTokensArray[j][0])) {
                                    learner.allTokensArray[0][0] = token[0];
                                    learner.allTokensArray[0][1] = token[1];
                                    learner.allTokensArray[0][2] = token[2];
                                    learner.allTokensArray[0][3] = token[3];
                                    learner.allTokensArray[0][4] = token[4];
                                    learner.allTokensArray[0][5] = token[5];
                                    learner.allTokensArray[0][6] = token[6];
                                    learner.allTokensArray[0][7] = token[7];
                                    findFlag = true;
                                }
                            }
                        }
                        if (findFlag != true) {
                            learner.allTokensArray = Arrays.copyOf(learner.allTokensArray, learner.allTokensArray.length + 1);
                            learner.allTokensArray[learner.allTokensArray.length - 1] = new String[8];
                            learner.allTokensArray[learner.allTokensArray.length - 1][0] = token[0];
                            learner.allTokensArray[learner.allTokensArray.length - 1][1] = token[1];
                            learner.allTokensArray[learner.allTokensArray.length - 1][2] = token[2];
                            learner.allTokensArray[learner.allTokensArray.length - 1][3] = token[3];
                            learner.allTokensArray[learner.allTokensArray.length - 1][4] = token[4];
                            learner.allTokensArray[learner.allTokensArray.length - 1][5] = token[5];
                            learner.allTokensArray[learner.allTokensArray.length - 1][6] = token[6];
                            learner.allTokensArray[learner.allTokensArray.length - 1][7] = token[7];

                        }
                    }

                } else if (token[0] != null && !token[0].isEmpty() && token[0].length() > 2 && i == tokArr.length - 1) {
                    learner.totalDoucument = Integer.parseInt(token[0]);

                    String negDoumentsInfo = token[1];
                    String[] negDoumentsInfoArray = negDoumentsInfo.split(":");
                    learner.negDoumentsCount = Integer.parseInt(negDoumentsInfoArray[0]);
                    learner.negTokensCount = Integer.parseInt(negDoumentsInfoArray[1]);

                    String posDoumentsInfo = token[2];
                    String[] posDoumentsInfoArray = posDoumentsInfo.split(":");
                    learner.posDoumentsCount = Integer.parseInt(posDoumentsInfoArray[0]);
                    learner.posTokensCount = Integer.parseInt(posDoumentsInfoArray[1]);

                    String unsupDoumentsInfo = token[3];
                    String[] unsupDoumentsInfoArray = unsupDoumentsInfo.split(":");
                    learner.unsupDoumentsCount = Integer.parseInt(unsupDoumentsInfoArray[0]);
                    learner.unsupTokensCount = Integer.parseInt(unsupDoumentsInfoArray[1]);

                }
            }
            tok = null;
            sb = null;

        } else {
            JOptionPane.showMessageDialog(f, "Sorry file tokens.txt not found.");
        }
    }
}
