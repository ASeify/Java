package irp;

import irp.FilesReader;
import irp.MyTokenizer;
import irp.Learner;
//***********************************************
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Search {

    static String filePath = null;
    static FileReader fileReader;
    static String[][] tokenArray = new String[1][2];
    static int[][] indexs;

    static String[][][] filesTokenArray;
    static int[][] filesIndexs;

    static String finalState = "unsup";
    static JFrame f;
    static File[] selectedFilesArr;
    static String[] finalStateArr;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static int posAnswer = 0;
    public static int negAnswer = 0;
    public static int unsupAnswer = 0;

    FilesReader filesReader = new FilesReader();
    MyTokenizer myStringTokenizer = new MyTokenizer();
    Learner learner = new Learner();

    public void getAnswerViaTextArea(String str) {
        f = new JFrame();
        posAnswer = 0;
        negAnswer = 0;
        unsupAnswer = 0;
        if (learner.allTokensArray.length > 1) {
            indexs = null;
            String tok = str.replaceAll("[^a-zA-Z0-9\\-]", " ");
            tokenArray = myStringTokenizer.myStringTokenizer(tok);
            indexs = new int[tokenArray.length][2];
            indexSearch(tokenArray);
            finalState = getState(indexs);
            ShowResult(finalState);
        } else {
            JOptionPane.showMessageDialog(f, "<html><font color=red>Learned data is empty.</font></html>");
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

    public void getAnswerFile() throws IOException {
        f = new JFrame();
        posAnswer = 0;
        negAnswer = 0;
        unsupAnswer = 0;
        if (learner.allTokensArray.length > 1) {
            filesTokenArray = null;
            filesTokenArray = null;
            filesTokenArray = getTextFromFile();
            finalStateArr = new String[filesTokenArray.length];
            for (int i = 0; i < filesTokenArray.length; i++) {
                String[][] fileInTokArray = new String[filesTokenArray[i].length][1];
                fileInTokArray = filesTokenArray[i];
                indexs = new int[filesTokenArray[i].length][2];
                indexSearch(fileInTokArray);
                finalState = getState(indexs);
                finalStateArr[i] = finalState;
                if (filesTokenArray.length == 1) {
                    ShowResult(finalState);
                } else if (i == filesTokenArray.length - 1) {
                    ShowResults(finalStateArr);
                }
            }
            System.out.print(ANSI_GREEN + "Positive Count = " + posAnswer + ANSI_RESET);
            System.out.print(" And " + ANSI_RED + " And Negative Count = " + negAnswer + ANSI_RESET);
            System.out.println(" And " + ANSI_BLUE + "Unsup Count = " + unsupAnswer + ANSI_RESET);
        } else {
            JOptionPane.showMessageDialog(f, "<html><font color=red>Learned data is empty.</font></html>");
        }

    }

    static void ShowResult(String state) {
        if (state.equalsIgnoreCase("Positive")) {
            JOptionPane.showMessageDialog(f, "<html><font color=green>Positive</font></html>");
        } else if (state.equalsIgnoreCase("Negative")) {
            JOptionPane.showMessageDialog(f, "<html><font color=red>Negative</font></html>");
        } else {
            JOptionPane.showMessageDialog(f, "<html><font color=blue>Unsup</font></html>");
        }
    }

    static void ShowResults(String[] state) {
        String messString = "";
        int c = 1;
        for (int i = 0; i < state.length; i++) {
            String counter = " " + Integer.toString(c) + "- ";
            if (state[i] != null && !state[i].isEmpty()) {
                c = c + 1;
            }
            if (state[i].equalsIgnoreCase("Positive")) {
                messString += "<html><font color=green> " + counter + state[i] + "</font></html>" + "\n";
            } else if (state[i].equalsIgnoreCase("Negative")) {
                messString += "<html><font color=red> " + counter + state[i] + "</font></html>" + "\n";
            } else {
                messString += "<html><font color=blue> " + counter + state[i] + "</font></html>" + "\n";
            }

        }
        JOptionPane.showMessageDialog(f, messString);

    }

    static void printToks(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    static void printIndexs(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(i + " -> " + arr[i][0]);
        }
    }

    void indexSearch(String[][] tokArr) {
        for (int i = 0; i < tokArr.length; i++) {
            if (tokArr[i][0] != null) {
                indexs[i][0] = bSearch(learner.allTokensArray, tokArr[i][0]);
                indexs[i][1] = Integer.parseInt(tokArr[i][1]);
            } else {
                indexs[i][0] = -1;
                indexs[i][1] = -1;
            }
        }
    }

    public String[][][] getTextFromFile() throws FileNotFoundException, IOException {
        String[][][] tokArr = new String[1][1][1];
        JFileChooser fileChooser;
        FileNameExtensionFilter filter;
        fileChooser = new JFileChooser(new File(System.getProperty("user.home") + "\\train"));
        fileChooser.setDialogTitle("Select File");
        filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        int selectDirectoryAction = fileChooser.showDialog(fileChooser, "select");
        if (selectDirectoryAction != 1) {
            selectedFilesArr = fileChooser.getSelectedFiles();
            for (int i = 0; i < selectedFilesArr.length; i++) {
                //System.out.println(tokArr.length);
                if (i == 0) {
                    //tokArr = Arrays.copyOf(tokArr, 1);
                    tokArr[i] = filesReader.readFileTokens(selectedFilesArr, selectedFilesArr[i].getName());
                } else {
                    tokArr = Arrays.copyOf(tokArr, tokArr.length + 1);
                    tokArr[i] = filesReader.readFileTokens(selectedFilesArr, selectedFilesArr[i].getName());
                }
            }

        }
        return tokArr;
    }

    public static int bSearch(String arr[][], String strSearch) {
        if (strSearch != null) {
            int firstIndex = 0;
            int lastIndex = arr.length - 1;
            if (arr.length == 1 && arr[0][0].equalsIgnoreCase(strSearch)) {
                return 0;
            } else {

                while (firstIndex <= lastIndex) {
                    int middleIndex = (firstIndex + lastIndex) / 2;
                    if (arr[middleIndex][0] != null && strSearch != null) {
                        if (arr[middleIndex][0].equalsIgnoreCase(strSearch)) {
                            return middleIndex;
                        } else if (arr[middleIndex][0].compareToIgnoreCase(strSearch) < 0) {
                            firstIndex = middleIndex + 1;
                        } else if (arr[middleIndex][0].compareToIgnoreCase(strSearch) > 0) {
                            lastIndex = middleIndex - 1;
                        }

                    }
                }
            }
        }
        return -1;
    }

    String getState(int[][] indexArr) {
        String state = "unsup";
        float finalScor = 0;
        float[] scor = new float[indexArr.length];
        int queryTokenCount = indexArr.length;

        for (int i = 0; i < indexArr.length; i++) {
            if (indexArr[i][0] != -1) {
                String token = learner.allTokensArray[indexArr[i][0]][0];

                float negFerqent = Integer.parseInt(learner.allTokensArray[indexArr[i][0]][1]);
                float posFerqent = Integer.parseInt(learner.allTokensArray[indexArr[i][0]][2]);
                float unsupFerqent = Integer.parseInt(learner.allTokensArray[indexArr[i][0]][3]);

                float negFerqentNonRepetition = Integer.parseInt(learner.allTokensArray[indexArr[i][0]][4]);
                float posFerqentNonRepetition = Integer.parseInt(learner.allTokensArray[indexArr[i][0]][5]);
                float unsupFerqentNonRepetition = Integer.parseInt(learner.allTokensArray[indexArr[i][0]][6]);

                float wordState = Integer.parseInt(learner.allTokensArray[indexArr[i][0]][7]);

                float queryFer = indexArr[i][1];
                float queryTF = indexArr[i][1];
                float queryIDF = (float) ((queryFer) / (1 + (queryTokenCount)));
                float query_tf_idf = queryTF * queryIDF;

                float negIDF = (float) ((negFerqentNonRepetition) / (1 + (Learner.negDoumentsCount)));
                float negIDFRepetition = (float) ((negFerqent) / (1 + (Learner.negDoumentsCount)));
                float neg_tf_idf = (negIDF + negIDFRepetition);

                float posIDF = (float) ((posFerqentNonRepetition) / (1 + (Learner.posDoumentsCount)));
                float posIDFRepetition = (float) ((posFerqent) / (1 + (Learner.posDoumentsCount)));
                float pos_tf_idf = (posIDF + posIDFRepetition);

                float neg_tf_idf_Score = (float) ((neg_tf_idf * 100000) / (1 + Learner.totalDoucument));
                float pos_tf_idf_Score = (float) ((pos_tf_idf * 100000) / (1 + Learner.totalDoucument));

                //System.out.println((neg_tf_idf_Score) + "," + (pos_tf_idf_Score) + " , " + Learner.totalDoucument);
                if ((neg_tf_idf_Score) > (pos_tf_idf_Score)) {
                    scor[i] = (-1)*(((neg_tf_idf_Score) - (pos_tf_idf_Score))) * query_tf_idf;
                    if (learner.allTokensArray[indexArr[i][0]][7].equalsIgnoreCase("1")) {
                        scor[i] = (float) (scor[i] * (1000));
                    }
                } else if ((neg_tf_idf_Score) < (pos_tf_idf_Score)) {

                    scor[i] = (((pos_tf_idf_Score) - (neg_tf_idf_Score)) * query_tf_idf);
                    if (learner.allTokensArray[indexArr[i][0]][7].equalsIgnoreCase("2")) {
                        scor[i] = (float) (scor[i] * (500));
                    }
                } else {
                    scor[i] = 0;
                }
            } else {
                scor[i] = 0;
            }

        }
        for (int i = 0; i < scor.length; i++) {
            finalScor = scor[i] + finalScor;

        }

        if (finalScor > 0) {
            state = "Positive";
            posAnswer += 1;
           // System.out.println(finalScor + " -> " + ANSI_GREEN + "Positive" + ANSI_RESET);
        } else if (finalScor < 0) {
            state = "Negative";
            negAnswer += 1;
            //System.out.println(finalScor + " -> " + ANSI_RED + "Negative" + ANSI_RESET);
        } else {
            state = "Unsup";
            unsupAnswer += 1;
            //System.out.println(finalScor + " -> " + ANSI_BLUE + "Unsup" + ANSI_RESET);
        }
        return state;
    }

}
