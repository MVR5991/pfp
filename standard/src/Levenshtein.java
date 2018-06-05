/**
 * 
 * Abstract base class for the Levenshtein distance
 * 
 * @author Georg Dotzler
 * @author Marius Kamp
 *
 */
public abstract class Levenshtein {

	/**
	 * Computes the value of the table at position [row][column], with 1 <= row < table.length and
	 * 1 <= column < table[0].length
	 * 
	 * @param row
	 * @param column
	 * @param table the half filled table with the Levenshtein distances of the prefixes.
	 * @param wordHorizontal the word used horizontal in the comparison
	 * @param wordVertical the word used vertically in the comparison
	 * @return
	 */
	public int computeValue(int row, int column, int[][] table, char[] wordHorizontal, char[] wordVertical) {
		// TODO
		return -1;
	}
	
	

	/**
	 * Returns the matrix that represents the Levenshtein distances between
	 * all prefixes of the two words. The Levenshtein distance between both words is found
	 * at matrix[wordVertical.length()-1][wordHorizontal.length()-1].
	 * @param wordHorizontal first word, used horizontal in the matrix
	 * @param wordVertical second word, used vertical in the matrix
	 * @return the filled matrix
	 */
	public abstract int[][] computeLevenshtein(char[] wordHorizontal, char[] wordVertical);

}
