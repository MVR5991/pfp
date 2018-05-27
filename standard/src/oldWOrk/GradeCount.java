package oldWOrk;

import java.util.Map;

/**
 * Combines a grade and the number of its occurrences.
 */
public class GradeCount implements Comparable<GradeCount> {
	/**
	 * The grade.
	 */
	public String grade;
	/**
	 * The number of occurrences of grade.
	 */
	public int count;

	/**
	 * Creates a new oldWOrk.GradeCount based on the specified grade and count.
	 *
	 * @param grade the grade.
	 * @param count the number of occurrences of grade.
	 */
	public GradeCount(final String grade, final int count) {
		this.grade = grade;
		this.count = count;
	}

	/**
	 * Creates a new oldWOrk.GradeCount based on the specified map entry. This is useful
	 * if you iterate over the elements of an entry set of a map and want to
	 * create oldWOrk.GradeCount objects for each entry.
	 *
	 * @param entry the map entry that shall be converted to a oldWOrk.GradeCount
	 */
	public GradeCount(final Map.Entry<String, ? extends Number> entry) {
		this.grade = entry.getKey();
		this.count = entry.getValue().intValue();
	}

	public int compareTo(final GradeCount other) {
		final int gradeCmp = this.grade.compareTo(other.grade);
		return gradeCmp == 0 ? Integer.compare(this.count, other.count) : gradeCmp;
	}
}

