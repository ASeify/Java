package irp;

import java.util.Arrays;
import java.util.StringTokenizer;
//**********************************
import irp.ArraySearcher;

public class MyTokenizer {

    ArraySearcher arraySearcher = new ArraySearcher();
    Stemmer slemToken = new Stemmer();

    //first case = token And  Second case = token frequency
    public String[][] myStringTokenizer(String inputStrig) {

        String token = null;
        String[][] tokensArray = new String[1][2];
        boolean firstRound = true;
        Stemmer slemToken = new Stemmer();
        String tokensString = inputStrig.replaceAll("[^a-zA-Z0-9\\-]", " ");
        StringTokenizer strTokenizer = new StringTokenizer(tokensString, " ");
        while (strTokenizer.hasMoreTokens()) {
            token = strTokenizer.nextToken();
            String[][] handleDashCharsArray = handleDashChars(token);

            for (int i = 0; i < handleDashCharsArray.length; i++) { 
                token = handleDashCharsArray[i][0];
                if (token != null) {
                    token = kGramsHandler(token);
                    if (token != null && !(token.isEmpty()) && token.length() > 2) {
                        if (firstRound == true && tokensArray.length == 1) {
                            firstRound = false;
                            tokensArray[0][0] = token;
                            tokensArray[0][1] = String.valueOf(1);

                        } else {
                            //searcherTokenAnswer[0] is fund or not found
                            //searcherTokenAnswer[1] is funded index or not found -1
                            int[] searcherTokenAnswer = arraySearcher.twoDimensionalArraySearcher(tokensArray, token);
                            //if searcherTokenAnswer[0] is 0 mean token not funded else if it is 1 token funded
                            if (searcherTokenAnswer[0] == 0) {
                                tokensArray = Arrays.copyOf(tokensArray, tokensArray.length + 1);
                                tokensArray[tokensArray.length - 1] = new String[2];
                                tokensArray[tokensArray.length - 1][0] = token;
                                tokensArray[tokensArray.length - 1][1] = String.valueOf(1);
                            } else {
                                tokensArray[searcherTokenAnswer[1]][1] = String.valueOf(Integer.parseInt(tokensArray[searcherTokenAnswer[1]][1]) + 1);
                            }
                        }

                    }
                }
            }
        }
        tokensArray = deleteStopWords(tokensArray, Learner.stopWordsArray);
        return tokensArray;
    }

    public String[][] handleDashChars(String str) {
        String token = str;
        boolean firstRound = true;
        String[][] handledTokenArray = new String[1][2];
        byte dashcounter = 0;
        for (int j = 0; j < token.length(); j++) {
            if (token.charAt(0) == '-') {
                token = token.substring(1, (token.length()));
                dashcounter = 2;
                break;

            }
            if (token.charAt((token.length() - 1)) == '-') {
                token = token.substring(0, (token.length() - 2));
                dashcounter = 2;
                break;

            }
            if (token.charAt(j) == '-') {
                dashcounter++;
                if (dashcounter >= 2) {
                    break;
                }
            }
        }
        if (dashcounter > 1) {
            String[] dashHanledTokens = dashHandler(token);
            for (int i = 0; i < dashHanledTokens.length; i++) {
                if (dashHanledTokens[i] != null && !((dashHanledTokens[i]).isEmpty())) {
                    dashHanledTokens[i] = handleNumbers(dashHanledTokens[i]);
                    dashHanledTokens[i] = slemToken.stem(dashHanledTokens[i]);
                    if (dashHanledTokens[i] != null && dashHanledTokens[i].length() > 3) {
                        if (firstRound == true && handledTokenArray.length == 1) {
                            firstRound = false;
                            handledTokenArray[0][0] = dashHanledTokens[i];
                            handledTokenArray[0][1] = String.valueOf(1);

                        } else {
                            //searcherTokenAnswer[0] is fund or not found
                            //searcherTokenAnswer[1] is funded index or not found -1
                            int[] searcherTokenAnswer = arraySearcher.twoDimensionalArraySearcher(handledTokenArray, dashHanledTokens[i]);
                            //if searcherTokenAnswer[0] is 0 mean token not funded else if it is 1 token funded
                            if (searcherTokenAnswer[0] == 0) {
                                handledTokenArray = Arrays.copyOf(handledTokenArray, handledTokenArray.length + 1);
                                handledTokenArray[handledTokenArray.length - 1] = new String[2];
                                handledTokenArray[handledTokenArray.length - 1][0] = dashHanledTokens[i];
                                handledTokenArray[handledTokenArray.length - 1][1] = String.valueOf(1);
                            } else {
                                handledTokenArray[searcherTokenAnswer[1]][1] = String.valueOf(Integer.parseInt(handledTokenArray[searcherTokenAnswer[1]][1]) + 1);
                            }
                        }
                    }
                }
            }
        } else {

            token = slemToken.stem(token);
            if (token != null && !((token).isEmpty())) {
                token = handleNumbers(token);
                token = slemToken.stem(token);
                if (token != null && token.length() > 2) {
                    if (firstRound == true && handledTokenArray.length == 1) {
                        firstRound = false;
                        handledTokenArray[0][0] = token;
                        handledTokenArray[0][1] = String.valueOf(1);

                    } else {
                        //searcherTokenAnswer[0] is fund or not found
                        //searcherTokenAnswer[1] is funded index or not found -1
                        int[] searcherTokenAnswer = arraySearcher.twoDimensionalArraySearcher(handledTokenArray, token);
                        //if searcherTokenAnswer[0] is 0 mean token not funded else if it is 1 token funded
                        if (searcherTokenAnswer[0] == 0) {
                            handledTokenArray = Arrays.copyOf(handledTokenArray, handledTokenArray.length + 1);
                            handledTokenArray[handledTokenArray.length - 1] = new String[2];
                            handledTokenArray[handledTokenArray.length - 1][0] = token;
                            handledTokenArray[handledTokenArray.length - 1][1] = String.valueOf(1);
                        } else {
                            handledTokenArray[searcherTokenAnswer[1]][1] = String.valueOf(Integer.parseInt(handledTokenArray[searcherTokenAnswer[1]][1]) + 1);
                        }
                    }

                }
            }

        }
        return handledTokenArray;
    }

