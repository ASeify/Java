package irp;

//**************************************
//import static com.sun.corba.se.impl.util.Utility.printStackTrace;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FilesReader {

    MyTokenizer myTokenizer = new MyTokenizer();
    ArraySearcher arraySearcher = new ArraySearcher();
    static JFrame f;

    public String[][] readFiles(File[] files) throws FileNotFoundException, IOException {
        String[][] tokenArray = new String[1][2];
        String[][] tokensArray = new String[1][3];
        tokensArray[0][2] = String.valueOf(0);
        StringBuilder sb = null;
        String token = null;
        boolean firstRound = true;
        boolean nonRepetition = false;
        for (int i = 0; i < files.length; i++) {
            try (Scanner in = new Scanner(new java.io.FileReader(files[i]))) {
                sb = new StringBuilder();
                while (in.hasNext()) {
                    sb.append(in.next());
                    sb.append('\n');
                }
            }

            token = sb.toString();
            if (token != null) {
                tokenArray = myTokenizer.myStringTokenizer(token);
                for (int j = 0; j < tokenArray.length; j++) {
                    token = tokenArray[j][0];
                    if (token != null && !(token.isEmpty())) {
                        if (firstRound == true && tokensArray.length == 1) {
                            firstRound = false;
                            tokensArray[0][0] = token;
                            tokensArray[0][1] = String.valueOf(Integer.parseInt(tokenArray[i][1]));
                            tokensArray[0][2] = String.valueOf(1);

                        } else {
                            //searcherTokenAnswer[0] is fund or not found
                            //searcherTokenAnswer[1] is funded index or not found -1
                            int[] searcherTokenAnswer = arraySearcher.twoDimensionalArraySearcher(tokensArray, token);
                            //if searcherTokenAnswer[0] is 0 mean token not funded else if it is 1 token funded

                            if (searcherTokenAnswer[0] == 0) {
                                tokensArray = Arrays.copyOf(tokensArray, tokensArray.length + 1);
                                tokensArray[tokensArray.length - 1] = new String[3];
                                tokensArray[tokensArray.length - 1][0] = token;
                                tokensArray[tokensArray.length - 1][1] = String.valueOf(Integer.parseInt(tokenArray[j][1]));
                                tokensArray[tokensArray.length - 1][2] = String.valueOf(1);

                            } else if (searcherTokenAnswer[1] != -1 && searcherTokenAnswer[0] == 1) {
                                tokensArray[searcherTokenAnswer[1]][1] = String.valueOf(Integer.parseInt(tokenArray[j][1])
                                        + Integer.parseInt(tokensArray[searcherTokenAnswer[1]][1]));
                                tokensArray[searcherTokenAnswer[1]][2] = String.valueOf(Integer.parseInt(tokensArray[searcherTokenAnswer[1]][2]) + 1);
                            }

                        }

                    }

                }
            }
            token = null;
            sb = null;
        }
        return tokensArray;

    }

    public String[] readFile(File[] files, String fileName) throws FileNotFoundException, IOException {
        String[][] tokensArray = new String[1][2];
        StringBuilder sb = null;
        String token = null;
        int fileIndex = -1;
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
        }
        else{
            JOptionPane.showMessageDialog(f, (fileName + " file not Ffound in path: " + files[fileIndex]));
        }
        token = sb.toString();
        token = token.replaceAll("[^a-zA-Z0-9\\-]", " ");
        tokensArray = myTokenizer.myStringTokenizer(token);
        String[] tokenArray = new String[tokensArray.length];
        token = null;
        sb = null;
        for (int i = 0; i < tokensArray.length; i++) {
            tokenArray[i] = tokensArray[i][0];

        }
        return tokenArray;

    }

    public String[][] readFileTokens(File[] files, String fileName) throws FileNotFoundException, IOException {
        String[][] tokensArray = new String[1][2];
        StringBuilder sb = null;
        String token = null;
        int fileIndex = -1;
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
        }
        token = sb.toString();
        token = token.replaceAll("[^a-zA-Z0-9\\-]", " ");
        tokensArray = myTokenizer.myStringTokenizer(token);
        token = null;
        sb = null;

        return tokensArray;

    }

    public String[] readStoprWordFile(File[] files, String fileName) throws FileNotFoundException, IOException {
        String[] tokensArray = new String[1];
        StringBuilder sb = null;
        String token = null;
        int fileIndex = -1;
        
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
                    sb.append(",");
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(f, (fileName + " file Not found in path: " + files[fileIndex]));
        }
        token = sb.toString();
        token = token.replaceAll("[^a-zA-Z0-9\\-]", ",");
        //tokensArray = myTokenizer.myStringTokenizer(token);
        tokensArray = token.split(",");
        token = null;
        sb = null;
        return tokensArray;

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

}
