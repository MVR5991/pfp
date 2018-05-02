public interface FuncPainter {
	/**
	 * Evaluate a two-dimensional function at random points
	 *
	 * @param screen
	 * 		The screen to show the result
	 * @param func
	 * 		The function to evaluate
	 * @param nThreads
	 * 		The number of threads to use
	 */
	public void randomPaint(Screen screen, Function func, int nThreads);

	/**
	 * Evaluate a two-dimensional function at random points, the critical parts
	 * are synchronized
	 *
	 * @param screen
	 * 		The screen to show the result
	 * @param func
	 * 		The function to evaluate
	 * @param nThreads
	 * 		The number of threads to use
	 */
	public void synchronizedPaint(Screen screen, Function func, int nThreads);

	/**
	 * Evaluate a two-dimensional function. Collisions are avoided without
	 * synchronization
	 *
	 * @param screen
	 * 		The screen to show the result
	 * @param func
	 * 		The function to evaluate
	 * @param nThreads
	 * 		The number of threads to use
	 */
	public void syncFreePaint(Screen screen, Function func, int nThreads);
}

