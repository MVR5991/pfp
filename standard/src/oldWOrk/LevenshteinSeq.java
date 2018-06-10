package oldWOrk;

public class LevenshteinSeq extends Levenshtein {

    @Override
    public int[][] computeLevenshtein(char[] wordHorizontal, char[] wordVertical) {

        int levenStheinMatrix[][] = new int[wordVertical.length+1][wordHorizontal.length+1];

        for (int i = 0; i < levenStheinMatrix.length; i++) {
            for (int j = 0; j < levenStheinMatrix[0].length; j++) {
                levenStheinMatrix[i][j] = computeValue(i,j,levenStheinMatrix,wordHorizontal, wordVertical);
            }
        }

        return levenStheinMatrix;
    }
}