    public String handleNumbers(String str) {
        String token = str;
        if (token.length() > 3) {
            for (int j = 0; j < token.length(); j++) {
                if (Character.isDigit(token.charAt(0))) {
                    if ((token.length() > 1) && (Character.isDigit(token.charAt(1)))) {
                        if ((token.length() > 2) && (Character.isDigit(token.charAt(2)))) {
                            if ((token.length() > 3) && (Character.isDigit(token.charAt(3)))) {
                                if ((token.length() > 4) && ((Character.isDigit(token.charAt(4))) || token.charAt(4) != '-')) {
                                    token = token.replaceAll("[^a-zA-Z]", "");
                                }
                            } else {
                                token = token.replaceAll("[^a-zA-Z]", "");
                            }
                        } else {
                            token = token.replaceAll("[^a-zA-Z]", "");
                        }
                    } else {
                        token = token.replaceAll("[^a-zA-Z]", "");
                    }

                }
            }
        } else {
            token = token.replaceAll("[^a-zA-Z]", "");
        }
        return token;
    }

    public String[] dashHandler(String str) {
        String[] tokensArray = new String[1];
        boolean firstRound = true;
        String tokens = str.replaceAll("[^a-zA-Z0-9]", " ");
        StringTokenizer strTokenizer = new StringTokenizer(tokens, " ");
        while (strTokenizer.hasMoreTokens()) {
            String token = strTokenizer.nextToken();
            token = token.trim();
            if (token != null && !token.isEmpty() && token.length() > 2) {
                if (firstRound == true && tokensArray.length == 1) {
                    firstRound = false;
                    tokensArray[0] = token;
                } else {
                    tokensArray = Arrays.copyOf(tokensArray, tokensArray.length + 1);
                    tokensArray[tokensArray.length - 1] = token;
                }

            }
        }
        return tokensArray;
    }

    public String kGramsHandler(String str) {
        String token = str;

        if (token != null) {
            int j = 0;
            int position = token.length() - j;
            while (j < token.length()) {
                position = (token.length()) - j;
                if ((position > (j + 8)) && token.substring(j, j + 4).equalsIgnoreCase(token.substring(j + 4, j + 8))) {
                    token = token.substring(0, j) + token.substring(j + 4, token.length());

                    j -= 1;
                } else {
                    if ((position == (8)) && token.substring(j, j + 4).equalsIgnoreCase(token.substring(j + 4, token.length()))) {
                        token = token.substring(0, 4);
                        //break;
                    }
                }
                j += 1;
            }
            int l = 0;
            while (l < token.length()) {
                position = (token.length()) - l;
                if ((position > (l + 6)) && token.substring(l, l + 3).equalsIgnoreCase(token.substring(l + 3, l + 6))) {
                    token = token.substring(0, l) + token.substring(l + 4, token.length());
                    l -= 1;
                } else {
                    if ((position == 6) && token.substring(l, l + 3).equalsIgnoreCase(token.substring(l + 3, token.length()))) {
                        token = token.substring(0, 3);
                        //break;
                    }
                }
                l += 1;
            }
            int c = 0;
            while (c < token.length()) {
                position = (token.length()) - c;
                if (((position > 4) && Character.toLowerCase(token.charAt(c)) == Character.toLowerCase(token.charAt(c + 1))
                        && Character.toLowerCase(token.charAt(c)) == Character.toLowerCase(token.charAt(c + 2)))) {
                    token = token.substring(0, position - 1) + token.substring(position, token.length());
                    c -= 1;
                } else {
                    if (((position == 4) && Character.toLowerCase(token.charAt(c)) == Character.toLowerCase(token.charAt(c + 1))
                            && Character.toLowerCase(token.charAt(c)) == Character.toLowerCase(token.charAt(c + 2)))) {
                        token = token.substring(0, 2) + token.substring(3, token.length());
                        //break;
                    }
                }
                c += 1;
            }
            token = slemToken.stem(token);

        } else {
            System.err.println(token);
        }
        return token;
    }

    public String[][] deleteStopWords(String[][] tokensArray, String[] stopWordsArray) {
        if (stopWordsArray.length > 1) {
            String[][] deleteStopWordsArray = new String[1][2];
            boolean firstRound = true;
            boolean foundElement = false;
            for (int i = 0; i < tokensArray.length; i++) {
                foundElement = ArraySearcher.searchWordInArray(stopWordsArray, tokensArray[i][0]);
                if (foundElement == false) {
                    if (firstRound == true && deleteStopWordsArray.length == 1) {
                        firstRound = false;
                        deleteStopWordsArray[0][0] = tokensArray[i][0];
                        deleteStopWordsArray[0][1] = tokensArray[i][1];
                    } else {
                        deleteStopWordsArray = Arrays.copyOf(deleteStopWordsArray, deleteStopWordsArray.length + 1);
                        deleteStopWordsArray[deleteStopWordsArray.length - 1] = new String[2];
                        deleteStopWordsArray[deleteStopWordsArray.length - 1][0] = tokensArray[i][0];
                        deleteStopWordsArray[deleteStopWordsArray.length - 1][1] = tokensArray[i][1];
                    }
                }

            }
            return deleteStopWordsArray;

        }
        return tokensArray;
    }
}
