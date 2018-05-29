import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CompletionServiceTest {

	@Parameterized.Parameters
	public static Object[][] data() {
		return new Object[50][0];
	}

	@Test(timeout=1000)
	public void testSubmitShutdownAndTakeSingleThread() throws InterruptedException {

		FixedThreadCompletionService<Integer> cs = new FixedThreadCompletionService<Integer>(1);

		for (int i = 0; i < 1; i++) {
			MyCallable c = new MyCallable(20);
			cs.submit(c);
		}

		cs.shutdown();

		for (int i = 0; i < 1; i++) {
			Future<Integer> f = cs.take();
			assertTrue(f != null);
		}

		Future<Integer> f = cs.take();
		assertEquals(f, null);
	}
	
	@Test(timeout=1000)
	public void testSubmitAndTakeSingleThread() throws InterruptedException {

		FixedThreadCompletionService<Integer> cs = new FixedThreadCompletionService<Integer>(1);

		HashSet<Future<Integer>> set1 = new HashSet<Future<Integer>>();
		for (int i = 0; i < 1; i++) {
			MyCallable c = new MyCallable(200);
			Future<Integer> f = cs.submit(c);
			set1.add(f);
		}

		HashSet<Future<Integer>> set2 = new HashSet<Future<Integer>>();
		for (int i = 0; i < 1; i++) {
			Future<Integer> f = cs.take();
			set2.add(f);
			assertTrue(set1.contains(f));
		}

		assertEquals(set1.size(), 1);
		assertEquals(set2.size(), 1);
	}

	@Test(timeout=1000, expected=RejectedExecutionException.class)
	public void testShutdownAndSubmitSingleThread() throws InterruptedException {

		FixedThreadCompletionService<Integer> cs = new FixedThreadCompletionService<Integer>(1);

		for (int i = 0; i < 1; i++) {
			MyCallable c = new MyCallable(200);
			Future<Integer> f = cs.submit(c);
		}

		cs.shutdown();

		for (int i = 0; i < 1; i++) {
			MyCallable c = new MyCallable(200);
			Future<Integer> f = cs.submit(c);
			assertEquals(f, null);
		}
	}

	@Test
	public void testSubmitShutdownAndTake() throws InterruptedException {
		System.out.println("running testSubmitShutdownAndTake");

		FixedThreadCompletionService<Integer> cs = new FixedThreadCompletionService<Integer>(4);

		for (int i = 0; i < 10; i++) {
			MyCallable c = new MyCallable(2000);
			cs.submit(c);
		}

		cs.shutdown();

		System.out.println("\ttaking");
		for (int i = 0; i < 10; i++) {
			Future<Integer> f = cs.take();
			assertTrue(f != null);
		}
		System.out.println("\ttaking done, trying last take");

		Future<Integer> f = cs.take();
		assertEquals(f, null);
	}
	
	@Test
	public void testSubmitAndTake() throws InterruptedException {
		System.out.println("running testSubmitAndTake");

		FixedThreadCompletionService<Integer> cs = new FixedThreadCompletionService<Integer>(4);

		HashSet<Future<Integer>> set1 = new HashSet<Future<Integer>>();
		for (int i = 0; i < 10; i++) {
			MyCallable c = new MyCallable(2000);
			Future<Integer> f = cs.submit(c);
			set1.add(f);
		}

		System.out.println("\ttaking");
		HashSet<Future<Integer>> set2 = new HashSet<Future<Integer>>();
		for (int i = 0; i < 10; i++) {
			Future<Integer> f = cs.take();
			set2.add(f);
			assertTrue(set1.contains(f));
		}

		assertEquals(set1.size(), 10);
		assertEquals(set2.size(), 10);
	}

	@Test(timeout=1000, expected=RejectedExecutionException.class)
	public void testShutdownAndSubmit() throws InterruptedException {
		System.out.println("running testShutdownAndSubmit");
		long startTime = System.currentTimeMillis();
		FixedThreadCompletionService<Integer> cs = new FixedThreadCompletionService<Integer>(4);

		for (int i = 0; i < 5; i++) {
			MyCallable c = new MyCallable(2000);
			Future<Integer> f = cs.submit(c);
		}
		cs.shutdown();
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(elapsedTime);

		System.out.println("\tsubmit after shutdown");
		for (int i = 0; i < 5; i++) {
			MyCallable c = new MyCallable(2000);
			Future<Integer> f = cs.submit(c);
			assertEquals(f, null);
		}
	}

	@Test(timeout=10000)
	public void testSubmitShutdownAndTakeMultipleThreads() throws InterruptedException {
		System.out.println("running testSubmitShutdownAndTakeMultipleThreads");

		FixedThreadCompletionService<Integer> cs = new FixedThreadCompletionService<Integer>(4);

		MyThread[] threads = new MyThread[4];

		for (int i = 0; i < 4; i++) {
			threads[i] = new MyThread(i, cs);
			threads[i].start();
		}
		System.out.println("Threads started");
		for (int i = 0; i < 4; i++) {
			threads[i].join();
		}
		System.out.println("Threads joined");

		Future<Integer> submitReturn = null;
		Future<Integer> takeReturn = null;
		for (int i = 0; i < 4; i++) {
			if (threads[i].submitReturn != null) {
				assertEquals(submitReturn, null);
				submitReturn = threads[i].submitReturn;
			}
			if (threads[i].takeReturn != null) {
				assertEquals(takeReturn, null);
				takeReturn = threads[i].takeReturn;
			}
		}

		assertTrue(submitReturn != null);
		assertTrue(takeReturn != null);
		assertEquals(submitReturn, takeReturn);
	}
	
	class MyThread extends Thread {
		private int index;
		private FixedThreadCompletionService<Integer> cs;
		public Future<Integer> submitReturn;
		public Future<Integer> takeReturn;

		public MyThread(int index, FixedThreadCompletionService<Integer> cs) {
			this.index = index;
			this.cs = cs;
		}

		public void run() {
			if (index == 3) {
				submitReturn = cs.submit(new MyCallable(1000));
				System.out.println("thread " + index + ": submit returned " + submitReturn);
				cs.shutdown();
			}

			try {
				takeReturn = cs.take();
				System.out.println("thread " + index + ": take returned " + takeReturn);
			} catch (Exception e) {
			}
		}
	}
	
	class MyCallable implements Callable<Integer> {
		private final int sleep;

		public MyCallable(int sleep) {
			this.sleep = sleep;
		}

		@Override
		public Integer call() throws Exception {
			Thread.sleep(sleep);
			return sleep;
		}
	}
}
