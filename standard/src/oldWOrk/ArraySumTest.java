package oldWOrk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Small example JUnit test to find bugs in the own implementation.
 * 
 * @author Silvia Schreier<sisaschr@stud.informatik.uni-erlangen.de>
 * @author Marius Kamp <marius.kamp@fau.de>
 */
public class ArraySumTest {

	private long[] generateRandomArray(final int size) {
		final long[] array = new long[size];
		for (int i = 0; i < size; i++) {
			array[i] = (long) (Math.random() * 1000);
		}
		return array;
	}

	private long sum(final long[] array) {
		long sum = 0;
		for (final long a : array) {
			sum += a;
		}
		return sum;
	}

	/**
	 * Checks the parallel implementation with only one thread
	 * (array.length = 1)
	 */
	@Test
	public void testArraySumOneElement() {
		assertEquals(42, new ArraySumImpl().sum(new long[] { 42 }, 1));
	}

	/**
	 * Checks the parallel implementation with only one thread
	 * (array.length = 2)
	 */
	@Test
	public void testArraySumTwoElements() {
		assertEquals(7, new ArraySumImpl().sum(new long[] { -1, 8 }, 1));
	}

	/**
	 * Compares the parallel implementation with a sequential implementation
	 * (array.length = 10).
	 */
	@Test
	public void testArraySumSmall() {
		final int size = 10;
		final int threads = 24;
		final long[] array = generateRandomArray(size);
		final ArraySum sum = new ArraySumImpl();
		final long result = sum.sum(array, threads);
		final long seqResult = sum(array);
		assertEquals(seqResult, result);
	}

	public void testArraySumSmallWithMoreThreadsTHanCalulations() {
		final int size = 10;
		final int threads = 24;
		final long[] array = generateRandomArray(size);
		final ArraySum sum = new ArraySumImpl();
		final long result = sum.sum(array, threads);
		final long seqResult = sum(array);
		assertEquals(seqResult, result);
	}

	/**
	 * Compares the parallel implementation with a sequential implementation
	 * using a larger array and 2 to 14 threads.
	 */
	@Test
	public void testArraySumMedium() {
		for (int threads = 2; threads < 15; threads++) {
			for (int size = 500; size < 5000; size += 355) {
				final long[] array = generateRandomArray(size);
				final ArraySum sum = new ArraySumImpl();
				final long result = sum.sum(array, threads);
				final long seqResult = sum(array);
				assertEquals(seqResult, result);			
			}
		}
	}

	/**
	 * Compares the parallel implementation with a sequential implementation
	 * using an even larger array and many threads.
	 */
	@Test
	public void testArraySumLarge() {
		final int size = 1000000;
		final int threads = 123;
		final long[] array = generateRandomArray(size);
		final ArraySum sum = new ArraySumImpl();
		final long result = sum.sum(array, threads);
		final long seqResult = sum(array);
		assertEquals(seqResult, result);
	}

	/**
	 * Compares the parallel implementation with a sequential implementation
	 * using an empty array.
	 */
	@Test
	public void testArraySumEmpty() {
		final int size = 0;
		final int threads = 24;
		final long[] array = generateRandomArray(size);
		final ArraySum sum = new ArraySumImpl();
		final long result = sum.sum(array, threads);
		final long seqResult = sum(array);
		assertEquals(seqResult, result);
	}
}
