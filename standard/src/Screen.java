public interface Screen {
	/**
	 * Sets the point (x, y) to the specified value.
	 *
	 * @param x
	 * 		X coordinate in the interval [0; getWidth())
	 * @param y
	 * 		Y coordinate in the interval [0; getHeight())
	 * @param value
	 */
	public  void setValue(final int x, final int y, final double value);

	/**
	 * Checks whether a value has been assigned to the specified point (x, y).
	 *
	 * @param x
	 * 		X coordinate in the interval [0; getWidth())
	 * @param y
	 * 		Y coordinate in the interval [0; getHeight())
	 *
	 * @return <code>true</code>, if the point (x, y) already has a value.
	 */
	public boolean hasValue(final int x, final int y);

	/**
	 * @return <code>true</code>, if the number of calls to setValue is equal or
	 * 		greater than the number of elements of this screen.
	 */
	public boolean finished();

	/**
	 * @return the width of this screen.
	 */
	public int getWidth();

	/**
	 * @return the height of this screen.
	 */
	public int getHeight();
}

