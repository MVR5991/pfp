import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * Simple test suite for the Levenshtein distance
 * Examples taken from 
 * http://de.wikipedia.org/wiki/Levenshtein-Distanz
 * http://en.wikipedia.org/wiki/Levenshtein_distance
 * 
 * @author Georg Dotzler<georg.dotzler@cs.fau.de>
 *
 */

public class LevenshteinTest {
	
	private static void print(int[][] table) {
		for (int[] line : table) {
			System.out.println(Arrays.toString(line));
		}
	}


	public static final int MAX_THREADS = 10;
	public static final int THREADS = 8;


	@Test
	public void testComputeValue() {
		final Levenshtein l = new LevenshteinSeq();

		int[][] table = {{0, 1, 2, 3}, {1, 0, 1, 2}, {2, 1, 1, 2}, {3, 2, 2, 2}, {4, 3, 3, 2}};

		char[] wordHorizontal = "Tor".toCharArray();
		char[] wordVertical = "Tier".toCharArray();

		for (int i = 1; i < table.length; i++) {
			for (int j = 1; j < table[0].length; j++) {
				assertEquals(table[i][j], l.computeValue(i, j, table, wordHorizontal, wordVertical));
			}
		}
	}
	
	
	@Test
	public void testTorSeq() {
		final Levenshtein l = new LevenshteinSeq();

		int[][] solution = l.computeLevenshtein("Tor".toCharArray(), "Tier".toCharArray());

		assertNotNull(solution);
		assertNotNull(solution[0]);
		assertEquals(solution[solution.length-1][solution[0].length-1], 2);
		//print(solution);
	}


	@Test
	public void testKittenSeq() {
		final Levenshtein l = new LevenshteinSeq();

		int[][] solution = l.computeLevenshtein("kitten".toCharArray(),"sitting".toCharArray());

		assertNotNull(solution);
		assertNotNull(solution[0]);
		assertEquals(solution[solution.length-1][solution[0].length-1], 3);
		//print(solution);
	}


	@Test
	public void testSaturdaySeq() {
		final Levenshtein l = new LevenshteinSeq();

		int[][] solution = l.computeLevenshtein("Saturday".toCharArray(),"Sunday".toCharArray());

		assertNotNull(solution);
		assertNotNull(solution[0]);
		assertEquals(solution[solution.length-1][solution[0].length-1], 3);
		//print(solution);
	}


	@Test
	public void testHabitSeq() {
		final Levenshtein l = new LevenshteinSeq();

		int[][] solution = l.computeLevenshtein("HABIT".toCharArray(),"HOBBIT".toCharArray());

		assertNotNull(solution);
		assertNotNull(solution[0]);
		assertEquals(solution[solution.length-1][solution[0].length-1], 2);
		//print(solution);
	}

	
	@Test
	public void testTorQueue() {
		final Levenshtein l = new LevenshteinQueue(4);

		int[][] solution = l.computeLevenshtein("Tor".toCharArray(), "Tier".toCharArray());

		assertNotNull(solution);
		assertNotNull(solution[0]);
		assertEquals(solution[solution.length-1][solution[0].length-1], 2);
		//print(solution);
	}


	@Test
	public void testKittenQueue() {
		final Levenshtein l = new LevenshteinQueue(4);

		int[][] solution = l.computeLevenshtein("kitten".toCharArray(), "sitting".toCharArray());

		assertNotNull(solution);
		assertNotNull(solution[0]);
		assertEquals(solution[solution.length-1][solution[0].length-1], 3);
		//print(solution);
	}


	@Test
	public void testSaturdayQueue() {
		final Levenshtein l = new LevenshteinQueue(4);

		int[][] solution = l.computeLevenshtein("Saturday".toCharArray(), "Sunday".toCharArray());

		assertNotNull(solution);
		assertNotNull(solution[0]);
		assertEquals(solution[solution.length-1][solution[0].length-1], 3);
		//print(solution);
	}


	@Test
	public void testHabitQueue() {
		final Levenshtein l = new LevenshteinQueue(4);

		int[][] solution = l.computeLevenshtein("HABIT".toCharArray(), "HOBBIT".toCharArray());

		assertNotNull(solution);
		assertNotNull(solution[0]);
		assertEquals(solution[solution.length-1][solution[0].length-1], 2);
		//print(solution);
	}

	
	@Test
	public void testTorExchanger() {
		final Levenshtein l = new LevenshteinExchanger(4);

		int[][] solution = l.computeLevenshtein("Tor".toCharArray(), "Tier".toCharArray());

		assertNotNull(solution);
		assertNotNull(solution[0]);
		assertEquals(solution[solution.length-1][solution[0].length-1], 2);
		//print(solution);
	}


	@Test
	public void testKittenExchanger() {
		final Levenshtein l = new LevenshteinExchanger(4);

		int[][] solution = l.computeLevenshtein("kitten".toCharArray(), "sitting".toCharArray());

		assertNotNull(solution);
		assertNotNull(solution[0]);
		assertEquals(solution[solution.length-1][solution[0].length-1], 3);
		//print(solution);
	}


	@Test
	public void testSaturdayExchanger() {
		final Levenshtein l = new LevenshteinExchanger(4);

		int[][] solution = l.computeLevenshtein("Saturday".toCharArray(), "Sunday".toCharArray());

		assertNotNull(solution);
		assertNotNull(solution[0]);
		assertEquals(solution[solution.length-1][solution[0].length-1], 3);
		//print(solution);
	}


	@Test
	public void testHabitExchanger() {
		final Levenshtein l = new LevenshteinExchanger(4);

		int[][] solution = l.computeLevenshtein("HABIT".toCharArray(), "HOBBIT".toCharArray());

		assertNotNull(solution);
		assertNotNull(solution[0]);
		assertEquals(solution[solution.length-1][solution[0].length-1], 2);
		//print(solution);
	}


	@Test
	public void testMultipleThreadsQueue() {
		for (int i = 1; i < 10; i++){
			final Levenshtein l = new LevenshteinQueue(i);

			int[][] solution = l.computeLevenshtein("HABIT".toCharArray(), "HOBBIT".toCharArray());

			assertNotNull("threads = " + i, solution);
			assertNotNull("threads = " + i, solution[0]);
			assertEquals("threads = " + i, solution[solution.length-1][solution[0].length-1], 2);
			//print(solution);
		}
	}

	
	@Test
	public void testMultipleThreadsExchanger() {
		for (int i = 1; i < 10; i++){
			final Levenshtein l = new LevenshteinExchanger(i);

			int[][] solution = l.computeLevenshtein("HABIT".toCharArray(), "HOBBIT".toCharArray());

			assertNotNull("threads = " + i, solution);
			assertNotNull("threads = " + i, solution[0]);
			assertEquals("threads = " + i, solution[solution.length-1][solution[0].length-1], 2);
			//print(solution);
		}
	}

	
	@Test
	public void testLargeQueue() {
		Random random = new Random(System.currentTimeMillis());
		StringBuffer bufferA = new StringBuffer();
		StringBuffer bufferB = new StringBuffer();
		for (int i = 0; i < 12000; i++){
			char c = (char)(65 + random.nextInt(26));
			char d = (char)(65 + random.nextInt(26));
			bufferA.append(c);
			bufferB.append(d);
		}

		int[][] solution = null;
		boolean error = false;

		final Levenshtein l = new LevenshteinQueue(8);

		try {
			solution = l.computeLevenshtein(bufferA.toString().toCharArray(), bufferB.toString().toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertNotNull(solution);
		assertFalse(error);
	} 

} 

