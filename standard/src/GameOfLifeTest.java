import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class GameOfLifeTest {

	@Parameters(name = "{index}: {2}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
//				{ new GameOfLifeSeq(), 1, "Sequential Version" },
				{ new GameOfLifePar(), 1, "Parallel Version - 1 Thread" },
				{ new GameOfLifePar(), 2, "Parallel Version - 2 Threads" },
				{ new GameOfLifePar(), 3, "Parallel Version - 3 Threads" },
				{ new GameOfLifePar(), 4, "Parallel Version - 4 Threads" },
				{ new GameOfLifePar(), 5, "Parallel Version - 5 Threads" },
				{ new GameOfLifePar(), 6, "Parallel Version - 6 Threads" },
				{ new GameOfLifePar(), 7, "Parallel Version - 7 Threads" },
				{ new GameOfLifePar(), 8, "Parallel Version - 8 Threads" },

		});
	}

	private GameOfLife gameOfLife;
	@SuppressWarnings("unused")
	private String id;
	private int threads;

	public GameOfLifeTest(final GameOfLife gameOfLife, int threads,
			final String id) {
		this.gameOfLife = gameOfLife;
		this.id = id;
		this.threads = threads;
	}

	@Test
	public void testRectangularField() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 30, 100, 25, 25);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 30, 100, 25, 25);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlock() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 4, 4, 1, 1);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 4, 4, 1, 1);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlockBorder00() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 0, 0);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 0, 0);
		gameOfLife.compute(initialGameState, threads, 50, 100, 1, 1000, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlockBorder01() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 0, 1);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 0, 1);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlockBorder02() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 0, 2);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 0, 2);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlockBorder03() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 0, 3);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 0, 3);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlockBorder10() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 1, 0);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 1, 0);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlockBorder20() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 2, 0);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 2, 0);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlockBorder30() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 3, 0);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 3, 0);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlockBorder31() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 3, 1);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 3, 1);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlockBorder32() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 3, 2);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 3, 2);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlockBorder33() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 3, 3);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 3, 3);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlockBorder13() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 1, 3);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 1, 3);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlockBorder23() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 2, 3);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLOCK, 5, 5, 2, 3);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBeehive() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BEEHIVE, 10, 10, 1, 1);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BEEHIVE, 10, 10, 1, 1);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testLoaf() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_LOAF, 10, 10, 1, 1);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_LOAF, 10, 10, 1, 1);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBoat() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BOAT, 10, 10, 1, 1);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BOAT, 10, 10, 1, 1);
		gameOfLife.compute(initialGameState, threads, 0, 100, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testBlinker() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLINKER_1, 5, 5, 2, 1);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_BLINKER_2, 5, 5, 1, 2);
		gameOfLife.compute(initialGameState, threads, 10, 101, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testGlider1() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_GLIDER_1, 10, 10, 3, 3);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_GLIDER_2, 10, 10, 4, 3);
		gameOfLife.compute(initialGameState, threads, 10, 1, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testGlider2() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_GLIDER_2, 10, 10, 3, 3);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_GLIDER_3, 10, 10, 3, 3);
		gameOfLife.compute(initialGameState, threads, 10, 1, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testGlider3() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_GLIDER_3, 10, 10, 3, 3);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_GLIDER_4, 10, 10, 3, 4);
		gameOfLife.compute(initialGameState, threads, 10, 1, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testGlider4() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_GLIDER_4, 10, 10, 3, 3);
		int[][] refPosition = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_GLIDER_1, 10, 10, 3, 3);
		gameOfLife.compute(initialGameState, threads, 10, 1, 1, 0, false, false);
		int[][] endPosition = gameOfLife.getEndPosition();
		assertArrayEquals(refPosition, endPosition);
	}

	@Test
	public void testRPentomino() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_R_PENTOMINO, 1000, 1000, -1, -1);
		gameOfLife
				.compute(initialGameState, threads, 10, 1103, 1, 0, false, false);

		int[][] endPosition = gameOfLife.getEndPosition();
		int count = 0;
		for (int i = 0; i < endPosition.length; i++) {
			for (int j = 0; j < endPosition[0].length; j++) {
				count += endPosition[i][j];
			}
		}
		assertEquals(116, count);
	}

	@Test
	public void testRPentomino2() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_R_PENTOMINO, 1000, 1000, -1, -1);
		gameOfLife.compute(initialGameState, threads, 10, 1103, 500, 0, false,
				false);

		int[][] endPosition = gameOfLife.getEndPosition();
		int count = 0;
		for (int i = 0; i < endPosition.length; i++) {
			for (int j = 0; j < endPosition[0].length; j++) {
				count += endPosition[i][j];
			}
		}
		assertEquals(116, count);
	}

	@Test
	public void testRPentomino3() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_R_PENTOMINO, 1000, 1000, -1, -1);
		gameOfLife.compute(initialGameState, threads, 10, 1103, 1103, 0, false,
				false);

		int[][] endPosition = gameOfLife.getEndPosition();
		int count = 0;
		for (int i = 0; i < endPosition.length; i++) {
			for (int j = 0; j < endPosition[0].length; j++) {
				count += endPosition[i][j];
			}
		}
		assertEquals(116, count);
	}

	@Test
	public void testDieHard1() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_DIE_HARD, 1000, 1000, -1, -1);
		gameOfLife.compute(initialGameState, threads, 10, 129, 1, 0, false, false);

		int[][] endPosition = gameOfLife.getEndPosition();
		int count = 0;
		for (int i = 0; i < endPosition.length; i++) {
			for (int j = 0; j < endPosition[0].length; j++) {
				count += endPosition[i][j];
			}
		}
		assertEquals(2, count);
	}

	@Test
	public void testDieHard2() {
		int[][] initialGameState = GameOfLife.getBoardFromRLE(
				GameOfLife.PATTERN_DIE_HARD, 1000, 1000, -1, -1);
		gameOfLife.compute(initialGameState, threads, 10, 130, 1, 0, false, false);

		int[][] endPosition = gameOfLife.getEndPosition();
		int count = 0;
		for (int i = 0; i < endPosition.length; i++) {
			for (int j = 0; j < endPosition[0].length; j++) {
				count += endPosition[i][j];
			}
		}
		assertEquals(0, count);
	}

	@Test
	public void testAcorn() {
		if (threads == 8) {
			int[][] initialGameState = GameOfLife.getBoardFromRLE(
					GameOfLife.PATTERN_ACORN, 1000, 1000, -1, -1);
			long timeStart = System.currentTimeMillis();
			gameOfLife.compute(initialGameState, threads, 10, 5206, 5206, 0,
					false, false);
			long timePar = System.currentTimeMillis() - timeStart;
			GameOfLife seq = new GameOfLifeSeq();
			timeStart = System.currentTimeMillis();
			seq.compute(initialGameState, threads, 10, 5206, 5206, 0, false, false);

			long timeSeq = System.currentTimeMillis() - timeStart;

			int[][] endPosition = gameOfLife.getEndPosition();
			int[][] endPositionSeq = seq.getEndPosition();

			assertArrayEquals(endPositionSeq, endPosition);
			assertTrue((double) timeSeq / timePar > 1.5);
		}
	}

// Long test!!!!!
//	@Test
//	public void testBreeder() {
//		if (threads == 8) {
//			int[][] initialGameState = GameOfLife.getBoardFromRLE(
//					GameOfLife.PATTERN_BREEDER_1, 5000, 5000, -1, -1);
//			long timeStart = System.currentTimeMillis();
//			gameOfLife.compute(initialGameState, threads, 10, 5206, 5206, 0,
//					false, false);
//			long timePar = System.currentTimeMillis() - timeStart;
//			GameOfLife seq = new GameOfLifeSeq();
//			timeStart = System.currentTimeMillis();
//			seq.compute(initialGameState, threads, 10, 5206, 5206, 0, false, false);
//
//			long timeSeq = System.currentTimeMillis() - timeStart;
//
//			int[][] endPosition = gameOfLife.getEndPosition();
//			int[][] endPositionSeq = seq.getEndPosition();
//
//			assertArrayEquals(endPositionSeq, endPosition);
//			assertTrue((double) timeSeq / timePar > 1.5);
//		}
//	}
}
