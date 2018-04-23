/**
 * @author Silvia Schreier<sisaschr@stud.informatik.uni-erlangen.de>
 * @author Marius Kamp <marius.kamp@fau.de>
 */
public interface ArraySum {

	/**
	 * Calculates the sum of all elements of the specified array in parallel.
	 * 
	 * @param array
	 *    The array to use
	 * @param threads
	 *    The number of threads to use
	 * @return The sum of the elements of array
	 */
	public long sum(long[] array, int threads);

}
