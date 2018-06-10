package oldWOrk;

/**
 * 
 * Abstract base class for the oldWOrk.Levenshtein distance
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
	 * @param table the half filled table with the oldWOrk.Levenshtein distances of the prefixes.
	 * @param wordHorizontal the word used horizontal in the comparison
	 * @param wordVertical the word used vertically in the comparison
	 * @return
	 */
	public int computeValue(int row, int column, int[][] table, char[] wordHorizontal, char[] wordVertical) {
		final int smallest;
		if(row == 0 && column == 0){
			smallest = 0;
		} else if(row >=1 && column == 0){
			smallest = row;
		}else if(column >=1 && row == 0){
			smallest = column;
		}
		else if(wordHorizontal[column-1] == wordVertical[row-1]){
			smallest = table[row-1][column-1];
		} else {
			smallest = Math.min(table[row][column-1], Math.min(table[row-1][column-1],table[row-1][column] )) +1;
		}
		return smallest;
	}
	
	

	/**
	 * Returns the matrix that represents the oldWOrk.Levenshtein distances between
	 * all prefixes of the two words. The oldWOrk.Levenshtein distance between both words is found
	 * at matrix[wordVertical.length()-1][wordHorizontal.length()-1].
	 * @param wordHorizontal first word, used horizontal in the matrix
	 * @param wordVertical second word, used vertical in the matrix
	 * @return the filled matrix
	 */
	public abstract int[][] computeLevenshtein(char[] wordHorizontal, char[] wordVertical);

}
