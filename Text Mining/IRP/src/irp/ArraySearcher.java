/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irp;

public class ArraySearcher {

    public int[] twoDimensionalArraySearcher(String[][] stringsArray, String str) {
        int[] answer = new int[2];
        answer[0] = 0;
        answer[1] = -1;
        for (int i = 0; i < stringsArray.length; i++) {
            if (stringsArray[i][0] != null && str != null && !(stringsArray[i][0].isEmpty()) && !(str.isEmpty()) &&
                    stringsArray[i][0].equalsIgnoreCase(str)) {
                answer[0] = 1;
                answer[1] = i;
                return answer;
            }

        }

        return answer;
    }

    public int oneDimensionalArraySearcher(String[] stringsArray, String str) {
        int answer = -1;
        for (int i = 0; i < stringsArray.length; i++) {
            if (stringsArray[i].equalsIgnoreCase(str)) {
                answer = i;
                break;
            }

        }

        return answer;
    }

    public static boolean searchWordInArray(String[] wordsArr, String word) {
        boolean findFlag = false;
        for (int i = 0; i < wordsArr.length; i++) {
            if (wordsArr[i].equalsIgnoreCase(word)) {
                findFlag = true;
                return true;
            }
        }
        return findFlag;
    }

}
