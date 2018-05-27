import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

public interface SimpleCompletionService<V> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#isShutdown()
	 */
	public boolean isShutdown();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#shutdown()
	 */
	public void shutdown();

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.concurrent.CompletionService#poll()
	 */
	public Future<V> poll();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.CompletionService#submit(java.util.concurrent.Callable)
	 */
	public Future<V> submit(Callable<V> task) throws RejectedExecutionException;

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.concurrent.CompletionService#take()
	 */
	public Future<V> take() throws InterruptedException;
}
