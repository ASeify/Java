package irp;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import irp.FilesReader;
import static irp.Search.ANSI_BLUE;
import static irp.Search.ANSI_GREEN;
import static irp.Search.ANSI_RED;
import static irp.Search.ANSI_RESET;

//******************************************
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class EvaluationModel {

    FilesReader filesReader = new FilesReader();
    MyTokenizer myStringTokenizer = new MyTokenizer();
    Learner learner = new Learner();
    Search shearcher = new Search();

    File folder;
    File[] negFiles;
    File[] posFiles;
    String[][][] negTokenArray = new String[1][1][1];
    String[][][] posTokenArray = new String[1][1][1];

    static int[][] negIndexs;
    static int[][] posIndexs;

    String[][] negStateArr = new String[1][1];
    String[][] posStateArr = new String[1][1];

    float [] evaluatorParams = new float[6];

    static JFrame f;

    static String finalState = "unsup";

    public void evaluation(String path) throws IOException {
        if (path != null) {
            //JOptionPane.showMessageDialog(f, path);
            String negPath = path + "\\test\\neg";
            String posPath = path + "\\test\\pos";

            long startTime = System.nanoTime();
            startTime = System.nanoTime();

            folder = new File(negPath);
            negFiles = folder.listFiles();
            negStateArr = Arrays.copyOf(negStateArr, negFiles.length);
            for (int i = 0; i < negFiles.length; i++) {
                negStateArr[i] = new String[2];
                if (i == 0) {
                    negTokenArray[i] = filesReader.readFileTokens(negFiles, negFiles[i].getName());
                } else {
                    negTokenArray = Arrays.copyOf(negTokenArray, negTokenArray.length + 1);
                    negTokenArray[i] = filesReader.readFileTokens(negFiles, negFiles[i].getName());
                }
            }
            getNegAnswerFile();
            float negSamples = negFiles.length;
            for (int i = 0; i < negStateArr.length; i++) {
                if (negStateArr[i][0].equalsIgnoreCase("Negative")) {
                    evaluatorParams[0] += 1;
                } else if (negStateArr[i][0].equalsIgnoreCase("Positive")) {
                    evaluatorParams[1] += 1;
                } else {
                    evaluatorParams[2] += 1;
                    negSamples -= 1;
                }
            }

            System.out.println("--------------------------------------------------------------------------------------------");

            folder = new File(posPath);
            posFiles = folder.listFiles();
            posStateArr = Arrays.copyOf(posStateArr, posFiles.length);
            for (int i = 0; i < posFiles.length; i++) {
                posStateArr[i] = new String[2];
                if (i == 0) {
                    posTokenArray[i] = filesReader.readFileTokens(posFiles, posFiles[i].getName());
                } else {
                    posTokenArray = Arrays.copyOf(posTokenArray, posTokenArray.length + 1);
                    posTokenArray[i] = filesReader.readFileTokens(posFiles, posFiles[i].getName());
                }
            }
            getPosAnswerFile();
            float posSamples = posFiles.length;
            for (int i = 0; i < posStateArr.length; i++) {
                if (posStateArr[i][0].equalsIgnoreCase("Positive")) {
                    evaluatorParams[3] += 1;
                } else if (posStateArr[i][0].equalsIgnoreCase("Negative")) {
                    evaluatorParams[4] += 1;
                } else {
                    evaluatorParams[5] += 1;
                    posSamples -= 1;
                }
            }

            
            System.out.println("True Negative: " + evaluatorParams[0] + " , False Positive : " + evaluatorParams[1]
                    + ", Unsup Negative: " + evaluatorParams[2] + " , True Positive: "
                    + evaluatorParams[3] + " , False Negative: " + evaluatorParams[4] + " , Unsup Positive: " + evaluatorParams[5]);

            float presision = ((evaluatorParams[3]) /(1+ (evaluatorParams[3] + evaluatorParams[1])));
            float recall = ((evaluatorParams[3]) / (1+(evaluatorParams[3] + evaluatorParams[4])));
            float accuracy = ((evaluatorParams[3]+evaluatorParams[0])/(1+(evaluatorParams[0]+evaluatorParams[1]+evaluatorParams[3]+evaluatorParams[4])));
            
            
            System.out.println("presision: "+presision);
            System.out.println("recall: "+recall);
            System.out.println("accuracy :"+accuracy);

            long endTime = System.nanoTime();
            long totalTime = (endTime - startTime) / 1000000000;
            long totalTimeMinet = (int) totalTime / 60;
            long totalTimeSec = totalTime - (totalTimeMinet * 60);
            System.out.println(" -------------------- " + "Evaluation Model. " + "Time: " + totalTimeMinet + ":" + totalTimeSec + " S -------------------- ");
        }
    }

    public void getNegAnswerFile() throws IOException {
        String[][] stateArr = new String[negTokenArray.length][1];
        if (Learner.allTokensArray.length > 1) {
            for (int i = 0; i < negTokenArray.length; i++) {
                String[][] fileInTokArray = new String[negTokenArray[i].length][1];
                negIndexs = indexSearch(negTokenArray[i]);
                finalState = shearcher.getState(negIndexs);
                negStateArr[i][0] = finalState;
            }
        }
    }

    public void getPosAnswerFile() throws IOException {
        String[][] stateArr = new String[posTokenArray.length][1];
        if (Learner.allTokensArray.length > 1) {
            for (int i = 0; i < posTokenArray.length; i++) {
                posIndexs = indexSearch(posTokenArray[i]);
                finalState = shearcher.getState(posIndexs);
                posStateArr[i][0] = finalState;
            }
        }
    }

    int[][] indexSearch(String[][] tokArr) {
        int[][] indexs = new int[tokArr.length][2];
        for (int i = 0; i < tokArr.length; i++) {
            indexs[i][0] = Search.bSearch(learner.allTokensArray, tokArr[i][0]);
            indexs[i][1] = Integer.parseInt(tokArr[i][1]);
        }
        return indexs;
    }

}
