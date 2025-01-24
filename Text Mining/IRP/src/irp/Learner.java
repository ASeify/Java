package irp;

import static irp.Search.ANSI_BLUE;
import static irp.Search.ANSI_GREEN;
import static irp.Search.ANSI_RED;
import static irp.Search.ANSI_RESET;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Learner {

    File[] negFiles;
    File[] posFiles;
    File[] unsupFiles;
    File[] posWords;
    File[] negWords;
    File[] stopWords;
    File folder;
    FileReader fileReader;
    StreamTokenizer tokenizer;
    public static boolean firstFlag = true;
    public static String[][] negTokArray = new String[1][3];
    public static String[][] posTokArray = new String[1][3];
    public static String[][] unsupTokArray = new String[1][3];

    public static String[] posWordsArray = new String[1];
    public static String[] negWordsArray = new String[1];
    public static String[] stopWordsArray = new String[1];

    public static String[][] allTokensArray = new String[1][8];
    static JFrame f;

    public static int totalDoucument;
    public static int negDoumentsCount;
    public static int negTokensCount;
    public static int posDoumentsCount;
    public static int posTokensCount;
    public static int unsupDoumentsCount;
    public static int unsupTokensCount;

    public static String gPath;

    public void myModelLearner(String path) throws FileNotFoundException, IOException {
        if (path != null) {
            gPath = path;
            //JOptionPane.showMessageDialog(f, path);
            //JOptionPane.showMessageDialog(f, "Please waite. ​Learning is running.");
            String negPath = path + "\\train\\neg";
            String posPath = path + "\\train\\pos";
            String unsupPath = path + "\\train\\unsup";
            FilesReader filesReader = new FilesReader();

            long allStartTime = System.nanoTime();
            long startTime = System.nanoTime();
            startTime = System.nanoTime();

            firstFlag = true;
            folder = new File(path);
            stopWords = folder.listFiles();
            stopWordsArray = filesReader.readStoprWordFile(stopWords, "stopWords.txt");
            long endTime = System.nanoTime();
            long totalTime = (endTime - startTime) / 1000000000;
            long totalTimeMinet = (int) totalTime / 60;
            long totalTimeSec = totalTime - (totalTimeMinet * 60);
            System.out.println(" -------------------- " + "Stop Words tokenized. " + "Time: " + totalTimeMinet + ":" + totalTimeSec + " S -------------------- ");

            folder = new File(negPath);
            firstFlag = true;
            negFiles = folder.listFiles();
            negTokArray = filesReader.readFiles(negFiles);
            endTime = System.nanoTime();
            totalTime = (endTime - startTime) / 1000000000;
            totalTimeMinet = (int) totalTime / 60;
            totalTimeSec = totalTime - (totalTimeMinet * 60);
            System.out.println(" -------------------- " + ANSI_BLUE + "Negative Documents tokenized. " + ANSI_RESET + "Time: " + totalTimeMinet + ":" + totalTimeSec + " S -------------------- ");

            
            
            startTime = System.nanoTime();
            firstFlag = true;
            folder = new File(posPath);
            posFiles = folder.listFiles();
            posTokArray = filesReader.readFiles(posFiles);
            endTime = System.nanoTime();
            totalTime = (endTime - startTime) / 1000000000;
            totalTimeMinet = (int) totalTime / 60;
            totalTimeSec = totalTime - (totalTimeMinet * 60);
            System.out.println(" -------------------- " + ANSI_GREEN + "Positive Documents tokenized. " + ANSI_RESET + "Time: " + totalTimeMinet + ":" + totalTimeSec + " S -------------------- ");

            startTime = System.nanoTime();
            firstFlag = true;
            folder = new File(unsupPath);
            unsupFiles = folder.listFiles();
            unsupTokArray = filesReader.readFiles(unsupFiles);
            endTime = System.nanoTime();
            totalTime = (endTime - startTime) / 1000000000;
            totalTimeMinet = (int) totalTime / 60;
            totalTimeSec = totalTime - (totalTimeMinet * 60);
            System.out.println(" -------------------- " + ANSI_RED + "Unsup Documents tokenized. " + ANSI_RESET + "Time: " + totalTimeMinet + ":" + totalTimeSec + " S -------------------- ");

            negDoumentsCount = negFiles.length;
            negTokensCount = negTokArray.length;
            posDoumentsCount = posFiles.length;
            posTokensCount = posTokArray.length;
            unsupDoumentsCount = unsupFiles.length;
            unsupTokensCount = unsupTokArray.length;
            totalDoucument = (negDoumentsCount) + (posDoumentsCount) + (unsupDoumentsCount);
//            System.out.println("negDoumentsCount: " + negDoumentsCount + " ,negTokensCount: " + negTokensCount);
//            System.out.println("posDoumentsCount: " + posDoumentsCount + " ,posTokensCount: " + posTokensCount);
//            System.out.println("unsupDoumentsCount: " + unsupDoumentsCount + " ,unsupTokensCount: " + unsupTokensCount);

            

            startTime = System.nanoTime();
            firstFlag = true;
            folder = new File(path);
            posFiles = folder.listFiles();
            posWordsArray = filesReader.readFile(posFiles, "posWords.txt");
            endTime = System.nanoTime();
            totalTime = (endTime - startTime) / 1000000000;
            totalTimeMinet = (int) totalTime / 60;
            totalTimeSec = totalTime - (totalTimeMinet * 60);
            System.out.println(" -------------------- " + ANSI_GREEN + "Positive Words tokenized. " + ANSI_RESET + "Time: " + totalTimeMinet + ":" + totalTimeSec + " S -------------------- ");

            startTime = System.nanoTime();
            firstFlag = true;
            folder = new File(path);
            negFiles = folder.listFiles();
            negWordsArray = filesReader.readFile(negFiles, "negWords.txt");
            endTime = System.nanoTime();
            totalTime = (endTime - startTime) / 1000000000;
            totalTimeMinet = (int) totalTime / 60;
            totalTimeSec = totalTime - (totalTimeMinet * 60);
            System.out.println(" -------------------- " + ANSI_RED + "Negative Words tokenized. " + ANSI_RESET + "Time: " + totalTimeMinet + ":" + totalTimeSec + " S -------------------- ");

            startTime = System.nanoTime();
            TokenRefinement tokenRefinement = new TokenRefinement();
            tokenRefinement.tokenrefine(negTokArray, posTokArray, unsupTokArray);
            //print2DArray(negTokArray);
            endTime = System.nanoTime();
            totalTime = (endTime - startTime) / 1000000000;
            totalTimeMinet = (int) totalTime / 60;
            totalTimeSec = totalTime - (totalTimeMinet * 60);
            System.out.println(" -------------------- Tokens Refinement. Time: " + totalTimeMinet + ":" + totalTimeSec + " S -------------------- ");

            //print2DArray(unsupTokArray);
            startTime = System.nanoTime();
            mergTokens(negTokArray, 1);
            mergTokens(posTokArray, 2);
            mergTokens(unsupTokArray, 3);
            //System.err.println(allTokensArray.length);
            endTime = System.nanoTime();
            totalTime = (endTime - startTime) / 1000000000;
            totalTimeMinet = (int) totalTime / 60;
            totalTimeSec = totalTime - (totalTimeMinet * 60);
            System.out.println(" -------------------- Tokens Merged. Time: " + totalTimeMinet + ":" + totalTimeSec + " S -------------------- ");

            for (int i = 0; i < allTokensArray.length; i++) {
                if (allTokensArray[i][1] == null) {
                    allTokensArray[i][1] = "0";
                }
                if (allTokensArray[i][2] == null) {
                    allTokensArray[i][2] = "0";
                }
                if (allTokensArray[i][3] == null) {
                    allTokensArray[i][3] = "0";
                }
                if (allTokensArray[i][4] == null) {
                    allTokensArray[i][4] = "0";
                }
                if (allTokensArray[i][5] == null) {
                    allTokensArray[i][5] = "0";
                }
                if (allTokensArray[i][6] == null) {
                    allTokensArray[i][6] = "0";
                }
                if (allTokensArray[i][7] == null) {
                    allTokensArray[i][7] = "0";
                }
            }

            startTime = System.nanoTime();
            allTokensArray = sortArr(allTokensArray);
            endTime = System.nanoTime();
            totalTime = (endTime - startTime) / 1000000000;
            totalTimeMinet = (int) totalTime / 60;
            totalTimeSec = totalTime - (totalTimeMinet * 60);
            System.out.println(" -------------------- Tokens Sort. Time: " + totalTimeMinet + ":" + totalTimeSec + " S -------------------- ");

            allTokensArray = Arrays.copyOf(allTokensArray, allTokensArray.length + 1);
            allTokensArray[allTokensArray.length - 1] = new String[8];
            allTokensArray[allTokensArray.length - 1][0] = Integer.toString(totalDoucument);
            allTokensArray[allTokensArray.length - 1][1] = Integer.toString(negDoumentsCount) + ":" + Integer.toString(negTokArray.length);
            allTokensArray[allTokensArray.length - 1][2] = Integer.toString(posDoumentsCount) + ":" + Integer.toString(posTokArray.length);
            allTokensArray[allTokensArray.length - 1][3] = Integer.toString(unsupDoumentsCount) + ":" + Integer.toString(unsupTokArray.length);
            allTokensArray[allTokensArray.length - 1][4] = Integer.toString(allTokensArray.length - 1);

            negTokArray = null;
            negFiles = null;
            posTokArray = null;
            posFiles = null;
            unsupTokArray = null;
            unsupFiles = null;
            negWordsArray = null;
            negWords = null;
            posWordsArray = null;
            posWords = null;

            startTime = System.nanoTime();
            writetokens(allTokensArray, path + "\\tokens.txt");
            endTime = System.nanoTime();
            totalTime = (endTime - startTime) / 1000000000;
            totalTimeMinet = (int) totalTime / 60;
            totalTimeSec = totalTime - (totalTimeMinet * 60);
            System.out.println(" -------------------- Tokens Write. Time: " + totalTimeMinet + ":" + totalTimeSec + " S -------------------- ");
            long allEndTime = System.nanoTime();
            long allTotalTime = (allEndTime - allStartTime) / 1000000000;
            totalTimeMinet = (int) allTotalTime / 60;
            totalTimeSec = allTotalTime - (totalTimeMinet * 60);
            System.out.println(" -------------------- Total Time: " + totalTimeMinet + ":" + totalTimeSec + " S -------------------- ");
            EvaluationModel evaluator = new EvaluationModel();
            evaluator.evaluation(path);
            JOptionPane.showMessageDialog(f, "Thank You. Learning complet.");
        }
    }

    public void mergTokens(String[][] toksArray, int index) {
        boolean findFlag = false;
        boolean posWord = false,
                negWord = false;
        for (int i = 0; i < toksArray.length; i++) {
            findFlag = false;
            posWord = ArraySearcher.searchWordInArray(posWordsArray, toksArray[i][0]);
            if (posWord == false) {
                negWord = ArraySearcher.searchWordInArray(negWordsArray, toksArray[i][0]);
            }
            for (int j = 0; j < allTokensArray.length; j++) {
                if (allTokensArray[j][0] != null && toksArray[i][0] != null) {
                    if (toksArray[i][0].equalsIgnoreCase(allTokensArray[j][0])) {
                        allTokensArray[j][0] = toksArray[i][0];
                        allTokensArray[j][index] = toksArray[i][1];
                        allTokensArray[j][index + 3] = toksArray[i][2];
                        if (posWord == true) {
                            allTokensArray[0][7] = String.valueOf(2);
                        } else if (negWord == true) {
                            allTokensArray[0][7] = String.valueOf(1);
                        } else {
                            allTokensArray[0][7] = String.valueOf(0);
                        }
                        findFlag = true;
                        break;
                    }
                } else {
                    allTokensArray[0][0] = toksArray[i][0];
                    allTokensArray[0][index] = toksArray[i][1];
                    allTokensArray[0][index + 3] = toksArray[i][2];
                    if (posWord == true) {
                        allTokensArray[0][7] = String.valueOf(2);
                    } else if (negWord == true) {
                        allTokensArray[0][7] = String.valueOf(1);
                    } else {
                        allTokensArray[0][7] = String.valueOf(0);
                    }
                    if (index == 1) {
                        allTokensArray[allTokensArray.length - 1][2] = String.valueOf(0);
                        allTokensArray[allTokensArray.length - 1][3] = String.valueOf(0);
                        allTokensArray[allTokensArray.length - 1][5] = String.valueOf(0);
                        allTokensArray[allTokensArray.length - 1][6] = String.valueOf(0);
                    } else if (index == 2) {
                        allTokensArray[allTokensArray.length - 1][1] = String.valueOf(0);
                        allTokensArray[allTokensArray.length - 1][3] = String.valueOf(0);
                        allTokensArray[allTokensArray.length - 1][4] = String.valueOf(0);
                        allTokensArray[allTokensArray.length - 1][6] = String.valueOf(0);
                    } else if (index == 3) {
                        allTokensArray[allTokensArray.length - 1][1] = String.valueOf(0);
                        allTokensArray[allTokensArray.length - 1][2] = String.valueOf(0);
                        allTokensArray[allTokensArray.length - 1][4] = String.valueOf(0);
                        allTokensArray[allTokensArray.length - 1][5] = String.valueOf(0);
                    }
                    findFlag = true;
                    break;
                }

            }
            if (findFlag != true) {
                allTokensArray = Arrays.copyOf(allTokensArray, allTokensArray.length + 1);
                allTokensArray[allTokensArray.length - 1] = new String[8];
                allTokensArray[allTokensArray.length - 1][0] = toksArray[i][0];
                allTokensArray[allTokensArray.length - 1][index] = toksArray[i][1];
                allTokensArray[allTokensArray.length - 1][index + 3] = toksArray[i][2];
                if (posWord == true) {
                    allTokensArray[allTokensArray.length - 1][7] = String.valueOf(2);
                } else if (negWord == true) {
                    allTokensArray[allTokensArray.length - 1][7] = String.valueOf(1);
                } else {
                    allTokensArray[allTokensArray.length - 1][7] = String.valueOf(0);
                }
                if (index == 1) {
                    allTokensArray[allTokensArray.length - 1][2] = String.valueOf(0);
                    allTokensArray[allTokensArray.length - 1][3] = String.valueOf(0);
                    allTokensArray[allTokensArray.length - 1][5] = String.valueOf(0);
                    allTokensArray[allTokensArray.length - 1][6] = String.valueOf(0);
                } else if (index == 2) {
                    allTokensArray[allTokensArray.length - 1][1] = String.valueOf(0);
                    allTokensArray[allTokensArray.length - 1][3] = String.valueOf(0);
                    allTokensArray[allTokensArray.length - 1][4] = String.valueOf(0);
                    allTokensArray[allTokensArray.length - 1][6] = String.valueOf(0);
                } else if (index == 3) {
                    allTokensArray[allTokensArray.length - 1][1] = String.valueOf(0);
                    allTokensArray[allTokensArray.length - 1][2] = String.valueOf(0);
                    allTokensArray[allTokensArray.length - 1][4] = String.valueOf(0);
                    allTokensArray[allTokensArray.length - 1][5] = String.valueOf(0);
                }
            }

        }

    }

    public static void writetokens(String[][] tokensArr, String fileName) {
        try {
            File myFile = new File(fileName);
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
                myFile.delete();
                writetokens(tokensArr, fileName);
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(fileName);
            for (int i = 0; i < tokensArr.length; i++) {
                myWriter.write(tokensArr[i][0] + "," + tokensArr[i][1] + "," + tokensArr[i][2] + "," + tokensArr[i][3]
                        + "," + tokensArr[i][4] + "," + tokensArr[i][5] + "," + tokensArr[i][6] + "," + tokensArr[i][7] + "\n");
            }
            myWriter.close();
            System.out.println("Successfully write to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String[][] sortArr(String[][] toksArr) {
        String[][] tempArr = toksArr;
        String[] tmp = new String[8];
        for (int i = 0; i < tempArr.length; i++) {
            for (int j = i + 1; j < tempArr.length; j++) {
                if (toksArr[i][0].compareToIgnoreCase(toksArr[j][0]) >= 0) {
                    tmp[0] = tempArr[i][0];
                    tmp[1] = tempArr[i][1];
                    tmp[2] = tempArr[i][2];
                    tmp[3] = tempArr[i][3];
                    tmp[4] = tempArr[i][4];
                    tmp[5] = tempArr[i][5];
                    tmp[6] = tempArr[i][6];
                    tmp[7] = tempArr[i][7];

                    tempArr[i][0] = tempArr[j][0];
                    tempArr[i][1] = tempArr[j][1];
                    tempArr[i][2] = tempArr[j][2];
                    tempArr[i][3] = tempArr[j][3];
                    tempArr[i][4] = tempArr[j][4];
                    tempArr[i][5] = tempArr[j][5];
                    tempArr[i][6] = tempArr[j][6];
                    tempArr[i][7] = tempArr[j][7];

                    tempArr[j][0] = tmp[0];
                    tempArr[j][1] = tmp[1];
                    tempArr[j][2] = tmp[2];
                    tempArr[j][3] = tmp[3];
                    tempArr[j][4] = tmp[4];
                    tempArr[j][5] = tmp[5];
                    tempArr[j][6] = tmp[6];
                    tempArr[j][7] = tmp[7];

                }
            }
        }

        return tempArr;
    }

    public static void print2DArray(String[][] arr2D) {
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

    public static void listFiles(String path, File[] filesList) {
        File folder = new File(path);
        filesList = folder.listFiles();
    }
}
