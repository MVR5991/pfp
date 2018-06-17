import static org.junit.Assert.*;

import org.junit.Test;



public class DijkstraTest {

	@Test
	public void testWithExampleFromExercise9_3() {
		final int[][] adjacencyMatrix = {
			{  0, 10,  1,  8, -1, -1 },
			{ 10,  0,  7, -1, -1,  2 },
			{  1,  7,  0,  4, -1,  6 },
			{  8, -1,  4,  0,  6, -1 },
			{ -1, -1, -1,  6,  0,  3 },
			{ -1,  2,  6, -1,  3,  0 }
		};
		final int[] expected = { 0, 8, 1, 5, 10, 7 };

		assertArrayEquals(expected, Dijkstra.findShortestPaths(adjacencyMatrix, 0));
	}



	@Test
	public void testWithExampleFromLectureSlides() {
		final int[][] adjacencyMatrix = {
			{  0, -1,  3,  8,  5 },
			{ -1,  0,  2,  1, 12 },
			{  3,  2,  0,  4, -1 },
			{  8,  1,  4,  0, -1 },
			{  5, 12, -1, -1,  0 }
		};
		final int[] expected = { 5, 10, 8, 11, 0 };

		assertArrayEquals(expected, Dijkstra.findShortestPaths(adjacencyMatrix, 4));
	}
}

