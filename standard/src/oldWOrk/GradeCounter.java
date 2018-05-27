package oldWOrk;

/**
 * An interface for counters of grades.
 */
public interface GradeCounter {
	/**
	 * Counts the number of occurrences of each grade in the specified array.
	 * This method uses the specified number of threads to compute the number of
	 * occurrences in parallel.
	 *
	 * @param grades the array containing the grades.
	 * @param nThreads the number of threads to use.
	 * @return an array of {@link GradeCount} objects. Contains an entry for
	 *   each grade that occurs in <code>grades</code>.
	 */
	GradeCount[] count(String[] grades, int nThreads);
}

