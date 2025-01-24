package irp;

import irp.Learner;
//**************************************
import java.util.Arrays;

public class TokenRefinement extends Learner{

    public static void tokenrefine(String[][] negArr, String[][] posArr, String[][] unsupArr) {
        Learner.negTokArray = deleteHighFrequencyTokens(negArr, "neg");
         Learner.posTokArray = deleteHighFrequencyTokens(posArr, "pos");
         Learner.unsupTokArray = deleteHighFrequencyTokens(unsupArr, "unsup");
//--------------------------------------------------------------------------------------------------
        jointtokens(Learner.negTokArray, Learner.posTokArray, Learner.unsupTokArray);

    }

    public static String[][] deleteHighFrequencyTokens(String[][] toksArr, String classLable) {
        boolean firstFlag = true;
        int documentCount = 0;
//        if (classLable.equalsIgnoreCase("neg")) {
//            documentCount = Learner.negDoumentsCount;
//        } else if (classLable.equalsIgnoreCase("pos")) {
//            documentCount = Learner.posDoumentsCount;
//        } else {
//            documentCount = Learner.unsupDoumentsCount;
//        }
        documentCount = Learner.totalDoucument;
        String[][] tempTokArray = new String[1][3];
        for (int i = 0; i < toksArr.length; i++) {
            if ((Integer.parseInt(toksArr[i][1])) > 0 && ((documentCount /Integer.parseInt(toksArr[i][1]))) >= 1) {
                if (firstFlag == true && tempTokArray.length == 1) {
                    firstFlag = false;
                    tempTokArray[0][0] = toksArr[i][0];
                    tempTokArray[0][1] = toksArr[i][1];
                    tempTokArray[0][2] = toksArr[i][2];
                    
                } else {
                    tempTokArray = Arrays.copyOf(tempTokArray, tempTokArray.length + 1);
                    tempTokArray[tempTokArray.length - 1] = new String[3];
                    tempTokArray[tempTokArray.length - 1][0] = toksArr[i][0];
                    tempTokArray[tempTokArray.length - 1][1] = toksArr[i][1];
                    tempTokArray[tempTokArray.length - 1][2] = toksArr[i][2];
                }
            }
        }
        return tempTokArray;

    }

    public static void jointtokens(String[][] negArr, String[][] posArr, String[][] unsupArr) {
        Learner.firstFlag = true;
        int x = -1;
        int y = -1;
        for (int i = 0; i < negArr.length; i++) {
            x = serachtok(posArr, negArr[i][0]);
            y = serachtok(unsupArr, negArr[i][0]);
            if (x != -1 && y != -1) {
                int negTokFerq = Integer.parseInt(negArr[i][1]);
                int posTokFerq = Integer.parseInt(posArr[x][1]);
                int unsupTokFerq = Integer.parseInt(unsupArr[y][1]);
                if (((negTokFerq >= (posTokFerq - 1)) && (negTokFerq == (posTokFerq + 1)))
                        && ((negTokFerq == (unsupTokFerq - 1)) && (negTokFerq == (unsupTokFerq + 1)))) {
                    deleteNegArrayElement(i);
                    deletePosArrayElement(x);
                    deleteUnsupArrayElement(y);
                }
            }
        }

    }

    public static int serachtok(String[][] tokArr, String str) {
        int index = -1;
        for (int i = 0; i < tokArr.length; i++) {
            if (tokArr[i][0] != null && tokArr[i][0].equalsIgnoreCase(str)) {
                return index;
            }
        }

        return index;
    }

    public static void deleteNegArrayElement(int x) {
        for (int i = x; i < Learner.negTokArray.length; i++) {
            Learner.negTokArray[i][0] = Learner.negTokArray[i + 1][0];
            Learner.negTokArray[i][1] = Learner.negTokArray[i + 1][1];
            Learner.negTokArray[i][2] = Learner.negTokArray[i + 1][2];
            Learner.negTokArray[i][0] = null;
            Learner.negTokArray[i][1] = null;
            Learner.negTokArray[i][2] = null;
        }
        Learner.posTokArray = Arrays.copyOf(Learner.posTokArray, Learner.posTokArray.length - 1);

    }

    public static void deletePosArrayElement(int x) {
        for (int i = x; i < Learner.posTokArray.length; i++) {
            Learner.posTokArray[i][0] = Learner.posTokArray[i + 1][0];
            Learner.posTokArray[i][1] = Learner.posTokArray[i + 1][1];
            Learner.posTokArray[i][2] = Learner.posTokArray[i + 1][2];
            Learner.posTokArray[i][0] = null;
            Learner.posTokArray[i][1] = null;
            Learner.posTokArray[i][2] = null;
        }
        Learner.posTokArray = Arrays.copyOf(Learner.posTokArray, Learner.posTokArray.length - 1);

    }

    public static void deleteUnsupArrayElement(int x) {
        for (int i = x; i < Learner.unsupTokArray.length; i++) {
            Learner.unsupTokArray[i][0] = Learner.unsupTokArray[i + 1][0];
            Learner.unsupTokArray[i][1] = Learner.unsupTokArray[i + 1][1];
            Learner.unsupTokArray[i][2] = Learner.unsupTokArray[i + 1][2];
            Learner.unsupTokArray[i][0] = null;
            Learner.unsupTokArray[i][1] = null;
            Learner.unsupTokArray[i][2] = null;
        }
        Learner.unsupTokArray = Arrays.copyOf(Learner.unsupTokArray, Learner.unsupTokArray.length - 1);

    }

}
