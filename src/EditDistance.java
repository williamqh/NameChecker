/**
 * Created with IntelliJ IDEA.
 * User: Hao Qu
 * Date: 12-9-20
 * Time: 上午12:04
 * Contains methods that used for calculating edit distance.
 */
public class EditDistance {

    // calculate weighted Levenshtein distance between 2 strings
    public static int distance(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();
        int[][] T = new int[m + 1][n + 1];
        T[0][0] = 0;
        for (int j = 0; j < n; j++) {
            T[0][j + 1] = T[0][j] + ins(str2, j);
        }
        for (int i = 0; i < m; i++) {
            T[i + 1][0] = T[i][0] + del(str1, i);
            for (int j = 0; j < n; j++) {
                T[i + 1][j + 1] = min(
                        T[i][j] + sub(str1, i, str2, j),
                        T[i][j + 1] + del(str1, i),
                        T[i + 1][j] + ins(str2, j)
                );
            }
        }
        return T[m][n];
    }

    private static int sub(String str1, int xi, String str2, int yi) {
        return str1.charAt(xi) == str2.charAt(yi) ? 0 : 1;
    }

    private static int ins(String str1, int xi) {
        return 1;
    }

    private static int del(String str1, int xi) {
        return 2;  // modified weight to increase the cost to delete a character, this would favour longer strings
    }

    private static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

}
